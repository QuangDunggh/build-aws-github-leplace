package com.laplace.api.common.repository.db;

import com.laplace.api.common.constants.enums.PaymentType;
import com.laplace.api.common.model.db.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Integer> {

  List<Payment> findPaymentByOrderId(String orderId);

  Optional<Payment> findPaymentByOrderIdAndPaymentRefId(String orderId, Integer paymentRefId);

  Page<Payment> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(Integer userId,
      ZonedDateTime startTime, ZonedDateTime endTime, Pageable pageRequest);

  Page<Payment> findByCreatedAtBetweenOrderByCreatedAtDesc(ZonedDateTime startTime,
      ZonedDateTime endTime, Pageable pageRequest);

  Page<Payment> findPaymentBySellerIdAndCreatedAtBetweenAndId(Integer sellerId, Date startTime,
      Date endTime, Integer paymentId, Pageable pageRequest);

  Page<Payment> findPaymentBySellerIdAndCreatedAtBetweenAndUserNameContainingIgnoreCase(
      Integer clientId, Date startTime, Date endTime, String userName, Pageable pageRequest);

  @Query(value = "select * from payments where " +
      "seller_id = :sellerId and " +
      "created_at between :startTime and :endTime " +
      "and (id = :id or user_name like %:userName%)", nativeQuery = true)
  Page<Payment> findPaymentBySellerIdAndCreatedAtBetweenAndIdOrUserNameContainingIgnoreCase(
      @Param("sellerId") Integer sellerId,
      @Param("startTime") Date startTime,
      @Param("endTime") Date endTime,
      @Param("id") Integer paymentId,
      @Param("userName") String userName,
      Pageable pageRequest);

  Optional<Payment> findPaymentByChargeIdAndOrderIdAndPaymentType(String chargeId, String orderId,
      PaymentType paymentType);

  Optional<Payment> findPaymentByChargeIdAndPaymentType(String chargeId, PaymentType paymentType);

  Optional<Payment> findFirstByChargeIdAndOrderIdAndPaymentTypeOrderByRefundAmountDesc(
      String chargeId, String orderId, PaymentType paymentType);
}