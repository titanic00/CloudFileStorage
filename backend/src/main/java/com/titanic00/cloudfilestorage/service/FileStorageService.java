package com.titanic00.cloudfilestorage.service;

import com.titanic00.cloudfilestorage.entity.User;
import com.titanic00.cloudfilestorage.repository.UserRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Value("${minio.bucket}")
    private String bucket;
    private final MinioClient minioClient;
    private final UserRepository userRepository;

    public FileStorageService(MinioClient minioClient, UserRepository userRepository) {
        this.minioClient = minioClient;
        this.userRepository = userRepository;
    }

    public record UploadResult(String fullPath) {}

    public UploadResult uploadFile(String username, String path, MultipartFile file) {
        User user = userRepository.findByUsername(username);
        String fullPath = "/user-" + user.getId() + "-files/" + path + file.getOriginalFilename();

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(fullPath)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Hochladen der Datei.", e);
        }

        return new UploadResult(fullPath);
    }
}
