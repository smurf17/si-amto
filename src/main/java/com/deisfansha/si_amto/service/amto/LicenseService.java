package com.deisfansha.si_amto.service.amto;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.model.amto.License;
import com.deisfansha.si_amto.repository.amto.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LicenseService {
    @Autowired
    private LicenseRepository licenseRepo;

    public List<License> getLicenseByTrue(boolean active){
        return licenseRepo.findByActive(active);
    }

    public ApiResponse createLicense(License licenseRequest){
        Optional<License> license = licenseRepo.findByNameAndGroupMajor(licenseRequest.getLicenseCode(), licenseRequest.getGroupMajor());

        if (licenseRequest.getLicenseCode() == null || licenseRequest.getLicenseCode().trim().isEmpty()) {
            return new ApiResponse<>(MessageContains.DATA_REQUIRED, null, false);
        }

        if (licenseRequest.getGroupMajor() == null || licenseRequest.getGroupMajor().trim().isEmpty()) {
            return new ApiResponse<>(MessageContains.DATA_REQUIRED, null, false);
        }

        if(license.isPresent()){
            return new ApiResponse<>(MessageContains.DATA_ALREADY_USED, null, false);
        }

        licenseRequest.setActive(true);
        License saved = licenseRepo.save(licenseRequest);
        return new ApiResponse<>(MessageContains.DATA_SUCCESSFULLY_SAVED, saved, true);

    }

    public ApiResponse updatedLicenseById(Long id, License request) {
        return licenseRepo.findById(id).map(existingLicense -> {
            // Update field yang ingin diubah
            existingLicense.setLicenseCode(request.getLicenseCode());
            existingLicense.setGroupMajor(request.getGroupMajor());

            Optional<License> license = licenseRepo.findByNameAndGroupMajor(request.getLicenseCode(), request.getGroupMajor());

            if(license.isPresent()){
                return new ApiResponse<>(MessageContains.DATA_ALREADY_USED, null, false);
            }

            // Simpan perubahan
            licenseRepo.save(existingLicense);

            // Kembalikan response sukses
            return new ApiResponse(MessageContains.DATA_UPDATED, existingLicense, true);

        }).orElseGet(() ->
                // Kalau data tidak ditemukan
                new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false)
        );

    }

    public ApiResponse softDeleteLicenseById(Long id) {
        return licenseRepo.findById(id).map(existingLicense -> {
            // Update field yang ingin diubah
            existingLicense.setActive(false);
            // Simpan perubahan
            licenseRepo.save(existingLicense);

            // Kembalikan response sukses
            return new ApiResponse(MessageContains.DATA_DELETED, existingLicense, true);

        }).orElseGet(() ->
                // Kalau data tidak ditemukan
                new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false)
        );

    }

}
