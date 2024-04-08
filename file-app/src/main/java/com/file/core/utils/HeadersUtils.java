package com.file.core.utils;

import com.file.api.dto.FileDataDto;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class HeadersUtils {
    public final String CONTENT_TYPE = "application/octet-stream";

    public HttpHeaders getHttpHeaders(FileDataDto data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", data.getContentType());
        headers.add("Content-Length", String.valueOf(data.getSize()));
        headers.add("Content-Disposition", "attachment; filename=" + data.getFileName());
        return headers;
    }
}
