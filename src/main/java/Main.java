import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.google.api.services.sheets.v4.Sheets;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Sistema de Processamento Robusto...");

        try {
            Sheets service = GoogleSheetsConfig.getSheetsService();
            GoogleSheetsService sheetsService = new GoogleSheetsService(service);

            Properties config = new Properties();
            config.load(new FileInputStream("config.properties"));
            String idDaPlanilha = config.getProperty("PLANILHA_ID");

            String diretorioBase = System.getProperty("user.dir");
            File pastaEntrada = new File(diretorioBase + File.separator + "processar_csv");
            File pastaConcluidos = new File(diretorioBase + File.separator + "concluidos");

            File[] arquivos = pastaEntrada.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

            if (arquivos != null && arquivos.length > 0) {
                LeitorCSV leitor = new LeitorCSV();
                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                for (File arquivo : arquivos) {
                    System.out.println("\n📄 Analisando: " + arquivo.getName());
                    List<String[]> todasLinhas = leitor.lerExtrato(arquivo.getPath());

                    // --- FILTRO E ORDENAÇÃO ---
                    // 1. Removemos o cabeçalho (linhas que começam com "Data")
                    // 2. Removemos linhas vazias
                    List<String[]> transacoesValidas = todasLinhas.stream()
                        .filter(linha -> linha.length > 0 && !linha[0].equalsIgnoreCase("Data"))
                        .sorted(Comparator.comparing(t -> LocalDate.parse(t[0], formatador)))
                        .collect(Collectors.toList());

                    System.out.println("📊 Encontradas " + transacoesValidas.size() + " transações válidas.");

                    for (String[] transacao : transacoesValidas) {
                        enviarParaPlanilha(transacao, sheetsService, idDaPlanilha);
                    }

                    // Move para concluídos
                    File destino = new File(pastaConcluidos, arquivo.getName());
                    Files.move(arquivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("✅ Arquivo " + arquivo.getName() + " finalizado!");
                }
            } else {
                System.out.println("ℹ️ Nenhum arquivo encontrado.");
            }

        } catch (Exception e) {
            System.err.println("❌ Erro no processamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void enviarParaPlanilha(String[] transacao, GoogleSheetsService sheetsService, String idDaPlanilha) throws Exception {
        String data = transacao[0];
        String valorRaw = transacao[1]; 
        String identificador = transacao[2];
        String descOriginal = transacao[3];

        String descCurta = descOriginal.replace("Transferência enviada pelo Pix - ", "")
                                       .replace("Transferência recebida pelo Pix - ", "")
                                       .replace("Compra no débito - ", "")
                                       .replace("Pagamento de fatura - ", "");
        
        if (descCurta.contains(" - ")) descCurta = descCurta.split(" - ")[0];
        if (descCurta.length() > 20) descCurta = descCurta.substring(0, 20).trim();

        String categoria = definirCategoria(descCurta);
        double valorNumerico = Double.parseDouble(valorRaw);
        String valorComSinal = String.format("%.2f", valorNumerico).replace(".", ",");
        
        String entrada = valorNumerico > 0 ? valorComSinal : "";
        String saudia = valorNumerico < 0 ? String.format("%.2f", Math.abs(valorNumerico)).replace(".", ",") : "";

        List<Object> linha = List.of(data, descCurta, categoria, valorComSinal, "", identificador, "", entrada, saudia);
        sheetsService.adicionarLinha(idDaPlanilha, "'Cartão 1'!A1", linha);
    }

    public static String definirCategoria(String d) {
        String desc = d.toLowerCase();
        if (desc.contains("ifood") || desc.contains("padaria") || desc.contains("marmita")) return "Alimentação";
        if (desc.contains("uber") || desc.contains("99app") || desc.contains("posto")) return "Transporte";
        if (desc.contains("mercado") || desc.contains("giassi") || desc.contains("angeloni")) return "Mercado";
        if (desc.contains("jane") || desc.contains("recebido")) return "Recebimento Pix";
        return "Outros";
    }
}