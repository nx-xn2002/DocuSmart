package com.nx.docusmart;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nx.docusmart.model.entity.Template;
import com.nx.docusmart.service.TemplateService;
import jakarta.annotation.Resource;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * test1
 *
 * @author nx-xn2002
 */
@SpringBootTest
public class DocxGenTest {
    @Resource
    private TemplateService templateService;

    @Test
    public void TestCreateFile() throws IOException {
        Template template = templateService.getById(1);
        JSONObject templateJson = JSONUtil.parseObj(template.getTemplateJson());
        String jsonResponse = "{\"content\":\"根据工作安排，请按要求完成任务。\\n换行测试\"}";
        JSONObject jsonObject = JSONUtil.parseObj(jsonResponse);
        // 读取模板文件
        ClassPathResource resource = new ClassPathResource(template.getTemplateFile());
        String path = resource.getFile().getPath();
        try (FileInputStream templateFile = new FileInputStream(path);
             XWPFDocument document = new XWPFDocument(templateFile)) {

            for (String placeholderKey : templateJson.keySet()) {
                if (jsonObject.containsKey(placeholderKey)) {
                    replacePlaceholder(document, "{{" + placeholderKey + "}}", (String) jsonObject.get(placeholderKey));
                }
            }
            // 保存生成的文档
            String fileName = "GeneratedDocument.docx";
            try (FileOutputStream out = new FileOutputStream("generate/" + fileName)) {
                document.write(out);
            }
            System.out.println("Document generated successfully!");
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
        // 遍历所有段落
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null && text.contains(placeholder)) {
                    String[] split = replacement.split("\n");
                    for (int i = 0; i < split.length; i++) {
                        text = text.replace(placeholder, split[i]);
                        // 替换内容
                        run.setText(split[i], i);
                        if (i != split.length - 1) {
                            run.addBreak();
                        }
                    }
                }
            }
        }
    }
}
