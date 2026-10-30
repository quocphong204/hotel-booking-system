package com.hotel.booking.patterns.creational.singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Singleton Pattern (Creational Group)
 * Thread-safe audit log repository using Double-Checked Locking.
 * Tracks every pattern execution across the system for live display on the Web UI sidebar.
 */
public class SystemAuditLogSingleton {

    // Volatile variable to prevent instruction reordering
    private static volatile SystemAuditLogSingleton instance;

    // Fixed capacity ring-buffer / deque for fast thread-safe concurrent additions
    private final Deque<AuditLogEntry> logEntries;
    private static final int MAX_LOGS = 100;

    // Private constructor prevents direct instantiation
    private SystemAuditLogSingleton() {
        this.logEntries = new ConcurrentLinkedDeque<>();
        // Record initialization of the Singleton itself
        this.logEntries.addFirst(new AuditLogEntry(
                "Creational",
                "Singleton Pattern",
                "SystemAuditLogSingleton được khởi tạo thành công với cơ chế Thread-safe Double-Checked Locking."
        ));
    }

    /**
     * Global access point with Double-Checked Locking
     */
    public static SystemAuditLogSingleton getInstance() {
        if (instance == null) {
            synchronized (SystemAuditLogSingleton.class) {
                if (instance == null) {
                    instance = new SystemAuditLogSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Log a design pattern execution action
     */
    public void log(String patternGroup, String patternName, String detailMessage) {
        AuditLogEntry entry = new AuditLogEntry(patternGroup, patternName, detailMessage);
        logEntries.addFirst(entry);
        while (logEntries.size() > MAX_LOGS) {
            logEntries.pollLast();
        }
    }

    /**
     * Retrieve all logs in reverse chronological order (newest first)
     */
    public List<AuditLogEntry> getLogs() {
        return Collections.unmodifiableList(new ArrayList<>(logEntries));
    }

    /**
     * Retrieve top N recent logs
     */
    public List<AuditLogEntry> getRecentLogs(int limit) {
        List<AuditLogEntry> list = new ArrayList<>(logEntries);
        if (list.size() > limit) {
            return Collections.unmodifiableList(list.subList(0, limit));
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Clear all logs
     */
    public void clear() {
        logEntries.clear();
        log("Creational", "Singleton Pattern", "Lịch sử giám sát Pattern (Audit Log) đã được đặt lại.");
    }
}
