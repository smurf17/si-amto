package com.deisfansha.si_amto.repository;

import com.deisfansha.si_amto.model.TranscriptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranscriptDetRepository extends JpaRepository<TranscriptDetail, Long> {
    boolean existsBySubjectId(Long subjectId);
    boolean existsByTranscriptIdAndSubjectId(Long transcriptId, Long subjectId);

    List<TranscriptDetail> findByTranscriptId(Long id);
//    List<TranscriptDetail> findByTranscriptIdOrderBySubject_SemesterAsc(Long Id);
}
