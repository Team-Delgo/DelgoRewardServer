//package com.delgo.reward;
//
//import com.delgo.reward.token.service.FcmService;
//import com.delgo.reward.user.domain.Pet;
//import com.delgo.reward.user.repository.PetRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BirthdayPushTest {
//    @Autowired
//    private PetRepository petRepository;
//    @Autowired
//    private FcmService fcmService;
//
//    @Test
//    public void test() {
//        LocalDate localDate = LocalDate.now();
//
//        System.out.println("Today: " + localDate);
//
//        String notifyMsg = "의 생일을 축하합니다. 오늘도 좋은 하루 되세요!";
//
//        List<Pet> petList = petRepository.findAll();
//        Map<Integer, String> birthdayPetUserIdAndPetNameMap = new HashMap<>();
//a
//        for(Pet pet: petList){
//            if(pet.getBirthday().getMonth().equals(localDate.getMonth()) && pet.getBirthday().getDayOfMonth() == localDate.getDayOfMonth()){
//                birthdayPetUserIdAndPetNameMap.put(pet.getUserId(), pet.getName());
//                System.out.println("UserId: " + pet.getUserId() + " Pet Name: " + pet.getName());
//            }
//        }
//
////        fcmService.birthdayPush(birthdayPetUserIdAndPetNameMap, notifyMsg);
//    }
//}
