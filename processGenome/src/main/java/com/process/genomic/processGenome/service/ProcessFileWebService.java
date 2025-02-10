/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.process.genomic.processGenome.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author salipe
 */

@RestController
@RequestMapping(value = "/files", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProcessFileWebService {
    
     @Value("${upload.dir:/mnt/storage}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Arquivo vazio");
        }

        try {
            Path path = Paths.get(uploadDir).toAbsolutePath().normalize();
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
    public ResponseEntity<List<String>> listFiles() {
        File directory = new File(uploadDir);
        if (!directory.exists() || !directory.isDirectory()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Arrays.asList("Diretório não encontrado"));
        }
        
        List<String> files = Arrays.stream(directory.listFiles())
                .map(File::getName)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(files);
    }
    
}
