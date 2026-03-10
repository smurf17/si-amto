package com.deisfansha.si_amto.service.amto;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.dtoRequest.MajorLicenseRequest;
import com.deisfansha.si_amto.model.Major;
import com.deisfansha.si_amto.model.amto.License;
import com.deisfansha.si_amto.model.amto.MajorLicense;
import com.deisfansha.si_amto.repository.MajorRepository;
import com.deisfansha.si_amto.repository.amto.LicenseRepository;
import com.deisfansha.si_amto.repository.amto.MajorLicenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MajorLicenseService {
    @Autowired
    private MajorLicenseRepository majorLicenseRepo;
    @Autowired
    private MajorRepository majorRepo;
    @Autowired
    private LicenseRepository licenseRepo;

    public List<MajorLicense> getLicenseByTrue(boolean active){
        return majorLicenseRepo.findByActive(active);
    }


    @Transactional
    public void batchCreateMajorLicense(Long majorId, MajorLicenseRequest req){

        Major major = majorRepo.findById(majorId)
                .orElseThrow(() -> new RuntimeException("Major not found"));

        for(Long licenseId : req.getLicenseId()){

            License license = licenseRepo.findById(licenseId)
                    .orElseThrow(() -> new RuntimeException("License not found: " + licenseId));

            MajorLicense majorLicense = new MajorLicense();
            majorLicense.setLicense(license);
            majorLicense.setMajor(major);
            majorLicenseRepo.save(majorLicense);
        }
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
