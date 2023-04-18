package co.DevelHopeHelloWorld.upload.download.controllers;

import co.DevelHopeHelloWorld.upload.download.services.FileStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;
    @PostMapping("/uploadOne")
    public String upload(@RequestBody MultipartFile file) {
        try {
            return fileStorageService.upload(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam MultipartFile[] files) {
        if (files.length == 0) return ResponseEntity.noContent().build();
        else if (files.length > 1) return ResponseEntity.badRequest().body("Please and just one picture");
        try {
            String fileName = fileStorageService.upload(files[0]);
            return ResponseEntity.ok("ok");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/download")
    public @ResponseBody byte[] download(@RequestParam String fileName, HttpServletResponse response)
            throws IOException {
        String extension = FilenameUtils.getExtension(fileName);
        switch(extension){
            case "gif" :
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
                break;
            case "jpg" : case "jpeg" :
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            case "png" :
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        return fileStorageService.download(fileName);

    }


}
