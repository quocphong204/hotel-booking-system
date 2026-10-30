package com.hotel.booking.patterns.creational.factory;

import com.hotel.booking.entity.RoomEntity;

public class SuiteRoomProduct implements IRoomProduct {

    public static final String TYPE = "SUITE";
    public static final double PRICE = 2_500_000.0;
    public static final String AMENITIES = "Wifi VIP không giới hạn, Smart TV 65 inch, Phòng khách riêng biệt, Bồn sục Jacuzzi đôi, Rượu vang đón tiếp, Dịch vụ quản gia 24/7";
    public static final int MAX_OCCUPANCY = 4;

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
