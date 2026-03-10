package com.deisfansha.si_amto.repository.amto;

import com.deisfansha.si_amto.model.amto.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {
    Optional<License> findById(Long id);
    List<License> findByActive(boolean active);
    Optional<License> findByNameAndGroupMajor(String name, String GroupMajor);

    boolean existByName();
}
