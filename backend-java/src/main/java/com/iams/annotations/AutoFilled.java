package com.iams.annotations;

import com.iams.enums.AutoFillType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/5 10:27
 * @Desc:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFilled {
    AutoFillType value();
}
