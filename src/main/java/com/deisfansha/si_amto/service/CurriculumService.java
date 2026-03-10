package com.deisfansha.si_amto.service;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.dtoRequest.CurriculumRequest;
import com.deisfansha.si_amto.model.Curriculum;
import com.deisfansha.si_amto.model.Major;
import com.deisfansha.si_amto.repository.CurriculumRepository;
import com.deisfansha.si_amto.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurriculumService {
    @Autowired
    private CurriculumRepository curriculumRepo;
    @Autowired
    private MajorRepository majorRepo;

    public List<Curriculum> getAllCurriculum() {
        return curriculumRepo.findAll();
    }

    public ApiResponse getCurriculumById(Long id) {
        return curriculumRepo.findById(id)
                .map(major -> new ApiResponse(MessageContains.DATA_FOUND, major, true))
                .orElse(new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false));
    }

    public List<Curriculum> getAllCurriculumByTrue(Boolean active) {
        return curriculumRepo.findByActive(active);
    }

    public ApiResponse saveCurriculum(CurriculumRequest curriculumRequest) {
        // =========================
        // VALIDASI WAJIB ISI
        // =========================
        if (curriculumRequest.getName() == null || curriculumRequest.getName().trim().isEmpty()) {
            return new ApiResponse("Name is required", null, false);
        }

        if (curriculumRequest.getYear() == 0) {
            return new ApiResponse("Year is required", null, false);
        }

        if (curriculumRequest.getMajorId() == null) {
            return new ApiResponse("Major is required", null, false);
        }

        // cek duplikat NIM
        if (curriculumRepo.existsByName(curriculumRequest.getName())) {
            return new ApiResponse(MessageContains.DATA_ALREADY_USED, null, false);
        }

        Major major = majorRepo.findById(curriculumRequest.getMajorId())
                .orElse(null);

        if (major == null) {
            return new ApiResponse("Major not found", null, false);
        }

        Curriculum curriculum = new Curriculum();
        curriculum.setName(curriculumRequest.getName());
        curriculum.setYear(curriculumRequest.getYear());
        curriculum.setMajor(major);
        curriculum.setActive(true);// auto true

        Curriculum saved = curriculumRepo.save(curriculum);

        return new ApiResponse(MessageContains.DATA_SUCCESSFULLY_SAVED, saved, true);
    }

    public boolean existByName(String name) {
        return curriculumRepo.findByName(name).isPresent();
    }

    public ApiResponse deleteCurriculumById(Long id) {
        Optional<Curriculum> existingCurriculum = curriculumRepo.findById(id);

        if (existingCurriculum.isEmpty()) {
            return new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, false);
        }
        curriculumRepo.deleteById(id);
        return new ApiResponse<>(MessageContains.DATA_DELETED, existingCurriculum, false);
    }

    public ApiResponse updatedCurriculumById(Long id, CurriculumRequest request) {
        return curriculumRepo.findById(id).map(existing -> {

            // =====================
            // VALIDASI NAME
            // =====================
            if (request.getName() != null && !request.getName().trim().isEmpty()) {

                // cek duplicate kalau name berubah
                if (!existing.getName().equalsIgnoreCase(request.getName()) &&
                        curriculumRepo.existsByName(request.getName())) {

                    return new ApiResponse("Curriculum name already used", null, false);
                }

                existing.setName(request.getName().trim());
            }
            // =====================
            // VALIDASI YEAR
            // =====================
            if (request.getYear() != 0) {
                existing.setYear(request.getYear());
            }

            // =====================
            // VALIDASI MAJOR
            // =====================
            if (request.getMajorId() != null) {

                Major major = majorRepo.findById(request.getMajorId())
                        .orElse(null);

                if (major == null) {
                    return new ApiResponse("Major not found", null, false);
                }

                existing.setMajor(major);
            }

            curriculumRepo.save(existing);

            return new ApiResponse(MessageContains.DATA_UPDATED, existing, true);

        }).orElseGet(() ->
                new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false)
        );

    }

    public ApiResponse softDeleteCurriculumById(Long id) {
        return curriculumRepo.findById(id).map(existingCurriculum -> {
            // Update field yang ingin diubah
            existingCurriculum.setActive(false);
            // Simpan perubahan
            curriculumRepo.save(existingCurriculum);

            // Kembalikan response sukses
            return new ApiResponse(MessageContains.DATA_DELETED, existingCurriculum, true);

        }).orElseGet(() ->
                // Kalau data tidak ditemukan
                new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false)
        );

    }
}