package com.deisfansha.si_amto.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputScore {
    private Long subjectId;
    private Double score;
}
