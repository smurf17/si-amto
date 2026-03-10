package com.deisfansha.si_amto.dtoResponse;

import com.deisfansha.si_amto.model.CurriculumDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurriDetResponse {
    private String nameCurriculum;
    private String codeSubject;
    private String nameSubject;
    private int semester;
    private int credit;
    private Double weight;
    private String nameMajor;

    public CurriDetResponse(CurriculumDetail curriculumDetail) {
        this.nameCurriculum = curriculumDetail.getCurriculum().getName();
        this.codeSubject = curriculumDetail.getSubject().getSubjectNumber();
        this.nameSubject = curriculumDetail.getSubject().getNameSubject();
        this.semester = curriculumDetail.getSemester();
        this.credit = curriculumDetail.getSubject().getCredit();
        this.weight = curriculumDetail.getWeight();
        this.nameMajor = curriculumDetail.getSubject().getMajorSubject();
    }
}
