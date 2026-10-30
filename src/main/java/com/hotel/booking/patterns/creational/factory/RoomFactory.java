package com.hotel.booking.patterns.creational.factory;

import com.hotel.booking.entity.RoomEntity;
import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

/**
 * Factory Method Pattern
 * Encapsulates the instantiation logic for various room types.
 */
public class RoomFactory {

    /**
     * Factory method returning the appropriate IRoomProduct implementation
     */
    public static IRoomProduct createRoomProduct(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Loại phòng không được để trống!");
        }

        String normalizedType = type.trim().toUpperCase();
        IRoomProduct product;

        switch (normalizedType) {
            case "STANDARD" -> product = new StandardRoomProduct();
            case "DELUXE" -> product = new DeluxeRoomProduct();
            case "SUITE" -> product = new SuiteRoomProduct();
            default -> throw new IllegalArgumentException("Loại phòng không hợp lệ: '" + type + "'. Hỗ trợ: STANDARD, DELUXE, SUITE.");
        }

        return product;
    }

    /**
     * Creates and initializes a RoomEntity based on the room type product
     */
    public static RoomEntity createRoomEntity(String roomNumber, String type) {
        IRoomProduct product = createRoomProduct(type);
        RoomEntity room = product.createRoomEntity(roomNumber);

        // Pattern Trace Log
        SystemAuditLogSingleton.getInstance().log(
                "Creational",
                "Factory Method Pattern",
                String.format("RoomFactory: Đã khởi tạo cấu hình phòng [%s] cho phòng số %s (Giá cơ bản: %,.0f VNĐ, Sức chứa: %d khách).",
                        product.getRoomType(), roomNumber, product.getBasePrice(), product.getMaxOccupancy())
        );

        return room;
    }
}
