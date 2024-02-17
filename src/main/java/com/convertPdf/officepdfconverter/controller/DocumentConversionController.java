package com.convertPdf.officepdfconverter.controller;

import com.convertPdf.officepdfconverter.service.DocumentConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DocumentConversionController {

    private final DocumentConversionService conversionService;

    public DocumentConversionController(DocumentConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @PostMapping("/convert-to-pdf")
    public ResponseEntity<String> convertToPdf(@RequestParam("file") MultipartFile file) throws Exception {
        String base64Pdf = conversionService.convertToPdfAndReturnBase64(file);
        return ResponseEntity.ok(base64Pdf);
    }
}
