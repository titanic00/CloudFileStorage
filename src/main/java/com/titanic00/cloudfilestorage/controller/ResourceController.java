package com.titanic00.cloudfilestorage.controller;

import com.titanic00.cloudfilestorage.dto.ResourceDTO;
import com.titanic00.cloudfilestorage.service.ResourceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/")
    public ResourceDTO getResource(@RequestParam String path) throws Exception {
        return resourceService.getResource(path);
    }

    @PostMapping("/")
    public ResourceDTO uploadResource(@RequestParam String path, @RequestParam("file") MultipartFile file) throws Exception {
        return resourceService.uploadResource(path, file);
    }

    @DeleteMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResource(@RequestParam String path) throws Exception {
        resourceService.deleteResource(path);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadResource(@RequestParam String path) throws Exception {
        byte[] zip = resourceService.downloadResource(path);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"download.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(zip);
    }

    @GetMapping("/move")
    public ResourceDTO moveResource(@RequestParam String from, @RequestParam String to) throws Exception {
        return resourceService.moveResource(from, to);
    }

    @GetMapping("/search")
    public List<ResourceDTO> searchResource(@RequestParam String query) throws Exception {
        return resourceService.searchResource(query);
    }
}
