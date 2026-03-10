package com.deisfansha.si_amto.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ImportResult {
    private int successCount;
    private int failedCount;
    private List<String> failedData;

    public ImportResult() {

    }
}
