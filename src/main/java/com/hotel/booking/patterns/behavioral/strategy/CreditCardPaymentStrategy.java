package com.hotel.booking.patterns.behavioral.strategy;

import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

import java.util.UUID;

/**
 * Concrete Strategy: Credit Card Payment (Thẻ tín dụng Quốc tế Visa/MasterCard)
 * Applies a 1.5% international gateway processing fee.
 */
public class CreditCardPaymentStrategy implements IPaymentStrategy {

    public static final double SURCHARGE_RATE = 0.015; // 1.5%

    @Override
    public PaymentResult pay(double amount, String customerName) {
        double fee = amount * SURCHARGE_RATE;
        double finalAmount = amount + fee;
        String txRef = "CC-AUTH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        SystemAuditLogSingleton.getInstance().log(
                "Behavioral",
                "Strategy Pattern",
                String.format("CreditCardPaymentStrategy: Xử lý quẹt thẻ cho [%s] - Tạm tính: %,.0f VNĐ + Phí cổng (+1.5%% = %,.0f VNĐ) ➔ Tổng thanh toán: %,.0f VNĐ. Mã ủy quyền POS: %s.",
                        customerName, amount, fee, finalAmount, txRef)
        );

        return new PaymentResult(
                true,
                getMethodName(),
                amount,
                fee,
                finalAmount,
                txRef,
                "Ủy quyền và thanh toán thẻ tín dụng thành công qua cổng thanh toán quốc tế (+1.5% phí xử lý)."
        );
    }

    @Override
    public String getMethodName() {
        return "Thẻ tín dụng Visa/MasterCard (+1.5% phí giao dịch)";
    }

    @Override
    public String getStrategyCode() {
        return "CREDIT_CARD";
    }
}
