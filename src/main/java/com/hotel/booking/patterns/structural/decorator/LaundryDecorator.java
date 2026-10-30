package com.hotel.booking.patterns.structural.decorator;

import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

/**
 * Concrete Decorator: Laundry Service
 * Adds 150,000 VND flat fee.
 */
public class LaundryDecorator extends RoomServiceDecorator {

    public static final double FLAT_PRICE = 150_000.0;

    public LaundryDecorator(IBookingCostComponent wrappee) {
        super(wrappee);

        SystemAuditLogSingleton.getInstance().log(
                "Structural",
                "Decorator Pattern",
                String.format("Áp dụng LaundryDecorator: Bổ sung Dịch vụ Giặt ủi cao cấp (+%,.0f VNĐ).", FLAT_PRICE)
        );
    }

    @Override
    public double calculateTotalCost() {
        return super.calculateTotalCost() + FLAT_PRICE;
    }

    @Override
    public String getCostBreakdown() {
        return super.getCostBreakdown() + String.format("\n• Dịch vụ: Giặt ủi cao cấp & là hơi = %,.0f VNĐ", FLAT_PRICE);
    }
}
