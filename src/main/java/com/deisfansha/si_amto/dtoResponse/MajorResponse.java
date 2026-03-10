package com.deisfansha.si_amto.dtoResponse;

import com.deisfansha.si_amto.model.Major;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MajorResponse {
    private int majorCode;
    private String eduLevel;
    private String name;

    public MajorResponse (Major major){
        this.majorCode = major.getMajorCode();
        this.eduLevel = major.getEduLevel();
        this.name = major.getName();
    }
}
