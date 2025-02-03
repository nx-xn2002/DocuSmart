package com.nx.docusmart.common;

import java.io.Serializable;
import lombok.Data;

/**
 * 删除请求
 *
 * @author nx-xn2002
 * @date 2025-01-11
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}