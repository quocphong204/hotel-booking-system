package com.hotel.booking.patterns.creational.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogEntry {
    private final String timestamp;
    private final String patternGroup; // Creational, Structural, Behavioral
    private final String patternName;  // Factory Method, Builder, Decorator, Facade, State, Strategy, Singleton
    private final String detailMessage;
    private final String threadName;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public AuditLogEntry(String patternGroup, String patternName, String detailMessage) {
        this.timestamp = LocalDateTime.now().format(FORMATTER);
        this.patternGroup = patternGroup;
        this.patternName = patternName;
        this.detailMessage = detailMessage;
        this.threadName = Thread.currentThread().getName();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPatternGroup() {
        return patternGroup;
    }

    public String getPatternName() {
        return patternName;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getBadgeColor() {
        if (patternGroup == null) return "secondary";
        return switch (patternGroup.toLowerCase()) {
            case "creational", "khởi tạo" -> "primary";
            case "structural", "cấu trúc" -> "success";
            case "behavioral", "hành vi" -> "warning";
            default -> "info";
        };
    }

    @Override
    public String toString() {
        return String.format("[%s] [%s - %s] %s", timestamp, patternGroup, patternName, detailMessage);
    }
}
