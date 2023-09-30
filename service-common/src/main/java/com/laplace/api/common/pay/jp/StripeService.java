package com.laplace.api.common.pay.jp;

import com.laplace.api.common.configuration.aws.LaplaceSecretsManager;
import com.laplace.api.common.constants.ApplicationConstants;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.*;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Slf4j
@Service
public class StripeService {

    private final LaplaceSecretsManager laplaceSecretsManager;

    @Autowired
    public StripeService(LaplaceSecretsManager laplaceSecretsManager) {
        this.laplaceSecretsManager = laplaceSecretsManager;
        try {
            Stripe.apiKey = laplaceSecretsManager.getSecretsManagerKey().getSecret();
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw throwApplicationException(STRIPE_CREDENTIALS_ERROR);
        }
    }

    public String getSetUpIntent(Map<String, Object> params) {
        try {
            return SetupIntent.create(params).getClientSecret();
        } catch (StripeException e) {
            log.error(e.getLocalizedMessage());
            throw throwApplicationException(SETUP_INTENT_CREATION_FAILED);
        }
    }

    public String createCustomerId(CustomerCreateParams customerMap) {
        try {
            Customer customer = Customer.create(customerMap);
            return customer.getId();
        } catch (StripeException e) {
            log.error(e.getLocalizedMessage());
            throw throwApplicationException(CUSTOMER_NOT_FOUND);
        }
    }

    public boolean detachCustomerPaymentMethod(String PaymentMethodId) {
        try {
            PaymentMethod.retrieve(PaymentMethodId).detach();
        } catch (StripeException e) {
            log.error(e.getLocalizedMessage());
            throw throwApplicationException(CUSTOMER_CARD_NOT_FOUND);
        }
        return true;
    }

    public PaymentMethodCollection getCustomerPaymentCollection(Map<String, Object> params) {
        try {
            return PaymentMethod.list(params);
        } catch (StripeException e) {
            log.error(e.getLocalizedMessage());
            throw throwApplicationException(CUSTOMER_CARD_NOT_FOUND);
        }
    }

    public PaymentMethod getPaymentMethodInfo(String paymentMethodId) {
        try {
            return PaymentMethod.retrieve(paymentMethodId);
        } catch (StripeException e) {
            log.error(e.getLocalizedMessage());
            throw throwApplicationException(CUSTOMER_CARD_NOT_FOUND);
        }
    }

    public PaymentIntent retrievePaymentIntentFromId(String intentId) {
        try {
            return PaymentIntent.retrieve(intentId);
        } catch (StripeException e) {
            log.error(e.getLocalizedMessage());
            throw throwApplicationException(INVALID_PAYMENT_INTENT_PROVIDED);
        }
    }

    public PaymentIntent createPaymentIntent(PaymentIntentCreateParams params) throws StripeException {
        return PaymentIntent.create(params);
    }

    public PaymentIntent capturePaymentIntent(PaymentIntent paymentIntent) throws StripeException {
        return paymentIntent.capture();
    }

    public PaymentIntent cancelPaymentIntent(PaymentIntent paymentIntent) throws StripeException {
        return paymentIntent.cancel();
    }

    public Account createCustomConnectAccount(AccountCreateParams params) throws StripeException {
        return Account.create(params);
    }

    public void uploadVerificationDocument(String accountId, PersonUpdateParams.Verification.Document documents)
            throws StripeException {
        Person person = retrievePersonInfo(accountId);

        person.update(PersonUpdateParams.builder()
                .setVerification(PersonUpdateParams.Verification.builder()
                        .setDocument(documents)
                        .build())
                .build());
    }

    public Person retrievePersonInfo(String accountId) throws StripeException {
        Account account = getAccount(accountId);

        Map<String, Object> params = new HashMap<>();
        params.put("limit", ApplicationConstants.ONE);

        PersonCollection persons = account.persons().list(params);

        if (persons.getData().isEmpty()) {
            throw throwApplicationException(NOT_FOUND);
        }

        return persons.getData().get(ApplicationConstants.VALUE_ZERO);
    }

    public BankAccount getExternalBankInfo(String accountId) throws StripeException {
        Account account = getAccount(accountId);

        Map<String, Object> params = new HashMap<>();
        params.put("object", "bank_account");
        params.put("limit", ApplicationConstants.ONE);

        ExternalAccountCollection bankAccounts = account.getExternalAccounts().list(params);

        if (bankAccounts.getData().isEmpty()) {
            return null;
        }
        return (BankAccount) (bankAccounts.getData().get(ApplicationConstants.VALUE_ZERO));
    }

    public Account getAccount(String accountId) throws StripeException {
        Account account = Account.retrieve(accountId);
        if (null == account || account.getPayoutsEnabled() || account.getRequirements().getCurrentlyDue().isEmpty()) {
            return account;
        }

        log.warn("Payouts enabled:: " + account.getPayoutsEnabled() + " :: deleted " + account.getDeleted() + " :: "
                + account.getRequirements().getCurrentlyDue() + " :: "
                + account.getRequirements().getPendingVerification()
        );

        return account;
    }

    public void fullRefundIfAlreadyPaid(String paymentIntentId) throws StripeException {
        Refund.create(RefundCreateParams.builder()
                .setPaymentIntent(paymentIntentId)
                .build());
    }

    public void partialRefundIfValid(String paymentIntentId, long amount) throws StripeException {
        Refund.create(RefundCreateParams.builder()
                .setAmount(amount)
                .setPaymentIntent(paymentIntentId)
                .build());
    }
}
