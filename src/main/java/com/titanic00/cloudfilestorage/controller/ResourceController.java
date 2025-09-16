package com.titanic00.cloudfilestorage.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @GetMapping("/")
    public void getFileInfo(@RequestParam(value = "path") String path) {
        return;
    }

    @DeleteMapping("/")
    public void deleteResource(@RequestParam(value = "path") String path) {
        return;
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


}
