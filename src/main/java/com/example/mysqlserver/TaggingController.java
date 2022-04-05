package com.example.mysqlserver;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@RestController
public class TaggingController {

    @PostMapping(value = "/tagDocument", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User tagDocument(@RequestPart("document") MultipartFile document, @RequestPart("mapping") MultipartFile mapping) throws IOException {


        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(mapping.getInputStream());
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        //Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheet("data");
        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            //For each row, iterate through all the columns
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                //Check the cell type and format accordingly
                switch (evaluator.evaluateInCell(cell).getCellType()) {
                    case NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "\t");
                        break;
                    case STRING:
                        System.out.print(cell.getStringCellValue() + "\t");
                        break;
                }
            }
            System.out.println("");
        }


        PDDocument pdDocument = Loader.loadPDF(document.getBytes());
        PDFTextStripper stripper = new PDFTextStripper();

        for (int p = 1; p <= pdDocument.getNumberOfPages(); ++p) {
            // Set the page interval to extract. If you don't, then all pages would be extracted.
            stripper.setStartPage(p);
            stripper.setEndPage(p);
            writeOnPDF(pdDocument, p);
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

        //Saving the document
        pdDocument.save(new File("/Users/ashgarg/Downloads/new.pdf"));

        //Closing the document
        pdDocument.close();

        User user = new User();
        user.setFirstName("ashish");
        return user;

//		return "success";
    }

    private void writeOnPDF(PDDocument pdDocument, int pageNo) throws IOException {
        //Retrieving the pages of the document
        PDPage page = pdDocument.getPage(pageNo - 1);
        PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page,
                PDPageContentStream.AppendMode.APPEND, true, true);
//        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        //Begin the Content stream
        contentStream.beginText();

        //Setting the font to the Content stream
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

        //Setting the position for the line
        contentStream.newLineAtOffset(25, 500);
        String text = "Ashish";

        //Adding text in the form of string
        contentStream.showText(text);

        //Ending the content stream
        contentStream.endText();

        System.out.println("Content added");

        //Closing the content stream
        contentStream.close();

    }
}
