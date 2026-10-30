package com.hotel.booking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String invoiceCode;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingEntity booking;

    @Column(nullable = false)
    private Double subTotal;

    @Column(nullable = false)
    private Double adjustmentAmount; // Positive for fees, negative for discounts

    @Column(nullable = false)
    private Double finalAmount;

    @Column(nullable = false, length = 50)
    private String paymentMethod; // CASH, CREDIT_CARD, VNPAY

    @Column(columnDefinition = "TEXT")
    private String costBreakdown;

    @Column(length = 100)
    private String transactionRef;

    @Column(nullable = false, length = 20)
    private String status = "PAID";

    private LocalDateTime paymentDate = LocalDateTime.now();

    public InvoiceEntity() {
    }

    public InvoiceEntity(String invoiceCode, BookingEntity booking, Double subTotal,
                         Double adjustmentAmount, Double finalAmount, String paymentMethod,
                         String costBreakdown, String transactionRef) {
        this.invoiceCode = invoiceCode;
        this.booking = booking;
        this.subTotal = subTotal;
        this.adjustmentAmount = adjustmentAmount;
        this.finalAmount = finalAmount;
        this.paymentMethod = paymentMethod;
        this.costBreakdown = costBreakdown;
        this.transactionRef = transactionRef;
        this.status = "PAID";
        this.paymentDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public BookingEntity getBooking() {
        return booking;
    }

    public void setBooking(BookingEntity booking) {
        this.booking = booking;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(Double adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public Double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCostBreakdown() {
        return costBreakdown;
    }

    public void setCostBreakdown(String costBreakdown) {
        this.costBreakdown = costBreakdown;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "InvoiceEntity{" +
                "id=" + id +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", finalAmount=" + finalAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
