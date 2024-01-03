package com.delgo.reward.mock;

import com.delgo.reward.comm.encoder.CustomPasswordEncoder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestPasswordEncoder implements CustomPasswordEncoder {

    @Override
    public String encode(String password) {
        return password;
    }
}
