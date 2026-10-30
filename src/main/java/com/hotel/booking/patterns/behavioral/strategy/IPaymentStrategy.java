package com.hotel.booking.patterns.behavioral.strategy;

/**
 * Strategy Pattern - Strategy Interface
 * Encapsulates different payment calculation algorithms and execution workflows.
 */
public interface IPaymentStrategy {

    PaymentResult pay(double amount, String customerName);

    String getMethodName();

    String getStrategyCode(); // CASH, CREDIT_CARD, VNPAY
}
