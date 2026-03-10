package com.deisfansha.si_amto.repository;


import com.deisfansha.si_amto.model.CurriculumDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurriculumDetRepository extends JpaRepository<CurriculumDetail, Long> {
    List<CurriculumDetail> findByCurriculumIdAndActive(Long curriculumId, Boolean active);
    boolean existsByCurriculumIdAndSubjectId(Long curriculumId, Long subjectId);
    List<CurriculumDetail>
    findByCurriculumIdAndSubjectIdInAndActiveTrue(
            Long curriculumId,
            List<Long> subjectIds
    );

    boolean existsBySubjectId(Long subjectId);
}
