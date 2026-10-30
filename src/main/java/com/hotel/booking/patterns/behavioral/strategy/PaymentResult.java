package com.hotel.booking.patterns.behavioral.strategy;

public class PaymentResult {
    private final boolean success;
    private final String methodName;
    private final double subTotal;
    private final double adjustmentAmount; // Positive for fee, negative for discount
    private final double finalAmount;
    private final String transactionRef;
    private final String message;

    public PaymentResult(boolean success, String methodName, double subTotal,
                         double adjustmentAmount, double finalAmount,
                         String transactionRef, String message) {
        this.success = success;
        this.methodName = methodName;
        this.subTotal = subTotal;
        this.adjustmentAmount = adjustmentAmount;
        this.finalAmount = finalAmount;
        this.transactionRef = transactionRef;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMethodName() {
        return methodName;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("Phương thức: %s | Tạm tính: %,.0f VNĐ | Điều chỉnh: %,.0f VNĐ | Thành tiền: %,.0f VNĐ | Mã GD: %s",
                methodName, subTotal, adjustmentAmount, finalAmount, transactionRef);
    }
}
