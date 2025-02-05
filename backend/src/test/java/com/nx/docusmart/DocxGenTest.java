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
import java.util.List;

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
        String jsonResponse = "{\"title\":\"测试标题\",\"content\":\"根据工作安排，请按要求完成任务。\\n换行测试\"}";
        JSONObject jsonObject = JSONUtil.parseObj(jsonResponse);
        // 读取模板文件
        ClassPathResource resource = new ClassPathResource(template.getTemplateFile());
        String path = resource.getFile().getPath();
        try (FileInputStream templateFile = new FileInputStream(path);
             XWPFDocument document = new XWPFDocument(templateFile)) {
            for (String placeholderKey : templateJson.keySet()) {
                if (jsonObject.containsKey(placeholderKey)) {
                    System.out.println("正在替换:" + placeholderKey);
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
//    private static void replacePlaceholder(XWPFDocument document, String placeholder, String replacement) {
//        // 遍历所有段落
//        for (XWPFParagraph paragraph : document.getParagraphs()) {
//            for (XWPFRun run : paragraph.getRuns()) {
//                String text = run.getText(0);
//                if (text != null && text.contains(placeholder)) {
//                    String[] split = replacement.split("\n");
//                    for (int i = 0; i < split.length; i++) {
//                        text = text.replace(placeholder, split[i]);
//                        // 替换内容
//                        run.setText(split[i], i);
//                        if (i != split.length - 1) {
//                            run.addBreak();
//                        }
//                    }
//                }
//            }
//        }
//    }
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
                fullText = fullText.replace(placeholder, replacement);

                // 清空原有 Runs（但保留第一个 Run 以保持格式）
                for (int i = runs.size() - 1; i > 0; i--) {
                    paragraph.removeRun(i);
                }

                // 处理换行，并保持格式
                String[] split = fullText.split("\n");
                XWPFRun firstRun = runs.get(0);
                firstRun.setText(split[0], 0); // 只替换第一个 Run
                for (int i = 1; i < split.length; i++) {
                    firstRun.addBreak(); // 添加换行
                    XWPFRun newRun = paragraph.createRun();
                    copyRunStyle(firstRun, newRun); // 复制格式
                    newRun.setText(split[i]);
                }
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
