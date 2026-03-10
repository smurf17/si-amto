package com.deisfansha.si_amto.controller;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.dtoResponse.MajorResponse;
import com.deisfansha.si_amto.model.Major;
import com.deisfansha.si_amto.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/majors")
public class MajorController {
    @Autowired
    private  MajorService majorService;

    @GetMapping("/{id}")
    public ResponseEntity getMajorById(@PathVariable Long id){
        ApiResponse response = majorService.getMajorById(id);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/actived/{active}")
    public ResponseEntity<ApiResponse<List<Major>>> getAllMajorByTrue(@PathVariable Boolean active){
        List<Major> major = majorService.getAllMajorByTrue(active);
        if (major.isEmpty()){
            return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, true));
        }
        return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_FOUND, major, true));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<MajorResponse>>> majorFilters(@RequestParam(required = false) String eduLevel, @RequestParam(required = false) String name){
        List<MajorResponse> student = majorService.filterMajors(eduLevel,name).stream()
                .map(MajorResponse::new)
                .toList();

        if (student.isEmpty()){
            return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, true));
        }
        return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_FOUND, student, true));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createMajor(@RequestBody Major major){
        ApiResponse response = majorService.savemajor(major);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMajor(@PathVariable Long id){
        ApiResponse response = majorService.deletemajorById(id);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateMajor(@PathVariable Long id, @RequestBody Major updateMajor){
        ApiResponse response = majorService.updatedMajorById(id, updateMajor);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<ApiResponse> softDeleteMajor(@PathVariable Long id){
        ApiResponse response = majorService.softDeleteMajorById(id);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
