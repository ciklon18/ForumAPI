package com.fileservice.core.service;


import com.fileservice.api.dto.FileDataDto;

public interface IFileService {
    FileDataDto uploadFile(byte[] file, String fileName);

    byte[] downloadFile(String id);

    FileDataDto getFileInfoById(String id);
}
