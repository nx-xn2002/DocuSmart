package com.nx.docusmart.controller;

import com.nx.docusmart.common.BaseResponse;
import com.nx.docusmart.common.ResultUtils;
import com.nx.docusmart.service.DocGenRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.nx.docusmart.common.ErrorCode.PARAMS_ERROR;
import static com.nx.docusmart.common.ErrorCode.SYSTEM_ERROR;

@Controller
@Slf4j
@RequestMapping("/files")
public class FileController {
    private static final String UPLOAD_DIR = "uploads";

    @Autowired
    private DocGenRecordService docGenRecordService;

    // 文件上传
    @PostMapping("/upload")
    @ResponseBody
    public BaseResponse uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultUtils.error( PARAMS_ERROR);
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + File.separator + file.getOriginalFilename());
            Files.write(path, bytes);
            log.info("file upload success：{}", file.getOriginalFilename());
            return ResultUtils.success(null);
        } catch (IOException e) {
            log.error("file upload error", e);
            return ResultUtils.error( PARAMS_ERROR);
        }
    }

    public BaseResponse saveDocFile(byte[] contents, String filename, String userId) {
        try {
            File parentDir = new File(UPLOAD_DIR + File.separator + userId);
            if (!parentDir.exists()) {
                log.info("mkdir {} res {}", parentDir.getPath(), parentDir.mkdirs());
            }
            Path path = Paths.get(UPLOAD_DIR + File.separator + userId + File.separator + filename);
            Files.write(path, contents);
            docGenRecordService.saveFile(filename, userId);
            log.info("file upload success：{}", filename);
            return ResultUtils.success(null);
        } catch (IOException e) {
            log.error("file upload error", e);
            return ResultUtils.error( PARAMS_ERROR);
        }
    }

    // 文件下载
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, String userId) {
        File file = new File(UPLOAD_DIR + File.separator + userId + File.separator + filename);
        if (file.exists()) {
            Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }

    // 查看文件列表
    @GetMapping("/list")
    @ResponseBody
    public List<String> listFiles() {
        List<String> fileNames = new ArrayList<>();
        File uploadDir = new File(UPLOAD_DIR);
        if (uploadDir.exists() && uploadDir.isDirectory()) {
            File[] files = uploadDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    fileNames.add(file.getName());
                }
            }
        }
        return fileNames;
    }

    // 文件删除
    @DeleteMapping("/delete/{filename}")
    @ResponseBody
    public BaseResponse deleteFile(@PathVariable String filename) {
        File file = new File(UPLOAD_DIR + File.separator + filename);
        if (file.exists()) {
            if (file.delete()) {
                return ResultUtils.success(null);
            } else {
                return ResultUtils.error(SYSTEM_ERROR);
            }
        }
        return ResultUtils.success(null);
    }
}