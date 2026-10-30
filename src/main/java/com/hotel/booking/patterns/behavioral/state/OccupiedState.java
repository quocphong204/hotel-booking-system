package com.hotel.booking.patterns.behavioral.state;

import com.hotel.booking.entity.RoomStatus;

/**
 * Concrete State: Occupied (Khách đang lưu trú trong phòng)
 */
public class OccupiedState implements IRoomState {

    @Override
    public void book(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Phòng đang có khách lưu trú (Occupied)! Không thể đặt phòng.");
    }

    @Override
    public void checkIn(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Khách đã Check-in và đang ở phòng này rồi!");
    }

    @Override
    public void checkOut(RoomStateContext ctx) {
        ctx.setState(new MaintenanceState(), RoomStatus.MAINTENANCE);
    }

    @Override
    public void finishMaintenance(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Phòng đang có khách lưu trú, không trong quá trình bảo trì!");
    }

    @Override
    public void cancelBooking(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Khách đang lưu trú tại phòng, không thể hủy mà phải thực hiện thủ tục Check-out thanh toán!");
    }

    @Override
    public String getStateName() {
        return "Đang ở (Occupied)";
    }

    @Override
    public String getBadgeColor() {
        return "primary";
    }
}
