package com.deisfansha.si_amto.dtoRequest;

import com.deisfansha.si_amto.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SMRequest {
    private Long id;
    private String studentNumber;
    private String name;
    private String birthplace;
    private LocalDate birthDate;
    private String classStudent;
    private Boolean active;
    private String majorName;

    public SMRequest(Student student) {
        this.id = student.getId();
        this.studentNumber = student.getStudentNumber();
        this.name = student.getName();
        this.birthplace = student.getBirthplace();
        this.birthDate = student.getBirthDate();
        this.majorName = student.getMajor().getName();
        this.classStudent = student.getClassStudent();
        this.active = student.getActive();
    }
}
