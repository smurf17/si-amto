package com.deisfansha.si_amto.dtoRequest;

import lombok.Data;

import java.util.List;

@Data
public class AddStudentCurriculum {
    private List<Long> studentId;
}
