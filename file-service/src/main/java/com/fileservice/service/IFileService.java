package com.fileservice.service;


import com.fileservice.dto.FileDataDto;

public interface IFileService {
    FileDataDto uploadFile(byte[] file, String fileName);

    byte[] downloadFile(String id);

    FileDataDto getFileInfoById(String id);
}
