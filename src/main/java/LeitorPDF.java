import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class LeitorPDF {

    public String extrairTexto(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            System.err.println("Arquivo PDF não encontrado: " + caminhoArquivo);
            return null;
        }

        // Sintaxe correta para a versão 2.x do PDFBox
        try (PDDocument documento = PDDocument.load(arquivo)) {
            PDFTextStripper extrator = new PDFTextStripper();
            return extrator.getText(documento);
        } catch (IOException e) {
            System.err.println("Erro ao ler o PDF: " + e.getMessage());
            return null;
        }
    }
}