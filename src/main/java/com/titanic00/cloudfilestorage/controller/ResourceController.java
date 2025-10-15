package com.titanic00.cloudfilestorage.controller;

import com.titanic00.cloudfilestorage.dto.ResourceDTO;
import com.titanic00.cloudfilestorage.service.ResourceService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/")
    public void getResource(@RequestParam String path) {
    }

    @PostMapping("/")
    public ResourceDTO uploadResource(@RequestParam String path, @RequestParam("file") MultipartFile file) throws Exception {
        return resourceService.uploadFile(path, file);
    }

    @DeleteMapping("/")
    public void deleteResource(@RequestParam String path) {
    }

    @GetMapping("/download")
    public void downloadResource(@RequestParam String path) {
    }

    @GetMapping("/move")
    public void moveResource(@RequestParam String from, @RequestParam String to) {
    }

    @GetMapping("/search")
    public void searchResource(@RequestParam String query) {
    }
}
