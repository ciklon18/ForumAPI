package com.file.core.service;


import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.file.api.dto.FileDataDto;
import com.file.config.MinioConfig;
import com.file.core.entity.File;
import com.file.core.repository.FileRepository;
import com.file.core.utils.HeadersUtils;
import com.file.integration.forum.client.ForumClient;
import com.file.integration.user.client.UserClient;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class MinioFileService implements IFileService {

    private final FileRepository fileRepository;
    private final MinioConfig minioConfig;
    private final MinioClient minioClient;
    private final ForumClient forumClient;
    private final UserClient userClient;
    private final HeadersUtils headersUtils;

    @SneakyThrows
    @Override
    @Transactional
    public FileDataDto uploadFile(byte[] file, String fileName, UUID messageId) {
        checkIsUserOwnerOfMessage(messageId);
        checkIsFileAlreadyExists(messageId);
        File savedFile = new File(fileName, file.length, messageId);
        fileRepository.save(savedFile);
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioConfig.getBucket())
                        .object(savedFile.getId().toString())
                        .stream(new ByteArrayInputStream(file), file.length, -1)
                        .build()
        );
        return new FileDataDto(savedFile.getId(), fileName, headersUtils.CONTENT_TYPE, file.length);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> downloadFile(UUID id) {
        byte[] file = getFileById(id);
        FileDataDto data = getFileInfoById(id);
        HttpHeaders headers = headersUtils.getHttpHeaders(data);
        return ResponseEntity.ok()
                .headers(headers)
                .body(file);
    }

    @SneakyThrows
    @Transactional
    public void deleteFileFromMessage(UUID messageId) {
        UUID fileId = fileRepository.getFileIdByMessageId(messageId)
                .orElseThrow(() -> new CustomException(
                        ExceptionType.BAD_REQUEST,
                        "File with message id=" + messageId + " not found"
                ));
        checkUserPermission(messageId);
        fileRepository.deleteByMessageId(messageId);
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(minioConfig.getBucket())
                        .object(fileId.toString())
                        .build()
        );
    }

    private byte[] getFileById(UUID id) {
        GetObjectArgs args = GetObjectArgs.builder().bucket(minioConfig.getBucket()).object(id.toString()).build();
        try (var in = minioClient.getObject(args)) {
            return in.readAllBytes();
        } catch (Exception e) {
            throw new CustomException(ExceptionType.FATAL, "Error while downloading file from Minio with id =" + id);
        }
    }

    private FileDataDto getFileInfoById(UUID id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "File with id=" + id + " not found"));
        return FileDataDto.builder().id(id).fileName(file.getName()).contentType(headersUtils.CONTENT_TYPE)
                .size(file.getSize()).build();
    }

    private void checkIsUserOwnerOfMessage(UUID messageId) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!forumClient.isUserOwnerOfMessage(messageId, userId)) {
            throw new CustomException(ExceptionType.FORBIDDEN, "User is not owner of message");
        }
    }

    private void checkIsFileAlreadyExists(UUID messageId) {
        if (fileRepository.existsByMessageId(messageId)) {
            throw new CustomException(
                    ExceptionType.BAD_REQUEST,
                    "File with message id=" + messageId + " already exists"
            );
        }
    }

    private void checkUserPermission(UUID messageId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = (UUID) authentication.getPrincipal();
        if (isAdmin(authentication) || isOwner(messageId, userId) || isModerator(messageId, userId)) {
            return;
        }
        throw new CustomException(ExceptionType.FORBIDDEN, "Message does not belong to user");
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ADMIN"));
    }

    private boolean isOwner(UUID messageId, UUID userId) {
        return forumClient.isUserOwnerOfMessage(messageId, userId);
    }

    private boolean isModerator(UUID messageId, UUID userId) {
        List<UUID> moderatorCategoryIds = userClient.getModeratorCategoriesByUserId(userId);
        return forumClient.isModerator(messageId, moderatorCategoryIds);
    }


}
