package com.titanic00.cloudfilestorage.service;

import com.titanic00.cloudfilestorage.context.AuthContext;
import com.titanic00.cloudfilestorage.dto.ResourceDTO;
import com.titanic00.cloudfilestorage.entity.User;
import com.titanic00.cloudfilestorage.enumeration.ObjectType;
import com.titanic00.cloudfilestorage.exception.AlreadyExistsException;
import com.titanic00.cloudfilestorage.exception.NotFoundException;
import com.titanic00.cloudfilestorage.repository.UserRepository;
import com.titanic00.cloudfilestorage.util.MinioObjectUtil;
import com.titanic00.cloudfilestorage.util.ReadableFormatUtil;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@Service
public class ResourceService {

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Value("${minio.root-folder}")
    private String rootFolderName;

    private final MinioClient minioClient;
    private final AuthContext authContext;
    private final UserRepository userRepository;

    public ResourceService(MinioClient minioClient, AuthContext authContext, UserRepository userRepository) {
        this.minioClient = minioClient;
        this.authContext = authContext;
        this.userRepository = userRepository;
    }

    public ResourceDTO getResource(String path) throws Exception {
        User user = userRepository.findByUsername(authContext.getUserDetails().getUsername());
        String objectName = String.format(rootFolderName, user.getId()) + path;

        if (!resourceExists(objectName)) {
            throw new AlreadyExistsException("Object doesn't exist.");
        }

        return buildResourceDTO(objectName);
    }

    public byte[] downloadResource(String path) throws Exception {
        User user = userRepository.findByUsername(authContext.getUserDetails().getUsername());
        String fullPath = String.format(rootFolderName, user.getId()) + path;

        Iterable<Result<Item>> items = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(fullPath)
                        .recursive(true)
                        .build()
        );

        if (!items.iterator().hasNext()) {
            throw new AlreadyExistsException("Object doesn't exist.");
        }

        return MinioObjectUtil.toZip(items);
    }

    public ResourceDTO uploadResource(String path, MultipartFile file) throws Exception {
        User user = userRepository.findByUsername(authContext.getUserDetails().getUsername());
        String objectName = MinioObjectUtil.buildObjectName(String.format(rootFolderName, user.getId()) + path,
                file.getOriginalFilename());

        if (resourceExists(objectName)) {
            throw new AlreadyExistsException("Object already exists.");
        }

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getInputStream().available(), -1)
                        .build()
        );

        return buildResourceDTO(objectName);
    }

    public void deleteResource(String path) throws Exception {
        User user = userRepository.findByUsername(authContext.getUserDetails().getUsername());
        String objectName = String.format(rootFolderName, user.getId()) + path;

        if (!resourceExists(objectName)) {
            throw new NotFoundException("Object doesn't exist.");
        }

        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(String.format(rootFolderName, user.getId()) + path)
                        .build()
        );
    }

    public boolean resourceExists(String objectName) throws Exception {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );

            return true;
        } catch (ErrorResponseException ex) {
            if (ex.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw ex;
        }
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

    public void createUserRootFolder(Long userId) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(String.format(rootFolderName, userId))
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build()
        );
    }

    public ResourceDTO buildResourceDTO(String objectName) throws Exception {
        StatObjectResponse statObjectResponse = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );

        return ResourceDTO.builder()
                // hide user-id-files folder and file name
                .path(MinioObjectUtil.getPathFromObjectName(statObjectResponse.object()))
                .name(MinioObjectUtil.getFileNameFromObjectName(statObjectResponse.object()))
                .size(ReadableFormatUtil.humanReadableByteCountSI(statObjectResponse.size()))
                .type(ObjectType.FILE)
                .build();
    }
}
