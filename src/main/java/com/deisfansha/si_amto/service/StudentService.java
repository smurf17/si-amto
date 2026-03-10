package com.deisfansha.si_amto.service;

import com.deisfansha.si_amto.ApiResponse;
import com.deisfansha.si_amto.MessageContains;
import com.deisfansha.si_amto.dtoRequest.AddStudentCurriculum;
import com.deisfansha.si_amto.dtoRequest.StudentMajorRequest;
import com.deisfansha.si_amto.dtoRequest.StudentRequest;
import com.deisfansha.si_amto.dtoResponse.ImportResult;
import com.deisfansha.si_amto.model.Curriculum;
import com.deisfansha.si_amto.model.Major;
import com.deisfansha.si_amto.model.Student;
import com.deisfansha.si_amto.repository.CurriculumRepository;
import com.deisfansha.si_amto.repository.MajorRepository;
import com.deisfansha.si_amto.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import  java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private CurriculumRepository curriculumRepo;
    private Curriculum curriculum;

    public List<Student> getAllStudent() {
        return studentRepo.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepo.findById(id);
    }

    public List<Student> getAllStudentByTrue(Boolean active) {
        return studentRepo.findByActive(active);
    }

    public  List<Student> getStudentByBatchReguler(){
        return studentRepo.findByBatch("Pagi");
    }

    public ApiResponse createStudent(StudentRequest studentRequest) {
        // cek duplikat NIM
        if (studentRepo.existsByStudentNumber(studentRequest.getStudentNumber())) {
            return new ApiResponse(MessageContains.DATA_ALREADY_USED, null, false);
        }

        Major major = majorRepository.findById(studentRequest.getMajorId())
                .orElse(null);

        if (major == null) {
            return new ApiResponse("Major not found", null, false);
        }

        // ===== LOGIC AMBIL BATCH =====
        String year = studentRequest.getStudentNumber().substring(6, 8); // ambil index 6-7
        String batch = "20" + year; // jadi 2024

        Student student = new Student();
        student.setStudentNumber(studentRequest.getStudentNumber());
        student.setName(studentRequest.getName());
        student.setBirthDate(studentRequest.getBirthDate());
        student.setBirthplace(studentRequest.getBirthplace());
        student.setBatch(batch);
        student.setClassStudent(studentRequest.getClassStudent());
        student.setMajor(major);
        student.setActive(true); // auto true

        Student saved = studentRepo.save(student);

        return new ApiResponse(MessageContains.DATA_SUCCESSFULLY_SAVED, saved, true);
    }

    public boolean existByNumberStudent(String numberStudent) {
        return studentRepo.findByStudentNumber(numberStudent).isPresent();
    }

    public ApiResponse deleteStudentById(Long id) {
        Optional<Student> existingStudent = studentRepo.findById(id);

        if (existingStudent.isEmpty()) {
            return new ApiResponse<>(MessageContains.DATA_NOT_FOUND, null, false);
        }
        studentRepo.deleteById(id);
        return new ApiResponse<>(MessageContains.DATA_DELETED, existingStudent, false);
    }

    public ApiResponse updatedStudentById(Long id, Student updateStudent) {
        return studentRepo.findById(id).map(existingStudent -> {
            // Update field yang ingin diubah
            existingStudent.setName(updateStudent.getName());
            existingStudent.setStudentNumber(updateStudent.getStudentNumber());
            existingStudent.setBirthDate(updateStudent.getBirthDate());
            existingStudent.setMajor(updateStudent.getMajor());
            existingStudent.setClassStudent(updateStudent.getClassStudent());
            existingStudent.setBirthplace(updateStudent.getBirthplace());

            // Simpan perubahan
            studentRepo.save(existingStudent);

            // Kembalikan response sukses
            return new ApiResponse(MessageContains.DATA_UPDATED, existingStudent, true);

        }).orElseGet(() ->
                // Kalau data tidak ditemukan
                new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false)
        );

    }

    public ApiResponse updatedStudentCodeMajor(Long id, StudentMajorRequest smRequest) {
        return studentRepo.findById(id).map(existingStudent -> {

            // update major
            if (smRequest.getCodeM() != null) {
                Major major = majorRepository.findById(smRequest.getCodeM())
                        .orElseThrow(() -> new RuntimeException("Major Not Found"));

                existingStudent.setMajor(major);
            }

            // save
            studentRepo.save(existingStudent);

            return new ApiResponse(
                    MessageContains.DATA_UPDATED,
                    existingStudent,
                    true
            );

        }).orElseGet(() ->
                new ApiResponse(
                        MessageContains.DATA_NOT_FOUND,
                        null,
                        false
                )
        );
    }


    public ApiResponse softDeleteStudentById(Long id) {
        return studentRepo.findById(id).map(existingStudent -> {
            // Update field yang ingin diubah
            existingStudent.setActive(false);
            // Simpan perubahan
            studentRepo.save(existingStudent);

            // Kembalikan response sukses
            return new ApiResponse(MessageContains.DATA_DELETED, existingStudent, true);

        }).orElseGet(() ->
                // Kalau data tidak ditemukan
                new ApiResponse(MessageContains.DATA_NOT_FOUND, null, false)
        );

    }

    public ApiResponse importStudent (MultipartFile file){
        int success = 0;
        int failed = 0;
        List<String> failedNumber = new ArrayList<>();

        String fileName = file.getOriginalFilename().toLowerCase();

        try {
              if(fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
                  Workbook workbook = new XSSFWorkbook(file.getInputStream());
                  Sheet sheet = workbook.getSheetAt(0);
                  for (int i = 1; i<=sheet.getLastRowNum(); i++ ){
                      Row row = sheet.getRow(i);
                      if (row == null) continue;

                      String studentNumber = row.getCell(1).getStringCellValue().trim();
                      String name = row.getCell(2).getStringCellValue().trim();
                      String studentClass = row.getCell(9).getStringCellValue().trim();
                      String birthdata = row.getCell(27).getStringCellValue().trim();
                      String majorName = row.getCell(3).getStringCellValue();

                      //Split data tanggal lahir
                      String[] split = birthdata.split(",");
                      String birthPlace = split[0].trim();
                      LocalDate birthdate = LocalDate.parse(split[1].trim());

                      if (studentRepo.existsByStudentNumber(studentNumber)){
                          failed++;
                          failedNumber.add(studentNumber + " Duplicate");
                          continue;
                      }

                      Major major = majorRepository.findByNameIgnoreCase(majorName).orElse(null);

                      if (major == null){
                          failed++;
                          failedNumber.add(studentNumber + "(Major Not Found: "+majorName+")");
                          continue;
                      }

                      String year = studentNumber.substring(6, 8); // ambil index 6-7
                      String batch = "20" + year; // jadi 2024

                      Student s = new Student();
                      s.setStudentNumber(studentNumber);
                      s.setName(name);
                      s.setClassStudent(studentClass);
                      s.setBirthplace(birthPlace);
                      s.setBirthDate(birthdate);
                      s.setBatch(batch);
                      s.setMajor(major);
                      s.setActive(true);

                      studentRepo.save(s);
                      success++;
                  }
              }else {
                  return new ApiResponse("Unsupported file format", null, false);
              }

          } catch (Exception e) {
            return new ApiResponse("Import failed: " + e.getMessage(), null, false);
        }
        ImportResult result = new ImportResult();
        result.setSuccessCount(success);
        result.setFailedCount(failed);
        result.setFailedData(failedNumber);
        return new ApiResponse("Import finished", result, true);
    }
    @Transactional
    public void batchAssignStudents(Long curriculumId, AddStudentCurriculum req){

        Curriculum curriculum = curriculumRepo.findById(curriculumId)
                .orElseThrow(() -> new RuntimeException("Curriculum not found"));

        for(Long studentId : req.getStudentId()){

            Student student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

            // VALIDASI PRODI
            if(!student.getMajor().getId().equals(curriculum.getMajor().getId())){
                throw new RuntimeException(
                        "Student " + studentId + " major does not match curriculum major"
                );
            }

            student.setCurriculum(curriculum);
            studentRepo.save(student);
        }
    }

    public List<Student> filterStudents(Long majorId, Long curriculumId){
        return studentRepo.filter(majorId, curriculumId);
    }

}