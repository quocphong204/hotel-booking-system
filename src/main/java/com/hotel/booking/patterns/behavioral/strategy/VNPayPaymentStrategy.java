package com.hotel.booking.patterns.behavioral.strategy;

import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete Strategy: VNPay QR Payment (Cổng thanh toán điện tử VNPay-QR)
 * Applies a 5% promotion discount for digital QR payments.
 */
public class VNPayPaymentStrategy implements IPaymentStrategy {

    public static final double DISCOUNT_RATE = 0.05; // 5% Discount

    @Override
    public PaymentResult pay(double amount, String customerName) {
        double discount = amount * DISCOUNT_RATE;
        double finalAmount = Math.max(0, amount - discount);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String txRef = "VNPAY-QR-" + timestamp;

        SystemAuditLogSingleton.getInstance().log(
                "Behavioral",
                "Strategy Pattern",
                String.format("VNPayPaymentStrategy: Quét mã VNPay-QR cho [%s] - Tạm tính: %,.0f VNĐ - Giảm giá QR (-5%% = %,.0f VNĐ) ➔ Thực thu: %,.0f VNĐ. Mã GD VNPay: %s.",
                        customerName, amount, discount, finalAmount, txRef)
        );

        return new PaymentResult(
                true,
                getMethodName(),
                amount,
                -discount,
                finalAmount,
                txRef,
                "Quét mã VNPay QR thành công! Áp dụng ưu đãi giảm ngay 5% vào tổng hóa đơn lưu trú."
        );
    }

    @Override
    public String getMethodName() {
        return "VNPay QR Pay (Giảm ngay 5% ưu đãi thanh toán)";
    }

    @Override
    public String getStrategyCode() {
        return "VNPAY";
    }
}
