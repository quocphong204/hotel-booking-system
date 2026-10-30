package com.hotel.booking.patterns.behavioral.state;

import com.hotel.booking.entity.RoomStatus;

/**
 * Concrete State: Booked (Phòng đã được khách đặt trước)
 */
public class BookedState implements IRoomState {

    @Override
    public void book(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Phòng hiện Đã được đặt trước! Không thể tạo thêm đơn đặt mới.");
    }

    @Override
    public void checkIn(RoomStateContext ctx) {
        ctx.setState(new OccupiedState(), RoomStatus.OCCUPIED);
    }

    @Override
    public void checkOut(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Khách chưa Check-in nhận phòng, không thể thực hiện Check-out!");
    }

    @Override
    public void finishMaintenance(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Phòng đang ở trạng thái Đã đặt, không trong quá trình bảo trì!");
    }

    @Override
    public void cancelBooking(RoomStateContext ctx) {
        ctx.setState(new AvailableState(), RoomStatus.AVAILABLE);
    }

    @Override
    public String getStateName() {
        return "Đã đặt (Booked)";
    }

    @Override
    public String getBadgeColor() {
        return "warning";
    }
}
