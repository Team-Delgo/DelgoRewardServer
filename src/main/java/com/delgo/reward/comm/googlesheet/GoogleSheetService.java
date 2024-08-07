package com.delgo.reward.comm.googlesheet;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.GoogleSheetException;
import com.delgo.reward.comm.ncp.geo.GeoData;
import com.delgo.reward.comm.ncp.geo.GeoDataService;
import com.delgo.reward.code.domain.Code;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.mungple.service.MungpleService;
import com.delgo.reward.code.service.CodeService;
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

    private final CodeService codeService;
    private final FigmaService figmaService;
    private final GeoDataService geoDataService;
    private final MungpleService mungpleService;

    private Sheets sheets;
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final String SHEET_ID ="10wrl07KdizmU2Z1ntSTVjbbYGV6V22tUMRJjjmDTBv4";

    private final Object lock = new Object();

    @PostConstruct
    public void connectSheet() throws GeneralSecurityException, IOException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        sheets = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials())
                .setApplicationName("Google Sheets API")
                .build();
    }

    public List<String> saveSheetsDataToDB() {
        synchronized (lock) {
            List<String> resultMessageList = new ArrayList<>();
            for (CategoryCode categoryCode : CategoryCode.values()) {
                if (categoryCode.shouldSkip()) // TOTAL, CA9999 일 경우에는 동작 X
                    continue;

                // Select Google Sheets Data
                List<List<Object>> responseValues = getResponseValuesFromSheet(categoryCode);
                if (responseValues.isEmpty())
                    continue;

                // Sheets Data -> MongoDB
                processResponseValues(categoryCode, responseValues, resultMessageList);
            }
            return resultMessageList;
        }
    }


    private List<List<Object>> getResponseValuesFromSheet(CategoryCode categoryCode) {
        log.info("saveSheetsDataToDB CategoryCode: {}", categoryCode);
        String range = generateRangeString("A", 1, "O", 1000);

        try {
            ValueRange response = sheets.spreadsheets().values()
                    .get(SHEET_ID, categoryCode.getSheetName() + "!" + range)
                    .execute();

            return response.getValues();
        } catch (Exception e) {
            throw new GoogleSheetException("[getSheetData] : " + e.getMessage());
        }

    }

    private void processResponseValues(CategoryCode categoryCode, List<List<Object>> ResponseValues, List<String> resultMessageList) {
        for (int rowNum = 1; rowNum < ResponseValues.size(); rowNum++) {
            List<Object> row = ResponseValues.get(rowNum);
            if(row.isEmpty())
                continue;

            String activeType = (String) row.get(0); // 동작 상태 지정 (TRUE, FALSE, UPDATE, DEL)
            activeType = activeType.trim();

            if (activeType.equals("TRUE") || activeType.equals("DEL SUCCESS") || activeType.isEmpty()) continue;
            try {
                GoogleSheetDTO sheet = new GoogleSheetDTO(row, categoryCode);
                GeoData geoData = geoDataService.getGeoData(sheet.getAddress());
                Code geoCode = codeService.getGeoCodeByAddress(geoData.getJibunAddress());
                Mungple mungple = sheet.toMongoEntity(categoryCode, geoData, geoCode);

                switch (activeType) {
                    case "FALSE" -> {
                        log.info("ADD sheet PlaceName:{}", sheet.getPlaceName());

                        // 중복 이중 체크 ( 주소, 이름 )
                        if (mungpleService.isMungpleExisting(mungple.getJibunAddress(), mungple.getPlaceName())) {
                            resultMessageList.add("[" + mungple.getPlaceName() + "]은 이미 등록된 장소입니다. \n");
                            continue;
                        }

                        if (StringUtils.hasText(sheet.getAcceptSize()))
                            mungple.setAcceptSize(sheet.getAcceptSize());
                        if (StringUtils.hasText(sheet.getBusinessHour()))
                            mungple.setBusinessHour(sheet.getBusinessHour());
                        if (StringUtils.hasText(sheet.getFigmaNodeId())) {
                            // Upload 및 setPhoto
                            figmaService.uploadFigmaDataToNCP(sheet.getFigmaNodeId(), mungple);
                            // Figma 사진 저장 까지 완료 후 저장
                            Mungple savedMungple = mungpleService.save(mungple);
                            // Cache Update
//                            mungpleQueryService.update(savedMungple.getMungpleId(), savedMungple);
                            // Sheet IsRegist Data update [ false -> true ]
                            checkSaveConfirm(categoryCode, rowNum + 1);
                            resultMessageList.add("[" + savedMungple.getPlaceName() + "] 등록되었습니다.");
                        }
                    }
                    case "UPDATE" -> {
                        log.info("UPDATE sheet PlaceName:{}", sheet.getPlaceName());

                        if (StringUtils.hasText(sheet.getAcceptSize()))
                            mungple.setAcceptSize(sheet.getAcceptSize());
                        if (StringUtils.hasText(sheet.getBusinessHour()))
                            mungple.setBusinessHour(sheet.getBusinessHour());
                        if (StringUtils.hasText(sheet.getFigmaNodeId()))
                            figmaService.uploadFigmaDataToNCP(sheet.getFigmaNodeId(), mungple);

                        Mungple dbMungple = mungpleService.getOneByPlaceName(sheet.getPlaceName());

                        mungple.setId(dbMungple.getId());
                        mungple.setMungpleId(dbMungple.getMungpleId());
                        Mungple savedMungple = mungpleService.save(mungple);

                        // Cache Update
//                        mungpleQueryService.update(savedMungple.getMungpleId(), savedMungple);
                        // Sheet IsRegist Data update [ update -> true ]
                        checkSaveConfirm(categoryCode, rowNum + 1);
                        resultMessageList.add("[" + savedMungple.getPlaceName() + "] 수정되었습니다.");
                    }
                    case "DEL" -> { // activeType 체크
                        log.info("DELETE sheet PlaceName:{}", sheet.getPlaceName());

                        Mungple dbMungple = mungpleService.getOneByPlaceName(sheet.getPlaceName());
                        mungpleService.delete(dbMungple.getMungpleId());

                        resultMessageList.add("[" + dbMungple.getPlaceName() + "] 삭제되었습니다.\n");
                        checkDeleteConfirm(categoryCode, rowNum + 1);
                    }
                }
            } catch (Exception e) {
                resultMessageList.add("[" + row.get(2) + "] 파싱 실패 에러가 발생했습니다 - " + e.getMessage());
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
            throw new GoogleSheetException("[checkSaveConfirm] : " + e.getMessage());
        }
    }

    public void checkDeleteConfirm(CategoryCode categoryCode, int rowNum) {
        try {
            String range = generateRangeString("A", rowNum, "B", rowNum);

            // 등록 여부(A)에 "DEL SUCCESS", 등록 날짜(B)에 현재 날짜를 설정
            List<Object> dataRow = Arrays.asList("DEL SUCCESS", LocalDate.now().toString());
            ValueRange body = new ValueRange().setValues(List.of(dataRow));

            sheets.spreadsheets().values()
                    .update(SHEET_ID, categoryCode.getSheetName() + "!" + range, body)
                    .setValueInputOption("RAW")
                    .execute();
        } catch (IOException e){
            throw new GoogleSheetException("[checkDeleteConfirm] : " + e.getMessage());
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