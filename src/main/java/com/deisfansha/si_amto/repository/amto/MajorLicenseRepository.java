package com.deisfansha.si_amto.repository.amto;

import com.deisfansha.si_amto.model.amto.MajorLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MajorLicenseRepository extends JpaRepository<MajorLicense, Long> {
    List<MajorLicense> findByActive(boolean active);
    Optional<MajorLicense> findByMajorIdAndLicenseId(Long majorId, Long licenseId);

    boolean existByName();
}
