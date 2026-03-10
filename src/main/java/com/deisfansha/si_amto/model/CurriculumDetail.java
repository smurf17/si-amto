package com.deisfansha.si_amto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_curriculum_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relasi ke tabel Program Studi
    @JsonIgnoreProperties("curriculums")
    @ManyToOne
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;

    @JsonIgnoreProperties("details")
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private int semester;
    private Double weight; // Bobot mata kuliah untuk AMTO MAPPING
    private Boolean is_mandatory; //mata kuliah amto atau bukan
    private Boolean active;
}
