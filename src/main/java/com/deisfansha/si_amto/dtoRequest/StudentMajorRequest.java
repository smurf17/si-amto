package com.deisfansha.si_amto.dtoRequest;

import com.deisfansha.si_amto.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentMajorRequest {
    private Long id;
    private Long codeM;

    public StudentMajorRequest(Student student) {
        this.id = student.getId();
        this.codeM = student.getMajor().getId();
    }
}
