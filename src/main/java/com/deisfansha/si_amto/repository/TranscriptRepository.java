package com.deisfansha.si_amto.repository;

import com.deisfansha.si_amto.model.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Long> {
    boolean existsByStudentIdAndActiveTrue(Long id);

}
