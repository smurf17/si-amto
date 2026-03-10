package com.deisfansha.si_amto.service;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.dtoRequest.BatchCurriDet;
import com.deisfansha.si_amto.dtoRequest.CurriculumDetRequest;
import com.deisfansha.si_amto.dtoRequest.UpdateCurriDetRequest;
import com.deisfansha.si_amto.dtoResponse.CurriDetResponse;
import com.deisfansha.si_amto.model.Curriculum;
import com.deisfansha.si_amto.model.CurriculumDetail;
import com.deisfansha.si_amto.model.Subject;
import com.deisfansha.si_amto.repository.CurriculumDetRepository;
import com.deisfansha.si_amto.repository.CurriculumRepository;
import com.deisfansha.si_amto.repository.SubjectRepository;
import com.deisfansha.si_amto.repository.TranscriptDetRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurriculumDetService {
    @Autowired
    private CurriculumDetRepository curriculumDetRepository;
    @Autowired
    private SubjectRepository subjectRepo;
    @Autowired
    private CurriculumRepository curriculumRepo;
    @Autowired
    private TranscriptDetRepository transcriptDetRepo;

    public List<CurriculumDetail> getAllCurriculumByTrue(Long curriculumId) {
        return curriculumDetRepository.findByCurriculumIdAndActive(curriculumId, true);
    }


    public ApiResponse addSubjectToCurriculum(Long curriculumId, CurriculumDetRequest request) {

        // 1️⃣ cek curriculum
        Curriculum curriculum = curriculumRepo.findById(curriculumId)
                .orElse(null);

        if (curriculum == null) {
            return new ApiResponse("Curriculum"+ MessageContains.DATA_NOT_FOUND, null, false);
        }

        // 2️⃣ cek subject
        Subject subject = subjectRepo.findById(request.getSubjectId())
                .orElse(null);

        if (subject == null) {
            return new ApiResponse("Subject not found", null, false);
        }

        // 3️⃣ cek duplicate
        if (curriculumDetRepository.existsByCurriculumIdAndSubjectId(curriculumId, request.getSubjectId())) {

            return new ApiResponse("Subject already exists in this curriculum", null, false);
        }

        // 4️⃣ save
        CurriculumDetail detail = new CurriculumDetail();
        detail.setCurriculum(curriculum);
        detail.setSubject(subject);
        detail.setSemester(request.getSemester());
        detail.setWeight(request.getWeight());
        detail.setIs_mandatory(request.getIs_mandatory());
        detail.setActive(true);

        CurriculumDetail saved = curriculumDetRepository.save(detail);

        CurriDetResponse response = new CurriDetResponse(
                saved.getCurriculum().getName(),
                saved.getSubject().getSubjectNumber(),
                saved.getSubject().getNameSubject(),
                saved.getSemester(),
                saved.getSubject().getCredit(),
                saved.getWeight(), // kalau ada
                saved.getCurriculum().getMajor().getName());

        return new ApiResponse("Subject added to curriculum", response, true);
    }

    public ApiResponse getCurriculumDet(Long curriculumId){
        List<CurriDetResponse> subjectCurriculum = curriculumDetRepository.findByCurriculumIdAndActive(curriculumId, true).stream()
                .map(CurriDetResponse::new)
                .toList();

        if(subjectCurriculum.isEmpty()){
            return new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, false);
        }
        return new ApiResponse<>(MessageContains.DATA_FOUND, subjectCurriculum, true);
    }

    public ApiResponse deleteCurriculumDetail(Long id){
        CurriculumDetail detail = curriculumDetRepository.findById(id)
                .orElse(null);

        if (detail == null) {
            return new ApiResponse("Curriculum detail not found", null, false);
        }

        if (transcriptDetRepo.existsBySubjectId(detail.getSubject().getId())) {
            return new ApiResponse(
                    "Cannot delete, subject already used in transcript",
                    null,
                    false
            );
        }
        detail.setActive(false);

        curriculumDetRepository.save(detail);

        return new ApiResponse("Subject removed from curriculum", null, true);
    }

    @Transactional
    public ApiResponse addSubjectsChecklist(Long curriculumId,
                                            BatchCurriDet request) {

        Curriculum curriculum = curriculumRepo.findById(curriculumId)
                .orElse(null);

        if (curriculum == null) {
            return new ApiResponse("Curriculum not found", null, false);
        }

        if (request.getSubjectIds() == null || request.getSubjectIds().isEmpty()) {
            return new ApiResponse("No subjects selected", null, false);
        }

        // ambil semua subject sekaligus (lebih efisien)
        List<Subject> subjects = subjectRepo.findAllById(request.getSubjectIds());

        if (subjects.size() != request.getSubjectIds().size()) {
            return new ApiResponse("Some subjects not found", null, false);
        }

        List<CurriculumDetail> detailsToSave = new ArrayList<>();

        for (Subject subject : subjects) {

            // cek duplicate
            boolean exists = curriculumDetRepository
                    .existsByCurriculumIdAndSubjectId(curriculumId, subject.getId());

            if (!exists) {

                CurriculumDetail detail = new CurriculumDetail();
                detail.setCurriculum(curriculum);
                detail.setSubject(subject);
                detail.setSemester(0);
                detail.setWeight(0.0);
                detail.setIs_mandatory(true);
                detail.setActive(true); // default

                detailsToSave.add(detail);
            }
        }

        if (detailsToSave.isEmpty()) {
            return new ApiResponse("All selected subjects already exist", null, false);
        }

        List<CurriculumDetail> saved = curriculumDetRepository.saveAll(detailsToSave);

        // Mapping ke Response DTO
        List<CurriDetResponse> responses = saved.stream()
                .map(this::mapToResponse)
                .toList();

        return new ApiResponse("Batch insert success", detailsToSave, true);
    }

    @Transactional
    public ApiResponse softDeleteBatch(Long curriculumId,
                                       BatchCurriDet request) {

        if (request.getSubjectIds() == null || request.getSubjectIds().isEmpty()) {
            return new ApiResponse("No subjects selected", null, false);
        }

        List<CurriculumDetail> details =
                curriculumDetRepository
                        .findByCurriculumIdAndSubjectIdInAndActiveTrue(
                                curriculumId,
                                request.getSubjectIds()
                        );

        if (details.isEmpty()) {
            return new ApiResponse("No matching subjects found", null, false);
        }

        // Soft delete
        for (CurriculumDetail detail : details) {
            detail.setActive(false);
        }

        curriculumDetRepository.saveAll(details);

        // Mapping ke response
        List<CurriDetResponse> responses = details.stream()
                .map(this::mapToResponse)
                .toList();

        return new ApiResponse("Batch soft delete success", responses, true);
    }


    private CurriDetResponse mapToResponse(CurriculumDetail detail) {
        return new CurriDetResponse(
                detail.getCurriculum().getName(),
                detail.getSubject().getSubjectNumber(),
                detail.getSubject().getNameSubject(),
                detail.getSemester(),
                detail.getSubject().getCredit(),
                detail.getWeight(), // kalau ada field weight
                detail.getCurriculum().getMajor().getName()
        );
    }

    @Transactional
    public ApiResponse updateBatch(Long curriculumId, List<UpdateCurriDetRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            return new ApiResponse("No data to update", null, false);
        }

        List<Long> ids = requests.stream()
                .map(UpdateCurriDetRequest::getCurriculumDetId)
                .toList();

        List<CurriculumDetail> details =
                curriculumDetRepository.findAllById(ids);

        if (details.size() != ids.size()) {
            return new ApiResponse("Some curriculum details not found", null, false);
        }

        // Map biar cepat akses
        Map<Long, UpdateCurriDetRequest> requestMap =
                requests.stream()
                        .collect(Collectors.toMap(
                                UpdateCurriDetRequest::getCurriculumDetId,
                                r -> r
                        ));

        for (CurriculumDetail detail : details) {

            // pastikan masih dalam curriculum yang sama
            if (!detail.getCurriculum().getId().equals(curriculumId)) {
                return new ApiResponse("Invalid curriculum detail detected", null, false);
            }

            UpdateCurriDetRequest req =
                    requestMap.get(detail.getId());

            if (req.getSemester() != 0)
                detail.setSemester(req.getSemester());

            if (req.getWeight() != null)
                detail.setWeight(req.getWeight());

            if (req.getIs_mandatory() != null)
                detail.setIs_mandatory(req.getIs_mandatory());
        }

        List<CurriculumDetail> updated =
                curriculumDetRepository.saveAll(details);

        List<CurriDetResponse> responses =
                updated.stream()
                        .map(this::mapToResponse)
                        .toList();

        return new ApiResponse("Batch update success", responses, true);
    }

}
