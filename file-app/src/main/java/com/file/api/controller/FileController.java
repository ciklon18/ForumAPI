package com.file.api.controller;


import com.file.api.constant.ApiPaths;
import com.file.api.dto.FileDataDto;
import com.file.core.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final IFileService fileService;

    @PostMapping(ApiPaths.UPLOAD_TO_MESSAGE_BY_ID)
    public FileDataDto uploadFileToMessage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @PathVariable("id") UUID messageId
    ) throws IOException {
        log.info("Название файла: {}", file.getOriginalFilename());
        return fileService.uploadFile(file.getBytes(), fileName, messageId);
    }

    @GetMapping(value = ApiPaths.DOWNLOAD, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadFile(@PathVariable UUID id) {
        log.info("Скачивание файла с id: {}", id);
        return fileService.downloadFile(id);
    }

    @DeleteMapping(ApiPaths.DELETE_BY_MESSAGE_ID)
    public void deleteFile(@PathVariable("id") UUID messageId) {
        log.info("Удаление файла с id: {}", messageId);
        fileService.deleteFileFromMessage(messageId);
    }
}
