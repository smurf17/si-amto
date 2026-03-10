package com.deisfansha.si_amto.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class PrintTranscript {
    private String majorName;
    private String eduLevel;
    private String transcriptNumber;
    private String name;
    private LocalDate birthDate;
    private String studentNumber;
    private LocalDate graduDate;
    private List<TranscriptScorePDF> scores;
    private int totalGradeLetter;
    private int totalGradePoint;
    private int totalCredit;
    private int totalGrade;
    private Double ipk;
    private String graduDesignation; // Sangat memuaskan || Memuaskan || Lulus || Cukup
    private String titleThesis;

    public PrintTranscript() {

    }
}
