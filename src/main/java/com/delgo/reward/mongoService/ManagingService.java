package com.delgo.reward.mongoService;

import com.delgo.reward.mongoDomain.Managing;
import com.delgo.reward.mongoRepository.ManagingRepository;
import com.google.api.client.util.ArrayMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ManagingService {
    private final ManagingRepository managingRepository;

    public Managing createLog(String controllerName, String methodName, ArrayMap<String, Object> args) {
        return managingRepository.save(new Managing().toEntity(controllerName, methodName, args));
    }
}
