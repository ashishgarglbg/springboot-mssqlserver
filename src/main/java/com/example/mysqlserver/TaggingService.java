package com.example.mysqlserver;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaggingService {
    public Map<String, String> prepareMapping(MultipartFile mapping) {
        Map<String, String> investorNameToIdMapping = new HashMap<>();
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(mapping.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        XSSFSheet sheet = workbook.getSheet("data");

        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row != null) {
                Cell cell1 = row.getCell(0);
                Cell cell2 = row.getCell(1);
                investorNameToIdMapping.put(evaluator.evaluateInCell(cell1).getStringCellValue(), evaluator.evaluateInCell(cell2).getStringCellValue());
            }
        }
        return investorNameToIdMapping;
    }

    public PDDocument prepareTaggedDocument(MultipartFile document, Map<String, String> investorNameToIdMapping) {
        PDDocument pdDocument;
        try {
            pdDocument = Loader.loadPDF(document.getBytes());
            PDFTextStripper stripper = new PDFTextStripper();

            for (int pageNo = 1; pageNo <= pdDocument.getNumberOfPages(); ++pageNo) {
                stripper.setStartPage(pageNo);
                stripper.setEndPage(pageNo);
                String text = stripper.getText(pdDocument);

                for(String key : investorNameToIdMapping.keySet()){
                    if(text.contains(key)){
                        writeOnPDF(pdDocument, pageNo, investorNameToIdMapping.get(key));
                    }
                }
            }
            return pdDocument;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void writeOnPDF(PDDocument pdDocument, int pageNo, String investorId) throws IOException {
        PDPage page = pdDocument.getPage(pageNo - 1);
        PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page,
                PDPageContentStream.AppendMode.APPEND, true, true);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 12);
        contentStream.setNonStrokingColor(Color.RED);
        contentStream.newLineAtOffset(25, 500);
        contentStream.showText(investorId);
        contentStream.endText();
        contentStream.close();
    }
}
