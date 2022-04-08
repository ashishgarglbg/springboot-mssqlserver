package com.example.mysqlserver;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

//@RestController
@Controller
public class TaggingController {

    private final TaggingService taggingService;

    public TaggingController(TaggingService taggingService) {
        this.taggingService = taggingService;
    }

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @PostMapping(value = "/tagDocument", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public Object tagDocument(@RequestPart("document") MultipartFile document, @RequestPart("mapping") MultipartFile mapping, RedirectAttributes attributes) throws IOException {
        boolean isError = false;
        if (document.isEmpty()) {
            attributes.addFlashAttribute("documentError", "Please select a report to tag");
            isError = true;
        }
        if (mapping.isEmpty()) {
            attributes.addFlashAttribute("mappingError", "Please select a mapping to tag");
            isError = true;
        }
        if (isError) {
            return "redirect:/";
        }

        Map<String, String> investorNameToIdMapping = taggingService.prepareMapping(mapping);
        PDDocument pdDocument = taggingService.prepareTaggedDocument(document, investorNameToIdMapping);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        pdDocument.save(out);

        //Closing the document
        pdDocument.close();

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TaggedDocument.pdf");


        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }
}
