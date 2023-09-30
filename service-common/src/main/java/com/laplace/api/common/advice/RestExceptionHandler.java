package com.laplace.api.common.advice;

import com.amazonaws.SdkClientException;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.Messages;
import com.laplace.api.common.constants.enums.StripeParamsType;
import com.laplace.api.common.constants.enums.ResponseType;
import com.laplace.api.common.exception.LaplaceApplicationException;
import com.laplace.api.common.logging.ErrorMessageInfo;
import com.laplace.api.common.logging.LogMessageConfig;
import com.laplace.api.common.util.BaseResponse;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Nonnull;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  private final LogMessageConfig logMessageConfig;

  @Value("${spring.profiles.active}")
  private String activeProfile;
  private final Integer MAX_ERROR_LINES = 20;

  @Autowired
  public RestExceptionHandler(LogMessageConfig logMessageConfig) {
    this.logMessageConfig = logMessageConfig;
  }


  @Override
  protected @Nonnull
  ResponseEntity<Object> handleMissingServletRequestParameter(
      @Nonnull MissingServletRequestParameterException ex, @Nonnull HttpHeaders headers,
      @Nonnull HttpStatus status, @Nonnull WebRequest request) {
    String error = ex.getParameterName() + " parameter is missing";
    return buildResponseEntity(error, ErrorCode.INSUFFICIENT_INFORMATION, HttpStatus.BAD_REQUEST,
        ex);
  }

  @Override
  protected @Nonnull
  ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      @Nonnull HttpHeaders headers, @Nonnull HttpStatus status, @Nonnull WebRequest request) {
    String error = getMessage(ErrorCode.METHOD_NOT_ALLOWED);
    return buildResponseEntity(error, ErrorCode.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED,
        ex);
  }

  @Override
  protected @Nonnull
  ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      @Nonnull HttpHeaders headers,
      @Nonnull HttpStatus status,
      @Nonnull WebRequest request) {
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
    return buildResponseEntity(builder.substring(0, builder.length() - 2), null,
        HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex);
  }

  @Override
  protected @Nonnull
  ResponseEntity<Object> handleMethodArgumentNotValid(
      @Nonnull MethodArgumentNotValidException ex, @Nonnull HttpHeaders headers,
      @Nonnull HttpStatus status, @Nonnull WebRequest request) {

    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();
    String errorCode = getErrorCode(fieldErrors);
    String message = getMessage(errorCode);

    return buildResponseEntity(Collections.singleton(message), errorCode, HttpStatus.BAD_REQUEST,
        ex, fieldErrors);
  }

  private String getErrorCode(List<FieldError> fieldErrors) {

    String code = ErrorCode.INVALID_ARGUMENT;
    return fieldErrors.stream().findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage)
        .orElse(code);
  }

  private String getMessage(String errorCode) {
    try {
      ErrorMessageInfo errorMessageInfo = logMessageConfig.getErrorMessageInfo(errorCode);
      return errorMessageInfo.getMessageTemplate();
    } catch (NoSuchElementException e) {
      return errorCode;
    }
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolation(
      ConstraintViolationException e) {
    Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

    Supplier<Set<ConstraintViolation<?>>> constrainViolationSupplier = () -> constraintViolations;

    Set<String> messages = new HashSet<>(constraintViolations.size());
    messages.addAll(constrainViolationSupplier.get().stream()
        .map(this::prepareViolationMessage)
        .collect(Collectors.toList()));

    Set<FieldError> fieldErrors = constrainViolationSupplier.get().stream()
        .map(cv -> new FieldError(cv.getLeafBean().toString(), cv.getPropertyPath().toString(),
            cv.getMessage()))
        .collect(Collectors.toSet());
    if (!messages.isEmpty()) {
      return buildResponseEntity(messages, ErrorCode.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST, e,
          fieldErrors);
    } else {
      return buildResponseEntity(Collections.singleton(Messages.VALIDATION_ERROR),
          ErrorCode.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST, e, fieldErrors);
    }
  }

  private String prepareViolationMessage(ConstraintViolation violation) {
    String message;
    try {
      message = logMessageConfig
          .getErrorMessageInfo(violation.getMessage()).getMessageTemplate();
    } catch (NoSuchElementException ex) {
      message = String
          .format("%s : %s", violation.getPropertyPath().toString(), violation.getMessage());
    }
    return message;
  }

  @ExceptionHandler({AccessDeniedException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public BaseResponse handle(AccessDeniedException e) {
    return BaseResponse.builder().responseType(ResponseType.ERROR)
        .message(Collections.singleton(e.getMessage())).code(ErrorCode.ACCESS_DENIED).build();
  }

  @ExceptionHandler({SdkClientException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public BaseResponse handle(SdkClientException e) {
    logger(e.getLocalizedMessage(), e);
    return BaseResponse.builder().responseType(ResponseType.ERROR)
        .message(Collections.singleton(e.getMessage())).code(ErrorCode.AWS_CONNECTION_ERROR)
        .build();
  }

  @ExceptionHandler({AuthenticationException.class})
  @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
  public BaseResponse handle(Exception e) {
    logger(e.getLocalizedMessage(), e);
    return BaseResponse.builder().responseType(ResponseType.ERROR)
            .message(Collections.singleton(e.getMessage())).code(ErrorCode.STRIPE_ERROR).build();
  }

  @ExceptionHandler({StripeException.class})
  @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
  public BaseResponse handleStripeError(StripeException err) {
    PaymentIntent paymentIntent = null;

    Map<String, Object> intent = new HashMap<>();
    logger(err.getLocalizedMessage(), err);
    intent.put(StripeParamsType.DESCRIPTION.getValue(), err.getLocalizedMessage());
    intent.put(StripeParamsType.CODE.getValue(), err.getCode());
    intent.put(StripeParamsType.STATUS.getValue(), err.getStatusCode());

    try {
      if (null == err.getStripeError().getPaymentIntent()) {
        return getStripeErrorResponse(intent, err.getMessage());
      }
      String paymentIntentId = err.getStripeError().getPaymentIntent().getId();
      intent.put("id", paymentIntentId);
      paymentIntent = PaymentIntent.retrieve(paymentIntentId);
      intent.put(StripeParamsType.CLIENT_SECRET.getValue(), paymentIntent.getClientSecret());
      intent.put(StripeParamsType.CODE.getValue(), paymentIntent.getLastPaymentError().getCode());
      intent.put("message", paymentIntent.getLastPaymentError().getMessage());
      return getStripeErrorResponse(intent, err.getMessage());
    } catch (StripeException e) {
      log.error(e.getLocalizedMessage());
      return getStripeErrorResponse(intent, err.getMessage());
    }
  }

  @ExceptionHandler({LaplaceApplicationException.class})
  public ResponseEntity<Object> handleAppError(LaplaceApplicationException ex) {
    ErrorMessageInfo errorMessageInfo = logMessageConfig.getErrorMessageInfo(ex.getErrorCode());
    String message = errorMessageInfo.getMessageTemplate();
    return buildResponseEntity(message, ex.getErrorCode(), ex.getStatus(), ex);
  }

  @Override
  protected @Nonnull
  ResponseEntity<Object> handleHttpMessageNotWritable(@Nonnull HttpMessageNotWritableException ex,
      @Nonnull HttpHeaders headers, @Nonnull HttpStatus status,
      @Nonnull WebRequest request) {
    String error = "Error writing JSON output";
    return buildResponseEntity(error, null, HttpStatus.INTERNAL_SERVER_ERROR, ex);
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<Object> handler(NullPointerException ex) {
    return buildResponseEntity(Messages.SERVER_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR, ex);
  }

  @Override
  protected @Nonnull
  ResponseEntity<Object> handleHttpMessageNotReadable(
      @Nonnull HttpMessageNotReadableException ex, @Nonnull HttpHeaders headers,
      @Nonnull HttpStatus status, @Nonnull WebRequest request) {
    ServletWebRequest servletWebRequest = (ServletWebRequest) request;
    log.info("{} to {}", servletWebRequest.getHttpMethod(),
        servletWebRequest.getRequest().getServletPath());
    String error = Messages.MALFORMED_JSON;
    return buildResponseEntity(error, ErrorCode.MALFORMED_JSON, HttpStatus.BAD_REQUEST, ex);
  }

  @Override
  protected @Nonnull
  ResponseEntity<Object> handleNoHandlerFoundException(
      @Nonnull NoHandlerFoundException ex, @Nonnull HttpHeaders headers, @Nonnull HttpStatus status,
      @Nonnull WebRequest request) {
    String error = String
        .format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL());
    return buildResponseEntity(error, null, HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  protected @Nonnull
  ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
      WebRequest request) {
    if (ex.getCause() instanceof ConstraintViolationException) {
      return buildResponseEntity("Database error", null, HttpStatus.CONFLICT, ex);
    }
    return buildResponseEntity(Messages.SERVER_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR, ex);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected @Nonnull
  ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
      WebRequest request) {
    String message = String
        .format("The parameter '%s' of value '%s' could not be converted to type '%s'",
            ex.getName(), ex.getValue(),
            Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
    return buildResponseEntity(message, ErrorCode.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Object> handler(ValidationException ex) {
    log.info("common validation exception");
    return buildResponseEntity(ex.getMessage(), null, HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handler(RuntimeException ex) {
    return buildResponseEntity(Messages.SERVER_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR, ex);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handler(Exception ex) {
    return buildResponseEntity(Messages.SERVER_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR, ex);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handler(IllegalArgumentException ex) {
    return buildResponseEntity(ex.getMessage(), ErrorCode.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST,
        ex);
  }

  private ResponseEntity<Object> buildResponseEntity(String message, String errorCode,
      HttpStatus status, Exception ex) {
    logger(message + " " + ex.getLocalizedMessage(), ex);
    return new ResponseEntity<>(BaseResponse.builder()
        .responseType(ResponseType.ERROR).message(Collections.singleton(message)).code(errorCode)
        .build(), status);

  }

  private ResponseEntity<Object> buildResponseEntity(Collection<String> message, String errorCode,
      HttpStatus status, Exception ex, Object error) {
    logger(message + " " + ex.getLocalizedMessage(), ex);
    return new ResponseEntity<>(BaseResponse.builder()
        .responseType(ResponseType.ERROR).message(message).code(errorCode).error(error).build(),
        status);
  }

  private BaseResponse getStripeErrorResponse(Map<String, Object> intent, String message) {
    return BaseResponse.builder().responseType(ResponseType.ERROR)
            .result(intent)
            .message(Collections.singleton(message)).code(ErrorCode.STRIPE_ERROR).build();
  }

  private void logger(String message, Exception ex) {

    StackTraceElement[] stackTrace = ex.getStackTrace();
    StringBuilder str = new StringBuilder();
    int i = 0;
    for (StackTraceElement stackTraceElement : stackTrace) {
      str.append(stackTraceElement.getFileName()).append(", ")
          .append(stackTraceElement.getLineNumber())
          .append(", ").append(stackTraceElement.getMethodName()).append("\n");
      if (++i == 5) {
        break;
      }
    }

    if (activeProfile.equalsIgnoreCase(ApplicationConstants.APPLICATION_LOCAL_PROFILE)) {
      log.error(message + " " + ex.getLocalizedMessage(), ex);
    } else {
      log.error("++Error: " + message + " " + Arrays.toString(Arrays.stream(ex.getStackTrace()).limit(MAX_ERROR_LINES).toArray()));
    }
  }
}
