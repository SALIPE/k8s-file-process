/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.process.genomic.processGenome.service;

import com.process.genomic.processGenome.vo.FileTreeResponse;
import com.process.genomic.processGenome.vo.UserDirRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author salipe
 */
@RestController
@RequestMapping(value = "/files", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProcessFileWebService {

    @Autowired
    private ProcessFileService processFileService;

    @Value("${upload.dir:/mnt/storage}")
    private String uploadDir;

    @PostMapping(value = "/create-user-dir")
    public ResponseEntity<String> createUserDir(
            @RequestBody UserDirRequest request) {

        File storage = new File(uploadDir);

        if (!storage.exists() || !storage.isDirectory()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Volume não encontrado");
        }

        Path path = Paths.get(uploadDir, request.getUserDir()).toAbsolutePath().normalize();
        File directory = new File(path.toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return ResponseEntity.ok("Diretório criado com sucesso: " + directory.getAbsolutePath());
    }

    @PostMapping(value = "/upload/{dir}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable String dir) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Arquivo vazio");
        }

        try {
            Path path = Paths.get(uploadDir, dir).toAbsolutePath().normalize();
            File directory = new File(path.toString());
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File destinationFile = new File(directory, file.getOriginalFilename());
            file.transferTo(destinationFile);
            return ResponseEntity.ok("Arquivo enviado com sucesso: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileTreeResponse>> listFiles() {
        File directory = new File(uploadDir);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Directory not found!");
        }

        return ResponseEntity.ok(processFileService.getFileTreeResponse(directory));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            File file = filePath.toFile();

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = new UrlResource(file.toURI());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
