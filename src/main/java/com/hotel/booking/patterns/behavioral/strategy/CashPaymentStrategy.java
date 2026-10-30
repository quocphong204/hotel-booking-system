package com.hotel.booking.patterns.behavioral.strategy;

import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

import java.util.UUID;

/**
 * Concrete Strategy: Cash Payment (Tiền mặt tại quầy lễ tân)
 * No transaction fee, no discount.
 */
public class CashPaymentStrategy implements IPaymentStrategy {

    @Override
    public PaymentResult pay(double amount, String customerName) {
        String txRef = "CASH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        double adjustment = 0.0;
        double finalAmount = amount;

        SystemAuditLogSingleton.getInstance().log(
                "Behavioral",
                "Strategy Pattern",
                String.format("CashPaymentStrategy: Lập phiếu thu tiền mặt tại quầy cho [%s] - Số tiền: %,.0f VNĐ. Mã hóa đơn: %s.",
                        customerName, finalAmount, txRef)
        );

        return new PaymentResult(
                true,
                getMethodName(),
                amount,
                adjustment,
                finalAmount,
                txRef,
                "Thu tiền mặt trực tiếp tại quầy lễ tân thành công. Đã xuất phiếu thu."
        );
    }

    @Override
    public String getMethodName() {
        return "Tiền mặt tại quầy (Cash - Miễn phí)";
    }

    @Override
    public String getStrategyCode() {
        return "CASH";
    }
}
