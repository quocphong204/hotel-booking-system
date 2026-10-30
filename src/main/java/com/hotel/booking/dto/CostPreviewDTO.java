package com.hotel.booking.dto;

public class CostPreviewDTO {
    private Long bookingId;
    private String bookingCode;
    private String customerName;
    private String customerPhone;
    private String roomNumber;
    private String roomType;
    private int nights;
    private double basePrice;
    private double totalAmount;
    private String costBreakdown;

    public CostPreviewDTO() {
    }

    public CostPreviewDTO(Long bookingId, String bookingCode, String customerName, String customerPhone,
                          String roomNumber, String roomType, int nights, double basePrice,
                          double totalAmount, String costBreakdown) {
        this.bookingId = bookingId;
        this.bookingCode = bookingCode;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.nights = nights;
        this.basePrice = basePrice;
        this.totalAmount = totalAmount;
        this.costBreakdown = costBreakdown;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCostBreakdown() {
        return costBreakdown;
    }

    public void setCostBreakdown(String costBreakdown) {
        this.costBreakdown = costBreakdown;
    }
}
