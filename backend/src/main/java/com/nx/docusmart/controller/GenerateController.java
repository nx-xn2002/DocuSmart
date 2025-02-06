package com.nx.docusmart.controller;

import com.nx.docusmart.common.BaseResponse;
import com.nx.docusmart.manager.TongYiManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 生成服务
 *
 * @author nx-xn2002
 */
@RestController
@RequestMapping("/generate")
@Slf4j
public class GenerateController {
    @Resource
    private TongYiManager tongYiManager;

    /**
     * chat
     * 仅测试，待删除
     *
     * @param fileList file list
     * @param content  content
     * @param prompt   prompt
     */
    @PostMapping("/chat")
    public void chat(List<MultipartFile> fileList, String content, String prompt) {
        log.info("content:{} prompt:{}", content, prompt);
        tongYiManager.chat(content, prompt, fileList);
    }

}
