package com.nx.docusmart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nx.docusmart.model.entity.ApiInfo;


/**
 * api info service
 *
 * @author nx-xn2002
 * @date 2025-01-12
 */
public interface ApiInfoService extends IService<ApiInfo> {
    /**
     * 校验
     *
     * @param apiInfo api info
     * @param add     add
     */
    void validApiInfo(ApiInfo apiInfo, boolean add);
}
