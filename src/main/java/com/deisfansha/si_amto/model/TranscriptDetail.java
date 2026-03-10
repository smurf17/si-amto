package com.deisfansha.si_amto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_transcript_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transcript_id")
    private Transcript transcript;

    @ManyToOne
    @JoinColumn(name = "subject_Id")
    private Subject subject;

    private String grade_letter;
    private Double grade_point;
    private Double score;

}
