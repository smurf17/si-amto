package com.deisfansha.si_amto.repository;

import com.deisfansha.si_amto.model.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository <Major, Long> {

    List<Major> findByActive(Boolean active);
    boolean existsByMajorCode(int majorCode);
    Optional<Major> findByNameIgnoreCase(String Name);

    @Query("""
    SELECT m FROM Major m
    WHERE (:eduLevel IS NULL OR LOWER(m.eduLevel) LIKE LOWER(CONCAT('%', :eduLevel, '%')))
      AND (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND m.active = true
""")
    List<Major> filter(@Param("eduLevel") String eduLevel,
                         @Param("name") String name);
}
