package com.isocial.minisocialbe.service.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryStorageService implements  StorageService{
    private final Cloudinary cloudinary;

    @Override
    public UploadResult uploadFile(MultipartFile file, String folder) throws IOException {

        String contentType = file.getContentType();
        String resourceType = (contentType != null && contentType.startsWith("image/")) ? "image" : "auto";

        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", resourceType,
                        "folder", folder,
                        "overwrite", true,
                        "unique_filename", true
                ));
        String url = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        return new UploadResult(url, publicId);
    }

    @Override
    public void deleteFile(String publicId, String resourceType) throws IOException {
        cloudinary.uploader().destroy(
                publicId,
                ObjectUtils.asMap(
                        "resource_type", resourceType
                )
        );
    }
}
