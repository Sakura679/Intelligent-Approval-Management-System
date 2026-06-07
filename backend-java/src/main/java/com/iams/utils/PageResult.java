package com.iams.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/15 17:36
 * @Desc:
 */
@Data
public class PageResult<T> {
    private Integer code;

    private String message;

    private List<T> data = Collections.emptyList();

    private Long total = 0L;

    private Long pageNum = 1L;

    private Long pageSize = 10L;

    private Long pages = 0L;

    public PageResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public PageResult(Integer code, String message, List<T> data, Long total, Long pageNum, Long pageSize, Long pages) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = pages;
    }

    public static <T> PageResult<T> success(IPage<T> iPage) {
        return new PageResult<>(200, "success", iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), iPage.getPages());
    }

    public static <T> PageResult<T> error(Integer code, String message) {
        return new PageResult<>(code, message);
    }
}
