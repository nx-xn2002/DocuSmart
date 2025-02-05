package com.nx.docusmart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nx.docusmart.common.ErrorCode;
import com.nx.docusmart.exception.BusinessException;
import com.nx.docusmart.exception.ThrowUtils;
import com.nx.docusmart.mapper.TemplateMapper;
import com.nx.docusmart.model.entity.Template;
import com.nx.docusmart.service.TemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * template service impl
 *
 * @author nx-xn2002
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template>
        implements TemplateService {

    @Override
    public void validTemplate(Template template, boolean add) {
        if (template == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String description = template.getDescription();
        String preview = template.getPreview();
        String templateName = template.getTemplateName();
        String templateFile = template.getTemplateFile();
        String templateJson = template.getTemplateJson();
        String templatePrompt = template.getTemplatePrompt();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(description, preview, templateName, templateFile, templateJson,
                            templatePrompt),
                    ErrorCode.PARAMS_ERROR);
        }
    }
}




