package utilities;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class PDFReportGenerator {

    public static File generateReport(String content, String filename) throws IOException {
        // Crear documento y página
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Crear el content stream
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Iniciar texto
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 14);
        contentStream.newLineAtOffset(100, 700);


        String[] lines = content.split("\n");
        for (String line : lines) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -20); // new line
        }

        // Finalizar escritura
        contentStream.endText();
        contentStream.close();

        // Guardar el archivo en el directorio del usuario
        String userDir = System.getProperty("user.home");
        File file = new File(userDir, filename + ".pdf");
        document.save(file);
        document.close();

        System.out.println("PDF saved at: " + file.getAbsolutePath());
        return file;
    }
}
