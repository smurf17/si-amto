package com.deisfansha.si_amto.dtoRequest;

import com.deisfansha.si_amto.model.Transcript;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptRequest {
    private Long studentId;
    private String concentration;
    private String transcriptNumber;
    private String title;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate graduateDate;

    public TranscriptRequest(Transcript transcript) {
        this.studentId = transcript.getStudent().getId();
        this.concentration = transcript.getConcentration();
        this.title = transcript.getTitleThesis();
        this.transcriptNumber = transcript.getTranscriptNumber();
        this.graduateDate = transcript.getGradu_date();
    }
}
