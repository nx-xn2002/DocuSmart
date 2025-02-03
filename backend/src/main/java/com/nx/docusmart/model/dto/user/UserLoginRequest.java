package com.nx.docusmart.model.dto.user;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户登录请求
 *
 * @author nx-xn2002
 * @date 2025-01-15
 */
@Data
public class UserLoginRequest implements Serializable {
    private String userAccount;
    private String userPassword;
}
