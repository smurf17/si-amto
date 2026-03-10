package com.deisfansha.si_amto.repository;

import  com.deisfansha.si_amto.model.Student;
import  org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

    List<Student> findByActive(Boolean Active);
    List<Student> findByBatch(String batch);

    Optional<Student> findByStudentNumber(String studentNumber);

    boolean existsByStudentNumber(String studentNumber);
    boolean existsByStudentNumberAndIdNot(String studentNumber, Long id);
    boolean existsByCurriculumId(Long Id);

    @Query("""
    SELECT s FROM Student s
    WHERE (:majorId IS NULL OR s.major.id = :majorId)
      AND (:curriculumId IS NULL OR s.curriculum.id = :curriculumId)
      AND s.active = true
""")
    List<Student> filter(@Param("majorId") Long majorId,
                         @Param("curriculumId") Long curriculumId);
}
