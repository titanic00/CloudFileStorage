package com.titanic00.cloudfilestorage.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/directory")
public class DirectoryController {

    @GetMapping("/")
    public void getDirectory(@RequestParam String path) {};

    @PostMapping("/")
    public void createEmptyDirectory(@RequestParam String path) {};
}
