package com.hotel.booking.patterns.structural.decorator;

import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

/**
 * Concrete Decorator: Airport Pickup Service
 * Adds 300,000 VND flat fee.
 */
public class AirportPickupDecorator extends RoomServiceDecorator {

    public static final double FLAT_PRICE = 300_000.0;

    public AirportPickupDecorator(IBookingCostComponent wrappee) {
        super(wrappee);

        SystemAuditLogSingleton.getInstance().log(
                "Structural",
                "Decorator Pattern",
                String.format("Áp dụng AirportPickupDecorator: Bổ sung Dịch vụ Xe đưa đón sân bay riêng (+%,.0f VNĐ).", FLAT_PRICE)
        );
    }

    @Override
    public double calculateTotalCost() {
        return super.calculateTotalCost() + FLAT_PRICE;
    }

    @Override
    public String getCostBreakdown() {
        return super.getCostBreakdown() + String.format("\n• Dịch vụ: Xe đưa đón sân bay riêng = %,.0f VNĐ", FLAT_PRICE);
    }
}
