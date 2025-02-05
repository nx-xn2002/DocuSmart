package com.nx.docusmart.service;

import com.nx.docusmart.model.entity.Template;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * template service
 *
 * @author nx-xn2002
 */
public interface TemplateService extends IService<Template> {
    /**
     * 校验
     *
     * @param template template
     * @param add      add
     */
    void validTemplate(Template template, boolean add);
}
