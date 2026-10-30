package com.hotel.booking.dto;

public class TestCaseResultDTO {
    private String testId;
    private String patternGroup;
    private String patternName;
    private String description;
    private String status; // PASSED / FAILED
    private long executionTimeMs;
    private String details;
    private String verificationNote;

    public TestCaseResultDTO() {
    }

    public TestCaseResultDTO(String testId, String patternGroup, String patternName,
                             String description, String status, long executionTimeMs,
                             String details, String verificationNote) {
        this.testId = testId;
        this.patternGroup = patternGroup;
        this.patternName = patternName;
        this.description = description;
        this.status = status;
        this.executionTimeMs = executionTimeMs;
        this.details = details;
        this.verificationNote = verificationNote;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getPatternGroup() {
        return patternGroup;
    }

    public void setPatternGroup(String patternGroup) {
        this.patternGroup = patternGroup;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getVerificationNote() {
        return verificationNote;
    }

    public void setVerificationNote(String verificationNote) {
        this.verificationNote = verificationNote;
    }
}
