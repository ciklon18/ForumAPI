package com.file.core.service;


import com.file.api.dto.FileDataDto;

public interface IFileService {
    FileDataDto uploadFile(byte[] file, String fileName);

    byte[] downloadFile(String id);

    FileDataDto getFileInfoById(String id);
}
