/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.process.genomic.processGenome.service;

import com.process.genomic.processGenome.vo.FileTreeResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author salipe
 */

@Service
public class ProcessFileService {
    
    public List<FileTreeResponse> getFileTreeResponse(File directory){
        
        List<FileTreeResponse> files = new ArrayList<>();
        
        for (File file: directory.listFiles()){
            FileTreeResponse f = new FileTreeResponse(file.getName(), file.isDirectory());
            if (file.isDirectory()){
                f.setContent(getFileTreeResponse(file));
            }
            files.add(f);
        }
        
        return files;
    }
    
}
