package com.hotel.booking.patterns.behavioral.state;

import com.hotel.booking.entity.RoomStatus;

/**
 * Concrete State: Available (Phòng trống sẵn sàng đón khách)
 */
public class AvailableState implements IRoomState {

    @Override
    public void book(RoomStateContext ctx) {
        ctx.setState(new BookedState(), RoomStatus.BOOKED);
    }

    @Override
    public void checkIn(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Phòng đang Trống, chưa được Đặt trước! Vui lòng Đặt phòng trước khi Check-in.");
    }

    @Override
    public void checkOut(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Phòng đang Trống, không có khách lưu trú để Check-out!");
    }

    @Override
    public void finishMaintenance(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Phòng đã Sẵn sàng / Trống, không trong quá trình bảo trì dọn phòng!");
    }

    @Override
    public void cancelBooking(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Phòng đang Trống, không có đơn đặt nào để hủy!");
    }

    @Override
    public String getStateName() {
        return "Trống (Available)";
    }

    @Override
    public String getBadgeColor() {
        return "success";
    }
}
