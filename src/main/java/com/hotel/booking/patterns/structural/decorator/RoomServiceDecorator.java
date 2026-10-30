package com.hotel.booking.patterns.structural.decorator;

/**
 * Decorator Pattern - Abstract Decorator
 * Wraps an IBookingCostComponent and delegates cost calculation and breakdown.
 */
public abstract class RoomServiceDecorator implements IBookingCostComponent {

    protected final IBookingCostComponent wrappee;

    public RoomServiceDecorator(IBookingCostComponent wrappee) {
        if (wrappee == null) {
            throw new IllegalArgumentException("Thành phần chi phí bọc (wrappee) không được null!");
        }
        this.wrappee = wrappee;
    }

    @Override
    public double calculateTotalCost() {
        return wrappee.calculateTotalCost();
    }

    @Override
    public String getCostBreakdown() {
        return wrappee.getCostBreakdown();
    }
}
