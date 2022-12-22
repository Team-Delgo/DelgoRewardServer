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

    // 애플 로그인
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
