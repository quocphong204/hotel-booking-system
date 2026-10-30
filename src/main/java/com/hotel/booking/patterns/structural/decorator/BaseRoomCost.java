package com.hotel.booking.patterns.structural.decorator;

import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

/**
 * Concrete Component in Decorator Pattern
 * Represents the fundamental room rental cost (basePrice * nights + extra bed if applicable).
 */
public class BaseRoomCost implements IBookingCostComponent {

    private final String roomNumber;
    private final String roomType;
    private final double basePrice;
    private final int nights;
    private final boolean extraBed;
    public static final double EXTRA_BED_PER_NIGHT = 150_000.0;

    public BaseRoomCost(String roomNumber, String roomType, double basePrice, int nights, boolean extraBed) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.basePrice = basePrice;
        this.nights = Math.max(1, nights);
        this.extraBed = extraBed;

        SystemAuditLogSingleton.getInstance().log(
                "Structural",
                "Decorator Pattern (Base)",
                String.format("Khởi tạo BaseRoomCost: Phòng %s (%s) x %d đêm = %,.0f VNĐ%s.",
                        roomNumber, roomType, this.nights, (basePrice * this.nights),
                        extraBed ? String.format(" (+ Giường phụ: %,.0f VNĐ)", EXTRA_BED_PER_NIGHT * this.nights) : "")
        );
    }

    @Override
    public double calculateTotalCost() {
        double roomTotal = basePrice * nights;
        if (extraBed) {
            roomTotal += EXTRA_BED_PER_NIGHT * nights;
        }
        return roomTotal;
    }

    @Override
    public String getCostBreakdown() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("• Tiền phòng cơ bản [%s - %s]: %,.0f VNĐ x %d đêm = %,.0f VNĐ",
                roomNumber, roomType, basePrice, nights, (basePrice * nights)));
        if (extraBed) {
            sb.append(String.format("\n• Phụ thu giường phụ (Extra Bed): %,.0f VNĐ x %d đêm = %,.0f VNĐ",
                    EXTRA_BED_PER_NIGHT, nights, (EXTRA_BED_PER_NIGHT * nights)));
        }
        return sb.toString();
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getNights() {
        return nights;
    }

    public boolean isExtraBed() {
        return extraBed;
    }
}
