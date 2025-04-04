package com.example.demo.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import technology.tabula.*;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FilesService {

    private static final String ANS_URL = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
    private static final String BASE_URL = "https://www.gov.br";
    private static final String CSV_SEPARATOR = ";";
    private static final String UTF8_BOM = "\uFEFF";

    public String extractTableFromPDF(String pdfPath) throws IOException {
        File pdfFile = new File(pdfPath);

        try (PDDocument document = PDDocument.load(pdfFile)) {
            ObjectExtractor extractor = new ObjectExtractor(document);
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();

            StringBuilder csvOutput = new StringBuilder();
            boolean isFirstRow = true;
            boolean headerAdded = false;  // Para evitar a repetição do cabeçalho

            for (int i = 3; i <= document.getNumberOfPages(); i++) { // Processa desde a página 1
                Page page = extractor.extract(i);
                List<Table> tables = sea.extract(page);

                if (tables.isEmpty()) {
                    tables = bea.extract(page);
                }

                for (Table table : tables) {
                    for (List<RectangularTextContainer> row : table.getRows()) {
                        StringBuilder rowOutput = new StringBuilder();
                        for (int col = 0; col < row.size(); col++) {
                            String cellText = row.get(col).getText().trim().replaceAll("\\s+", " ");
                            if (!isFirstRow) {
                                cellText = cellText.replace("OD", "Seg. Odontológica")
                                        .replace("AMB", "Seg. Ambulatorial");
                            }
                            rowOutput.append(cellText);
                            if (col < row.size() - 1) {
                                rowOutput.append(CSV_SEPARATOR);
                            }
                        }

                        // Verifica se a linha extraída é o cabeçalho e evita repetição
                        if (isFirstRow) {
                            if (!headerAdded) {
                                csvOutput.append(rowOutput).append("\n");
                                headerAdded = true;
                            }
                        } else {
                            csvOutput.append(rowOutput).append("\n");
                        }

                        isFirstRow = false; // A partir da segunda linha, as substituições são aplicadas
                    }
                }
            }
            return csvOutput.toString();
        }
    }


    public File saveTempPdfFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("upload_", ".pdf");
        file.transferTo(tempFile);
        return tempFile;
    }

    public File createCsvFile(String csvData) throws IOException {
        File csvFile = File.createTempFile("Teste_Rebeca", ".csv");
        try (FileOutputStream fos = new FileOutputStream(csvFile);
             OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            writer.write(UTF8_BOM);
            writer.write(csvData);
        }
        return csvFile;
    }

    public File createZipFile(File csvFile) throws IOException {
        File zipFile = new File(csvFile.getParent(), csvFile.getName().replace(".csv", ".zip"));

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            zipOut.putNextEntry(new ZipEntry(csvFile.getName()));
            Files.copy(csvFile.toPath(), zipOut);
            zipOut.closeEntry();
        }
        return zipFile;
    }

    public void cleanupTempFiles(File... files) {
        for (File file : files) {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    public ResponseEntity<byte[]> buildZipResponse(byte[] zipBytes, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(zipBytes);
    }

    public String getPdfUrl(String fileName) {
        try {
            Document doc = Jsoup.connect(ANS_URL)
                    .userAgent("Mozilla/5.0")
                    .get();

            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String href = link.attr("href");
                String text = link.text();
                if (text.contains(fileName) || href.contains(fileName.replace(" ", "%20"))) {
                    return href.startsWith("http") ? href : BASE_URL + href;
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // idealmente usar log
        }
        return null;
    }

    public byte[] downloadFromUrl(String urlString) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public void addToZip(ZipOutputStream zipOutputStream, String fileName, byte[] fileData) throws IOException {
        zipOutputStream.putNextEntry(new ZipEntry(fileName));
        zipOutputStream.write(fileData);
        zipOutputStream.closeEntry();
    }

}
