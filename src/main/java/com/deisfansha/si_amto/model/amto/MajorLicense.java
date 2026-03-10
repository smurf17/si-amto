package com.deisfansha.si_amto.model.amto;

import com.deisfansha.si_amto.model.Major;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_major_license")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MajorLicense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "license_id")
    private License license;

    private Boolean active;


}
