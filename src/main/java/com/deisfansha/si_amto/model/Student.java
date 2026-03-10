package com.deisfansha.si_amto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "t_students")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String studentNumber;
    private String name;
    private String birthplace;
    private String classStudent;
    private String batch;
    private String year;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;
    private Boolean active;

    // Relasi ke tabel Program Studi
    @JsonIgnoreProperties("students")
    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    // Curriculum (WAJIB 1)
    @ManyToOne
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;
}
