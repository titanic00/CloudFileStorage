package com.titanic00.cloudfilestorage.service;

import com.titanic00.cloudfilestorage.context.AuthContext;
import com.titanic00.cloudfilestorage.dto.ResourceDTO;
import com.titanic00.cloudfilestorage.entity.User;
import com.titanic00.cloudfilestorage.exception.NotFoundException;
import com.titanic00.cloudfilestorage.repository.UserRepository;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoryService {

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Value("${minio.root-folder}")
    private String rootFolderName;

    private final MinioClient minioClient;
    private final AuthContext authContext;
    private final UserRepository userRepository;
    private final ResourceService resourceService;

    public DirectoryService(MinioClient minioClient, AuthContext authContext,
                            UserRepository userRepository,
                            ResourceService resourceService) {
        this.minioClient = minioClient;
        this.authContext = authContext;
        this.userRepository = userRepository;
        this.resourceService = resourceService;
    }

    public List<ResourceDTO> getDirectoryInfo(String path) {
        User user = userRepository.findByUsername(authContext.getUserDetails().getUsername());
        String fullPath = String.format(rootFolderName, user.getId()) + path;

        try {
            Iterable<Result<Item>> items = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(fullPath)
                            .build()
            );

            List<ResourceDTO> resourceDTOs = new ArrayList<>();

            for (Result<Item> item : items) {
                resourceDTOs.add(resourceService.buildResourceDTO(item.get().objectName()));
            }

            return resourceDTOs;
        } catch (Exception ex) {
            throw new NotFoundException("Directory doesn't exist or empty.");
        }
    }

    public ResourceDTO createEmptyDirectory(String path) throws Exception {
        User user = userRepository.findByUsername(authContext.getUserDetails().getUsername());
        String fullPath = String.format(rootFolderName, user.getId()) + path;

        if (!fullPath.endsWith("/")) {
            fullPath += "/";
        }

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fullPath)
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build()
        );

        return resourceService.buildResourceDTO(fullPath);
    }
}
