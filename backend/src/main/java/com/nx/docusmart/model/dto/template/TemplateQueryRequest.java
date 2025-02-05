package com.nx.docusmart.model.dto.template;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.nx.docusmart.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author nx-xn2002
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

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