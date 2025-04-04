package com.example.demo.controller;

import com.example.demo.service.FilesService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/compress")
public class FileZipController {

    private final FilesService filesService;


    public FileZipController(FilesService filesService) {
        this.filesService = filesService;
    }


    @GetMapping("/download-anexos")
    public ResponseEntity<byte[]> downloadAndZipAnexos() {
        try {
            // Obter os links dos PDFs da página da ANS
            String anexo1Url = filesService.getPdfUrl("Anexo I");
            String anexo2Url = filesService.getPdfUrl("Anexo II");

            if (anexo1Url == null || anexo2Url == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(("Não foi possível encontrar os anexos na página da ANS.").getBytes());
            }

            // Baixar os arquivos
            byte[] anexoI = filesService.downloadFromUrl(anexo1Url);
            byte[] anexoII = filesService.downloadFromUrl(anexo2Url);

            // Criar o ZIP
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
                filesService.addToZip(zipOutputStream, "Anexo_I.pdf", anexoI);
                filesService.addToZip(zipOutputStream, "Anexo_II.pdf", anexoII);
            }

            // Cabeçalhos da resposta
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=anexos.zip");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(byteArrayOutputStream.toByteArray());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao processar os anexos: " + e.getMessage()).getBytes());
        }
    }


}
