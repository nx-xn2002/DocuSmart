package com.nx.docusmart.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nx.docusmart.annotation.AuthCheck;
import com.nx.docusmart.common.BaseResponse;
import com.nx.docusmart.common.DeleteRequest;
import com.nx.docusmart.common.ErrorCode;
import com.nx.docusmart.common.ResultUtils;
import com.nx.docusmart.constant.CommonConstant;
import com.nx.docusmart.exception.BusinessException;
import com.nx.docusmart.model.dto.template.TemplateAddRequest;
import com.nx.docusmart.model.dto.template.TemplateQueryRequest;
import com.nx.docusmart.model.dto.template.TemplateUpdateRequest;
import com.nx.docusmart.model.entity.Template;
import com.nx.docusmart.service.TemplateService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * api info controller
 *
 * @author nx-xn2002
 */
@RestController
@RequestMapping("/template")
@Slf4j
public class TemplateController {

    @Resource
    private TemplateService templateService;

    /**
     * 创建
     *
     * @param templateAddRequest api info add request
     * @return {@link BaseResponse }<{@link Long }>
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Long> addTemplate(@RequestBody TemplateAddRequest templateAddRequest) {
        if (templateAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Template template = new Template();
        BeanUtils.copyProperties(templateAddRequest, template);
        // 校验
        templateService.validTemplate(template, true);
        boolean result = templateService.save(template);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newTemplateId = template.getId();
        return ResultUtils.success(newTemplateId);
    }

    /**
     * 删除
     *
     * @param deleteRequest delete request
     * @return {@link BaseResponse }<{@link Boolean }>
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> deleteTemplate(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        Template oldTemplate = templateService.getById(id);
        if (oldTemplate == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean b = templateService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param templateUpdateRequest api info update request
     * @return {@link BaseResponse }<{@link Boolean }>
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> updateTemplate(@RequestBody TemplateUpdateRequest templateUpdateRequest) {
        if (templateUpdateRequest == null || templateUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Template template = new Template();
        BeanUtils.copyProperties(templateUpdateRequest, template);
        // 参数校验
        templateService.validTemplate(template, false);
        long id = templateUpdateRequest.getId();
        // 判断是否存在
        Template oldTemplate = templateService.getById(id);
        if (oldTemplate == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean result = templateService.updateById(template);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id id
     * @return {@link BaseResponse }<{@link Template }>
     */
    @GetMapping("/get")
    public BaseResponse<Template> getTemplateById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Template template = templateService.getById(id);
        return ResultUtils.success(template);
    }

    /**
     * 获取列表
     *
     * @param templateQueryRequest api info query request
     * @return {@link BaseResponse }<{@link List }<{@link Template }>>
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<Template>> listTemplate(TemplateQueryRequest templateQueryRequest) {
        Template templateQuery = new Template();
        if (templateQueryRequest != null) {
            BeanUtils.copyProperties(templateQueryRequest, templateQuery);
        }
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>(templateQuery);
        List<Template> templateList = templateService.list(queryWrapper);
        return ResultUtils.success(templateList);
    }

    /**
     * 分页获取列表
     *
     * @param templateQueryRequest api info query request
     * @return {@link BaseResponse }<{@link Page }<{@link Template }>>
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<Template>> listTemplateByPage(TemplateQueryRequest templateQueryRequest) {
        if (templateQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Template templateQuery = new Template();
        BeanUtils.copyProperties(templateQueryRequest, templateQuery);
        long current = templateQueryRequest.getCurrent();
        long size = templateQueryRequest.getPageSize();
        String sortField = templateQueryRequest.getSortField();
        String sortOrder = templateQueryRequest.getSortOrder();
        String description = templateQuery.getDescription();
        // description 需支持模糊搜索
        templateQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>(templateQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        Page<Template> templatePage = templateService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(templatePage);
    }
}
