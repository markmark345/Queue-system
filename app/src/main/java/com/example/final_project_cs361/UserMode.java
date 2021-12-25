package com.example.final_project_cs361;

public class UserMode {
    public enum Mode {
        UNINITIALISE,
        CUSTOMER,
        MERCHANT
    }

    private static Mode mode_ = Mode.UNINITIALISE;

    public static Mode getMode_() {
        return mode_;
    }

    public static void setMode_(Mode mode) {
        mode_ = mode;
    }
}
