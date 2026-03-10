package com.deisfansha.si_amto.service;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.dtoRequest.TranscriptRequest;
import com.deisfansha.si_amto.dtoResponse.PrintTranscript;
import com.deisfansha.si_amto.dtoResponse.TranscriptScorePDF;
import com.deisfansha.si_amto.model.Major;
import com.deisfansha.si_amto.model.Student;
import com.deisfansha.si_amto.model.Transcript;
import com.deisfansha.si_amto.model.TranscriptDetail;
import com.deisfansha.si_amto.repository.StudentRepository;
import com.deisfansha.si_amto.repository.TranscriptDetRepository;
import com.deisfansha.si_amto.repository.TranscriptRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TranscriptService {
    @Autowired
    private TranscriptRepository transcriptRepo;
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private TranscriptDetRepository transcriptDetRepo;
    @Value("${libreoffice.path}")
    private String libreOfficePath;

    @Transactional
    public ApiResponse addHeaderTranscript (TranscriptRequest request){
        Student student = studentRepo.findById(request.getStudentId()).orElse(null);

        if (student == null){
            return new ApiResponse<> ("Student Not Found", null, false);
        }

        // validasi sudah punya transkrip
        boolean exists = transcriptRepo.existsByStudentIdAndActiveTrue(student.getId());
        if (exists){
            return new ApiResponse<> ("Transcript Already Exists", null, false);
        }

        Transcript transcript = new Transcript();
        transcript.setStudent(student);
        transcript.setConcentration(request.getConcentration());
        transcript.setGradu_date(request.getGraduateDate());
        transcript.setCreated_at(LocalDateTime.now());
        transcript.setTranscriptNumber(request.getTranscriptNumber());
        transcript.setTitleThesis(request.getTitle());
        transcript.setActive(true);
        transcript.setGpa(0.0);
        transcript.setTotal_credit(0);

        transcriptRepo.save(transcript);
        return new ApiResponse<>("Add Header Transcript Success", transcript.getId(), true);
    }


    public ByteArrayInputStream generateTranscript(Long id) throws Exception {

        PrintTranscript data = buildTranscriptExcelData(id);

        ClassPathResource resource =
                new ClassPathResource("templates/transcript_template_letterhead_space.xlsx");

        Workbook workbook = new XSSFWorkbook(resource.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        // ======================
        // HEADER
        // ======================
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        setCellValue(sheet, 9, 3, ": "+data.getMajorName());
        setCellValue(sheet, 10, 3, ": "+data.getEduLevel());
        setCellValue(sheet, 11, 3, ": "+data.getTranscriptNumber());
        setCellValue(sheet, 12, 3, ": "+data.getName());
        setCellValue(sheet, 13, 3, ": "+data.getBirthDate().toString());
        setCellValue(sheet, 14, 3, ": "+data.getStudentNumber());
        setCellValue(sheet, 15, 3, ": "+data.getGraduDate().toString());

        // ======================
        // TABLE NILAI
        // ======================

        int firstPageStart = 19;
        int secondPageStart = 55;
        Row headerTemplate = sheet.getRow(18);   // row angka 1-7
        Row subjectTemplate = sheet.getRow(19);  // row template subject

        List<TranscriptScorePDF> subjects = data.getScores();

        int templateSubjectRow = 19;
        int subjectCount = subjects.size();

        if (subjectCount > 1) {
            sheet.shiftRows(
                    templateSubjectRow + 1,
                    sheet.getLastRowNum(),
                    subjectCount - 1
            );
        }

        for (int i = 0; i < subjects.size(); i++) {

            TranscriptScorePDF subject = subjects.get(i);

            int rowIndex;

            if (i < 34) {
                rowIndex = firstPageStart + i;
            } else {
                // saat subject ke 35
                if (i == 34) {

                    Row newHeader = sheet.createRow(secondPageStart - 1);

                    for (int c = 0; c < headerTemplate.getLastCellNum(); c++) {

                        Cell source = headerTemplate.getCell(c);
                        Cell target = newHeader.createCell(c);

                        if (source == null) continue;

                        target.setCellStyle(source.getCellStyle());

                        switch (source.getCellType()) {
                            case STRING:
                                target.setCellValue(source.getStringCellValue());
                                break;

                            case NUMERIC:
                                target.setCellValue(source.getNumericCellValue());
                                break;

                            default:
                                target.setCellValue(source.toString());
                        }
                    }
                }
                rowIndex = secondPageStart + (i - 34);
            }

            Row templateRow = subjectTemplate; // row contoh style

            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }

            row.setHeight((short) -1);
//            sheet.autoSizeColumn(1);

            copyRowStyle(templateRow, row);

            setCell(row, 0, i + 1);
            setCell(row, 1, subject.getNameSubject());
            setCell(row, 2, subject.getSubjectNumber());
            setCell(row, 3, subject.getGradeLetter());
            setCell(row, 4, subject.getGradePoint());
            setCell(row, 5, subject.getCredit());
            setCell(row, 6, subject.getTotalGradeScore());
        }

        for(int i = 0; i<=6;i++){
            sheet.autoSizeColumn(i);
        }

        int lastRow;

        if (subjects.size() <= 34) {
            lastRow = firstPageStart + subjects.size() - 1;
        } else {
            lastRow = secondPageStart + (subjects.size() - 34) - 1;
        }

        int rowTotal = lastRow + 1;

        Row totalRow = sheet.getRow(rowTotal);
        if (totalRow == null) totalRow = sheet.createRow(rowTotal);

        setCell(totalRow, 3, data.getTotalGradeLetter());
        setCell(totalRow, 4, data.getTotalGradePoint());
        setCell(totalRow, 5, data.getTotalCredit());
        setCell(totalRow, 6, data.getTotalGrade());


// Jumlah Kredit Kumulatif
        int rowCredit = lastRow + 2;
        Row creditRow = sheet.getRow(rowCredit);
        if (creditRow == null) creditRow = sheet.createRow(rowCredit);

        setCell(creditRow, 4, data.getTotalCredit());


// Jumlah Mutu
        int rowMutu = lastRow + 3;
        Row mutuRow = sheet.getRow(rowMutu);
        if (mutuRow == null) mutuRow = sheet.createRow(rowMutu);

        setCell(mutuRow, 4, data.getTotalGrade());


// IPK
        int rowIpk = lastRow + 4;
        Row ipkRow = sheet.getRow(rowIpk);
        if (ipkRow == null) ipkRow = sheet.createRow(rowIpk);

        setCell(ipkRow, 4, data.getIpk());


// Predikat
        int rowPredicate = lastRow + 5;
        Row predicateRow = sheet.getRow(rowPredicate);
        if (predicateRow == null) predicateRow = sheet.createRow(rowPredicate);

        setCell(predicateRow, 4, data.getGraduDesignation());


// Judul Skripsi
        int rowThesis = lastRow + 6;
        Row thesisRow = sheet.getRow(rowThesis);
        if (thesisRow == null) thesisRow = sheet.createRow(rowThesis);

        setCell(thesisRow, 2, data.getTitleThesis());

        CellRangeAddress newRegion =
                new CellRangeAddress(lastRow + 6, lastRow + 6, 2, 7);

        boolean isMerged = false;

        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {

            CellRangeAddress region = sheet.getMergedRegion(i);

            if (region.intersects(newRegion)) {
                isMerged = true;
                break;
            }
        }

        if (!isMerged) {
            sheet.addMergedRegion(newRegion);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    private PrintTranscript buildTranscriptExcelData(Long transcriptId) {

        Transcript transcript = transcriptRepo.findById(transcriptId)
                .orElseThrow(() -> new RuntimeException("Transcript not found"));

        Student student = transcript.getStudent();
        Major major = student.getMajor();

        List<TranscriptDetail> details =
                transcriptDetRepo.findByTranscriptId(transcriptId);

        List<TranscriptScorePDF> scoreList = new ArrayList<>();

        int totalGradeLetter = 0;
        int totalGradePoint = 0;
        int totalCredit = 0;
        int totalGrade = 0;

        for (TranscriptDetail d : details) {

            int credit = d.getSubject().getCredit();
            int gradePoint = d.getGrade_point().intValue();
            int totalScore = gradePoint * credit;

            TranscriptScorePDF score = new TranscriptScorePDF(
                    d.getSubject().getNameSubject(),
                    d.getSubject().getSubjectNumber(),
                    d.getGrade_letter(),
                    gradePoint,
                    credit,
                    totalScore
            );

            scoreList.add(score);

            totalGradeLetter += 1;
            totalGradePoint += gradePoint;
            totalCredit += credit;
            totalGrade += totalScore;
        }

        Double ipk = transcript.getGpa();

        return new PrintTranscript(
                major.getName(),
                major.getEduLevel(),
                transcript.getTranscriptNumber(),
                student.getName(),
                student.getBirthDate(),
                student.getStudentNumber(),
                transcript.getGradu_date(),
                scoreList,
                totalGradeLetter,
                totalGradePoint,
                totalCredit,
                totalGrade,
                ipk,
                determinePredicate(ipk),
                transcript.getTitleThesis()
        );
    }

    private String determinePredicate(Double ipk) {

        if (ipk >= 3.75) return "Dengan Pujian";
        if (ipk >= 3.50) return "Sangat Memuaskan";
        if (ipk >= 3.00) return "Memuaskan";
        return "Cukup";
    }

    private void copyRowFull(Row sourceRow, Row newRow) {

        newRow.setHeight(sourceRow.getHeight());

        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {

            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);

            if (oldCell == null) continue;

            // Copy Style (border, font, alignment, dll)
            newCell.setCellStyle(oldCell.getCellStyle());

            // Copy Cell Type + Value
            switch (oldCell.getCellType()) {

                case STRING:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;

                case NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;

                case BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;

                case FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;

                default:
                    newCell.setCellValue(oldCell.toString());
            }
        }
    }

    private void setCellValue(Sheet sheet, int rowIndex, int colIndex, String value) {
        Row row = sheet.getRow(rowIndex);
        if (row == null)
            row = sheet.createRow(rowIndex);

        Cell cell = row.getCell(colIndex);
        if (cell == null) cell = row.createCell(colIndex);

        cell.setCellValue(value);
    }


    private void setCell(Row row, int index, Object value){

        Cell cell = row.getCell(index);

        if(cell == null){
            cell = row.createCell(index);
        }

        if(value instanceof String){
            cell.setCellValue((String) value);
        }
        else if(value instanceof Integer){
            cell.setCellValue((Integer) value);
        }
        else if(value instanceof Double){
            cell.setCellValue((Double) value);
        }
    }

    private void copyRowStyle(Row sourceRow, Row newRow)
    {

        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {

            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.getCell(i);

            if (newCell == null) {
                newCell = newRow.createCell(i);
            }

            if (oldCell != null) {
                newCell.setCellStyle(oldCell.getCellStyle());
            }
        }
    }
}
