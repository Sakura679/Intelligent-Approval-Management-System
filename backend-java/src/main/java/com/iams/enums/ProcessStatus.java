package com.iams.enums;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/15 15:08
 * @Desc:
 */
public enum ProcessStatus {
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");

    private final String value;
    ProcessStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
