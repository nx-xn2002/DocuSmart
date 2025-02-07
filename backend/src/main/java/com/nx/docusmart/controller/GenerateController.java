package com.nx.docusmart.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nx.docusmart.common.BaseResponse;
import com.nx.docusmart.common.ErrorCode;
import com.nx.docusmart.common.ResultUtils;
import com.nx.docusmart.exception.BusinessException;
import com.nx.docusmart.manager.TongYiManager;
import com.nx.docusmart.model.entity.Template;
import com.nx.docusmart.service.TemplateService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
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
    @Resource
    private TemplateService templateService;

    //    @PostMapping("/doc")
//    public ResponseEntity<InputStreamResource> generateDocument(List<MultipartFile> fileList,
//                                                                String content, Long templateId, String fileName) {
//        // 参数检查
//        if (StringUtils.isBlank(content) || templateId == null) {
//            log.error("参数错误: content 或 templateId 为空");
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//
//        // 获取模板
//        Template template = templateService.getById(templateId);
//        if (template == null) {
//            log.error("模板不存在: templateId={}", templateId);
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
//        }
//
//        // 与大模型进行交互
//        String jsonResponse = tongYiManager.chat(content, template.getTemplatePrompt(), fileList);
//        if (jsonResponse == null) {
//            log.error("大模型服务异常: templateId={}, content={}", templateId, content);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "大模型服务异常");
//        }
//
//        // 检查返回的 JSON 格式
//        if (!JSONUtil.isTypeJSON(jsonResponse)) {
//            log.error("大模型返回非JSON格式: {}", jsonResponse);
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, jsonResponse);
//        }
//
//        JSONObject jsonObject = JSONUtil.parseObj(jsonResponse);
//
//        // 读取模板文件
//        ClassPathResource resource = new ClassPathResource(template.getTemplateFile());
//        String path = null;
//        try {
//            path = resource.getFile().getPath();
//            log.info("模板文件路径: {}", path);
//        } catch (IOException e) {
//            log.error("读取模板文件失败: templateFile={}, 错误信息={}", template.getTemplateFile(), e.getMessage());
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
//        }
//
//        JSONObject templateJson = JSONUtil.parseObj(template.getTemplateJson());
//
//        // 使用 ByteArrayOutputStream 代替文件生成
//        try (FileInputStream templateFile = new FileInputStream(path);
//             XWPFDocument document = new XWPFDocument(templateFile);
//             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
//
//            log.info("开始替换模板中的占位符...");
//            for (String placeholderKey : templateJson.keySet()) {
//                if (jsonObject.containsKey(placeholderKey)) {
//                    log.info("正在替换占位符: {}", placeholderKey);
//                    replacePlaceholder(document, "^^" + placeholderKey + "^^", (String) jsonObject.get
//                    (placeholderKey));
//                }
//            }
//            // 将文档内容写入到 ByteArrayOutputStream
//            document.write(byteArrayOutputStream);
//            byte[] documentContent = byteArrayOutputStream.toByteArray();
//            log.info("文档生成成功，准备返回给前端");
//
//            // 返回文件流给前端
//            InputStreamResource inputStreamResource =
//                    new InputStreamResource(new ByteArrayInputStream(documentContent));
//            fileName = StringUtils.isBlank(fileName) ? "GeneratedDocument.docx" : fileName + ".docx";
//
//            // 返回 ResponseEntity，不要返回 BaseResponse
//            return ResponseEntity.ok()
//                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(inputStreamResource);
//        } catch (IOException e) {
//            log.error("处理文档生成失败: 错误信息={}", e.getMessage());
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文档生成失败");
//        }
//    }
    @PostMapping("/doc")
    public BaseResponse<String> generateDocument(List<MultipartFile> files,
                                                 String content, Long templateId) {
        // 参数检查
        if (StringUtils.isBlank(content) || templateId == null) {
            log.error("参数错误: content 或 templateId 为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取模板
        Template template = templateService.getById(templateId);
        if (template == null) {
            log.error("模板不存在: templateId={}", templateId);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }
        log.info("用户上传了{}个文件", files != null ? files.size() : 0);
        // 与大模型进行交互
        String jsonResponse = tongYiManager.chat(content, template.getTemplatePrompt(), files);
        if (jsonResponse == null) {
            log.error("大模型服务异常: templateId={}, content={}", templateId, content);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "大模型服务异常");
        }

        // 检查返回的 JSON 格式
        if (!JSONUtil.isTypeJSON(jsonResponse)) {
            log.error("大模型返回非JSON格式: {}", jsonResponse);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, jsonResponse);
        }

        JSONObject jsonObject = JSONUtil.parseObj(jsonResponse);

        // 读取模板文件
        ClassPathResource resource = new ClassPathResource(template.getTemplateFile());
        String path = null;
        try {
            path = resource.getFile().getPath();
            log.info("模板文件路径: {}", path);
        } catch (IOException e) {
            log.error("读取模板文件失败: templateFile={}, 错误信息={}", template.getTemplateFile(), e.getMessage());
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }

        JSONObject templateJson = JSONUtil.parseObj(template.getTemplateJson());

        // 使用 ByteArrayOutputStream 代替文件生成
        try (FileInputStream templateFile = new FileInputStream(path);
             XWPFDocument document = new XWPFDocument(templateFile);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            log.info("开始替换模板中的占位符...");
            for (String placeholderKey : templateJson.keySet()) {
                if (jsonObject.containsKey(placeholderKey)) {
                    log.info("正在替换占位符: {}", placeholderKey);
                    replacePlaceholder(document, "^^" + placeholderKey + "^^", (String) jsonObject.get(placeholderKey));
                }
            }
            // 将文档内容写入到 ByteArrayOutputStream
            document.write(byteArrayOutputStream);
            byte[] documentContent = byteArrayOutputStream.toByteArray();
            log.info("文档生成成功，准备返回给前端");

            // 将字节数组转换为 Base64 字符串
            String base64Str = Base64.getEncoder().encodeToString(documentContent);

            // 返回 Base64 字符串
            return ResultUtils.success(base64Str);
        } catch (IOException e) {
            log.error("处理文档生成失败: 错误信息={}", e.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文档生成失败");
        }
    }

    /**
     * 替换模板中的占位符
     *
     * @param document    document
     * @param placeholder placeholder
     * @param replacement replacement
     */
    private static void replacePlaceholder(XWPFDocument document, String placeholder, String replacement) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            String paragraphText = paragraph.getText();
            if (paragraphText.contains(placeholder)) {
                // 记录所有的 Run
                List<XWPFRun> runs = paragraph.getRuns();
                if (runs == null || runs.isEmpty()) {
                    continue;
                }
                // 先拼接完整文本，并进行替换
                String fullText = "";
                for (XWPFRun run : runs) {
                    fullText += run.getText(0) == null ? "" : run.getText(0);
                }
                // 替换占位符
                fullText = fullText.replace(placeholder, replacement);
                // 清空原有 Runs（保留第一个 Run 以保持格式）
                for (int i = runs.size() - 1; i > 0; i--) {
                    paragraph.removeRun(i);
                }
                // 处理换行
                String[] split = fullText.split("\n");
                // 重新构建每个 Run，保持格式并处理换行和缩进
                for (int i = 0; i < split.length; i++) {
                    XWPFRun newRun = paragraph.createRun();
                    newRun.setText(split[i]);
                    // 保持原有格式
                    copyRunStyle(runs.get(0), newRun);
                    if (i < (split.length - 1)) {
                        newRun.addBreak(); // 添加换行
                    }
                }
                paragraph.removeRun(0);
            }
        }
    }

    /**
     * 复制 XWPFRun 的格式到新的 Run
     */
    private static void copyRunStyle(XWPFRun source, XWPFRun target) {
        target.setBold(source.isBold());
        target.setItalic(source.isItalic());
        target.setUnderline(source.getUnderline());
        target.setColor(source.getColor());
        target.setFontSize(source.getFontSize());
        target.setFontFamily(source.getFontFamily());
        target.setTextPosition(source.getTextPosition());
        target.setCapitalized(source.isCapitalized());
        target.setCharacterSpacing(source.getCharacterSpacing());
    }
}
