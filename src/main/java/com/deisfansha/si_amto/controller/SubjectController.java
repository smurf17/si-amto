package com.deisfansha.si_amto.controller;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.model.Subject;
import com.deisfansha.si_amto.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public List<Subject> getAllSubjects(){
        return subjectService.getAllSubject();
    }

    @GetMapping("/{id}")
    public ResponseEntity getSubjectById(@PathVariable Long id){
        ApiResponse response = subjectService.getSubjectById(id);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/major/{id}")
    public ResponseEntity getSubjectByIdMajor(@PathVariable Long id){
        ApiResponse response = subjectService.getSubjectByMajor(id);
        if (!response.isSuccess()){
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/actived/{status}")
    public ResponseEntity<ApiResponse> getAllSubjectByTrue(@PathVariable Boolean status){
        List<Subject> subject = subjectService.getAllSubjectByTrue(status);
        if (subject.isEmpty()){
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, false));}

        return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_FOUND, subject,true));
    }


    @PostMapping
    public ResponseEntity createSubject(@RequestBody Subject subject){
        ApiResponse response = subjectService.savedSubject(subject);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSubject(@PathVariable Long id){
        ApiResponse response = subjectService.deleteSubjectById(id);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateSubject(@PathVariable Long id, @RequestBody Subject updateSubject)
    {
        updateSubject.setActive(true);
        ApiResponse response = subjectService.updatedSubjectById(id, updateSubject);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/import")
    public ResponseEntity<ApiResponse> importSubject(@RequestParam("file") MultipartFile file){
        ApiResponse response = subjectService.importSubjects(file);
        return ResponseEntity.ok(response);
    }

}
