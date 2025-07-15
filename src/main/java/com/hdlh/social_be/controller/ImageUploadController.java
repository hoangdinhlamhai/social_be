package com.hdlh.social_be.controller;

import com.hdlh.social_be.adapter.CloudinaryAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageUploadController {

    private final CloudinaryAdapter cloudinaryAdapter;

    public ImageUploadController(CloudinaryAdapter cloudinaryAdapter) {
        this.cloudinaryAdapter = cloudinaryAdapter;
    }

    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryAdapter.uploadImage(file, "avatars");
        return ResponseEntity.ok(url);
    }

    @PostMapping("/cover")
    public ResponseEntity<String> uploadCover(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryAdapter.uploadImage(file, "covers");
        return ResponseEntity.ok(url);
    }

    @PostMapping("/post")
    public ResponseEntity<List<String>> uploadImagePost(@RequestParam("files") List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = cloudinaryAdapter.uploadImage(file, "posts");
            urls.add(url);
        }

        return ResponseEntity.ok(urls);
    }

}


