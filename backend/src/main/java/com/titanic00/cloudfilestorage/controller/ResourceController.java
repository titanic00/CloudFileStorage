package com.titanic00.cloudfilestorage.controller;

import com.titanic00.cloudfilestorage.configuration.UserSession;
import com.titanic00.cloudfilestorage.dto.FileDTO;
import com.titanic00.cloudfilestorage.dto.FileType;
import com.titanic00.cloudfilestorage.service.FileStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    private final FileStorageService fileStorageService;

    public ResourceController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public void getFileInfo(@RequestParam(value = "path") String path) {
        return;
    }

    @DeleteMapping("/")
    public void deleteResource(@RequestParam(value = "path") String path) {
        return;
    }

    @PostMapping("/")
    public FileDTO upload(@RequestParam(value = "path") String path, @RequestBody MultipartFile file) {
        FileStorageService.UploadResult result =
                fileStorageService.uploadFile(UserSession.getNickname(), path, file);

        return mapToResponse(result, file);
    }

    @GetMapping("/download")
    public void downloadResource(@RequestParam(value = "path") String path) {
        return;
    }

    @GetMapping("/move")
    public void moveResource(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to) {
        return;
    }

    @GetMapping("/search")
    public void search(@RequestParam(value = "query") String query) {
        return;
    }

    private FileDTO mapToResponse(FileStorageService.UploadResult result, MultipartFile file) {
        return FileDTO.builder()
                .path(result.fullPath())
                .name(file.getOriginalFilename())
                .size(file.getSize())
                .type(fileOrDirectory(file.getOriginalFilename()))
                .build();
    }

    private FileType fileOrDirectory(String fileName) {
        return FilenameUtils.getExtension(fileName) == null ? FileType.DIRECTORY : FileType.FILE;
    }
}
