package com.fileservice.dto;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class FileDataDto {
    private String id;
    private String fileName;
    private String contentType;
    private long size;
}
