package com.hotel.booking.patterns.creational.factory;

import com.hotel.booking.entity.RoomEntity;

public class DeluxeRoomProduct implements IRoomProduct {

    public static final String TYPE = "DELUXE";
    public static final double PRICE = 1_000_000.0;
    public static final String AMENITIES = "Wifi tốc độ cao, Smart TV 55 inch, Ban công view biển, Bồn tắm nằm, Minibar, Máy pha cà phê cao cấp";
    public static final int MAX_OCCUPANCY = 3;

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
