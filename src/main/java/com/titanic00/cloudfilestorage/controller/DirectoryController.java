package com.titanic00.cloudfilestorage.controller;

import com.titanic00.cloudfilestorage.dto.ResourceDTO;
import com.titanic00.cloudfilestorage.service.DirectoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directory")
public class DirectoryController {

    private final DirectoryService directoryService;

    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping("/")
    public List<ResourceDTO> getDirectory(@RequestParam String path) {
        return directoryService.getDirectoryInfo(path);
    }

    @PostMapping("/")
    public ResourceDTO createEmptyDirectory(@RequestParam String path) throws Exception {
        return directoryService.createEmptyDirectory(path);
    }
}
