/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.process.genomic.processGenome.vo;

import java.util.List;

/**
 *
 * @author salipe
 */
public class FileTreeResponse {
    
    private String name;
    private Boolean isDir;
    private List<FileTreeResponse> content;

    public FileTreeResponse(String name, Boolean isDir) {
        this.name = name;
        this.isDir = isDir;
    }
    
    

    public FileTreeResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDir() {
        return isDir;
    }

    public void setIsDir(Boolean isDir) {
        this.isDir = isDir;
    }

    public List<FileTreeResponse> getContent() {
        return content;
    }

    public void setContent(List<FileTreeResponse> content) {
        this.content = content;
    }

    
    
    
    
}
