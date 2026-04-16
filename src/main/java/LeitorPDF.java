import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class LeitorPDF {

    public String extrairTexto(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        
        if (!arquivo.exists()){
            System.err.println("Arquivo PDF não encontrado: " + caminhoArquivo);
            return null;
        }
            
        try (PDDocument document = Loader.loadPDF(arquivo)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            System.err.println("Erro ao ler o PDF: " + e.getMessage());
            return null; // Adicionado para o método sempre retornar algo
        }
    }
}