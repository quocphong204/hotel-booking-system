package com.hotel.booking.patterns.behavioral.state;

/**
 * State Pattern - State Interface
 * Defines state transitions for a hotel room.
 */
public interface IRoomState {

    void book(RoomStateContext ctx);

    void checkIn(RoomStateContext ctx);

    void checkOut(RoomStateContext ctx);

    void finishMaintenance(RoomStateContext ctx);

    void cancelBooking(RoomStateContext ctx);

    String getStateName();

    String getBadgeColor();
}
