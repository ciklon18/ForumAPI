package com.fileservice.core.service;


import com.fileservice.api.dto.FileDataDto;
import com.fileservice.config.MinioConfig;
import com.fileservice.core.entity.File;
import com.fileservice.core.repository.FileRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;


@Service
@AllArgsConstructor
public class MinioFileService implements IFileService {
    private final FileRepository fileRepository;
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;


    @Override
    public FileDataDto uploadFile(byte[] file, String fileName) {
        try {
            var id = UUID.randomUUID().toString();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucket())
                            .object(id)
                            .stream(new ByteArrayInputStream(file), file.length, -1)
                            .build()
            );
            fileRepository.save(
                    File.builder()
                            .id(UUID.fromString(id))
                            .name(fileName)
                            .size(file.length)
                            .build()
            );
            return new FileDataDto(id, fileName, "application/octet-stream", file.length);
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading file to Minio", e);
        }

    }

    @Override
    public byte[] downloadFile(String id) {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(minioConfig.getBucket())
                .object(id)
                .build();
        try (var in = minioClient.getObject(args)) {
            return in.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error while downloading file from Minio with id =" + id, e);
        }
    }

    @Override
    public FileDataDto getFileInfoById(String id) {
        var file = fileRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("File with id=" + id + " not found"));
        return FileDataDto.builder()
                .id(id)
                .fileName(file.getName())
                .contentType("application/octet-stream")
                .size(file.getSize())
                .build();
    }
}
