package com.hotel.booking.patterns.behavioral.state;

import com.hotel.booking.entity.RoomStatus;

/**
 * Concrete State: Maintenance (Phòng đang được dọn dẹp, khử khuẩn hoặc bảo trì)
 */
public class MaintenanceState implements IRoomState {

    @Override
    public void book(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Phòng đang Dọn dẹp / Bảo trì! Nhân viên buồng phòng cần hoàn tất dọn phòng trước khi mở đặt lại.");
    }

    @Override
    public void checkIn(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Phòng đang trong quá trình Bảo trì / Dọn phòng, không thể Check-in cho khách!");
    }

    @Override
    public void checkOut(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Khách đã Check-out trước đó, phòng đang được nhân viên dọn dẹp!");
    }

    @Override
    public void finishMaintenance(RoomStateContext ctx) {
        ctx.setState(new AvailableState(), RoomStatus.AVAILABLE);
    }

    @Override
    public void cancelBooking(RoomStateContext ctx) {
        throw new IllegalStateException("Lỗi chuyển trạng thái: Phòng đang dọn dẹp, không có đơn đặt nào để hủy!");
    }

    @Override
    public String getStateName() {
        return "Bảo trì / Dọn dẹp (Maintenance)";
    }

    @Override
    public String getBadgeColor() {
        return "danger";
    }
}
