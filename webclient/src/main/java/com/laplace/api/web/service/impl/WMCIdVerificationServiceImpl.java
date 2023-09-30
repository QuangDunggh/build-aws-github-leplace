package com.laplace.api.web.service.impl;

import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.StripeParamsType;
import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.dto.request.DocumentUploadDto;
import com.laplace.api.common.dto.request.StripeAccountRequestDTO;
import com.laplace.api.common.dto.response.BankAccountInfo;
import com.laplace.api.common.model.db.Address;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.AppUserProfile;
import com.laplace.api.common.pay.jp.CustomerService;
import com.laplace.api.common.service.AddressService;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.util.UserContext;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.security.service.JwtTokenService;
import com.laplace.api.web.service.WMCAuthenticationService;
import com.laplace.api.web.service.WMCEmailService;
import com.laplace.api.web.service.WMCIdVerificationService;
import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import com.stripe.param.AccountCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.ZonedDateTime;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.*;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Slf4j
@Service
public class WMCIdVerificationServiceImpl implements WMCIdVerificationService {

    private final AuthenticationFacade authenticationFacade;
    private final AddressService addressService;
    private final AppUserService appUserService;
    private final CustomerService customerService;
    private final WMCAuthenticationService authenticationService;
    private final JwtTokenService jwtTokenService;
    private final WMCEmailService wmcEmailService;


    @Autowired
    public WMCIdVerificationServiceImpl(
            AuthenticationFacade authenticationFacade,
            AddressService addressService, AppUserService appUserService,
            CustomerService customerService, JwtTokenService jwtTokenService,
            WMCAuthenticationService authenticationService, WMCEmailService wmcEmailService) {
        this.authenticationFacade = authenticationFacade;
        this.addressService = addressService;
        this.appUserService = appUserService;
        this.customerService = customerService;
        this.authenticationService = authenticationService;
        this.jwtTokenService = jwtTokenService;
        this.wmcEmailService = wmcEmailService;
    }

    @Override
    public String createStripeAccount(StripeAccountRequestDTO stripeAccountRequestDTO) throws StripeException {
        UserContext userContext = authenticationFacade.getUserContext()
            .orElseThrow(() -> throwApplicationException(AUTH_FAILURE));
        Integer userId = userContext.getUserId();

        AppUser user = appUserService.findById(userId)
                .orElseThrow(() -> throwApplicationException(USER_PROFILE_NOT_FOUND));

        if (ObjectUtils.isEmpty(customerService.getCustomerPaymentMethods(userId))) {
            throw throwApplicationException(CUSTOMER_CARD_NOT_FOUND);
        }

        Address address = addressService.getAddressById(stripeAccountRequestDTO.getAddressId());

        customerService.createStripeAccount(userId, getCustomAccountCreateParams(stripeAccountRequestDTO, user, address));

        user.setVerificationStatus(VerificationStatus.BEING_VERIFIED);

        appUserService.saveUser(user);

        authenticationService.logout(userId);

        return jwtTokenService.getTokens(user, userContext.getAuthenticationType(), AppType.WEB_CLIENT).getAccessToken().getToken();
    }

    @Override
    public Boolean uploadVerificationDocument(DocumentUploadDto documentUploadDto) throws StripeException {
        Integer userId = authenticationFacade.getUserId();

        AppUser user = appUserService.findById(userId)
                .orElseThrow(() -> throwApplicationException(USER_PROFILE_NOT_FOUND));

        if (ObjectUtils.nullSafeEquals(user.getVerificationStatus(),
                VerificationStatus.NOT_VERIFIED) || ObjectUtils.nullSafeEquals(user.getVerificationStatus(),
                VerificationStatus.VERIFIED)) {
            throw throwApplicationException(OPERATION_TYPE_UNDEFINED);
        }

        validateVerificationDocuments(documentUploadDto);

        customerService.uploadVerificationDocument(userId, documentUploadDto);

        user.setVerificationStatus(VerificationStatus.ID_VERIFICATION_ON_GOING);

        appUserService.saveUser(user);
        return true;
    }

    @Override
    public BankAccountInfo findBankAccount() {
        BankAccount bankAccount = null;
        try {
            bankAccount = customerService.getBankAccountInfo(authenticationFacade.getUserId());
        } catch (StripeException err) {
            log.info(err.getLocalizedMessage());
        }
        return BankAccountInfo.make(bankAccount);
    }

    @Override
    public void checkAndModifyVerificationStatus(Integer userId) throws StripeException {
        AppUser user = appUserService.findById(userId)
                .orElseThrow(() -> throwApplicationException(USER_PROFILE_NOT_FOUND));

        AppUserProfile profile = user.getAppUserProfile();

        Pair<String, String> verification = customerService.checkAndModifyVerificationStatus(profile.getAccountId());
        if (verification.getLeft().equals("verified")) {
            user.setVerificationStatus(VerificationStatus.VERIFIED);
            wmcEmailService.sendIdVerificationSuccessMail(user.getEmail(), Languages.JAPANESE);
        } else if (verification.getLeft().equals("unverified")) {
            user.setVerificationStatus(VerificationStatus.ID_VERIFICATION_FAILED);
            wmcEmailService.sendIdVerificationFailedMail(user.getEmail(), Languages.JAPANESE);
        }
        appUserService.saveUser(user);
    }

    private AccountCreateParams getCustomAccountCreateParams(StripeAccountRequestDTO accountRequestDTO, AppUser user, Address address) {
        return AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.CUSTOM)
                .setCountry(StripeParamsType.COUNTRY.getValue())
                .setDefaultCurrency(StripeParamsType.CURRENCY_JP.getValue())
                .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                .setCapabilities(getAccountCapabilities())
                .setBusinessProfile(getBusinessProfile())
                .setExternalAccount(accountRequestDTO.getExternalAccount())
                .setTosAcceptance(getTOSAcceptanceInfo(accountRequestDTO))
                .setIndividual(getIndividualInfo(user.getEmail(), address, user.getAppUserProfile()))
                .build();
    }

    private AccountCreateParams.Capabilities getAccountCapabilities() {
        return AccountCreateParams.Capabilities.builder()
                .setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder()
                        .setRequested(true)
                        .build())
                .setTransfers(AccountCreateParams.Capabilities.Transfers.builder()
                        .setRequested(true)
                        .build())
                .build();
    }

    private AccountCreateParams.BusinessProfile getBusinessProfile() {
        return AccountCreateParams.BusinessProfile.builder()
                .setMcc(StripeParamsType.MCC.getValue())
                .setUrl(StripeParamsType.URL.getValue())
                .setProductDescription(StripeParamsType.PRODUCT_DESCRIPTIONS.getValue())
                .build();
    }

    private AccountCreateParams.TosAcceptance getTOSAcceptanceInfo(StripeAccountRequestDTO accountRequestDTO) {
        return AccountCreateParams.TosAcceptance.builder()
                .setDate(ZonedDateTime.now().toEpochSecond())
                .setIp(accountRequestDTO.getIp())
                .build();
    }

    private AccountCreateParams.Individual getIndividualInfo(String email, Address address, AppUserProfile appUserProfile) {
        return AccountCreateParams.Individual
                .builder()
                .setAddressKana(getIndividualKanaAddress(address))
                .setAddressKanji(getIndividualKanjiAddress(address))
                .setDob(getIndividualDOB(appUserProfile.getBirthDate()))
                .setEmail(email)
                .setPhone(appUserProfile.getPhoneNumber())
                .setFirstNameKana(appUserProfile.getLastNameKana())
                .setLastNameKana(appUserProfile.getFirstNameKana())
                .setFirstNameKanji(appUserProfile.getLastName())
                .setLastNameKanji( appUserProfile.getFirstName())
                .build();
    }

    private AccountCreateParams.Individual.AddressKana getIndividualKanaAddress(Address address) {
        return AccountCreateParams.Individual.AddressKana.builder()
                .setCountry(StripeParamsType.COUNTRY.getValue())
                .setPostalCode(address.getZip())
                .setLine1(address.getStreetKana())
                .setLine2(address.getBuildingNameRoomNumberKana())
                .build();
    }

    private AccountCreateParams.Individual.AddressKanji getIndividualKanjiAddress(Address address) {
        return AccountCreateParams.Individual.AddressKanji.builder()
                .setCountry(StripeParamsType.COUNTRY.getValue())
                .setPostalCode(address.getZip())
                .setTown(address.getMunicipalityChome().replace(":", " ") + "丁目")
                .setLine1(address.getStreet())
                .setLine2(address.getBuildingNameRoomNumber())
                .build();
    }

    private AccountCreateParams.Individual.Dob getIndividualDOB(ZonedDateTime date) {
        return AccountCreateParams.Individual.Dob.builder()
                .setDay((long) date.getDayOfMonth())
                .setMonth((long) date.getMonthValue())
                .setYear((long) date.getYear())
                .build();
    }

    private void validateVerificationDocuments(DocumentUploadDto documentUploadDto) {
        if (StringUtils.isBlank(documentUploadDto.getFileIdBack()) && StringUtils.isBlank(documentUploadDto.getFileIdFront())) {
            throw throwApplicationException(INVALID_ARGUMENT);
        }
    }
}
