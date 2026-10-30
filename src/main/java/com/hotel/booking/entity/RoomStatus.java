package com.hotel.booking.entity;

public enum RoomStatus {
    AVAILABLE("Trống", "success"),
    BOOKED("Đã đặt", "warning"),
    OCCUPIED("Đang ở", "primary"),
    MAINTENANCE("Bảo trì/Dọn phòng", "danger");

    private final String displayName;
    private final String badgeColor;

    RoomStatus(String displayName, String badgeColor) {
        this.displayName = displayName;
        this.badgeColor = badgeColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeColor() {
        return badgeColor;
    }
}
