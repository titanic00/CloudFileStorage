package com.titanic00.cloudfilestorage.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/directory")
public class DirectoryController {

    @GetMapping("/")
    public void getDirectoryInfo(@RequestParam(value = "path") String path) {
        return;
    }

    @PostMapping("/")
    public void createEmptyFolder(@RequestParam(value = "path") String path) {
        return;
    }
}
