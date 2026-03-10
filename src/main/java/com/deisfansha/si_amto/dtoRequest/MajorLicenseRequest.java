package com.deisfansha.si_amto.dtoRequest;

import lombok.Data;

import java.util.List;

@Data
public class MajorLicenseRequest {
    private List<Long> licenseId;
}
