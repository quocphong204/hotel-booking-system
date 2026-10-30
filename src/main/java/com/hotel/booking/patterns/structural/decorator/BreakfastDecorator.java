package com.hotel.booking.patterns.structural.decorator;

import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

/**
 * Concrete Decorator: Buffet Breakfast
 * Adds 200,000 VND per night to the booking cost.
 */
public class BreakfastDecorator extends RoomServiceDecorator {

    public static final double PRICE_PER_NIGHT = 200_000.0;
    private final int nights;

    public BreakfastDecorator(IBookingCostComponent wrappee, int nights) {
        super(wrappee);
        this.nights = Math.max(1, nights);

        SystemAuditLogSingleton.getInstance().log(
                "Structural",
                "Decorator Pattern",
                String.format("Áp dụng BreakfastDecorator: Bổ sung Bữa sáng Buffet (+%,.0f VNĐ/đêm x %d đêm = %,.0f VNĐ).",
                        PRICE_PER_NIGHT, this.nights, (PRICE_PER_NIGHT * this.nights))
        );
    }

    @Override
    public double calculateTotalCost() {
        return super.calculateTotalCost() + (PRICE_PER_NIGHT * nights);
    }

    @Override
    public String getCostBreakdown() {
        return super.getCostBreakdown() + String.format("\n• Dịch vụ: Buffet sáng hàng ngày (%,.0f VNĐ/đêm x %d đêm) = %,.0f VNĐ",
                PRICE_PER_NIGHT, nights, (PRICE_PER_NIGHT * nights));
    }
}
