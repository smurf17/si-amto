package com.deisfansha.si_amto.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreBatchRequest {

    private List<InputScore> details;
}
