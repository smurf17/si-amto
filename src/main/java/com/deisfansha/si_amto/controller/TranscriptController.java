package com.deisfansha.si_amto.controller;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.dtoRequest.TranscriptRequest;
import com.deisfansha.si_amto.service.TranscriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("api/transcript")
public class TranscriptController {
    @Autowired
    TranscriptService transcriptService;

    @PostMapping
    public ResponseEntity createHeader(@RequestBody TranscriptRequest request){
        ApiResponse response = transcriptService.addHeaderTranscript(request);

        if (!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/print")
    public ResponseEntity<byte[]> downloadTranscriptExcel(
            @PathVariable Long id) throws Exception {

        ByteArrayInputStream bis = transcriptService.generateTranscript(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=transcript.xlsx");
        headers.add("Content-Type",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bis.readAllBytes());
    }

}
