package com.delgo.reward.service;

import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.mungple.Mungple;
import com.delgo.reward.mongoDomain.MungpleDetail;
import com.delgo.reward.mongoRepository.MungpleDetailRepository;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.repository.MungpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
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

    private final PhotoService photoService;
    private final ObjectStorageService objectStorageService;
    private final MongoMungpleService mongoMungpleService;

    @Value("${config.photoDir}")
    String DIR;
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

            Mungple mungple = mungpleRepository.findMungpleByPlaceName(mungpleName);
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
            Boolean isParking =  row.getCell(20).getBooleanCellValue();
            String parkingInfo =  getStringExcelData(row.getCell(19));


            String businessHour = getStringExcelData(row.getCell(11));
            if (!businessHour.equals("")) {
                Map<BusinessHourCode, String> map = parseBusinessHour(businessHour);
                mungpleDetail.setBusinessHour(map);
//                map.forEach((key, value) -> log.info("{} : {}", key, value));
            }

            String acceptSize = getStringExcelData(row.getCell(17));
            if (!acceptSize.equals("")) {
                Map<String, DetailCode> map = parseAcceptSize(acceptSize);
                mungpleDetail.setAcceptSize(map);
//                map.forEach((key, value) -> log.info("{} : {}", key, value.getCode()));
            }

            // 썸네일 사진
            String ThumbnailsText = getStringExcelData(row.getCell(8));
            if (!ThumbnailsText.isEmpty()) {
                List<String> thumbnails = Arrays.stream(ThumbnailsText.split("\n"))
                        .filter(s -> !s.isEmpty())
                        .toList();

                thumbnails.forEach(thumbnail -> log.info("thumbnails: {}", thumbnail));
                List<String> ncpUrls = thumbnailNCPUpload(mungpleName, thumbnails);
                mungpleDetail.setPhotoUrls(ncpUrls);
            }

            // 메뉴판 사진
            String menuBoardText = getStringExcelData(row.getCell(25));
            if (!menuBoardText.isEmpty()) {
                List<String> menuBoards = Arrays.stream(menuBoardText.split("\n"))
                        .filter(s -> !s.isEmpty())
                        .toList();

                List<String> ncpUrls = menuBoardNCPUpload(mungpleName,menuBoards);
                mungpleDetail.setRepresentMenuPhotoUrls(ncpUrls);
//                menuBoards.forEach(menu_board -> log.info("menu_board: {}", menu_board));
            }

            // 메뉴 사진
            String representMenuPhotoText =  getStringExcelData(row.getCell(16));
            if (!representMenuPhotoText.isEmpty()) {
                List<String> representMenuPhotos = Arrays.stream(representMenuPhotoText.split("\n"))
                        .filter(s -> !s.isEmpty())
                        .toList();

                List<String> ncpUrls = menuNCPUpload(mungpleName, representMenuPhotos);
                // 메뉴판 사진 -> 메뉴 저장.
                mungpleDetail.getRepresentMenuPhotoUrls().addAll(ncpUrls);
                mungpleDetail.setRepresentMenuPhotoUrls(mungpleDetail.getRepresentMenuPhotoUrls());
//                representMenuPhotos.forEach(menu_photo -> log.info("menu_photo: {}", menu_photo));
            }

            log.info("phoneNo: {}", phoneNo);
            log.info("editorNoteUrl :{}", mungple.getDetailUrl());
            log.info("copyLink :{}", COPY_URL + mungple.getMungpleId());
            log.info("enterDesc :{}", enterDesc);
            log.info("residentDogName: {}", residentDogName);
            log.info("residentDogPhoto: {}", residentDogPhoto);
            log.info("representMenuTitle: {}", representMenuTitle);
            log.info("instaId: {}", instaId);
            log.info("isParking: {}", isParking);
            log.info("parkingInfo: {}", parkingInfo);

            mungpleDetail.setMungpleId(mungple.getMungpleId());
            mungpleDetail.setEditorNoteUrl(mungple.getDetailUrl());
            mungpleDetail.setCopyLink(COPY_URL + mungple.getMungpleId());
            mungpleDetail.setEnterDesc(enterDesc);
            mungpleDetail.setResidentDogName(residentDogName);
            mungpleDetail.setResidentDogPhoto(residentDogPhoto);
            mungpleDetail.setRepresentMenuTitle(representMenuTitle);
            mungpleDetail.setInstaId(instaId);
            mungpleDetail.setIsParking(isParking);
            mungpleDetail.setParkingInfo(parkingInfo);

            // 저장
            mungpleDetailRepository.save(mungpleDetail);

            log.info("--------------------------------------------------------------------------------------");
        }

        workbook.close();
        file.close();
    }

    public void parseExcelFileOfHospital(String filePath) throws IOException {
        FileInputStream file = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 선택

        for(int i = 123 ; i < 126 ; i++) {
            Row row = sheet.getRow(i);

            // 흰색일 때만 파싱을 동작시킨다.
            if (!checkColor(row)) {
                log.info("--------------------------------------------------------------------------------------");
                continue;
            }

            String mungpleName = getStringExcelData(row.getCell(3));
            log.info("mungpleName :{}", mungpleName);

            Mungple mungple = mungpleRepository.findMungpleByPlaceName(mungpleName);
            if(mungple == null){
                log.info("--------------------------------------------------------------------------------------");
                continue;
            }

            String phoneNo = getStringExcelData(row.getCell(9));
            mungple.setPhoneNo(phoneNo);

            MungpleDetail mungpleDetail = new MungpleDetail();

            String enterDesc = getStringExcelData(row.getCell(18));
            String instaId =  getStringExcelData(row.getCell(14));
            Boolean isParking =  row.getCell(20).getBooleanCellValue();
            String parkingInfo =  getStringExcelData(row.getCell(19));

            String businessHour = getStringExcelData(row.getCell(11));
            if (!businessHour.equals("")) {
                Map<BusinessHourCode, String> map = parseBusinessHour(businessHour);
                mungpleDetail.setBusinessHour(map);
                map.forEach((key, value) -> log.info("{} : {}", key, value));
            }

            String acceptSize = getStringExcelData(row.getCell(17));
            if (!acceptSize.equals("")) {
                Map<String, DetailCode> map = parseAcceptSize(acceptSize);
                mungpleDetail.setAcceptSize(map);
                map.forEach((key, value) -> log.info("{} : {}", key, value.getCode()));
            }

            // 썸네일 사진
            String ThumbnailsText = getStringExcelData(row.getCell(8));
            if (!ThumbnailsText.isEmpty()) {
                List<String> thumbnails = Arrays.stream(ThumbnailsText.split("\n"))
                        .filter(s -> !s.isEmpty())
                        .toList();

                thumbnails.forEach(thumbnail -> log.info("thumbnails: {}", thumbnail));
                List<String> ncpUrls = thumbnailNCPUpload(mungpleName, thumbnails);
                mungpleDetail.setPhotoUrls(ncpUrls);
            }

            // 가격표 사진
            String priceTagPhotosText = getStringExcelData(row.getCell(26));
            if (!priceTagPhotosText.isEmpty()) {
                List<String> priceTags = Arrays.stream(priceTagPhotosText.split("\n"))
                        .filter(s -> !s.isEmpty())
                        .toList();

                priceTags.forEach(menu_board -> log.info("priceTags: {}", menu_board));
                List<String> ncpUrls = priceTagNCPUpload(mungpleName, priceTags);
                mungpleDetail.setPriceTagPhotoUrls(ncpUrls);
            }
            Boolean isPriceTag = !priceTagPhotosText.isEmpty();

            log.info("phoneNo: {}", phoneNo);
            log.info("editorNoteUrl :{}", mungple.getDetailUrl());
            log.info("copyLink :{}", COPY_URL + mungple.getMungpleId());
            log.info("enterDesc :{}", enterDesc);
            log.info("instaId: {}", instaId);
            log.info("isParking: {}", isParking);
            log.info("parkingInfo: {}", parkingInfo);
            log.info("isPriceTag: {}", isPriceTag);

            mungpleDetail.setMungpleId(mungple.getMungpleId());
            mungpleDetail.setEditorNoteUrl(mungple.getDetailUrl());
            mungpleDetail.setCopyLink(COPY_URL + mungple.getMungpleId());
            mungpleDetail.setEnterDesc(enterDesc);
            mungpleDetail.setInstaId(instaId);
            mungpleDetail.setIsParking(isParking);
            mungpleDetail.setParkingInfo(parkingInfo);
            mungpleDetail.setIsPriceTag(!priceTagPhotosText.isEmpty());

            // 저장
            mungpleDetailRepository.save(mungpleDetail);

            log.info("--------------------------------------------------------------------------------------");
        }

        workbook.close();
        file.close();
    }

    public Map<BusinessHourCode, String> parseBusinessHour(String input) {
        Map<BusinessHourCode, String> businessHour = new HashMap<>();

        String[] arr = input.replaceAll("[\n\"]","").split(",");
        for (String s : arr) {
            String[] keyValue = s.split(": ");
            businessHour.put(BusinessHourCode.valueOf(keyValue[0]), keyValue[1]);
        }

        // Default 값 세팅
        for (BusinessHourCode code : BusinessHourCode.values()) {
            if (!businessHour.containsKey(code))
                businessHour.put(code, code.getDefaultValue());
        }

        return businessHour;
    }

    public Map<String, DetailCode> parseAcceptSize(String input) {
        Map<String, DetailCode> acceptSize = new HashMap<>();

        String[] str_arr = input.replaceAll("[\n\"]", "").split(",");
        for (String s : str_arr) {
            String[] keyValue = s.split(": ");
            acceptSize.put(keyValue[0], DetailCode.valueOf(keyValue[1]));
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

    public List<String> thumbnailNCPUpload(String mungpleName, List<String> urls) {
        List<String> ncpUrls = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            String fileName = photoService.convertWebpFromUrl(mungpleName + "_" + (i + 1), urls.get(i));
            objectStorageService.uploadObjects(BucketName.DETAIL_THUMBNAIL, fileName, DIR + fileName);
            ncpUrls.add(BucketName.DETAIL_THUMBNAIL.getUrl() + fileName);
        }

        return ncpUrls;
    }

    // menu랑 menuBoard는 동일한 Map에 저장하기 때문에 menuBoard먼저 저장 해야 함.
    public List<String> menuBoardNCPUpload(String mungpleName, List<String> urls) {
        List<String> ncpUrls = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            String fileName = photoService.convertWebpFromUrl(mungpleName + "_menu_board_" + (i + 1), urls.get(i));
            objectStorageService.uploadObjects(BucketName.DETAIL_MENU_BOARD, fileName, DIR + fileName);
            ncpUrls.add(BucketName.DETAIL_MENU_BOARD.getUrl() + fileName);
        }

        return ncpUrls;
    }

    public List<String> menuNCPUpload(String mungpleName, List<String> urls) {
        List<String> ncpUrls = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            String fileName = photoService.convertWebpFromUrl(mungpleName + "_menu_" + (i + 1), urls.get(i));
            objectStorageService.uploadObjects(BucketName.DETAIL_MENU, fileName, DIR + fileName);
            ncpUrls.add(BucketName.DETAIL_MENU.getUrl() + fileName);
        }

        return ncpUrls;
    }

    public List<String> priceTagNCPUpload(String mungpleName, List<String> urls) {
        List<String> ncpUrls = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            String fileName = photoService.convertWebpFromUrl(mungpleName + "_price_tag_" + (i + 1), urls.get(i));
            objectStorageService.uploadObjects(BucketName.DETAIL_PRICE_TAG, fileName, DIR + fileName);
            ncpUrls.add(BucketName.DETAIL_PRICE_TAG.getUrl() + fileName);
        }

        return ncpUrls;
    }


}