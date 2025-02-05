package com.nx.docusmart.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;

/**
 * 生成表
 *
 * @author nx-xn2002
 */
@TableName(value = "generate")
@Data
public class Generate {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 其它信息 json 格式
     */
    private String info;

    /**
     * 上传文件地址 json 数组格式
     */
    private String uploadFile;

    /**
     * 生成文件地址
     */
    private String generateFile;

    /**
     * 生成进展 start/fail/success
     */
    private String process;

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