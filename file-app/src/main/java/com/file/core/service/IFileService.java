package com.file.core.service;


import com.file.api.dto.FileDataDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IFileService {
    FileDataDto uploadFile(byte[] file, String fileName, UUID messageId);

    ResponseEntity<byte[]> downloadFile(UUID id);

    void deleteFileFromMessage(UUID id);
}
