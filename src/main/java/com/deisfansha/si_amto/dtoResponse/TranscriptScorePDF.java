package com.deisfansha.si_amto.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TranscriptScorePDF {
    private String nameSubject;
    private String subjectNumber;
    private String gradeLetter;
    private int gradePoint;
    private int credit;
    private int totalGradeScore; // gradePoint*credit

    public TranscriptScorePDF(TranscriptScorePDF transcriptScorePDF) {
        this.nameSubject = transcriptScorePDF.getNameSubject();
        this.subjectNumber = transcriptScorePDF.getSubjectNumber();
        this.gradeLetter = transcriptScorePDF.getGradeLetter();
        this.gradePoint = transcriptScorePDF.getGradePoint();
        this.credit = transcriptScorePDF.getCredit();
        this.totalGradeScore = transcriptScorePDF.totalGradeScore;
    }

    public TranscriptScorePDF() {

    }
}
