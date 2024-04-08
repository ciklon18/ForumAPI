package com.file.api.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDataDto {
    private UUID id;
    private String fileName;
    private String contentType;
    private long size;
}
