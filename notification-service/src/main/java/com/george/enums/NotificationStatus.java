package com.george.enums;

public enum NotificationStatus implements EnumCode {
    NEW("N"),            // Notification has been created but not yet processed
    IN_PROGRESS("I"),    // Notification is currently being sent to the recipient
    DELIVERED("D"),      // Notification has been successfully delivered to the recipient
    RESENDING("R"),      // Notification is being retried for delivery after a failure
    FAILED("F"),         // Notification delivery failed due to an error or issue
    UNDELIVERABLE("U");  // Notification cannot be delivered (e.g., system error)


    private final String code;

    NotificationStatus(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}


