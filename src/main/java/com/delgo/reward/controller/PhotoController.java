package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.service.*;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Hidden
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photo")
public class PhotoController extends CommController {

    private final UserService userService;
    private final PhotoService photoService;

    /*
     * profile 등록 및 수정 [회원가입 or AccountPage]
     * Request Data : userId, photo
     * - userId : NCP에 올릴 Photo File명에 사용됨. & DB에 photoUrl 등록할 때 User 식별
     * - photo : 확장자는 .png를 기본으로 한다. [.jpeg도 가능] [ 따로 확장자를 체크하진 않는다.]
     * Response Data : ApiCode
     */
    @PostMapping(value = {"/upload/profile/{userId}", "/upload/profile"})
    public ResponseEntity<?> uploadProfile(@PathVariable Integer userId, @RequestPart(required = false) MultipartFile photo) {
        if (photo.isEmpty()) ParamErrorReturn("photo");

        String fileName = photoService.makeProfileFileName(userId, photo);
        String url = photoService.saveAndUpload(fileName, photo, BucketName.PROFILE);
        return SuccessReturn(userService.changePhoto(userId, url));
    }

    /*
     * TODO 고도화 필요
     * 멍플 사진 등록 및 수정
     * Request Data : mungpleId, photo
     * Response Data : 사진 저장된 URL
     */
//    @PostMapping(value={"/mungple/{mungpleId}","/upload/mungple"})
//    public ResponseEntity<?> uploadMungplePhoto(@PathVariable Integer mungpleId, @RequestPart MultipartFile photo) {
//        if (photo.isEmpty()) ParamErrorReturn("photo");
//        return SuccessReturn(mongoMungpleService.modifyPhoto(mungpleId, photo));
//    }
}
