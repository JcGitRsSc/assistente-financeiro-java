import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

public class GoogleSheetsConfig {
    
    private static final String APPLICATION_NAME = "Assistente Financeiro Jonas";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    
    // Este é o nome do ficheiro JSON que baixaste do Google Cloud
    private static final String CREDENTIALS_FILE_PATH = "credenciais.json";

    /**
     * Este método cria e devolve a ligação autenticada com a API do Google.
     */
    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // 1. Abre o ficheiro de segredos (JSON)
        FileInputStream credenciaisStream = new FileInputStream(CREDENTIALS_FILE_PATH);

        // 2. Define que o robô só pode mexer em Planilhas (Escopo de segurança)
        GoogleCredentials credentials = GoogleCredentials.fromStream(credenciaisStream)
                .createScoped(Collections.singletonList(SheetsScopes.SPREADSHEETS));

        // 3. Constrói o serviço final
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}