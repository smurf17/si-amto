package com.deisfansha.si_amto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_transcript")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transcript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String concentration;
    private String titleThesis;
    private String transcriptNumber;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate gradu_date;
    private Double gpa;
    private int total_credit;
    private LocalDateTime created_at;
    private Boolean active;

    @OneToMany(mappedBy = "transcript", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TranscriptDetail> details = new ArrayList<>();
}
