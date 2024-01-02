package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.user.UserResponse;
import com.delgo.reward.service.*;
import com.delgo.reward.service.user.UserCommandService;
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
