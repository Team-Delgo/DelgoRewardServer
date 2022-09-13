package com.delgo.reward.comm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Random;

@Slf4j
@Service
public class CommService {

    // Date Validate
    // - 날짜 차이가 2주 이내인가?
    // - 시작날짜가 오늘보다 같거나 큰가?
    // - 종료날짜는 만료날짜랑 같거나 작은가?
    // - 시작날짜가 종료날짜보다 작은가?
    public boolean checkDate(String startDt, String endDt) {
        LocalDate now = LocalDate.now(); // 오늘 날짜
        LocalDate expireDate = now.plusMonths(2); // 만료 날짜
        LocalDate startDate = LocalDate.parse(startDt); // 시작 날짜
        LocalDate endDate = LocalDate.parse(endDt); // 종료 날짜
        LocalDate maxDate = startDate.plusWeeks(2); // 시작 날짜 기준 최대 예약 날짜 ( 14일 )
//        log.info("now :{}, expire: {}, start:{}, end: {}, max:{}", now, expireDate, startDate, endDate, maxDate);

        return startDate.isBefore(endDate) && !endDate.isAfter(maxDate) && !now.isAfter(startDate) && !endDate.isAfter(expireDate);
    }

    public String formatIntToPrice(int price) {
        DecimalFormat df = new DecimalFormat("###,###원"); //포맷팅
        return df.format(price);
    }

    public int formatPriceToInt(String price) {
        price = price.replace(",", "");
        price = price.replace("원", "");

        return Integer.parseInt(price);
    }

    /**
     * 전달된 파라미터에 맞게 난수를 생성한다
     *
     * @param len   : 생성할 난수의 길이
     * @param dupCd : 중복 허용 여부 (1: 중복허용, 2:중복제거)
     *              <p>
     *              Created by 닢향
     *              http://niphyang.tistory.com
     */
    public static String numberGen(int len, int dupCd) {
        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수
        for (int i = 0; i < len; i++) {
            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));
            if (dupCd == 1) {
                //중복 허용시 numStr에 append
                numStr += ran;
            } else if (dupCd == 2) {
                //중복을 허용하지 않을시 중복된 값이 있는지 검사한다
                if (!numStr.contains(ran)) {
                    //중복된 값이 없으면 numStr에 append
                    numStr += ran;
                } else {
                    //생성된 난수가 중복되면 루틴을 다시 실행한다
                    i -= 1;
                }
            }
        }
        return numStr;
    }
}
