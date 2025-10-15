package com.titanic00.cloudfilestorage.service;

import com.titanic00.cloudfilestorage.context.AuthContext;
import com.titanic00.cloudfilestorage.dto.ResourceDTO;
import com.titanic00.cloudfilestorage.entity.User;
import com.titanic00.cloudfilestorage.enumeration.ItemType;
import com.titanic00.cloudfilestorage.repository.UserRepository;
import com.titanic00.cloudfilestorage.util.MinioObjectUtil;
import com.titanic00.cloudfilestorage.util.ReadableFormatUtil;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@Service
public class ResourceService {

    @Value("${minio.bucket.name}")
    private String bucketName;

    private final MinioClient minioClient;

    private final AuthContext authContext;

    private final UserRepository userRepository;

    public ResourceService(MinioClient minioClient, AuthContext authContext, UserRepository userRepository) {
        this.minioClient = minioClient;
        this.authContext = authContext;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void createBucket() throws Exception {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    public void createUserRootFolder(Long id) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object("user-" + id + "-files/")
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build()
        );
    }

    public ResourceDTO uploadFile(String path, MultipartFile file) throws Exception {
        User user = userRepository.findByUsername(authContext.getUserDetails().getUsername());
        String objectName = MinioObjectUtil.buildObjectString(path, file.getOriginalFilename(), user);

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getInputStream().available(), -1)
                        .build()
        );

        return buildResourceDTO(objectName, file);
    }

    public ResourceDTO buildResourceDTO(String path, MultipartFile file) {
        return ResourceDTO.builder()
                // hide user-id-files folder and file name
                .path(path.substring(path.indexOf("/") + 1, path.lastIndexOf("/")))
                .name(file.getOriginalFilename())
                .size(ReadableFormatUtil.humanReadableByteCountSI(file.getSize()))
                .type(ItemType.FILE)
                .build();
    }
}
