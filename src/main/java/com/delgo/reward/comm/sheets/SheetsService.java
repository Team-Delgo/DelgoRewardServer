package com.delgo.reward.comm.sheets;

import com.delgo.reward.comm.code.CategoryCode;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import static com.amazonaws.util.ClassLoaderHelper.getResourceAsStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SheetsService {
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public void getSheetsData(CategoryCode categoryCode) throws IOException, GeneralSecurityException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        String sheetId = "10wrl07KdizmU2Z1ntSTVjbbYGV6V22tUMRJjjmDTBv4";
        String sheetName = categoryCode.getCode() + "_LIST";
        String range = sheetName + "!A1:M300";

        Sheets sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Google Sheets API")
                .build();

        ValueRange response = sheetsService.spreadsheets().values()
                .get(sheetId, range)
                .execute();

        List<List<Object>> values = response.getValues();
        for (List row : values) {
            SheetsDTO dto = new SheetsDTO(row);
            log.info("dto : {}", dto);
        }
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = getResourceAsStream("/credentials.json");
        if (in == null) throw new FileNotFoundException("Resource not found: credentials.json ");

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}