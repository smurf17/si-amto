package com.deisfansha.si_amto.repository;

import com.deisfansha.si_amto.model.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Long>{

    List<Curriculum> findByActive(Boolean Active);

    Optional<Curriculum> findByName(String name);

    boolean existsByName(String name);
}
