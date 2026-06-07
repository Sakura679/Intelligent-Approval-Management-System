package com.iams.enums;


/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/5 11:05
 * @Desc:
 */
public enum FieldsType {
    CREATETIME("setCreateTime"),
    UPDATETIME("setUpdateTime"),
    CREATEUSER("setCreateUser"),
    UPDATEUSER("setUpdateUser");

    private final String methodName;
    FieldsType(String value) {
        this.methodName = value;
    }

    public String getMethodName() {
        return methodName;
    }
}
