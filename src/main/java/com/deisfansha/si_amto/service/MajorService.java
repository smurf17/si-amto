package com.deisfansha.si_amto.service;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.model.Major;
import com.deisfansha.si_amto.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MajorService {
    @Autowired
    private MajorRepository majorRepo;

    public List<Major> getAllMajor(){
        return majorRepo.findAll();
    }

    public ApiResponse getMajorById(Long id) {
        return majorRepo.findById(id)
                .map(major -> new ApiResponse(MessageContains.DATA_FOUND, major, true))
                .orElse(new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false));

    }

    public List<Major> getAllMajorByTrue(Boolean active) {
        return majorRepo.findByActive(active);
    }
    public ApiResponse savemajor(Major major) {
        // VALIDASI WAJIB DIISI
        if(major.getMajorCode() == 0){
            return new ApiResponse<>(MessageContains.DATA_REQUIRED, null, false);
        }

        if (major.getName() == null || major.getName().trim().isEmpty()) {
            return new ApiResponse<>(MessageContains.DATA_REQUIRED, null, false);
        }

        if (major.getEduLevel() == null || major.getEduLevel().trim().isEmpty()) {
            return new ApiResponse<>(MessageContains.DATA_REQUIRED, null, false);
        }

        if (majorRepo.existsByMajorCode(major.getMajorCode())){
            return new ApiResponse<>(MessageContains.DATA_ALREADY_USED, null, false);
        }
        major.setActive(true);

        Major saved = majorRepo.save(major);
        return new ApiResponse(MessageContains.DATA_SUCCESSFULLY_SAVED, saved, true);
    }

    public ApiResponse deletemajorById(Long id) {
        Optional<Major> existingMajor = majorRepo.findById(id);

        if (existingMajor.isEmpty()) {
            return new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, false);
        }
        majorRepo.deleteById(id);
        return new ApiResponse<>(MessageContains.DATA_DELETED, existingMajor, false);
    }

    public ApiResponse updatedMajorById(Long id, Major updateMajor) {
        return majorRepo.findById(id).map(existingMajor -> {
            // Update field yang ingin diubah
            existingMajor.setName(updateMajor.getName());
            existingMajor.setMajorCode(updateMajor.getMajorCode());
            existingMajor.setEduLevel(updateMajor.getEduLevel());

            // Simpan perubahan
            majorRepo.save(existingMajor);

            // Kembalikan response sukses
            return new ApiResponse(MessageContains.DATA_UPDATED, existingMajor, true);

        }).orElseGet(() ->
                // Kalau data tidak ditemukan
                new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false)
        );

    }

    public ApiResponse softDeleteMajorById(Long id) {
        return majorRepo.findById(id).map(existingMajor -> {
            // Update field yang ingin diubah
            existingMajor.setActive(false);
            // Simpan perubahan
            majorRepo.save(existingMajor);

            // Kembalikan response sukses
            return new ApiResponse(MessageContains.DATA_DELETED, existingMajor, true);

        }).orElseGet(() ->
                // Kalau data tidak ditemukan
                new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false)
        );

    }

    public List<Major> filterMajors(String eduLevel, String name){
        return majorRepo.filter(eduLevel, name);
    }
}
