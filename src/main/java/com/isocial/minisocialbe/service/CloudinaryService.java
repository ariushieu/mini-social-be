package com.isocial.minisocialbe.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Map uploadFile(MultipartFile file, String folder) throws IOException {
        String contentType = file.getContentType();
        String resourceType = (contentType != null && contentType.startsWith("image/")) ? "image" : "auto";

        return cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", resourceType,
                        "folder", folder,
                        "overwrite", true,
                        "unique_filename", true
                )
        );
    }

    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(
                publicId,
                ObjectUtils.asMap(
                        "resource_type", "auto"
                )
        );
    }
}
