package com.nx.docusmart.service;

import com.nx.docusmart.model.entity.DocGenRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author yison
* @description 针对表【doc_gen_record】的数据库操作Service
* @createDate 2025-02-10 22:35:49
*/
public interface DocGenRecordService extends IService<DocGenRecord> {

    void saveFile(String fileName, String userId);
}
