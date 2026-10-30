package com.hotel.booking.patterns.creational.factory;

import com.hotel.booking.entity.RoomEntity;

/**
 * Factory Method Pattern - Product Interface
 */
public interface IRoomProduct {
    String getRoomType();
    double getBasePrice();
    String getDefaultAmenities();
    int getMaxOccupancy();
    RoomEntity createRoomEntity(String roomNumber);
}
