package com.delgo.reward.comm.oauth;


import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.dto.OAuthDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@NoArgsConstructor
public class KakaoService {
    @Value("${config.kakao.REDIRECT_URL}")
    String REDIRECT_URL;

    public String getKakaoAccessToken (String code) {
        String access_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("grant_type=authorization_code");
            sb.append("&client_id=b40f84b68ce44634317bb5530b0166c1");
            sb.append("&redirect_uri=" + REDIRECT_URL); // test
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("get token responseCode : " + responseCode);
            System.out.println("get token responseMessage : " + conn.getResponseMessage());
            if(responseCode != 200)
                return null;

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public OAuthDTO createKakaoUser(String token) throws Exception {

        OAuthDTO oAuthDTO = new OAuthDTO();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("create kakao user responseCode : " + responseCode);

            if (responseCode != 200)
                throw new Exception(ApiCode.UNKNOWN_ERROR.getMsg());

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            System.out.println("************************************************");
            System.out.println("response body : " + result);
            System.out.println("************************************************");

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            boolean hasPhoneNo = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_phone_number").getAsBoolean();
            if(!hasPhoneNo) return oAuthDTO;

            String id = element.getAsJsonObject().get("id").getAsString();
            String email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            String phoneNo = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("phone_number").getAsString();

            br.close();

            oAuthDTO.setSocialId(id);
            oAuthDTO.setEmail(email);
            oAuthDTO.setPhoneNo(phoneNo);

            return oAuthDTO;
        } catch (IOException e) {
            e.printStackTrace();
            return oAuthDTO;
        }
    }

    public void logout(String kakaoId) throws Exception {

        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        String APP_ADMIN_KEY ="7c55121fe68a5339bab4a1611a1e67d5";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "KakaoAK " + APP_ADMIN_KEY); //전송할 header 작성, access_token전송

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("target_id_type=user_id");
            sb.append("&target_id=" + kakaoId);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("logout kakao user responseCode : " + responseCode);

            if (responseCode != 200)
                throw new Exception(ApiCode.UNKNOWN_ERROR.getMsg());

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            System.out.println("************************************************");
            System.out.println("response body : " + result);
            System.out.println("************************************************");

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
