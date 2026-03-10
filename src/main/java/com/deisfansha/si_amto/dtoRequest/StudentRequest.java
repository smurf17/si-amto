package com.deisfansha.si_amto.dtoRequest;

import com.deisfansha.si_amto.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class StudentRequest {
    private Long id;
    private String studentNumber;
    private String name;
    private String birthplace;
    private LocalDate birthDate;
    private String classStudent;
    private Boolean active;
    private Long majorId;

    public StudentRequest(Student student) {
        this.id = student.getId();
        this.studentNumber = student.getStudentNumber();
        this.name = student.getName();
        this.birthplace = student.getBirthplace();
        this.birthDate = student.getBirthDate();
        this.majorId = student.getMajor().getId();
        this.classStudent = student.getClassStudent();
        this.active = student.getActive();
    }
}
