package com.deisfansha.si_amto.controller;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.dtoRequest.ScoreBatchRequest;
import com.deisfansha.si_amto.service.TranscriptDetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transcript-detail")
public class TranscriptDetController {
    @Autowired
    private TranscriptDetService transcriptDetService;

    @PostMapping("/{id}")
    public ResponseEntity inputBatchScore (@PathVariable Long id, @RequestBody ScoreBatchRequest request){
        ApiResponse response = transcriptDetService.inputScoreBatch(id, request);

        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);

    }
}
