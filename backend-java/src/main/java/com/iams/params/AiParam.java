package com.iams.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/22 10:53
 * @Desc:
 */
@Data
@ApiModel("AI请求参数")
public class AiParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("问题")
    private String request = "";

    @ApiModelProperty("图片地址")
    private String imageUrl = "";

    @ApiModelProperty("会话ID")
    private String threadId = "";
}
