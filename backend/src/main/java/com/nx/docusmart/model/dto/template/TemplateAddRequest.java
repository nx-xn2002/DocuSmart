package com.nx.docusmart.model.dto.template;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author nx-xn2002
 */
@Data
public class TemplateAddRequest implements Serializable {
    /**
     * 模板介绍
     */
    private String description;

    /**
     * 略缩图地址
     */
    private String preview;

    /**
     * 模版名称
     */
    private String templateName;

    /**
     * 模版地址
     */
    private String templateFile;

    /**
     * 模版 json 表示
     */
    private String templateJson;

    /**
     * 模版 prompt 工程
     */
    private String templatePrompt;
}