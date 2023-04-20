package com.delgo.reward.mongoService;

import com.delgo.reward.mongoDomain.Log;
import com.delgo.reward.mongoRepository.LogRepository;
import com.google.api.client.util.ArrayMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;

    public Log createLog(String httpMethod, String controllerName, String methodName, ArrayMap<String, Object> args, Map<String, String> responseMap) {
        return logRepository.save(new Log().toEntity(httpMethod, controllerName, methodName, args, responseMap));
    }

    public Log createLog(String httpMethod, String controllerName, String methodName, ArrayMap<String, Object> args, String responseStr) {
        return logRepository.save(new Log().toEntity(httpMethod, controllerName, methodName, args, responseStr));
    }
}
