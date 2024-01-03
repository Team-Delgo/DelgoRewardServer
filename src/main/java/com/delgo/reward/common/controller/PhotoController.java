package com.delgo.reward.common.controller;

import com.delgo.reward.common.service.PhotoService;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.response.UserResponse;
import com.delgo.reward.user.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photo")
public class PhotoController extends CommController {
    private final PhotoService photoService;
    private final UserCommandService userCommandService;

    @PostMapping(value = {"/upload/profile/{userId}", "/upload/profile"})
    public ResponseEntity<?> uploadProfile(@PathVariable Integer userId, @RequestPart(required = false) MultipartFile photo) {
        if (photo.isEmpty()) ParamErrorReturn("photo");

        String url = photoService.createProfile(userId, photo);
        User user = userCommandService.updateProfile(userId, url);
        return SuccessReturn(UserResponse.from(user));
    }
}
