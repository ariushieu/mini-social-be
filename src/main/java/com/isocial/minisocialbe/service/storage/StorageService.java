package com.isocial.minisocialbe.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
     UploadResult uploadFile(MultipartFile file, String folder) throws IOException;
     void deleteFile(String publicId, String resourceType) throws IOException;
}
