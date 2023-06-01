package com.delgo.reward.service;

import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.mongoDomain.MungpleDetail;
import com.delgo.reward.mongoRepository.MungpleDetailRepository;
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

    String COPY_URL = "https://www.test.delgo.pet/detail/";

    public void parseExcelFile(String filePath) throws IOException {
        FileInputStream file = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 선택

        for(int i = 5 ; i < 29 ; i++) {
            Cell cell = sheet.getRow(i).getCell(0);
            log.info("{}", cell.getNumericCellValue());
            CellStyle cellStyle = cell.getCellStyle();
            XSSFColor backgroundColor = (XSSFColor) cellStyle.getFillForegroundColorColor();

            String color = null;
            if (backgroundColor != null) {
                byte[] rgb = backgroundColor.getRGB();
                int red = (rgb[0] >= 0) ? rgb[0] : (256 + rgb[0]);
                int green = (rgb[1] >= 0) ? rgb[1] : (256 + rgb[1]);
                int blue = (rgb[2] >= 0) ? rgb[2] : (256 + rgb[2]);

                color = String.format("#%02X%02X%02X", red, green, blue);
            }

            if (color == null ||color.equals("#FF00FF")) {
                log.info("--------------------------------------------------------------------------------------");
                continue;
            }
            String mungpleName = sheet.getRow(i).getCell(3).getStringCellValue();
            log.info("mungpleName :{}", mungpleName);

            Mungple mungple = mungpleRepository.findByPlaceName(mungpleName);
            if(mungple == null){
                log.info("--------------------------------------------------------------------------------------");
                continue;
            }

            String phoneNo = Optional.ofNullable(sheet.getRow(i).getCell(9))
                    .map(Cell::getStringCellValue)
                    .filter(str -> !str.isEmpty())
                    .orElse(null);

            if(phoneNo != null) {
                mungple.setPhoneNo(phoneNo);
                log.info("phoneNo: {}", phoneNo);
            }

            Boolean isExist = mungpleDetailRepository.existsByMungpleId(mungple.getMungpleId());
            if(isExist){
                log.info("디테일 데이터가 이미 존재합니다.");
                log.info("--------------------------------------------------------------------------------------");
                continue;
            }

            MungpleDetail mungpleDetail = new MungpleDetail();

            int mungpleId = mungple.getMungpleId();

            mungpleDetail.setMungpleId(mungpleId);

            String editorNoteUrl = mungple.getDetailUrl();
//            log.info("editorNoteUrl :{}", editorNoteUrl);
            mungpleDetail.setEditorNoteUrl(editorNoteUrl);

            String copyLink = COPY_URL + mungpleId;
//            log.info("copyLink :{}", copyLink);
            mungpleDetail.setCopyLink(copyLink);

            String enterDesc = Optional.ofNullable(sheet.getRow(i).getCell(18))
                    .map(Cell::getStringCellValue)
                    .filter(str -> !str.isEmpty())
                    .map(str -> str.replace("\"",""))
                    .orElse(null);

            if(enterDesc != null) {
                mungpleDetail.setEnterDesc(enterDesc);
            }

            String residentDogName = Optional.ofNullable(sheet.getRow(i).getCell(13))
                    .map(Cell::getStringCellValue)
                    .filter(str -> !str.isEmpty())
                    .map(s -> s.replaceAll("\"", ""))
                    .orElse(null);

            if (residentDogName != null) {
//                log.info("residentDogName: {}", residentDogName);
                mungpleDetail.setResidentDogName(residentDogName);
            }

            String residentDogPhoto = Optional.ofNullable(sheet.getRow(i).getCell(12))
                    .map(Cell::getStringCellValue)
                    .filter(str -> !str.isEmpty())
                    .orElse(null);

            if (residentDogPhoto != null) {
//                log.info("residentDogPhoto: {}", residentDogPhoto);
                mungpleDetail.setResidentDogPhoto(residentDogPhoto);
            }


            String representMenuTitle = Optional.ofNullable(sheet.getRow(i).getCell(15))
                    .map(Cell::getStringCellValue)
                    .filter(str -> !str.isEmpty())
                    .orElse(null);

            if (representMenuTitle != null) {
//                log.info("representMenuTitle: {}", representMenuTitle);
                mungpleDetail.setRepresentMenuTitle(representMenuTitle);
            }


            String representMenuPhotoText = Optional.ofNullable(sheet.getRow(i).getCell(16))
                    .map(Cell::getStringCellValue)
                    .orElse(null);

            if (representMenuPhotoText != null && !representMenuPhotoText.isEmpty()) {
                List<String> representMenuPhotos = Arrays.stream(representMenuPhotoText.split("\n"))
                        .filter(s -> !s.isEmpty())
                        .toList();

//                representMenuPhotos.forEach(menu_photo -> log.info("menu_photo: {}", menu_photo));
                mungpleDetail.setRepresentMenuPhotoUrls(representMenuPhotos);
            }


            String instaId = Optional.ofNullable(sheet.getRow(i).getCell(14))
                    .map(Cell::getStringCellValue)
                    .filter(str -> !str.isEmpty())
                    .map(str -> str.replace("\"",""))
                    .orElse(null);

            if (instaId != null) {
//                log.info("instaId: {}", instaId);
                mungpleDetail.setInstaId(instaId);
            }


            String parkingLimit = Optional.ofNullable(sheet.getRow(i).getCell(20))
                    .map(Cell::getStringCellValue)
                    .map(s -> s.replaceAll("\"", ""))
                    .filter(str -> !str.isEmpty())
                    .orElse(null);

            if (parkingLimit != null) {
//                log.info("parkingLimit: {}", parkingLimit);
                mungpleDetail.setParkingLimit(parkingLimit);
            }


            String parkingInfo = Optional.ofNullable(sheet.getRow(i).getCell(19))
                    .map(Cell::getStringCellValue)
                    .map(s -> s.replaceAll("\"", ""))
                    .filter(str -> !str.isEmpty())
                    .orElse(null);

            if (parkingInfo != null) {
//                log.info("parkingInfo: {}", parkingInfo);
                mungpleDetail.setParkingInfo(parkingInfo);
            }

            String businessHour = Optional.ofNullable(sheet.getRow(i).getCell(11))
                    .map(Cell::getStringCellValue)
                    .map(s -> s.replaceAll("\"", ""))
                    .filter(str -> !str.isEmpty())
                    .orElse(null);

            if (businessHour != null) {
                Map<BusinessHourCode, String> map = parseBusinessHourString(businessHour);

//                map.forEach((key, value) -> log.info("{} : {}", key, value));
                mungpleDetail.setBusinessHour(map);
            }

            String acceptSize = Optional.ofNullable(sheet.getRow(i).getCell(17))
                    .map(Cell::getStringCellValue)
                    .map(s -> s.replaceAll("\"", ""))
                    .filter(str -> !str.isEmpty())
                    .orElse(null);

            if (acceptSize != null) {
                Map<String, DetailCode> map = parseAcceptSize(acceptSize);
//                map.forEach((key, value) -> log.info("{} : {}", key, value.getCode()));
                mungpleDetail.setAcceptSize(map);
            }


            mungpleDetailRepository.save(mungpleDetail);
            log.info("mungple detail : {}", mungpleDetail);
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

    public static Map<String, DetailCode> parseAcceptSize(String input) {
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
}