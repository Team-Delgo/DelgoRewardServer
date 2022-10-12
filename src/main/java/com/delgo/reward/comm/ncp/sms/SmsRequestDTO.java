package com.delgo.reward.comm.ncp.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequestDTO {
    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String content;
    private List<SmsMessageDTO> messages;

}
