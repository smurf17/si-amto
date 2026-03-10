package com.deisfansha.si_amto.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCurriDetRequest {
    private Long curriculumDetId;
    private int semester;
    private Double weight;
    private Boolean is_mandatory;

    public UpdateCurriDetRequest(UpdateCurriDetRequest request) {
        this.curriculumDetId = request.getCurriculumDetId();
        this.semester = request.getSemester();
        this.weight = request.getWeight(); // untuk AMTO
        this.is_mandatory = request.getIs_mandatory(); // untuk AMTO
    }

}
