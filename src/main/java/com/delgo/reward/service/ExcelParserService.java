package com.delgo.reward.service;

import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.mongoDomain.MungpleDetail;
import com.delgo.reward.mongoRepository.MungpleDetailRepository;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.repository.MungpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ExcelParserService {

    private final MungpleRepository mungpleRepository;
    private final MungpleDetailRepository mungpleDetailRepository;

    private final ObjectStorageService objectStorageService;
    private final MongoMungpleService mongoMungpleService;

    String COPY_URL = "https://www.test.delgo.pet/detail/";

    public void parseExcelFileOfCafe(String filePath) throws IOException {
        FileInputStream file = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 선택

        for(int i = 26 ; i < 50 ; i++) {
            Row row = sheet.getRow(i);

            // 흰색일 때만 파싱을 동작시킨다.
            if (!checkColor(row)) {
                log.info("--------------------------------------------------------------------------------------");
                continue;
            }

            String mungpleName = getStringExcelData(row.getCell(3));

            log.info("mungpleName :{}", mungpleName);


            Mungple mungple = mungpleRepository.findByPlaceName(mungpleName);
            if(mungple == null){
                log.info("--------------------------------------------------------------------------------------");
                continue;
            }

//            Boolean isExist = mungpleDetailRepository.existsByMungpleId(mungple.getMungpleId());
//            if (isExist) {
//                log.info("디테일 데이터가 이미 존재합니다.");
//                log.info("--------------------------------------------------------------------------------------");
//                continue;
//            }

            String phoneNo = getStringExcelData(row.getCell(9));
            mungple.setPhoneNo(phoneNo);

            MungpleDetail mungpleDetail = new MungpleDetail();

            String enterDesc = getStringExcelData(row.getCell(18));
            String residentDogName =  getStringExcelData(row.getCell(13));
            String residentDogPhoto =  getStringExcelData(row.getCell(12));
            String representMenuTitle =  getStringExcelData(row.getCell(15));
            String instaId =  getStringExcelData(row.getCell(14));
            String parkingLimit =  getStringExcelData(row.getCell(20));
            String parkingInfo =  getStringExcelData(row.getCell(19));


            String representMenuPhotoText =  getStringExcelData(row.getCell(16));
            if (!representMenuPhotoText.isEmpty()) {
                List<String> representMenuPhotos = Arrays.stream(representMenuPhotoText.split("\n"))
                        .filter(s -> !s.isEmpty())
                        .toList();
                mungpleDetail.setRepresentMenuPhotoUrls(representMenuPhotos);
//                representMenuPhotos.forEach(menu_photo -> log.info("menu_photo: {}", menu_photo));
            }

            String businessHour = getStringExcelData(row.getCell(11));

            if (!businessHour.equals("")) {
                Map<BusinessHourCode, String> map = parseBusinessHourString(businessHour);
                mungpleDetail.setBusinessHour(map);
//                map.forEach((key, value) -> log.info("{} : {}", key, value));
            }

            String acceptSize = getStringExcelData(row.getCell(17));

            if (!acceptSize.equals("")) {
                Map<String, DetailCode> map = parseAcceptSize(acceptSize);
                mungpleDetail.setAcceptSize(map);
//                map.forEach((key, value) -> log.info("{} : {}", key, value.getCode()));
            }

            // 메뉴판 사진
            String menuBoardText = getStringExcelData(row.getCell(25));

            if (!menuBoardText.isEmpty()) {
                List<String> menuBoards = Arrays.stream(menuBoardText.split("\n"))
                        .filter(s -> !s.isEmpty())
                        .toList();
                mongoMungpleService.addMenuBoardPhotoUrl(mungple.getMungpleId(), menuBoards);
            }

            log.info("phoneNo: {}", phoneNo);
            log.info("editorNoteUrl :{}", mungple.getDetailUrl());
            log.info("copyLink :{}", COPY_URL + mungple.getMungpleId());
            log.info("enterDesc :{}", enterDesc);
            log.info("residentDogName: {}", residentDogName);
            log.info("residentDogPhoto: {}", residentDogPhoto);
            log.info("representMenuTitle: {}", representMenuTitle);
            log.info("instaId: {}", instaId);
            log.info("parkingLimit: {}", parkingLimit);
            log.info("parkingInfo: {}", parkingInfo);

            mungpleDetail.setMungpleId(mungple.getMungpleId());
            mungpleDetail.setEditorNoteUrl(mungple.getDetailUrl());
            mungpleDetail.setCopyLink(COPY_URL + mungple.getMungpleId());
            mungpleDetail.setEnterDesc(enterDesc);
            mungpleDetail.setResidentDogName(residentDogName);
            mungpleDetail.setResidentDogPhoto(residentDogPhoto);
            mungpleDetail.setRepresentMenuTitle(representMenuTitle);
            mungpleDetail.setInstaId(instaId);
            mungpleDetail.setParkingLimit(parkingLimit);
            mungpleDetail.setParkingInfo(parkingInfo);

            // 저장
            mungpleDetailRepository.save(mungpleDetail);

            log.info("--------------------------------------------------------------------------------------");
        }

        workbook.close();
        file.close();
    }

    public Map<BusinessHourCode, String> parseBusinessHourString(String businessHour) {
        Map<BusinessHourCode, String> map = new HashMap<>();

        String[] scheduleEntries = businessHour.replace("\n","").split(",");
        for (String entry : scheduleEntries) {
            String[] keyValue = entry.split(": ");
            BusinessHourCode key = BusinessHourCode.valueOf(keyValue[0].replace("\"", ""));
            String value = keyValue[1].replace("\"", "");
            map.put(key, value);
        }

        for (BusinessHourCode code : BusinessHourCode.values()) {
            if (!map.containsKey(code))
                map.put(code, code.getDefaultValue());
        }

        return map;
    }

    public Map<String, DetailCode> parseAcceptSize(String input) {
        Map<String, DetailCode> acceptSize = new HashMap<>();

        input = input.replaceAll("[\n\"]", ""); // 엔터와 쌍따옴표 제거

        String[] pairs = input.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(": ");
            String key = keyValue[0];
            String value = keyValue[1];

            DetailCode detailCode = DetailCode.from(value);
            acceptSize.put(key, detailCode);
        }

        return acceptSize;
    }

    public boolean checkColor(Row row) {
        CellStyle cellStyle = row.getCell(0).getCellStyle();
        XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();

        if (bgColor == null)
            return false;

        // RGB 컬러 값을 16진수 (HEX) 문자열로 변환
        byte[] rgb = bgColor.getRGB();
        String color = String.format("#%02X%02X%02X", rgb[0] & 0xFF, rgb[1] & 0xFF, rgb[2] & 0xFF);

        // 흰색인 경우에만 정상동작 시킨다.
        return !color.equals("#FF00FF");
    }

    public String getStringExcelData(Cell cell){
        return Optional.ofNullable(cell)
                .map(Cell::getStringCellValue)
                .filter(str -> !str.isEmpty())
                .map(str -> str.replace("\"",""))
                .orElse("");
    }
}