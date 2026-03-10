package com.deisfansha.si_amto.controller;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.dtoRequest.CurriculumRequest;
import com.deisfansha.si_amto.dtoResponse.CurriculumResponse;
import com.deisfansha.si_amto.model.Curriculum;
import com.deisfansha.si_amto.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculums")
public class CurriculumController {
    @Autowired
    private CurriculumService curriculumService;

    @GetMapping
    public List<Curriculum> getAllCurriculums(){
        return curriculumService.getAllCurriculum();
    }

    @GetMapping("/{id}")
    public ResponseEntity getCurriculumById(@PathVariable Long id){
        ApiResponse response = curriculumService.getCurriculumById(id);
        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/actived/{active}")
    public ResponseEntity<ApiResponse<List<CurriculumResponse>>> getAllCurriculumByTrue(@PathVariable Boolean active){
        List<CurriculumResponse> curriculum = curriculumService.getAllCurriculumByTrue(active).stream()
                .map(CurriculumResponse::new)
                .toList();
        if (curriculumService.getAllCurriculumByTrue(active).isEmpty()){
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, false));}

        return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_FOUND, curriculum,true));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCurriculum(@RequestBody CurriculumRequest request){
        ApiResponse response = curriculumService.saveCurriculum(request);
        if (!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCurriculum(@PathVariable Long id){
        ApiResponse response = curriculumService.deleteCurriculumById(id);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCurriculum(@PathVariable Long id, @RequestBody CurriculumRequest request){
        ApiResponse response = curriculumService.updatedCurriculumById(id, request);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<ApiResponse> softDeleteCurriculum(@PathVariable Long id){
        ApiResponse response = curriculumService.softDeleteCurriculumById(id);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
