package com.delgo.reward.code.controller;

import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.comm.code.CodeType;
import com.delgo.reward.code.response.CodeResponse;
import com.delgo.reward.code.service.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
public class CodeController extends CommController {
    private final CodeService codeService;

    @Operation(summary = "지역 코드 조회")
    @ApiResponse(content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CodeResponse.class))})
    @GetMapping("/geo")
    public ResponseEntity getGeoCode() {
        List<CodeResponse> response = CodeResponse.fromList(codeService.getListByType(CodeType.geo));
        return SuccessReturn(response);
    }

    @Operation(summary = "품종 코드 조회")
    @ApiResponse(content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CodeResponse.class))})
    @GetMapping("/breed")
    public ResponseEntity getDogCode() {
        List<CodeResponse> response = CodeResponse.fromList(codeService.getListByType(CodeType.breed));
        return SuccessReturn(response);
    }
}