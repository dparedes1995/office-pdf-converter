package com.convertPdf.officepdfconverter.service;

import com.convertPdf.officepdfconverter.exception.DocumentConversionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Service
public class DocumentConversionService {

    @Value("${libreoffice.command.path}")
    private String libreOfficeCommandPath;

    public String convertToPdfAndReturnBase64(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new DocumentConversionException("The file is empty.");
        }

        // Crear archivos temporales para la conversión y el PDF resultante
        Path inputFileTemp = Files.createTempFile("input-", ".tmp");
        file.transferTo(inputFileTemp.toFile());

        Path outputFileTemp = Files.createTempFile("output-", ".pdf");

        ProcessBuilder processBuilder = new ProcessBuilder(
                libreOfficeCommandPath, "--headless", "--convert-to", "pdf:writer_pdf_Export",
                "--outdir", inputFileTemp.toFile().getParent(),
                inputFileTemp.toString());
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                reader.lines().forEach(System.out::println); // Log output for debugging
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new DocumentConversionException("Failed to convert document, exit code " + exitCode);
            }

            // El nombre del archivo PDF resultante puede variar, así que lo buscamos en el directorio temporal
            File pdfFile = outputFileTemp.toFile().getParentFile().listFiles((dir, name) -> name.endsWith(".pdf"))[0];

            // Convertir el archivo PDF resultante a Base64
            byte[] fileContent = Files.readAllBytes(pdfFile.toPath());
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            return encodedString;
        } catch (Exception e) {
            throw new DocumentConversionException("Error converting document", e);
        } finally {
            // Asegurar que los archivos temporales se eliminen
            Files.deleteIfExists(inputFileTemp);
            Files.deleteIfExists(outputFileTemp);
        }
    }
}
