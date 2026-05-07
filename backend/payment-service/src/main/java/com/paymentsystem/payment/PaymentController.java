package com.paymentsystem.payment;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.common.PaymentStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付接口控制器，提供创建支付单、模拟支付成功/失败和查询支付单能力。
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    /** 以内存 Map 模拟支付单存储。 */
    private final Map<String, PaymentView> payments = new ConcurrentHashMap<String, PaymentView>();
    /** 幂等键到支付单号的索引。 */
    private final Map<String, String> idempotencyIndex = new ConcurrentHashMap<String, String>();

    /**
     * 创建支付单，同一幂等键重复请求会返回同一支付单。
     *
     * @param request 创建支付请求
     * @return 支付单视图
     */
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

    /**
     * 模拟第三方支付成功回调。
     *
     * @param paymentNo 支付单号
     * @return 支付单视图
     */
    @PostMapping("/{paymentNo}/mock-success")
    public ApiResponse<PaymentView> mockSuccess(@PathVariable String paymentNo) {
        return complete(paymentNo, PaymentStatus.SUCCESS);
    }

    /**
     * 模拟第三方支付失败回调。
     *
     * @param paymentNo 支付单号
     * @return 支付单视图
     */
    @PostMapping("/{paymentNo}/mock-fail")
    public ApiResponse<PaymentView> mockFail(@PathVariable String paymentNo) {
        return complete(paymentNo, PaymentStatus.FAILED);
    }

    /**
     * 查询指定支付单。
     *
     * @param paymentNo 支付单号
     * @return 支付单视图
     */
    @GetMapping("/{paymentNo}")
    public ApiResponse<PaymentView> get(@PathVariable String paymentNo) {
        PaymentView payment = payments.get(paymentNo);
        return payment == null ? ApiResponse.fail("PAYMENT_NOT_FOUND", "payment not found") : ApiResponse.ok(payment);
    }

    /**
     * 完成支付单状态流转，终态支付单保持幂等。
     *
     * @param paymentNo 支付单号
     * @param target 目标支付状态
     * @return 支付单视图
     */
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
}
