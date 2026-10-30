package com.hotel.booking.patterns.behavioral.state;

import com.hotel.booking.entity.RoomEntity;
import com.hotel.booking.entity.RoomStatus;
import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

/**
 * State Pattern - Context
 * Maintains the current state of a room and delegates state-specific behavior.
 */
public class RoomStateContext {

    private final RoomEntity room;
    private IRoomState currentState;

    public RoomStateContext(RoomEntity room) {
        this.room = room;
        this.currentState = resolveStateFromEntity(room);
    }

    public static IRoomState resolveStateFromEntity(RoomEntity room) {
        if (room == null || room.getStatus() == null) {
            return new AvailableState();
        }
        return switch (room.getStatus()) {
            case AVAILABLE -> new AvailableState();
            case BOOKED -> new BookedState();
            case OCCUPIED -> new OccupiedState();
            case MAINTENANCE -> new MaintenanceState();
        };
    }

    public void setState(IRoomState newState, RoomStatus newStatus) {
        String oldStateName = this.currentState != null ? this.currentState.getStateName() : "Unknown";
        this.currentState = newState;
        if (this.room != null) {
            this.room.setStatus(newStatus);
        }

        SystemAuditLogSingleton.getInstance().log(
                "Behavioral",
                "State Pattern",
                String.format("Phòng %s chuyển trạng thái: [%s] ➔ [%s].",
                        room != null ? room.getRoomNumber() : "N/A",
                        oldStateName,
                        newState.getStateName())
        );
    }

    public void book() {
        currentState.book(this);
    }

    public void checkIn() {
        currentState.checkIn(this);
    }

    public void checkOut() {
        currentState.checkOut(this);
    }

    public void finishMaintenance() {
        currentState.finishMaintenance(this);
    }

    public void cancelBooking() {
        currentState.cancelBooking(this);
    }

    public RoomEntity getRoom() {
        return room;
    }

    public IRoomState getCurrentState() {
        return currentState;
    }

    public String getCurrentStateName() {
        return currentState != null ? currentState.getStateName() : "Không rõ";
    }

    public String getCurrentBadgeColor() {
        return currentState != null ? currentState.getBadgeColor() : "secondary";
    }
}
