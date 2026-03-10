package com.deisfansha.si_amto.dtoRequest;

import com.deisfansha.si_amto.model.Curriculum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurriculumRequest {
    private Long id;
    private String name;
    private int year;
    private Long majorId;

    public CurriculumRequest(Curriculum curriculum) {
        this.id = curriculum.getId();
        this.name = curriculum.getName();
        this.year = curriculum.getYear();
        this.majorId = curriculum.getMajor().getId();
    }

}
