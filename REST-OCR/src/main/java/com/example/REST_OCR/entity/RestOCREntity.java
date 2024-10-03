package com.example.REST_OCR.entity;

import lombok.Data;

@Data
public class RestOCREntity {
    String fileName;
    String content;

    public RestOCREntity(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }
}
