package com.deisfansha.si_amto.dtoResponse;

import com.deisfansha.si_amto.model.Curriculum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumResponse {
    private String name;
    private int year;
    private String majorName;
    private String edu;
    private String majorCode;

    public CurriculumResponse(Curriculum curriculum) {
        this.name = curriculum.getName();
        this.year = curriculum.getYear();
        this.majorName = curriculum.getMajor().getName();
        this.edu = curriculum.getMajor().getEduLevel();
        this.majorCode = String.valueOf(curriculum.getMajor().getMajorCode());
    }
}
