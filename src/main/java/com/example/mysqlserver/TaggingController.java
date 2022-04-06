package com.example.mysqlserver;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
public class TaggingController {

    private final TaggingService taggingService;

    public TaggingController(TaggingService taggingService) {
        this.taggingService = taggingService;
    }

    @PostMapping(value = "/tagDocument", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> tagDocument(@RequestPart("document") MultipartFile document, @RequestPart("mapping") MultipartFile mapping) throws IOException {
        Map<String, String> investorNameToIdMapping = taggingService.prepareMapping(mapping);
        PDDocument pdDocument = taggingService.prepareTaggedDocument(document, investorNameToIdMapping);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        pdDocument.save(out);

        //Closing the document
        pdDocument.close();

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=citiesreport.pdf");


        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }
}
