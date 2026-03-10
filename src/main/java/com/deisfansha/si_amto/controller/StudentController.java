package com.deisfansha.si_amto.controller;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.dtoRequest.AddStudentCurriculum;
import com.deisfansha.si_amto.dtoRequest.StudentMajorRequest;
import com.deisfansha.si_amto.dtoRequest.StudentRequest;
import com.deisfansha.si_amto.model.Student;
import com.deisfansha.si_amto.dtoResponse.StudentResponse;
import com.deisfansha.si_amto.service.CurriculumService;
import com.deisfansha.si_amto.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    private CurriculumService curriculumService;

    @GetMapping
    public List<Student> getAllStudents(){
        return studentService.getAllStudent();
    }

    @GetMapping("/{active}")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudentByTrue(@PathVariable Boolean active){
        List<StudentResponse> student = studentService.getAllStudentByTrue(active).stream()
                .map(StudentResponse::new)
                .toList();;

        if (student.isEmpty()){
            return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, true));
        }
        return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_FOUND, student, true));
    }

    @GetMapping("/reguler")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudentByBatchReguler(){
        List<StudentResponse> sr = studentService.getStudentByBatchReguler().stream()
                .map(StudentResponse::new)
                .toList();
        if (sr.isEmpty()){
            return ResponseEntity.badRequest().body(new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, false));
        }

        return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_FOUND, sr, true));

    }

    @PostMapping
    public ResponseEntity<ApiResponse> createStudent(@RequestBody StudentRequest request){
        ApiResponse response = studentService.createStudent(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Long id){
        ApiResponse response = studentService.deleteStudentById(id);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateStudent(@PathVariable Long id, @RequestBody Student updateStudent){
        ApiResponse response = studentService.updatedStudentById(id, updateStudent);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/major")
    public ResponseEntity<ApiResponse> updateStudentMajor(@PathVariable Long id, @RequestBody StudentMajorRequest smRequest){
        ApiResponse response = studentService.updatedStudentCodeMajor(id, smRequest);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<ApiResponse> softDeleteStudent(@PathVariable Long id){
        ApiResponse response = studentService.softDeleteStudentById(id);

        if (!response.isSuccess()){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/import")
    public ResponseEntity<ApiResponse> importStudents(@RequestParam("file") MultipartFile file){
        ApiResponse response = studentService.importStudent(file);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/curriculums/{curriculumId}/students/batch")
    public ResponseEntity<ApiResponse<?>> batchAssignStudents(@PathVariable Long curriculumId, @RequestBody AddStudentCurriculum req){
        studentService.batchAssignStudents(curriculumId,req);
        return ResponseEntity.ok(
                new ApiResponse<>("Students successfully assigned to curriculum", null, true)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> filterStudents(@RequestParam(required = false) Long majorId, @RequestParam(required = false) Long curriculumId){
        List<StudentResponse> student = studentService.filterStudents(majorId, curriculumId).stream()
                .map(StudentResponse::new)
                .toList();

        if (student.isEmpty()){
            return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, true));
        }
        return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_FOUND, student, true));
    }

}
