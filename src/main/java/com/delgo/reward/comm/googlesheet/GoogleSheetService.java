package com.delgo.reward.comm.googlesheet;

import com.delgo.reward.cacheService.MungpleCacheService;
import com.delgo.reward.comm.code.CategoryCode;

import com.delgo.reward.common.domain.Code;
import com.delgo.reward.domain.common.GeoData;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.ncp.service.port.GeoDataPort;
import com.delgo.reward.common.service.CodeService;
import com.delgo.reward.service.FigmaService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleSheetService {

    private final GeoDataPort geoDataPort;
    private final CodeService codeService;
    private final FigmaService figmaService;
    private final MungpleCacheService mungpleCacheService;
    private final MongoMungpleService mongoMungpleService;

    private Sheets sheets;
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final String SHEET_ID ="10wrl07KdizmU2Z1ntSTVjbbYGV6V22tUMRJjjmDTBv4";

    @PostConstruct
    public void connectSheet() throws GeneralSecurityException, IOException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        sheets = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials())
                .setApplicationName("Google Sheets API")
                .build();
    }

    public List<String> saveSheetsDataToDB() {
        List<String> placeNames = new ArrayList<>();
        for (CategoryCode categoryCode : CategoryCode.values()) {
            if (categoryCode.shouldSkip()) // TOTAL, CA9999 일 경우에는 동작 X
                continue;

            // Select Google Sheets Data
            List<List<Object>> responseValues = getSheetResponseValues(categoryCode);
            if (responseValues.isEmpty())
                continue;

            // Sheets Data -> MongoDB
            processResponseValues(categoryCode, responseValues, placeNames);
        }

        return placeNames;
    }


    private List<List<Object>>  getSheetResponseValues(CategoryCode categoryCode) {
        log.info("saveSheetsDataToDB CategoryCode: {}", categoryCode);
        String range = generateRangeString("A", 1, "O", 300);

        try {
            ValueRange response = sheets.spreadsheets().values()
                    .get(SHEET_ID, categoryCode.getSheetName() + "!" + range)
                    .execute();

            return response.getValues();
        } catch (Exception e) {
            throw new NullPointerException("[getSheetData] : " + e.getMessage());
        }

    }

    private void processResponseValues(CategoryCode categoryCode, List<List<Object>> ResponseValues, List<String> placeNames) {
        for (int rowNum = 1; rowNum < ResponseValues.size(); rowNum++) {
            List<Object> row = ResponseValues.get(rowNum);
            if (!Boolean.parseBoolean((String) row.get(0))) { // isRegist 체크
                GoogleSheetDTO sheet = new GoogleSheetDTO(row, categoryCode);
                log.info("sheet PlaceName:{}", sheet.getPlaceName());

                // Mongo Mungple Setting & Save
                GeoData geoData = geoDataPort.getGeoData(sheet.getAddress());
                Code geoCode = codeService.getGeoCodeByAddress(sheet.getAddress());
                MongoMungple mongoMungple = sheet.toMongoEntity(categoryCode, geoData, geoCode);

                // 중복 이중 체크 ( 주소, 이름 )
                if(mongoMungpleService.isMungpleExisting(mongoMungple.getJibunAddress())
                        && mongoMungpleService.isMungpleExistingByPlaceName(mongoMungple.getPlaceName())) {
                    log.info("이미 등록된 멍플입니다. : {}", mongoMungple.getPlaceName());
                    placeNames.add("[" + mongoMungple.getPlaceName() + "]은 중복 데이터입니다.");
                    continue;
                }

                if (StringUtils.hasText(sheet.getAcceptSize()))
                    mongoMungple.setAcceptSize(sheet.getAcceptSize());
                if (StringUtils.hasText(sheet.getBusinessHour()))
                    mongoMungple.setBusinessHour(sheet.getBusinessHour());

                // Figma
                if (StringUtils.hasText(sheet.getFigmaNodeId())) {
                    figmaService.uploadFigmaDataToNCP(sheet.getFigmaNodeId(), mongoMungple);

                    // Figma 사진 저장 까지 완료 후 저장
                    MongoMungple savedMongoMungple = mongoMungpleService.save(mongoMungple);

                    // Cache Update
                    mungpleCacheService.updateCacheData(savedMongoMungple.getMungpleId(), savedMongoMungple);

                    placeNames.add("[" + savedMongoMungple.getPlaceName() + "] 등록 되었습니다.");
                    // Sheet IsRegist Data update [ false -> true ]
                    checkSaveConfirm(categoryCode, rowNum + 1);
                }
            }
        }
    }

    public void checkSaveConfirm(CategoryCode categoryCode, int rowNum) {
        try {
            String range = generateRangeString("A", rowNum, "B", rowNum);

            // 등록 여부(A)에 "True", 등록 날짜(B)에 현재 날짜를 설정
            List<Object> dataRow = Arrays.asList("TRUE", LocalDate.now().toString());
            ValueRange body = new ValueRange().setValues(List.of(dataRow));

            sheets.spreadsheets().values()
                    .update(SHEET_ID, categoryCode.getSheetName() + "!" + range, body)
                    .setValueInputOption("RAW")
                    .execute();
        } catch (IOException e){
            throw new RuntimeException("[checkSaveConfirm] : " + e.getMessage());
        }
    }

    private static Credential getCredentials() throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("service-account.json");
        if (in == null) throw new FileNotFoundException("Resource not found: service-account.json");

        return GoogleCredential.fromStream(in).createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    }

    public String generateRangeString(String startColumn, int startRow, String endColumn, int endRow) {
        return startColumn + startRow + ":" + endColumn + endRow;
    }
}