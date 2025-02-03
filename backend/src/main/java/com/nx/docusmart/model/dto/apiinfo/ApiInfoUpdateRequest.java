package com.nx.docusmart.model.dto.apiinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @author nx-xn2002
 * @date 2025-01-12
 */
@Data
public class ApiInfoUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态 0-关闭 1-开启
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;
}