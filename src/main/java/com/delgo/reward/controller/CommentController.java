package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.Comment;
import com.delgo.reward.dto.CommentDTO;
import com.delgo.reward.dto.ReplyDTO;
import com.delgo.reward.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController extends CommController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity createComment(@Validated @RequestBody CommentDTO commentDTO){
        Comment comment = commentService.createComment(commentDTO);
        return SuccessReturn(comment);
    }

    @PostMapping("/reply")
    public ResponseEntity createReply(@Validated @RequestBody ReplyDTO replyDTO){
        Comment comment = commentService.createReply(replyDTO);
        return SuccessReturn(comment);
    }
}
