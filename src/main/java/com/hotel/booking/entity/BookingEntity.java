package com.hotel.booking.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String bookingCode;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Column(nullable = false, length = 20)
    private String customerPhone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(nullable = false)
    private int nights;

    // Optional Add-ons
    private boolean extraBed = false;
    private boolean breakfastIncluded = false;
    private boolean spaPackage = false;
    private boolean laundryService = false;
    private boolean airportPickup = false;

    @Column(length = 500)
    private String specialRequests;

    @Column(nullable = false)
    private Double totalEstimatedCost = 0.0;

    @Column(nullable = false, length = 30)
    private String status = "CONFIRMED"; // CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED

    private LocalDateTime createdAt = LocalDateTime.now();

    public BookingEntity() {
    }

    // Constructor with all fields
    public BookingEntity(String bookingCode, String customerName, String customerPhone, RoomEntity room,
                         LocalDate checkInDate, LocalDate checkOutDate, int nights, boolean extraBed,
                         boolean breakfastIncluded, boolean spaPackage, boolean laundryService,
                         boolean airportPickup, String specialRequests, Double totalEstimatedCost,
                         String status) {
        this.bookingCode = bookingCode;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.nights = nights;
        this.extraBed = extraBed;
        this.breakfastIncluded = breakfastIncluded;
        this.spaPackage = spaPackage;
        this.laundryService = laundryService;
        this.airportPickup = airportPickup;
        this.specialRequests = specialRequests;
        this.totalEstimatedCost = totalEstimatedCost;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public boolean isExtraBed() {
        return extraBed;
    }

    public void setExtraBed(boolean extraBed) {
        this.extraBed = extraBed;
    }

    public boolean isBreakfastIncluded() {
        return breakfastIncluded;
    }

    public void setBreakfastIncluded(boolean breakfastIncluded) {
        this.breakfastIncluded = breakfastIncluded;
    }

    public boolean isSpaPackage() {
        return spaPackage;
    }

    public void setSpaPackage(boolean spaPackage) {
        this.spaPackage = spaPackage;
    }

    public boolean isLaundryService() {
        return laundryService;
    }

    public void setLaundryService(boolean laundryService) {
        this.laundryService = laundryService;
    }

    public boolean isAirportPickup() {
        return airportPickup;
    }

    public void setAirportPickup(boolean airportPickup) {
        this.airportPickup = airportPickup;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public Double getTotalEstimatedCost() {
        return totalEstimatedCost;
    }

    public void setTotalEstimatedCost(Double totalEstimatedCost) {
        this.totalEstimatedCost = totalEstimatedCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "BookingEntity{" +
                "id=" + id +
                ", bookingCode='" + bookingCode + '\'' +
                ", customerName='" + customerName + '\'' +
                ", room=" + (room != null ? room.getRoomNumber() : null) +
                ", nights=" + nights +
                ", totalEstimatedCost=" + totalEstimatedCost +
                ", status='" + status + '\'' +
                '}';
    }
}
