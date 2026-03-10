package com.deisfansha.si_amto.controller.amto;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.dtoRequest.MajorLicenseRequest;
import com.deisfansha.si_amto.model.amto.MajorLicense;
import com.deisfansha.si_amto.service.amto.MajorLicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/major_license")
public class MajorLicenseController {
    @Autowired
    private MajorLicenseService mlService;

    @GetMapping("/{active}")
    public ResponseEntity<ApiResponse<List<MajorLicense>>> getAllMajorLicenseActive(@PathVariable boolean active){
        List<MajorLicense> ml = mlService.getLicenseByTrue(active);

        if (ml.isEmpty()){
            return ResponseEntity.badRequest().body(new ApiResponse<>(MessageContains.DATA_FOUND, null, false));
        }

        return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_FOUND, ml, true));

    }

    @PutMapping("/batch/{majorId}")
    public ResponseEntity<ApiResponse<?>> batchAssignStudents(@PathVariable Long curriculumId, @RequestBody MajorLicenseRequest req){
        mlService.batchCreateMajorLicense(curriculumId,req);
        return ResponseEntity.ok(
                new ApiResponse<>("License successfully assigned to Major", null, true)
        );
    }
}
