package com.laplace.api.common.pay.jp;

import com.laplace.api.common.configuration.email.MailConfiguration;
import com.laplace.api.common.configuration.email.MailDataProvider;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.MailTemplateFields;
import com.laplace.api.common.constants.MailTemplateName;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.OrderStatus;
import com.laplace.api.common.constants.enums.PaymentType;
import com.laplace.api.common.constants.enums.StripeParamsType;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.AppUserProfile;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.model.db.Payment;
import com.laplace.api.common.repository.db.AppUserProfileRepository;
import com.laplace.api.common.repository.db.PaymentRepository;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.EmailService;
import com.laplace.api.common.service.ItemService;
import com.laplace.api.common.util.DateUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Transfer;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.TransferCreateParams;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.*;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Slf4j
@Service
public class PaymentService {

    private final AppUserProfileRepository appUserProfileRepository;

    private final PaymentRepository paymentRepository;

    private final StripeService stripeService;

    private final ItemService itemService;

    private final EmailService emailService;

    private final MailConfiguration mailConfig;

    private final MailDataProvider mailDataProvider;
    private final AppUserService appUserService;

    @Autowired
    public PaymentService(AppUserProfileRepository appUserProfileRepository,
        ItemService itemService,
        PaymentRepository paymentRepository, StripeService stripeService,
        EmailService emailService,
        MailConfiguration mailConfig,
        MailDataProvider mailDataProvider,
        AppUserService appUserService) {
        this.appUserProfileRepository = appUserProfileRepository;
        this.paymentRepository = paymentRepository;
        this.stripeService = stripeService;
        this.itemService = itemService;
        this.emailService = emailService;
        this.mailConfig = mailConfig;
        this.mailDataProvider = mailDataProvider;
        this.appUserService = appUserService;
    }

    public Payment confirmPayment(Payment payment, Item item, String email, Order order) throws StripeException {
        PaymentIntent paymentIntent = null;
        Transfer transfer = null;

        Integer userId = payment.getUserId();

        if (StringUtils.isNotBlank(payment.getChargeId())) {
            capturePaymentIntent(payment, order, email);
            storePaymentInfo(payment, null, null, item);
            return payment;
        }

        try {
            paymentIntent = stripeService.createPaymentIntent(getPaymentIntentParams(payment, order, userId));
            payment.setChargeId(paymentIntent.getId());
            payment.setOriginalAmount(paymentIntent.getAmount().intValue());
            payment.setStripeProcessingFee(paymentIntent.getAmount().intValue() - paymentIntent.getAmountReceived()
                    .intValue());
            log.info(paymentIntent.toJson());

            switch (order.getStatus()) {
                case RETURN:
                    if(order.getConfirmed()){
                        validateAndUpdateItemStatus(userId, ItemStatus.PREPARE_TO_SEND_TO_SELLER, item.getItemId(), paymentIntent);
                    }else{
                        payment.setPaymentType(PaymentType.FIX);
                    }
                    break;
                case CANCEL:
                    validateAndUpdateItemStatus(userId, ItemStatus.PREPARE_TO_SEND_TO_SELLER, item.getItemId(), paymentIntent);
                    payment.setProcessingFee(payment.getGiftWrappingPrice() + payment.getProcessingFee() -
                            payment.getStripeProcessingFee());
                    break;
                case PURCHASE:
                    validateAndUpdateItemStatus(userId, ItemStatus.PREPARE_TO_SEND_TO_BUYER, item.getItemId(), paymentIntent);
                    transfer = createTransfer(item.getSellerId(), payment);
                    payment.setProcessingFee(payment.getGiftWrappingPrice() + payment.getProcessingFee() -
                            payment.getStripeProcessingFee());
                    break;
            }
        } catch (StripeException ex) {
            log.error(ex.getLocalizedMessage());
            try {
                sendPaymentProblemMail(email, payment.getUserName(), StripeParamsType.CURRENCY_JP.getValue());
            } catch (Exception exc) {
                log.error("Order -> Item purchase -> cancel -> email sending failed: " + exc.getLocalizedMessage());
            }
            throw ex;
        }
        storePaymentInfo(payment, paymentIntent, transfer, item);
        return payment;
    }

    public void validateIntent(String paymentIntentId, String paymentMethodId) {
        PaymentIntent intent = stripeService.retrievePaymentIntentFromId(paymentIntentId);

        log.warn(intent.toJson());

        if (!(intent.getStatus().equals("succeeded") && intent.getPaymentMethod().equals(paymentMethodId)
                && null == intent.getLastPaymentError() && null == intent.getNextAction())) {

            throw throwApplicationException(INVALID_PAYMENT_INTENT_PROVIDED);
        }

        Optional<Payment> previousPayments = paymentRepository.findPaymentByChargeIdAndPaymentType(paymentIntentId,
                PaymentType.PAY);

        if (previousPayments.isPresent()) {
            throw throwApplicationException(ALREADY_CAPTURED);
        }
    }

    public void capturePaymentIntent(Payment payment, Order order, String email) throws StripeException{

        if (order.getStatus() == OrderStatus.RETURN) {
            try {
                PaymentIntent intent = stripeService.retrievePaymentIntentFromId(payment.getChargeId());

                log.warn(intent.toJson());

                if (!(intent.getStatus().equals("requires_capture") && intent.getPaymentMethod().equals(payment.getPaymentMethod())
                        && null == intent.getLastPaymentError())) {
                    throw throwApplicationException(INVALID_PAYMENT_INTENT_PROVIDED);
                }

                payment.setPaymentType(PaymentType.PAY);
                stripeService.capturePaymentIntent(intent);
            } catch (StripeException ex) {
                log.error(ex.getLocalizedMessage());
                try {
                    sendPaymentProblemMail(email, payment.getUserName(), StripeParamsType.CURRENCY_JP.getValue());
                } catch (Exception exc) {
                    log.error("Item -> return -> payment capture email sending failed -> " + exc.getLocalizedMessage());
                }
                throw ex;
            }
        } else {
            validateIntent(payment.getChargeId(), payment.getPaymentMethod());
        }
    }

    public void storePaymentInfo(Payment payment, PaymentIntent intent, Transfer transfer, Item item) {
        try {
            paymentRepository.save(payment);
        } catch (Exception ex) {
            refundAndReverse(intent, transfer);
            rollBackItemStatus(item);
            throw throwApplicationException(PAYMENT_NOT_COMPLETED);
        }
    }

    public void rollBackItemStatus(Item item){
        try {
            item.setStatus(ItemStatus.ON_SALE);
            itemService.save(item);
        } catch (Exception ex) {
            log.error("Failed to rollback item status to ON_SALE " + ex.getLocalizedMessage());
        }
    }

    public void refundAndReverse(PaymentIntent intent, Transfer transfer) {
        try {
            if (null != intent && intent.getStatus().equals("succeeded")) {
                stripeService.fullRefundIfAlreadyPaid(intent.getId());
                log.warn("Successfully refund for  " + intent.getId());
                if (null != transfer) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("amount", transfer.getAmount());
                    try {
                        transfer.getReversals().create(params);
                    } catch (Exception ex) {
                        log.error("Failed to transfer money from seller account: " + ex.getLocalizedMessage());
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Failed to refund to buyer account: " + ex.getLocalizedMessage());
        }
    }

    public void fullRefundIfValid(String paymentIntentId, String paymentMethodId) throws StripeException {
        validateIntent(paymentIntentId, paymentMethodId);
        stripeService.fullRefundIfAlreadyPaid(paymentIntentId);
        log.warn("Successfully refund for  " + paymentIntentId);
    }

    public void releasePaymentIntent(Payment payment) {
        try {
            PaymentIntent intent = stripeService.retrievePaymentIntentFromId(payment.getChargeId());
            payment.setPaymentType(PaymentType.RELEASE);
            stripeService.cancelPaymentIntent(intent);
            paymentRepository.save(payment);
        } catch (StripeException ex) {
            log.error(ex.getLocalizedMessage());
        }
    }

    private PaymentIntentCreateParams getPaymentIntentParams(Payment payment, Order order, Integer userId) {
        switch (order.getStatus()) {
            case RETURN:
                if(!order.getConfirmed()){
                    return PaymentIntentCreateParams.builder()
                            .setCurrency(StripeParamsType.CURRENCY_JP.getValue())
                            .setAmount(payment.getOriginalAmount().longValue())
                            .setPaymentMethod(payment.getPaymentMethod())
                            .setCustomer(getCustomerKey(userId))
                            .setDescription(payment.getDescription())
                            .setTransferGroup(payment.getOrderId())
                            .setConfirm(true)
                            .setOffSession(true)
                            .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL)
                            .build();
                }
            case PURCHASE:
            case CANCEL:
            case EXTEND:
            default:
                return PaymentIntentCreateParams.builder()
                        .setCurrency(StripeParamsType.CURRENCY_JP.getValue())
                        .setAmount(payment.getOriginalAmount().longValue())
                        .setPaymentMethod(payment.getPaymentMethod())
                        .setCustomer(getCustomerKey(userId))
                        .setDescription(payment.getDescription())
                        .setTransferGroup(payment.getOrderId())
                        .setConfirm(true)
                        .setOffSession(true)
                        .build();

        }
    }

    private Transfer createTransfer(Integer sellerId, Payment payment) throws StripeException {
        AppUserProfile sellerProfile = getUserProfile(sellerId);

        if (StringUtils.isEmpty(sellerProfile.getAccountId())) {
            log.error(sellerId + " : Invalid seller account. Money is not transferred in seller account ! ");
            return null;
        }

        try {
            Transfer transfer = Transfer.create(TransferCreateParams.builder()
                    .setDestination(sellerProfile.getAccountId())
                    .setAmount(payment.getSellerAmount().longValue())
                    .setTransferGroup(payment.getOrderId())
                    .setCurrency(StripeParamsType.CURRENCY_JP.getValue())
                    .build());

            payment.setSellerAmount(transfer.getAmount().intValue());
            return transfer;
        } catch (StripeException ex) {
            log.error(sellerId + " : Invalid seller account. Money is not transferred in seller account!! " + ex.getLocalizedMessage());
            payment.setSellerAmount(ApplicationConstants.ZERO.intValue());
            return null;
        }
    }

    private String getCustomerKey(Integer userId) {
        AppUserProfile appUserProfile = getUserProfile(userId);
        if (StringUtils.isBlank(appUserProfile.getCustomerKey())) {
            throw throwApplicationException(CUSTOMER_NOT_FOUND);
        }

        return appUserProfile.getCustomerKey();
    }

    private AppUserProfile getUserProfile(Integer userId) {
        return appUserProfileRepository.findById(userId)
                .orElseThrow(() -> throwApplicationException(USER_PROFILE_NOT_FOUND));

    }

    private void validateAndUpdateItemStatus(Integer userId, ItemStatus status, String itemId,
                                             PaymentIntent intent) throws StripeException {
        try {
            Item item = getItem(itemId);
            if (ObjectUtils.notEqual(userId, item.getLockedBy())) {
                log.info(userId + " -- " + item.getLockedBy() + " -- " + item.getOnHoldAt()
                        + " -- " + Duration.between(item.getOnHoldAt(), DateUtil.timeNow()).getSeconds());
                stripeService.fullRefundIfAlreadyPaid(intent.getId());
                throw throwApplicationException(ITEM_ALREADY_SOLD);
            }
            item.setStatus(status);
            itemService.save(item);
        } catch (Exception ex) {
            stripeService.fullRefundIfAlreadyPaid(intent.getId());
            throw throwApplicationException(ITEM_ALREADY_SOLD);
        }
    }

    private Item getItem(String item) {
        return itemService.findOnSaleItem(item).orElseThrow(
                () -> throwApplicationException(ITEM_NOT_FOUND));
    }

    @SneakyThrows
    @Async("taskExecutor")
    public void sendPaymentProblemMail(String email, String name, String lang) {
        String subject = mailDataProvider
                .getMailSubjectCommon(ApplicationConstants.MailType.PAYMENT_PROBLEM, lang);
        String templateNamePrefix = MailTemplateName.PAYMENT_PROBLEM_PREFIX;
        Map<String, String> model = new HashMap<>();
        model.put(MailTemplateFields.USER_NAME, getFullName(email));
        model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
        model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
        String body = mailDataProvider.getMailBody(model, templateNamePrefix, lang);

        emailService.sendMail(email, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
                subject, body);
        emailService.sendMail(mailConfig.getOrderMail(), mailConfig.getMailFromAddress(),
                mailConfig.getMailFromName(), subject, body);
    }

    private String getFullName(String email) {
        Optional<AppUser> appUser = appUserService.findByEmail(email);
        return appUser.isPresent() ? appUser.get().getFullName() : "";
    }
}
