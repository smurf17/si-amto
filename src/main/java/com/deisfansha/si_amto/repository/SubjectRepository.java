package com.deisfansha.si_amto.repository;

import com.deisfansha.si_amto.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>{

    List<Subject> findByActive(Boolean active);
    List<Subject> findByMajorSubjectAndActive(String major, Boolean active);

    Optional<Subject> findBySubjectNumber(String subjectNumber);

    boolean existsBySubjectNumber(String subjectNumber);
}
