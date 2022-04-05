package com.example.mysqlserver;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class TaggingController {

    @PostMapping(value = "/tagDocument", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User tagDocument(@RequestPart("document") MultipartFile document) throws IOException {
//        document.getBytes()
        PDDocument pdDocument = Loader.loadPDF(document.getBytes());
        PDFTextStripper stripper = new PDFTextStripper();

        for (int p = 1; p <= pdDocument.getNumberOfPages(); ++p) {
            // Set the page interval to extract. If you don't, then all pages would be extracted.
            stripper.setStartPage(p);
            stripper.setEndPage(p);

            // let the magic happen
            String text = stripper.getText(pdDocument);

            // do some nice output with a header
            String pageStr = String.format("page %d:", p);
            System.out.println(pageStr);
            for (int i = 0; i < pageStr.length(); ++i) {
                System.out.print("-");
            }
            System.out.println();
            System.out.println(text.trim());
            System.out.println();

            // If the extracted text is empty or gibberish, please try extracting text
            // with Adobe Reader first before asking for help. Also read the FAQ
            // on the website:
            // https://pdfbox.apache.org/2.0/faq.html#text-extraction
        }
        User user = new User();
        user.setFirstName("ashish");
        return user;

//		return "success";
    }
}
