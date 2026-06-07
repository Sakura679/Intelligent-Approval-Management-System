package com.iams.aop;

import com.iams.annotations.AutoFilled;
import com.iams.context.UserContext;
import com.iams.enums.AutoFillType;
import com.iams.enums.FieldsType;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/4 19:53
 * @Desc:
 */
@Aspect
@Component
public class DbDateTimeAop {

    @Before("@annotation(autoFilled)")
    public void beforeMethod(JoinPoint joinPoint, AutoFilled autoFilled) {
        Object[] args = joinPoint.getArgs();
        AutoFillType autoFillType = autoFilled.value();

        for (Object obj : args) {
            if (obj != null) {
                autoFillFields(obj, autoFillType);
            }
        }
    }

    private void autoFillFields(Object obj, AutoFillType autoFillType) {
        Class<?> aClass = obj.getClass();
        LocalDateTime dateTime = LocalDateTime.now();

        if (Objects.requireNonNull(autoFillType) == AutoFillType.INSERT) {
            setFieldValue(obj, aClass, FieldsType.CREATETIME.getMethodName(), dateTime);
            setFieldValue(obj, aClass, FieldsType.UPDATETIME.getMethodName(), dateTime);
            setFieldValue(obj, aClass, FieldsType.CREATEUSER.getMethodName(), UserContext.getCurrentUserId());
            setFieldValue(obj, aClass, FieldsType.UPDATEUSER.getMethodName(), UserContext.getCurrentUserId());
        } else if (autoFillType == AutoFillType.UPDATE) {
            setFieldValue(obj, aClass, FieldsType.UPDATETIME.getMethodName(), dateTime);
            setFieldValue(obj, aClass, FieldsType.UPDATEUSER.getMethodName(), UserContext.getCurrentUserId());
        }
    }

    @SneakyThrows
    private void setFieldValue(
            Object obj,
            Class<?> aClass,
            String methodName,
            Object value) {
        Method method = aClass.getMethod(methodName, value != null ? value.getClass() : null);
        method.invoke(obj, value);
    }
}
