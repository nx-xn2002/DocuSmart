package com.nx.docusmart.model.dto.user;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户注册请求体
 *
 * @author nx-xn2002
 * @date 2025-01-18
 */
@Data
public class UserRegisterRequest implements Serializable {

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
