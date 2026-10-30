package com.hotel.booking.patterns.structural.decorator;

/**
 * Decorator Pattern - Component Interface
 * Defines contracts for calculating booking costs and generating itemized breakdown.
 */
public interface IBookingCostComponent {
    double calculateTotalCost();
    String getCostBreakdown();
}
