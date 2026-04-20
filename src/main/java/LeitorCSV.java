import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class LeitorCSV {
    public List<String[]> lerExtrato(String caminhoArquivo) {
        List<String[]> linhas = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(caminhoArquivo))) {
            String[] linha;
            reader.readNext(); // Pula o cabeçalho
            while ((linha = reader.readNext()) != null) {
                linhas.add(linha);
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler CSV: " + e.getMessage());
        }
        return linhas;
    }
}