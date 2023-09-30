package com.laplace.api.common.pay.jp;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.StripeParamsType;
import com.laplace.api.common.dto.request.DocumentUploadDto;
import com.laplace.api.common.dto.response.PaymentMethodResponseDTO;
import com.laplace.api.common.model.db.AppUserProfile;
import com.laplace.api.common.repository.db.AppUserProfileRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.BankAccount;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.Person;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PersonUpdateParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.*;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Service
@Slf4j
public class CustomerService {

    private final AppUserProfileRepository appUserProfileRepository;
    private final StripeService stripeService;

    @Autowired
    public CustomerService(AppUserProfileRepository appUserProfileRepository, StripeService stripeService) {
        this.appUserProfileRepository = appUserProfileRepository;
        this.stripeService = stripeService;
    }

    public String getSetUpIntentForSavingPaymentInfo(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put(StripeParamsType.CUSTOMER.getValue(), createOrGetCustomerId(userId));
        return stripeService.getSetUpIntent(params);
    }

    public String createOrGetCustomerId(Integer userId) {

        AppUserProfile appUserProfile = getAppUserProfile(userId);

        if (!StringUtils.isBlank(appUserProfile.getCustomerKey())) {
            return appUserProfile.getCustomerKey();
        }
        String customerKey = stripeService.createCustomerId(CustomerCreateParams.builder()
                .setName(appUserProfile.getName())
                .setPhone(appUserProfile.getPhoneNumber())
                .setDescription(appUserProfile.getLastName() + " "
                        + appUserProfile.getFirstName() + " :: " + appUserProfile.getUserId())
                .build());
        appUserProfile.setCustomerKey(customerKey);
        appUserProfileRepository.save(appUserProfile);
        return customerKey;
    }

    public boolean detachCustomerPaymentMethod(String PaymentMethodId, Integer userId) {
        Integer noOfPaymentMethod = getTotalNumberOfPaymentMethod(userId);
        if (noOfPaymentMethod <= ApplicationConstants.ONE) {
            throw throwApplicationException(MINIMUM_ONE_CARD_NEEDED);
        }
        return stripeService.detachCustomerPaymentMethod(PaymentMethodId);
    }

    public List<PaymentMethodResponseDTO> getCustomerPaymentMethods(Integer userId) {
        try {
           return getConvertedPaymentResponse(getCustomerPaymentCollection(userId));
        } catch (StripeException e) {
            log.error(e.getLocalizedMessage());
            throw throwApplicationException(CUSTOMER_NOT_FOUND);
        }
    }

    public void createStripeAccount(Integer userId, AccountCreateParams params) throws StripeException {
        AppUserProfile appUserProfile = getAppUserProfile(userId);
        if (!StringUtils.isBlank(appUserProfile.getAccountId())){
            throw throwApplicationException(ALREADY_REGISTER);
        }
        Account account =  stripeService.createCustomConnectAccount(params);
        appUserProfile.setAccountId(account.getId());
        appUserProfileRepository.save(appUserProfile);
    }

    public void uploadVerificationDocument(Integer userId, DocumentUploadDto uploadDto) throws StripeException {
        AppUserProfile appUserProfile = getAppUserProfile(userId);

        if (StringUtils.isBlank(appUserProfile.getAccountId())) {
            throw throwApplicationException(NOT_FOUND);
        }

        stripeService.uploadVerificationDocument(appUserProfile.getAccountId(),
                PersonUpdateParams.Verification.Document.builder()
                        .setFront(uploadDto.getFileIdFront())
                        .setBack(uploadDto.getFileIdBack())
                        .build());
    }

    public Pair<String, String> checkAndModifyVerificationStatus(String accountId) throws StripeException {
        Person person = stripeService.retrievePersonInfo(accountId);
        return Pair.of(person.getVerification().getStatus(), person.getVerification().getDetails());
    }

    public BankAccount getBankAccountInfo(Integer userId) throws StripeException {
        AppUserProfile appUserProfile = getAppUserProfile(userId);
        if (StringUtils.isBlank(appUserProfile.getAccountId())){
            return null;
        }
        return stripeService.getExternalBankInfo(appUserProfile.getAccountId());
    }

    private List<PaymentMethodResponseDTO> getConvertedPaymentResponse(PaymentMethodCollection paymentMethodCollection) throws StripeException {
        if (null == paymentMethodCollection) {
            return new ArrayList<>(ApplicationConstants.VALUE_ZERO);
        }

        return paymentMethodCollection
                .getData()
                .stream()
                .map(paymentMethod -> PaymentMethodResponseDTO.builder()
                        .paymentMethod(paymentMethod.getId())
                        .cardHolderName(paymentMethod.getBillingDetails().getName())
                        .cardType(paymentMethod.getCard().getBrand())
                        .expiration(paymentMethod.getCard().getExpMonth() +
                                ApplicationConstants.StringUtils.BACK_SLASH +
                                paymentMethod.getCard().getExpYear())
                        .lastFourDigit(paymentMethod.getCard().getLast4())
                        .build())
                .collect(Collectors.toList());
    }

    private PaymentMethodCollection getCustomerPaymentCollection(Integer userId) {
        AppUserProfile appUserProfile = getAppUserProfile(userId);
        if (StringUtils.isEmpty(appUserProfile.getCustomerKey())) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put(StripeParamsType.CUSTOMER.getValue(), appUserProfile.getCustomerKey());
        params.put(StripeParamsType.TYPE.getValue(), StripeParamsType.CARD.getValue());
        return stripeService.getCustomerPaymentCollection(params);
    }

    private Integer getTotalNumberOfPaymentMethod(Integer userId) {
        PaymentMethodCollection lists = getCustomerPaymentCollection(userId);
        return null == lists ? ApplicationConstants.VALUE_ZERO : lists.getData().size();
    }

    private AppUserProfile getAppUserProfile(Integer userId) {
        return appUserProfileRepository.findById(userId)
                .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
    }
}
