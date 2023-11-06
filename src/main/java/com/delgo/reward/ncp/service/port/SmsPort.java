package com.delgo.reward.ncp.service.port;



public interface SmsPort {
    void send(String phone, String msg);
}
