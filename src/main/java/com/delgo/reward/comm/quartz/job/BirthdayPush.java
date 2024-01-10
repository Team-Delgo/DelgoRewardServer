package com.delgo.reward.comm.quartz.job;

import com.delgo.reward.token.service.FcmService;
import com.delgo.reward.user.domain.Pet;
import com.delgo.reward.user.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BirthdayPush extends QuartzJobBean {
    private final PetRepository petRepository;
    private final FcmService fcmService;

    @Override
    protected void executeInternal(@NotNull JobExecutionContext jobExecutionContext) {
        log.info(LocalTime.now() + ": BirthdayPush Execute");
        LocalDate localDate = LocalDate.now();

        List<Pet> petList = petRepository.findAll();
        for(Pet pet: petList){
            if(pet.getBirthday().getMonth().equals(localDate.getMonth()) && pet.getBirthday().getDayOfMonth() == localDate.getDayOfMonth()){
//              // TODO 서은님 축하사진 작업되어야 함.
//                fcmService.pushByBirthday(pet.getUserId());
                log.info("[BirthdayPush] User Id: " + pet.getUserId() + " Pet Name: " + pet.getName());
            }
        }

        log.info(LocalTime.now() + ": BirthdayPush Exit");
    }
}
