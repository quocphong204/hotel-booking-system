package com.hotel.booking.patterns.behavioral.strategy;

import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

/**
 * Strategy Pattern - Context
 * Maintains a reference to a Strategy object and delegates payment calculation.
 */
public class PaymentContext {

    private IPaymentStrategy strategy;

    public PaymentContext() {
        this.strategy = new CashPaymentStrategy(); // Default strategy
    }

    public PaymentContext(IPaymentStrategy strategy) {
        this.strategy = strategy != null ? strategy : new CashPaymentStrategy();
    }

    public void setStrategy(IPaymentStrategy strategy) {
        this.strategy = strategy != null ? strategy : new CashPaymentStrategy();
    }

    /**
     * Resolves the concrete strategy from string code
     */
    public void setStrategyByCode(String code) {
        if (code == null) {
            this.strategy = new CashPaymentStrategy();
            return;
        }

        switch (code.trim().toUpperCase()) {
            case "CREDIT_CARD", "CARD" -> this.strategy = new CreditCardPaymentStrategy();
            case "VNPAY", "QR" -> this.strategy = new VNPayPaymentStrategy();
            case "CASH" -> this.strategy = new CashPaymentStrategy();
            default -> this.strategy = new CashPaymentStrategy();
        }
    }

    public static IPaymentStrategy createStrategy(String code) {
        if (code == null) return new CashPaymentStrategy();
        return switch (code.trim().toUpperCase()) {
            case "CREDIT_CARD", "CARD" -> new CreditCardPaymentStrategy();
            case "VNPAY", "QR" -> new VNPayPaymentStrategy();
            default -> new CashPaymentStrategy();
        };
    }

    public PaymentResult executePayment(double amount, String customerName) {
        if (this.strategy == null) {
            this.strategy = new CashPaymentStrategy();
        }

        SystemAuditLogSingleton.getInstance().log(
                "Behavioral",
                "Strategy Pattern (Context)",
                String.format("PaymentContext: Thực thi chiến lược thanh toán [%s] cho khách hàng [%s] với số tiền gốc %,.0f VNĐ.",
                        strategy.getMethodName(), customerName, amount)
        );

        return this.strategy.pay(amount, customerName);
    }

    public IPaymentStrategy getStrategy() {
        return strategy;
    }
}
