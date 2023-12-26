package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.service.*;
import com.delgo.reward.service.user.UserCommandService;
import com.delgo.reward.service.user.UserQueryService;
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

    private final UserCommandService userCommandService;
    private final PhotoService photoService;

    /***
     * profile 수정 [AccountPage]
     * - userId : NCP에 올릴 Photo File명에 사용됨. & DB에 photoUrl 등록할 때 User 식별
     * - photo : 확장자는 .jpg를 기본으로 한다. [.jpeg도 가능] [ 따로 확장자를 체크하진 않는다.]
     ***/
    @PostMapping(value = {"/upload/profile/{userId}", "/upload/profile"})
    public ResponseEntity<?> uploadProfile(@PathVariable Integer userId, @RequestPart(required = false) MultipartFile photo) {
        if (photo.isEmpty()) ParamErrorReturn("photo");

        String fileName = photoService.makeProfileFileName(userId, photo);
        String url = photoService.saveAndUpload(fileName, photo, BucketName.PROFILE);
        return SuccessReturn(userCommandService.changePhoto(userId, url));
    }
}
