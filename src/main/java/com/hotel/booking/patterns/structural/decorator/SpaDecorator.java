package com.hotel.booking.patterns.structural.decorator;

import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

/**
 * Concrete Decorator: Spa & Wellness Package
 * Adds 500,000 VND flat fee.
 */
public class SpaDecorator extends RoomServiceDecorator {

    public static final double FLAT_PRICE = 500_000.0;

    public SpaDecorator(IBookingCostComponent wrappee) {
        super(wrappee);

        SystemAuditLogSingleton.getInstance().log(
                "Structural",
                "Decorator Pattern",
                String.format("Áp dụng SpaDecorator: Bổ sung Gói trị liệu Spa & Chăm sóc sức khỏe trọn gói (+%,.0f VNĐ).",
                        FLAT_PRICE)
        );
    }

    @Override
    public double calculateTotalCost() {
        return super.calculateTotalCost() + FLAT_PRICE;
    }

    @Override
    public String getCostBreakdown() {
        return super.getCostBreakdown() + String.format("\n• Dịch vụ: Gói trị liệu Spa & Massage thư giãn = %,.0f VNĐ", FLAT_PRICE);
    }
}
