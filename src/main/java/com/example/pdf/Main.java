package com.example.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static PdfGeneratorTcpClient pdfGeneratorTcpClient = new PdfGeneratorTcpClient();

    public static void main(String[] args) {
        try {
            String fodt = readFile("./klima.fodt", StandardCharsets.UTF_8);
            byte[] pdfContent = pdfGeneratorTcpClient.convertFodtToPdf(fodt.getBytes(StandardCharsets.UTF_8));
            try (FileOutputStream stream = new FileOutputStream("./klima.pdf")) {
                stream.write(pdfContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
