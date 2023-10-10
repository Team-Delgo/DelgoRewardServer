package com.delgo.reward.comm.quartz.job;

import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class BirthdayPush extends QuartzJobBean {
    private final PetRepository petRepository;
    private final FcmService fcmService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info(LocalTime.now() + ": BirthdayPush Execute");

        LocalDate localDate = LocalDate.now();
        String notifyMsg = "의 생일을 축하합니다. 오늘도 좋은 하루 되세요!";

        List<Pet> petList = petRepository.findAll();
        Map<Integer, String> birthdayPetUserIdAndPetNameMap = new HashMap<>();

        for(Pet pet: petList){
            if(pet.getBirthday().getMonth().equals(localDate.getMonth()) && pet.getBirthday().getDayOfMonth() == localDate.getDayOfMonth()){
                birthdayPetUserIdAndPetNameMap.put(pet.getUserId(), pet.getName());
                log.info("[BirthdayPush] User Id: " + pet.getUserId() + " Pet Name: " + pet.getName());
            }
        }

        fcmService.birthdayPush(birthdayPetUserIdAndPetNameMap, notifyMsg);

        log.info(LocalTime.now() + ": BirthdayPush Exit");
    }
}
