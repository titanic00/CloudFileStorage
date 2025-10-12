package com.titanic00.cloudfilestorage.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @GetMapping("/")
    public void getResource(@RequestParam String path) {};

    @PostMapping("/")
    public void uploadResource(@RequestParam String path) {};

    @DeleteMapping("/")
    public void deleteResource(@RequestParam String path) {};

    @GetMapping("/download")
    public void downloadResource(@RequestParam String path) {};

    @GetMapping("/move")
    public void moveResource(@RequestParam String from, @RequestParam String to) {};

    @GetMapping("/search")
    public void searchResource(@RequestParam String query) {};
}
