package com.deisfansha.si_amto.service;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.dtoResponse.ImportResult;
import com.deisfansha.si_amto.model.Major;
import com.deisfansha.si_amto.model.Subject;
import com.deisfansha.si_amto.repository.MajorRepository;
import com.deisfansha.si_amto.repository.SubjectRepository;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepo;
    @Autowired
    MajorRepository majorRepository;

    public List<Subject> getAllSubject() {
        return subjectRepo.findAll();
    }

    public ApiResponse getSubjectById(Long id) {
        return subjectRepo.findById(id)
                .map(subject -> new ApiResponse(MessageContains.DATA_FOUND, subject, true))
                .orElse(new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false));
    }

    public List<Subject> getAllSubjectByTrue(Boolean acive) {
        return subjectRepo.findByActive(acive);
    }

    public ApiResponse getSubjectByMajor(Long id){
        return majorRepository.findById(id).map(major -> {

            List<Subject> subjects =
                    subjectRepo.findByMajorSubjectAndActive(major.getName(), true);

            if (subjects.isEmpty()){
                return new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, false);
            }

            return new ApiResponse<>(MessageContains.DATA_FOUND, subjects, true);

        }).orElse(new ApiResponse<>("Major not found", null, false));

    }

    
    public ApiResponse savedSubject(Subject subject){
        // =========================
        // VALIDASI WAJIB ISI
        // =========================
        if (subject.getNameSubject() == null || subject.getNameSubject().trim().isEmpty()) {
            return new ApiResponse("Name Subject is required", null, false);
        }

        if (subject.getSubjectNumber() == null || subject.getSubjectNumber().trim().isEmpty()) {
            return new ApiResponse("Subject Code is required", null, false);
        }

        if (subject.getCredit() == 0) {
            return new ApiResponse("Credit is required", null, false);
        }

        if(subject.getMajorSubject() == null || subject.getMajorSubject().trim().isEmpty()){
            return new ApiResponse("Major Subject is required", null, false);
        }

        // cek duplikat NIM
        if (subjectRepo.existsBySubjectNumber(subject.getSubjectNumber())) {
            return new ApiResponse(MessageContains.DATA_ALREADY_USED, null, false);
        }

        Subject subjectSave = new Subject();
        subjectSave.setNameSubject(subject.getNameSubject());
        subjectSave.setSubjectNumber(subject.getSubjectNumber());
        subjectSave.setCredit(subject.getCredit());
        subjectSave.setMajorSubject(subject.getMajorSubject());
        subjectSave.setActive(true);// auto true

        subjectRepo.save(subjectSave);

        return new ApiResponse(MessageContains.DATA_SUCCESSFULLY_SAVED, subjectSave, true);
    }

    public boolean existBySubjectNumber(String numberSubject) {
        return subjectRepo.findBySubjectNumber(numberSubject).isPresent();
    }

    public ApiResponse deleteSubjectById(Long id) {
        Optional<Subject> existingSubject = subjectRepo.findById(id);

        if (existingSubject.isEmpty()) {
            return new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, false);
        }
        subjectRepo.deleteById(id);
        return new ApiResponse<>(MessageContains.DATA_DELETED, existingSubject, false);
    }

    public ApiResponse updatedSubjectById(Long id, Subject updateSubject) {
        return subjectRepo.findById(id).map(existing -> {
            // =====================
            // VALIDASI Subject Number
            // =====================

            if (updateSubject.getSubjectNumber() != null && !updateSubject.getSubjectNumber().trim().isEmpty()) {
                //cek duplicate subjectNumber
                if(!existing.getSubjectNumber().equalsIgnoreCase(updateSubject.getSubjectNumber()) && subjectRepo.existsBySubjectNumber(updateSubject.getSubjectNumber())){
                    return new ApiResponse("Sucject Code already used", null, false);
                }

                existing.setNameSubject(updateSubject.getNameSubject().trim());

            }

            // =====================
            // VALIDASI NAME
            // =====================
            if (updateSubject.getNameSubject() != null) {
                existing.setNameSubject(updateSubject.getNameSubject());
            }

            // =====================
            // VALIDASI CREDIT
            // =====================
            if (updateSubject.getCredit() != 0) {
                existing.setCredit(updateSubject.getCredit());
            }

            if (updateSubject.getMajorSubject()!= null) {
                existing.setMajorSubject(updateSubject.getMajorSubject());
            }

            subjectRepo.save(existing);

            return new ApiResponse(MessageContains.DATA_UPDATED, existing, true);

        }).orElseGet(() ->
                new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false)
        );

    }

    public ApiResponse importSubjects (MultipartFile file){
        int success = 0;
        int failed = 0;

        List<String> failedNumber = new ArrayList<>();

        try {

            String fileName = file.getOriginalFilename();

            if (fileName == null ||
                    !(fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))) {

                return new ApiResponse("Unsupported file format", null, false);
            }

            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                String code = formatter.formatCellValue(row.getCell(1)).trim();
                String name = formatter.formatCellValue(row.getCell(2)).trim();
                String creditStr = formatter.formatCellValue(row.getCell(3)).trim();
                String majorName = formatter.formatCellValue(row.getCell(4)).trim();

                if (code.isEmpty()) continue;

                // cek duplicate
                if (subjectRepo.existsBySubjectNumber(code)) {
                    failed++;
                    failedNumber.add(code + " (Duplicate)");
                    continue;
                }

                int credit;

                try {
                    credit = Integer.parseInt(creditStr);
                } catch (Exception e) {
                    failed++;
                    failedNumber.add(code + " (Invalid credit)");
                    continue;
                }

                Major major = majorRepository
                        .findByNameIgnoreCase(majorName)
                        .orElse(null);

                if (major == null && !majorName.equalsIgnoreCase("ALL")) {
                    failed++;
                    failedNumber.add(code + " (Major Not Found: " + majorName + ")");
                    continue;
                }

                Subject subject = new Subject();
                subject.setSubjectNumber(code);
                subject.setNameSubject(name);
                subject.setCredit(credit);
                subject.setMajorSubject(majorName);
                subject.setActive(true);

                subjectRepo.save(subject);

                success++;
            }

            workbook.close();

        } catch (Exception e) {
            return new ApiResponse("Import failed: " + e.getMessage(), null, false);
        }

        ImportResult result = new ImportResult();
        result.setSuccessCount(success);
        result.setFailedCount(failed);
        result.setFailedData(failedNumber);

        return new ApiResponse("Import finished", result, true);
    }
}