package com.example.final_project_cs361;

public class CustomerBookingQueueManager {
    private static MessageTransaction messageTransaction_;

    public static void set(MessageTransaction messageTransaction) {
        messageTransaction_ = null;
        messageTransaction_ = messageTransaction;
    }

    public static MessageTransaction get() {
        return messageTransaction_;
    }
    public static boolean isNull() {
        return messageTransaction_ == null;
    }
}
