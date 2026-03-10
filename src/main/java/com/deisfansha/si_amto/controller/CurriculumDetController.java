package com.deisfansha.si_amto.controller;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.dtoRequest.BatchCurriDet;
import com.deisfansha.si_amto.dtoRequest.CurriculumDetRequest;
import com.deisfansha.si_amto.dtoRequest.UpdateCurriDetRequest;
import com.deisfansha.si_amto.service.CurriculumDetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/curriculum-detail/")
public class CurriculumDetController {
    @Autowired
    private CurriculumDetService curriculumDetService;

    @PostMapping("/{curriculumId}")
    public ResponseEntity addSubjectToCurriculum(@PathVariable Long curriculumId, @RequestBody CurriculumDetRequest request) {
        ApiResponse response = curriculumDetService.addSubjectToCurriculum(curriculumId, request);
        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch/{curriculumId}")
    public ResponseEntity<ApiResponse<?>> addSubjectsChecklist(
            @PathVariable Long curriculumId,
            @RequestBody BatchCurriDet request) {

        ApiResponse response = curriculumDetService.addSubjectsChecklist(curriculumId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{curriculumId}")
    public ResponseEntity getSubjectCurriculum(@PathVariable Long curriculumId){
        ApiResponse response = curriculumDetService.getCurriculumDet(curriculumId);

        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCurriculumDetail(@PathVariable Long id) {

        ApiResponse response = curriculumDetService.deleteCurriculumDetail(id);

        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/batch/{curriculumId}")
    public ResponseEntity softDeleteBatch(
            @PathVariable Long curriculumId,
            @RequestBody BatchCurriDet request) {

        ApiResponse response =
                curriculumDetService.softDeleteBatch(curriculumId, request);

        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/batch/{curriculumId}")
    public ResponseEntity<?> updateBatchCurriDet(@PathVariable Long curriculumId, @RequestBody List<UpdateCurriDetRequest> request){
        ApiResponse response = curriculumDetService.updateBatch(curriculumId, request);
        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

}
