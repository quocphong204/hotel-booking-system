package com.hotel.booking.patterns.creational.factory;

import com.hotel.booking.entity.RoomEntity;

public class StandardRoomProduct implements IRoomProduct {

    public static final String TYPE = "STANDARD";
    public static final double PRICE = 500_000.0;
    public static final String AMENITIES = "Wifi, TV 43 inch, Điều hòa, Bình đun nước, Bàn làm việc, Nước suối miễn phí";
    public static final int MAX_OCCUPANCY = 2;

    @Override
    public String getRoomType() {
        return TYPE;
    }

    @Override
    public double getBasePrice() {
        return PRICE;
    }

    @Override
    public String getDefaultAmenities() {
        return AMENITIES;
    }

    @Override
    public int getMaxOccupancy() {
        return MAX_OCCUPANCY;
    }

    @Override
    public RoomEntity createRoomEntity(String roomNumber) {
        return new RoomEntity(roomNumber, TYPE, PRICE, AMENITIES, MAX_OCCUPANCY);
    }
}
