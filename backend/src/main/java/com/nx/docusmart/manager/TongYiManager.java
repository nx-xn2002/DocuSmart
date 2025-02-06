package com.nx.docusmart.manager;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TongYiManager {
    @Value("${tong-yi.apiKey}")
    private String apiKey;
    @Value("${tong-yi.file-manage-url}")
    private String fileManageUrl;
    @Value("${tong-yi.model-url}")
    private String modelUrl;
    @Value("${tong-yi.max-file-size}")
    private long maxFileSize;

    /**
     * 上传文件并获取 id
     */
    private String uploadFile(MultipartFile multipartFile) {
        if (multipartFile.getSize() > maxFileSize) {
            log.error("文件大小超过限制: {}", multipartFile.getOriginalFilename());
            return null;
        }
        String authorizationHeader = "Bearer " + apiKey;
        String purpose = "file-extract";
        try (HttpResponse response = HttpRequest.post(fileManageUrl)
                .header("Authorization", authorizationHeader)
                .form("file", multipartFile.getBytes(), multipartFile.getOriginalFilename())
                .form("purpose", purpose)
                .execute()) {

            String responseBody = response.body();
            if (JSONUtil.isTypeJSON(responseBody)) {
                return (String) JSONUtil.parseObj(responseBody).getOrDefault("id", null);
            }
        } catch (IOException e) {
            log.error("文件上传失败: {}", multipartFile.getOriginalFilename(), e);
        }
        return null;
    }

    /**
     * 删除文件
     */
    private void deleteFile(String fileId) {
        String authorizationHeader = "Bearer " + apiKey;
        String deleteUrl = fileManageUrl + "/" + fileId;

        try (HttpResponse response = HttpRequest.delete(deleteUrl)
                .header("Authorization", authorizationHeader)
                .execute()) {
            String responseBody = response.body();
            if (JSONUtil.isTypeJSON(responseBody)) {
                boolean deleted = JSONUtil.parseObj(responseBody).getBool("deleted", false);
                if (deleted) {
                    log.info("文件删除成功，fileId: {}", fileId);
                } else {
                    log.warn("文件删除失败，fileId: {}", fileId);
                }
            }
        }
    }

    /**
     * 发送聊天请求
     *
     * @param content  content
     * @param prompt   prompt
     * @param fileList file list
     * @return {@link String }
     */
    public String chat(String content, String prompt, List<MultipartFile> fileList) {
        String authorizationHeader = "Bearer " + apiKey;
        List<Map<String, String>> messages = new ArrayList<>();
        // 添加 System 信息
        if (prompt != null) {
            messages.add(createMessage("system", prompt));
        }
        List<String> fileIds = new ArrayList<>();
        // 上传文件并添加文件id
        if (fileList != null) {
            for (MultipartFile file : fileList) {
                try {
                    String fileId = uploadFile(file);
                    if (fileId != null) {
                        messages.add(createMessage("system", "fileid://" + fileId));
                        // 保存文件ID
                        fileIds.add(fileId);
                    }
                } catch (Exception e) {
                    log.error("文件上传失败: {}", e.getMessage(), e);
                }
            }
        }
        // 添加用户消息
        if (content != null) {
            messages.add(createMessage("user", content));
        } else {
            log.warn("用户消息为空!");
        }
        // 请求体
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("model", "qwen-long");
        requestData.put("messages", messages);
        try (HttpResponse response = HttpRequest.post(modelUrl)
                .header("Authorization", authorizationHeader)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(requestData))
                .execute()) {
            // 输出响应内容
            String responseBody = response.body();
            log.info("Response: {}", responseBody);
            // 请求返回后，删除上传的文件
            for (String fileId : fileIds) {
                try {
                    deleteFile(fileId);
                } catch (Exception e) {
                    log.error("文件删除失败: {}", e.getMessage(), e);
                }
            }
            try {
                // 将字符串转换为JSONObject
                JSONObject jsonObject = JSONUtil.parseObj(responseBody);
                return jsonObject.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getStr("content");
            } catch (Exception e) {
                log.error("提取内容时发生错误: {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("请求失败: {}", e.getMessage(), e);
        }

        return null;
    }



    private Map<String, String> createMessage(String role, String content) {
        Map<String, String> message = new HashMap<>();
        message.put("role", role);
        message.put("content", content);
        return message;
    }
}
