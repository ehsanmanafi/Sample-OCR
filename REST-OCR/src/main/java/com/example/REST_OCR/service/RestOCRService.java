package com.example.REST_OCR.service;

import com.example.REST_OCR.entity.RestOCREntity;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RestOCRService {
    private final Tesseract tesseract;

    public RestOCRService(Tesseract tesseract) throws IOException {
        this.tesseract = tesseract;
    }
    @Value("${spring.application.FileTOScan.BASEURL}")
    private String BASEURL;
    @Async
    public CompletableFuture<Set<String>> getSearchResult(String search) throws IOException,TesseractException {
        System.out.println(BASEURL);
        Set<String> result =  getFilesList().stream().filter(file ->
                    {
                        try {
                            return tesseract.doOCR(new File(BASEURL+ "\\" + file)).contains(search);
                        } catch (TesseractException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toSet());

        return CompletableFuture.completedFuture(result);
    }

    public Set<RestOCREntity> getOcrFullResult() throws IOException {
        Set<RestOCREntity> ocrEntities=new HashSet<>();

        getFilesList().stream().forEach(file ->
                {
                    try {
                        ocrEntities.add( new RestOCREntity(file,tesseract.doOCR(new File(BASEURL+ "\\" + file))));
                    } catch (TesseractException e) {
                        throw new RuntimeException(e);
                    }
                });
        return ocrEntities;

    }

    private Set<String> getFilesList() throws IOException {
        return   Files.list(Paths.get(BASEURL))
                .filter(file -> !Files.isDirectory(file))
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toSet());
    }

}
