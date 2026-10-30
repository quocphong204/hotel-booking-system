package com.hotel.booking.patterns.creational.builder;

import com.hotel.booking.entity.BookingEntity;
import com.hotel.booking.entity.RoomEntity;
import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder Pattern (Creational Group)
 * Explicit, fluent builder to construct complex BookingEntity objects with mandatory
 * validation and modular optional add-on configurations.
 * (Rule: Written manually without Lombok @Builder).
 */
public class BookingOrderBuilder {

    // Mandatory fields
    private String customerName;
    private String customerPhone;
    private RoomEntity room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int nights;

    // Optional Add-on fields
    private boolean extraBed = false;
    private boolean breakfastIncluded = false;
    private boolean spaPackage = false;
    private boolean laundryService = false;
    private boolean airportPickup = false;
    private String specialRequests = "";

    // Internal / State fields
    private Double totalEstimatedCost = 0.0;
    private String status = "CONFIRMED";
    private String bookingCode;

    public BookingOrderBuilder() {
    }

    public BookingOrderBuilder customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public BookingOrderBuilder customerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
        return this;
    }

    public BookingOrderBuilder customerInfo(String customerName, String customerPhone) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        return this;
    }

    public BookingOrderBuilder room(RoomEntity room) {
        this.room = room;
        return this;
    }

    public BookingOrderBuilder checkInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        calculateNightsIfPossible();
        return this;
    }

    public BookingOrderBuilder checkOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        calculateNightsIfPossible();
        return this;
    }

    public BookingOrderBuilder dates(LocalDate checkInDate, LocalDate checkOutDate) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        calculateNightsIfPossible();
        return this;
    }

    public BookingOrderBuilder nights(int nights) {
        this.nights = nights;
        return this;
    }

    public BookingOrderBuilder withExtraBed(boolean extraBed) {
        this.extraBed = extraBed;
        return this;
    }

    public BookingOrderBuilder withBreakfast(boolean breakfastIncluded) {
        this.breakfastIncluded = breakfastIncluded;
        return this;
    }

    public BookingOrderBuilder withSpaPackage(boolean spaPackage) {
        this.spaPackage = spaPackage;
        return this;
    }

    public BookingOrderBuilder withLaundryService(boolean laundryService) {
        this.laundryService = laundryService;
        return this;
    }

    public BookingOrderBuilder withAirportPickup(boolean airportPickup) {
        this.airportPickup = airportPickup;
        return this;
    }

    public BookingOrderBuilder withSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
        return this;
    }

    public BookingOrderBuilder totalEstimatedCost(Double totalEstimatedCost) {
        this.totalEstimatedCost = totalEstimatedCost;
        return this;
    }

    public BookingOrderBuilder status(String status) {
        this.status = status;
        return this;
    }

    public BookingOrderBuilder bookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
        return this;
    }

    private void calculateNightsIfPossible() {
        if (this.checkInDate != null && this.checkOutDate != null) {
            long days = ChronoUnit.DAYS.between(this.checkInDate, this.checkOutDate);
            this.nights = (int) Math.max(1, days);
        }
    }

    /**
     * Builds and validates the complete BookingEntity
     */
    public BookingEntity build() {
        // Validation of mandatory fields
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalStateException("Builder Error: Họ tên khách hàng là bắt buộc!");
        }
        if (customerPhone == null || customerPhone.trim().isEmpty()) {
            throw new IllegalStateException("Builder Error: Số điện thoại khách hàng là bắt buộc!");
        }
        if (room == null) {
            throw new IllegalStateException("Builder Error: Phòng đặt là bắt buộc!");
        }
        if (checkInDate == null) {
            throw new IllegalStateException("Builder Error: Ngày nhận phòng (check-in) là bắt buộc!");
        }
        if (checkOutDate == null) {
            throw new IllegalStateException("Builder Error: Ngày trả phòng (check-out) là bắt buộc!");
        }
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalStateException("Builder Error: Ngày trả phòng phải sau ngày nhận phòng!");
        }
        if (nights <= 0) {
            calculateNightsIfPossible();
            if (nights <= 0) {
                this.nights = 1;
            }
        }

        if (bookingCode == null || bookingCode.trim().isEmpty()) {
            this.bookingCode = "BK-" + System.currentTimeMillis();
        }

        // Assemble BookingEntity
        BookingEntity booking = new BookingEntity(
                bookingCode,
                customerName.trim(),
                customerPhone.trim(),
                room,
                checkInDate,
                checkOutDate,
                nights,
                extraBed,
                breakfastIncluded,
                spaPackage,
                laundryService,
                airportPickup,
                specialRequests,
                totalEstimatedCost,
                status
        );

        // Summarize add-ons for pattern log
        List<String> addOns = new ArrayList<>();
        if (extraBed) addOns.add("Kê thêm giường");
        if (breakfastIncluded) addOns.add("Bữa sáng buffet");
        if (spaPackage) addOns.add("Liệu trình Spa");
        if (laundryService) addOns.add("Dịch vụ Giặt là");
        if (airportPickup) addOns.add("Đón sân bay");
        String addOnStr = addOns.isEmpty() ? "Không có dịch vụ phụ" : String.join(", ", addOns);

        // Log to Singleton
        SystemAuditLogSingleton.getInstance().log(
                "Creational",
                "Builder Pattern",
                String.format("BookingOrderBuilder: Đã hoàn tất lắp ráp đơn đặt phòng [%s] cho khách [%s - %s] phòng [%s] (%d đêm). Tùy chọn đi kèm: [%s].",
                        bookingCode, customerName, customerPhone, room.getRoomNumber(), nights, addOnStr)
        );

        return booking;
    }
}
