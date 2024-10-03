package com.example.REST_OCR.controller;

import com.example.REST_OCR.entity.RestOCREntity;
import com.example.REST_OCR.service.RestOCRService;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/ocr")
public class RestOCRController {

    private final RestOCRService ocrService;

    public RestOCRController(RestOCRService ocrService) {
        this.ocrService = ocrService;
    }
    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<?>> getImageToString(@RequestParam String search) throws TesseractException, IOException {
        return ocrService.getSearchResult(search).thenApply(ResponseEntity::ok);
    }
    @GetMapping("list")
    public ResponseEntity<Set<RestOCREntity>> getAllData() throws IOException {
        return new ResponseEntity<>(ocrService.getOcrFullResult(),HttpStatus.OK);
    }

}
