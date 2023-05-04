package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController extends CommController {

    private final UserService userService;

    // User 전체 조회 [ Admin 사용 ]
    @GetMapping("/all")
    public ResponseEntity<?> getUserAll(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return SuccessReturn(userService.getUserAll(pageable));
    }

}
