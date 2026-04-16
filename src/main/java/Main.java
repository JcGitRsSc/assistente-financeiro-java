import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Assistente Financeiro Iniciado com Sucesso!");
    
        LeitorPDF leitor = new LeitorPDF();
        String CaminhoDoArquivo = "fatura.pdf"; // Substitua pelo caminho do seu arquivo PDF
        System.out.println("Lendo o arquivo PDF: " + CaminhoDoArquivo);
        String Texto= leitor.extrairTexto(CaminhoDoArquivo);
    
    if(Texto != null){
        System.out.println("Texto extraído do PDF:");
        System.out.println(Texto);
    } else {
        System.out.println("Falha ao extrair texto do PDF.");
    }
}
}