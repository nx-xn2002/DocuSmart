package com.nx.docusmart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nx.docusmart.common.ErrorCode;
import com.nx.docusmart.exception.BusinessException;
import com.nx.docusmart.exception.ThrowUtils;
import com.nx.docusmart.mapper.ApiInfoMapper;
import com.nx.docusmart.model.entity.ApiInfo;
import com.nx.docusmart.service.ApiInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * api info service impl
 *
 * @author nx-xn2002
 * @date 2025-01-12
 */
@Service
public class ApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo>
        implements ApiInfoService {

    @Override
    public void validApiInfo(ApiInfo apiInfo, boolean add) {
        if (apiInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = apiInfo.getName();
        String description = apiInfo.getDescription();
        String url = apiInfo.getUrl();
        String requestHeader = apiInfo.getRequestHeader();
        String responseHeader = apiInfo.getResponseHeader();
        String method = apiInfo.getMethod();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, url, requestHeader, responseHeader, method),
                    ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(description) && description.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }
}




