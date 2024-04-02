package com.file.api.controller;


import com.file.api.constant.ApiPaths;
import com.file.api.dto.FileDataDto;
import com.file.core.service.IFileService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@AllArgsConstructor
public class FileController {

    private final IFileService fileService;

    @SneakyThrows
    @PostMapping(ApiPaths.UPLOAD)
    public FileDataDto uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName
    ) {
        log.info("Название файла: {}", file.getOriginalFilename());
        return fileService.uploadFile(file.getBytes(), fileName);
    }

    @SneakyThrows
    @GetMapping(value = ApiPaths.DOWNLOAD, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        var data = fileService.getFileInfoById(id);
        var file = fileService.downloadFile(id);
        return ResponseEntity.ok().header("Content-Disposition", "filename=" + data.getFileName()).body(file);
    }
}
