package com.example.demo.controller;

import com.example.demo.service.FilesService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api")
public class PDFToCSVController {

    private final FilesService filesService;

    public PDFToCSVController(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostMapping("/pdf-to-csv")
    public ResponseEntity<?> convertPdfToCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("O arquivo enviado está vazio.");
        }

        File pdfTempFile = null;
        File csvFile = null;
        File zipFile = null;

        try {
            pdfTempFile = filesService.saveTempPdfFile(file);
            String csvData = filesService.extractTableFromPDF(pdfTempFile.getAbsolutePath());

            csvFile = filesService.createCsvFile(csvData);
            zipFile = filesService.createZipFile(csvFile);

            byte[] zipBytes = Files.readAllBytes(zipFile.toPath());

            return filesService.buildZipResponse(zipBytes, "Teste_Rebeca.zip");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o arquivo: " + e.getMessage());
        } finally {
            filesService.cleanupTempFiles(pdfTempFile, csvFile, zipFile);
        }
    }
}
