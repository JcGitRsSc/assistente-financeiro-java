import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GoogleSheetsService {
    private final Sheets service;

    public GoogleSheetsService(Sheets service) {
        this.service = service;
    }

    /**
     * Adiciona uma nova linha no final da tabela.
     * @param spreadsheetId O ID longo que encontras no URL da tua planilha.
     * @param range O nome da folha e o ponto de partida (ex: "Página1!A1").
     * @param valores Uma lista com os dados da coluna (Data, Descrição, Valor, etc).
     */
    public void adicionarLinha(String spreadsheetId, String range, List<Object> valores) throws IOException {
        ValueRange body = new ValueRange().setValues(Arrays.asList(valores));

        service.spreadsheets().values()
                .append(spreadsheetId, range, body)
                .setValueInputOption("USER_ENTERED")
                .execute();

        System.out.println("✅ Dados enviados para o Google Sheets com sucesso!");
    }
}