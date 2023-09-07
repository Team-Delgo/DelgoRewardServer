package com.delgo.reward.comm.sheets;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.mongoDomain.MongoMungple;
import com.delgo.reward.mongoService.MongoMungpleService;
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

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.amazonaws.util.ClassLoaderHelper.getResourceAsStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SheetsService {
    private final GeoService geoService;
    private final MongoMungpleService mongoMungpleService;

    private Sheets sheets;
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final String SHEET_ID ="10wrl07KdizmU2Z1ntSTVjbbYGV6V22tUMRJjjmDTBv4";

    @PostConstruct
    public void connectSheet() throws GeneralSecurityException, IOException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        sheets = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Google Sheets API")
                .build();
    }

    public void getSheetsData(CategoryCode categoryCode) throws IOException, GeneralSecurityException {
        String range = generateRangeString("A", 1, "M", 300);

        ValueRange response = sheets.spreadsheets().values()
                .get(SHEET_ID, categoryCode.getSheetName() + "!" + range)
                .execute();

        List<List<Object>> values = response.getValues();
        for (int rowNum = 1; rowNum < values.size(); rowNum++) { // 1 행은 Filter 행 따라서 패싱 한다.
            List<Object> row = values.get(rowNum);
            SheetDTO sheet = new SheetDTO(row);
            if (!sheet.getIsRegist()) {
                MongoMungple mongoMungple = sheet.toMongoEntity(categoryCode, geoService.getGeoData(sheet.getAddress()));
                mongoMungple.setAcceptSize(sheet.getAcceptSize());
                mongoMungple.setBusinessHour(sheet.getBusinessHour());

                checkSaveConfirm(categoryCode, rowNum + 1); // values = 0부터 시작 따라서 + 1 해줘야 함.
                mongoMungpleService.save(mongoMungple);
            }
        }
    }

    public void checkSaveConfirm(CategoryCode categoryCode, int rowNum) throws IOException, GeneralSecurityException {
        String range = generateRangeString("A", rowNum, "B", rowNum);

        // 등록 여부(A)에 "True", 등록 날짜(B)에 현재 날짜를 설정
        List<Object> dataRow = Arrays.asList("TRUE", LocalDate.now().toString());
        ValueRange body = new ValueRange().setValues(List.of(dataRow));

        sheets.spreadsheets().values()
                .update(SHEET_ID, categoryCode.getSheetName() + "!" + range, body)
                .setValueInputOption("RAW")
                .execute();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = getResourceAsStream("/credentials.json");
        if (in == null) throw new FileNotFoundException("Resource not found: credentials.json ");

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                Collections.singletonList(SheetsScopes.SPREADSHEETS))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public String generateRangeString(String startColumn, int startRow, String endColumn, int endRow) {
        return startColumn + startRow + ":" + endColumn + endRow;
    }
}