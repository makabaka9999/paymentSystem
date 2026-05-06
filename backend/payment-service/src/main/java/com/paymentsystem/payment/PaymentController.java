package com.paymentsystem.payment;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.common.PaymentStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final Map<String, PaymentView> payments = new ConcurrentHashMap<String, PaymentView>();
    private final Map<String, String> idempotencyIndex = new ConcurrentHashMap<String, String>();

    @PostMapping
    public ApiResponse<PaymentView> create(@RequestBody CreatePaymentRequest request) {
        String key = request.getIdempotencyKey() == null ? request.getOrderId() + ":" + request.getUserId() : request.getIdempotencyKey();
        String paymentNo = idempotencyIndex.get(key);
        if (paymentNo == null) {
            paymentNo = "PAY" + Instant.now().toEpochMilli() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            idempotencyIndex.put(key, paymentNo);
        }
        PaymentView payment = payments.get(paymentNo);
        if (payment == null) {
            payment = new PaymentView(paymentNo, request.getOrderId(), request.getUserId(), request.getAmount(), PaymentStatus.INIT, Instant.now(), null, key);
            payments.put(paymentNo, payment);
        }
        return ApiResponse.ok(payment);
    }

    @PostMapping("/{paymentNo}/mock-success")
    public ApiResponse<PaymentView> mockSuccess(@PathVariable String paymentNo) {
        return complete(paymentNo, PaymentStatus.SUCCESS);
    }

    @PostMapping("/{paymentNo}/mock-fail")
    public ApiResponse<PaymentView> mockFail(@PathVariable String paymentNo) {
        return complete(paymentNo, PaymentStatus.FAILED);
    }

    @GetMapping("/{paymentNo}")
    public ApiResponse<PaymentView> get(@PathVariable String paymentNo) {
        PaymentView payment = payments.get(paymentNo);
        return payment == null ? ApiResponse.fail("PAYMENT_NOT_FOUND", "payment not found") : ApiResponse.ok(payment);
    }

    private ApiResponse<PaymentView> complete(String paymentNo, PaymentStatus target) {
        PaymentView current = payments.get(paymentNo);
        if (current == null) {
            return ApiResponse.fail("PAYMENT_NOT_FOUND", "payment not found");
        }
        if (current.getStatus() == PaymentStatus.SUCCESS || current.getStatus() == PaymentStatus.FAILED || current.getStatus() == PaymentStatus.CLOSED) {
            return ApiResponse.ok(current);
        }
        PaymentView next = current.withStatus(target);
        payments.put(paymentNo, next);
        return ApiResponse.ok(next);
    }

    public static class CreatePaymentRequest {
        private Long orderId;
        private Long userId;
        private BigDecimal amount;
        private String idempotencyKey;
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public BigDecimal getAmount() { return amount == null ? BigDecimal.ZERO : amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getIdempotencyKey() { return idempotencyKey; }
        public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    }
    public static class PaymentView {
        private String paymentNo;
        private Long orderId;
        private Long userId;
        private BigDecimal amount;
        private PaymentStatus status;
        private Instant createdAt;
        private Instant paidAt;
        private String idempotencyKey;
        public PaymentView(String paymentNo, Long orderId, Long userId, BigDecimal amount, PaymentStatus status, Instant createdAt, Instant paidAt, String idempotencyKey) {
            this.paymentNo = paymentNo; this.orderId = orderId; this.userId = userId; this.amount = amount; this.status = status; this.createdAt = createdAt; this.paidAt = paidAt; this.idempotencyKey = idempotencyKey;
        }
        PaymentView withStatus(PaymentStatus nextStatus) { return new PaymentView(paymentNo, orderId, userId, amount, nextStatus, createdAt, nextStatus == PaymentStatus.SUCCESS ? Instant.now() : paidAt, idempotencyKey); }
        public String getPaymentNo() { return paymentNo; }
        public Long getOrderId() { return orderId; }
        public Long getUserId() { return userId; }
        public BigDecimal getAmount() { return amount; }
        public PaymentStatus getStatus() { return status; }
        public Instant getCreatedAt() { return createdAt; }
        public Instant getPaidAt() { return paidAt; }
        public String getIdempotencyKey() { return idempotencyKey; }
    }
}
