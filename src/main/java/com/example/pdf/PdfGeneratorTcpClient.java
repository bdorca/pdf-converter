package com.example.pdf;

import com.artofsolving.jodconverter.*;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.ConnectException;
import java.util.LinkedHashMap;
import java.util.Map;

public class PdfGeneratorTcpClient {

    public byte[] convertFodtToPdf(byte[] fodt) {
        ByteArrayInputStream bis = new ByteArrayInputStream(fodt);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BasicDocumentFormatRegistry formatRegistry = new DefaultDocumentFormatRegistry();

        String host = "localhost";
        int port = 8100;
        boolean streamBased = true;
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(host, port);
        try {
            connection.connect();
            DocumentConverter converter;
            if (streamBased) {
                converter = new StreamOpenOfficeDocumentConverter(connection);
            } else {
                converter = new OpenOfficeDocumentConverter(connection);
            }
            DocumentFormat output = formatRegistry.getFormatByFileExtension("pdf");
            // https://issues.alfresco.com/jira/browse/MNT-14700
            Map<String, Integer> pdfOptions = new LinkedHashMap<>();
            pdfOptions.put("SelectPdfVersion", 1);
            output.setExportOption(DocumentFamily.TEXT, "FilterName", "writer_pdf_Export");
            output.setExportOption(DocumentFamily.TEXT, "FilterData", pdfOptions);
            converter.convert(bis, formatRegistry.getFormatByFileExtension("odt"), bos, output);
            return bos.toByteArray();
        } catch (ConnectException | RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (connection.isConnected()) {
                connection.disconnect();
            }
        }
    }

}
