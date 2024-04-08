package com.file.api.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiPaths {
        public static final String UPLOAD_TO_MESSAGE_BY_ID = "/api/file/upload/message/{id}";
        public static final String DOWNLOAD = "/api/file/download/{id}";
        public static final String DELETE_BY_MESSAGE_ID = "/api/file/message/{id}/delete";
}
