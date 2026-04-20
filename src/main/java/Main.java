import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import com.google.api.services.sheets.v4.Sheets;

public class Main {
    public static void main(String[] args) {
        System.out.println("A iniciar o Assistente Financeiro...");

        try {
            // 1. Inicializa a configuração do Google
            Sheets service = GoogleSheetsConfig.getSheetsService();
            GoogleSheetsService sheetsService = new GoogleSheetsService(service);

            // 2. Lê o ID do arquivo externo (Segurança)
            Properties config = new Properties();
            config.load(new FileInputStream("config.properties"));
            String idDaPlanilha = config.getProperty("PLANILHA_ID");

            // Se esquecer de criar o arquivo ou colocar o ID, ele avisa
            if (idDaPlanilha == null || idDaPlanilha.isEmpty()) {
                throw new RuntimeException("ID da planilha não encontrado no config.properties!");
            }

            // 3. Define os dados de teste
           List<Object> teste = List.of("16/04/2026", "Teste de Conexão Seguro", "Segurança", "100,00");

            // 4. Envia para a aba 'Cartão 1'
            sheetsService.adicionarLinha(idDaPlanilha, "'Cartão 1'!A1", teste);

            System.out.println("✅ Sucesso! Verifique a aba Cartão 1 na sua planilha.");

        } catch (Exception e) {
            System.err.println("❌ Erro ao rodar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}