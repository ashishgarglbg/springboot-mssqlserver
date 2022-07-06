package com.example.mysqlserver;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

//@RestController
@Controller
public class TaggingController {
    private final TaggingService taggingService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdHHmmss");

    public TaggingController(TaggingService taggingService) {
        this.taggingService = taggingService;
    }

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @PostMapping(value = "/tagDocument", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public Object tagDocument(@RequestPart("document") MultipartFile document,
                              @RequestPart("mapping") MultipartFile mapping,
                              RedirectAttributes attributes) throws IOException {
        boolean isError = false;
        if (document.isEmpty()) {
            attributes.addFlashAttribute("documentError", "Please select a report to tag");
            isError = true;
        }
        if (mapping.isEmpty()) {
            attributes.addFlashAttribute("mappingError", "Please select a mapping sheet");
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
        String filename = document.getOriginalFilename().split("\\.")[0] + "_" + LocalDateTime.now().format(formatter) + ".pdf";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_PDF)
//                .contentLength(out.size())
//                .body(new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(out.size())
                .body(new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));
        //https://github.com/eligrey/FileSaver.js/issues/238#issuecomment-224572273
        //https://stackoverflow.com/questions/1999607/download-and-open-pdf-file-using-ajax
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        ModelAndView model = new ModelAndView("welcome");
        model.addObject("error", "internal error while processing");
        return model;
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ModelAndView handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        ModelAndView model = new ModelAndView("welcome");
        if ("document".equalsIgnoreCase(ex.getRequestPartName())) {
            model.addObject("documentError", "Please select a report to tag");
        }
        else if ("mapping".equalsIgnoreCase(ex.getRequestPartName())) {
            model.addObject("mappingError", "Please select a mapping sheet");
        } else {
            model.addObject("error", "internal error while processing");
        }
        return model;
    }
}
