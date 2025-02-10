package com.nx.docusmart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nx.docusmart.model.entity.DocGenRecord;
import com.nx.docusmart.service.DocGenRecordService;
import com.nx.docusmart.mapper.DocGenRecordMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author yison
* @description 针对表【doc_gen_record】的数据库操作Service实现
* @createDate 2025-02-10 22:35:49
*/
@Service
public class DocGenRecordServiceImpl extends ServiceImpl<DocGenRecordMapper, DocGenRecord>
    implements DocGenRecordService{

    @Override
    public void saveFile(String fileName, String userId) {
        DocGenRecord record = new DocGenRecord();
        record.setUserId(userId);
        record.setFileName(fileName);
        record.setCreateDate(new Date());
        this.save(record);
    }
}




