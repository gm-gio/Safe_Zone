package com.george.fileservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void uploadUsers(MultipartFile file);

    byte[] generateCsv();
}
