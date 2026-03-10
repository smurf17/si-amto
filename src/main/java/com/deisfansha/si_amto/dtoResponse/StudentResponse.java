package com.deisfansha.si_amto.dtoResponse;

import com.deisfansha.si_amto.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String studentNumber;
    private String name;
    private String birthplace;
    private LocalDate birthDate;
    private String majorName;
    private String curriculumName;

    public StudentResponse(Student student) {
        this.id = student.getId();
        this.studentNumber = student.getStudentNumber();
        this.name = student.getName();
        this.birthplace = student.getBirthplace();
        this.birthDate = student.getBirthDate();
        this.majorName = student.getMajor().getName();
        this.curriculumName = student.getCurriculum().getName();
    }
}
