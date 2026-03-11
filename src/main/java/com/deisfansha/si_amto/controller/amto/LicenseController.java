package com.deisfansha.si_amto.controller.amto;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.model.amto.License;
import com.deisfansha.si_amto.service.amto.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/licenses")
public class LicenseController {
    @Autowired
    private LicenseService ls;

    @GetMapping("/{active}")
    public ResponseEntity<ApiResponse<List<License>>> getAllLicenseActive(@PathVariable boolean active){
        List<License> license = ls.getLicenseByTrue(active);

        if (license.isEmpty()){
            return ResponseEntity.badRequest().body(new ApiResponse<>(MessageContains.DATA_FOUND, null, false));
        }

        return ResponseEntity.ok(new ApiResponse<>(MessageContains.DATA_FOUND, license, true));

    }

    @PostMapping
    public ResponseEntity createLicense(@RequestBody License request){
        ApiResponse response = ls.createLicense(request);

        if (!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLicense(@PathVariable Long id, @RequestBody License request){
        ApiResponse response = ls.updatedLicenseById(id, request);

        if (!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLicense(@PathVariable Long id){
        ApiResponse response = ls.softDeleteLicenseById(id);

        if (!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
