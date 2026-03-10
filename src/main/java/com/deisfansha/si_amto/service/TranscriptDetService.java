package com.deisfansha.si_amto.service;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.dtoRequest.InputScore;
import com.deisfansha.si_amto.dtoRequest.ScoreBatchRequest;
import com.deisfansha.si_amto.model.Subject;
import com.deisfansha.si_amto.model.Transcript;
import com.deisfansha.si_amto.model.TranscriptDetail;
import com.deisfansha.si_amto.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TranscriptDetService {
    @Autowired
    private TranscriptDetRepository transcriptDetRepo;
    @Autowired
    private TranscriptRepository transcriptRepo;
    @Autowired
    private SubjectRepository subjectRepo;
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private CurriculumDetRepository curriDetRepo;

    @Transactional
    public ApiResponse inputScoreBatch(Long transcriptId, ScoreBatchRequest request){
        Transcript transcript = transcriptRepo.findById(transcriptId).orElse(null);

        if (transcript == null){
            return new ApiResponse<>("Transcript Not Found", null, false);
        }

        if (request.getDetails() == null || request.getDetails().isEmpty()) {
            return new ApiResponse("No grades submitted", null, false);
        }

        Long curriculumId =
                transcript.getStudent().getCurriculum().getId();

        List<TranscriptDetail> detailsToSave = new ArrayList<>();

        for (InputScore item : request.getDetails()) {

            Subject subject = subjectRepo.findById(item.getSubjectId())
                    .orElse(null);

            if (subject == null) {
                return new ApiResponse(
                        "Subject not found: " + item.getSubjectId(),
                        null,
                        false
                );
            }

            // 🔥 VALIDASI: subject ada di curriculum
            boolean valid = curriDetRepo
                    .existsByCurriculumIdAndSubjectId(
                            curriculumId,
                            subject.getId()
                    );

            if (!valid) {
                return new ApiResponse(
                        "Subject not part of student's curriculum",
                        null,
                        false
                );
            }

            // 🔥 CEK DUPLICATE
            boolean duplicate = transcriptDetRepo.existsByTranscriptIdAndSubjectId(transcriptId, subject.getId());

            if (duplicate) {
                continue; // skip kalau sudah ada
            }

            TranscriptDetail detail = new TranscriptDetail();
            detail.setTranscript(transcript);
            detail.setSubject(subject);
            detail.setScore(item.getScore());

            // convert nilai
            String gradeLetter = convertScoreToGrade(item.getScore());
            detail.setGrade_letter(gradeLetter);
            detail.setGrade_point(convertGradeToPoint(gradeLetter));

            detailsToSave.add(detail);
        }

        if (detailsToSave.isEmpty()) {
            return new ApiResponse("All subjects already graded", null, false);
        }
        recalculateGPA(transcript);

        transcriptDetRepo.saveAll(detailsToSave);
        return new ApiResponse<>("Batch grade inserted successfully", null, true);

    }

    private String convertScoreToGrade(Double score) {
        if (score >= 85) return "A";
        if (score >= 75) return "B";
        if (score >= 65) return "C";
        if (score >= 50) return "D";
        return "E";
    }

    private Double convertGradeToPoint(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0.0;
        }
    }

    private void recalculateGPA(Transcript transcript) {

        List<TranscriptDetail> details = transcriptDetRepo.findByTranscriptId(transcript.getId());

        double totalPoint = 0;
        int totalCredit = 0;

        for (TranscriptDetail d : details) {
            int credit = d.getSubject().getCredit();

            totalPoint += d.getGrade_point() * credit;
            totalCredit += credit;
        }

        double gpa = totalCredit == 0 ? 0 : totalPoint / totalCredit;

        transcript.setTotal_credit(totalCredit);
        transcript.setGpa(gpa);

        transcriptRepo.save(transcript);
    }
}
