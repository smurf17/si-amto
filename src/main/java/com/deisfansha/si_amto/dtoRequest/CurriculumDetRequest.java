package com.deisfansha.si_amto.dtoRequest;

import com.deisfansha.si_amto.model.CurriculumDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumDetRequest {
    private Long subjectId;
    private int semester;
    private Double weight;
    private Boolean is_mandatory;

    public CurriculumDetRequest(CurriculumDetail curriculumDetail) {
        this.subjectId = curriculumDetail.getCurriculum().getId();
        this.semester = curriculumDetail.getSemester();
        this.weight = curriculumDetail.getWeight();
        this.is_mandatory = curriculumDetail.getIs_mandatory();
    }
}
