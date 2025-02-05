package com.nx.docusmart.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;

/**
 * 模版
 *
 * @author nx-xn2002
 */
@TableName(value = "template")
@Data
public class Template {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDeleted;
}