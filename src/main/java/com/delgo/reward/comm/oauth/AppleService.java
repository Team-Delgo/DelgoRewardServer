package com.delgo.reward.comm.oauth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class AppleService {

    // 애플로그인
//    public static final String TEAM_ID = "GGS2HY6B73";
//    public static final String REDIRECT_URL = "https://delgo.pet/oauth/redirect/apple";
//    public static final String CLIENT_ID = "pet.delgo";
//    public static final String KEY_ID = "P9AZCBNYJG";
//    public static final String AUTH_URL = "https://appleid.apple.com";
//    public static final String KEY_PATH = "static/apple/AuthKey_P9AZCBNYJG.p8";

    public JSONObject decodeFromIdToken(String id_token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(id_token);
            ObjectMapper objectMapper = new ObjectMapper();

            ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
            JSONObject payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), JSONObject.class);

            if (payload != null) return payload;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
