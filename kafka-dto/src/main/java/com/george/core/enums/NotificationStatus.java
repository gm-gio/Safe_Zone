package com.george.core.enums;



public enum NotificationStatus implements EnumCode {

    NEW("NEW"),                    // Notification has been created but not yet processed
    IN_PROGRESS("IN_PROGRESS"),    // Notification is currently being sent to the recipient
    DELIVERED("DELIVERED"),        // Notification has been successfully delivered to the recipient
    RESENDING("RESENDING"),        // Notification is being retried for delivery after a failure
    FAILED("FAILED"),              // Notification delivery failed due to an error or issue
    UNDELIVERABLE("UNDELIVERABLE"); // Notification cannot be delivered (e.g., system error)

    private final String code;

    NotificationStatus(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
