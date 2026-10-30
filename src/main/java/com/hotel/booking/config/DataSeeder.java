package com.hotel.booking.config;

import com.hotel.booking.dto.BookingFormDTO;
import com.hotel.booking.entity.RoomEntity;
import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;
import com.hotel.booking.patterns.structural.facade.HotelManagementFacade;
import com.hotel.booking.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * DataSeeder (CommandLineRunner)
 * Pre-populates 6 rooms (101, 102, 201, 202, 301, 302) across Standard, Deluxe, Suite
 * using RoomFactory and sets up realistic initial states (Available, Booked, Occupied, Maintenance)
 * so the evaluator can test all features and Design Patterns immediately upon startup.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final HotelManagementFacade facade;
    private final RoomRepository roomRepository;

    public DataSeeder(HotelManagementFacade facade, RoomRepository roomRepository) {
        this.facade = facade;
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) {
        if (roomRepository.count() > 0) {
            return;
        }

        SystemAuditLogSingleton.getInstance().log(
                "Creational",
                "Factory Method Pattern",
                "DataSeeder: Bắt đầu khởi tạo dữ liệu mẫu 6 phòng (Standard, Deluxe, Suite) qua RoomFactory."
        );

        // 1. Seed 6 Rooms using RoomFactory via Facade
        RoomEntity r101 = facade.createRoomViaFactory("101", "STANDARD");
        RoomEntity r102 = facade.createRoomViaFactory("102", "STANDARD");
        RoomEntity r201 = facade.createRoomViaFactory("201", "DELUXE");
        RoomEntity r202 = facade.createRoomViaFactory("202", "DELUXE");
        RoomEntity r301 = facade.createRoomViaFactory("301", "SUITE");
        RoomEntity r302 = facade.createRoomViaFactory("302", "SUITE");

        // 2. Setup realistic initial states for demo & testing:
        // Room 102: BOOKED state (with booking sample)
        BookingFormDTO book102 = new BookingFormDTO();
        book102.setRoomId(r102.getId());
        book102.setCustomerName("Trần Thị Lan");
        book102.setCustomerPhone("0912345678");
        book102.setCheckInDate(LocalDate.now());
        book102.setCheckOutDate(LocalDate.now().plusDays(2));
        book102.setBreakfastIncluded(true);
        book102.setSpecialRequests("Khách đến buổi chiều, yêu cầu phòng yên tĩnh.");
        facade.bookRoomWithBuilder(book102);

        // Room 202: OCCUPIED state (booked then checked-in)
        BookingFormDTO book202 = new BookingFormDTO();
        book202.setRoomId(r202.getId());
        book202.setCustomerName("Lê Hoàng Nam");
        book202.setCustomerPhone("0988776655");
        book202.setCheckInDate(LocalDate.now().minusDays(1));
        book202.setCheckOutDate(LocalDate.now().plusDays(2));
        book202.setExtraBed(true);
        book202.setBreakfastIncluded(true);
        book202.setSpaPackage(true);
        book202.setAirportPickup(true);
        book202.setSpecialRequests("Khách VIP, cần chuẩn bị sẵn rượu vang.");
        facade.bookRoomWithBuilder(book202);
        facade.changeRoomState(r202.getId(), "checkin"); // Transition to OCCUPIED

        // Room 302: Put in MAINTENANCE state (simulating post check-out room cleaning)
        facade.changeRoomState(r302.getId(), "book");
        facade.changeRoomState(r302.getId(), "checkin");
        facade.changeRoomState(r302.getId(), "checkout"); // Transition to MAINTENANCE

        SystemAuditLogSingleton.getInstance().log(
                "Structural",
                "Facade Pattern",
                "DataSeeder: Khởi tạo hoàn tất 6 phòng với 4 trạng thái sinh động (Trống, Đã đặt, Đang ở, Bảo trì/Dọn phòng) sẵn sàng kiểm thử!"
        );
    }
}
