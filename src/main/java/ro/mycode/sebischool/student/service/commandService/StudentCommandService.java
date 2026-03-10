package ro.mycode.sebischool.student.service.commandService;

import jakarta.validation.Valid;
import ro.mycode.sebischool.student.service.dtos.StudentPatchRequest;
import ro.mycode.sebischool.student.service.dtos.StudentRequest;
import ro.mycode.sebischool.student.service.dtos.StudentResponse;

public interface StudentCommandService {
    StudentResponse addStudent( StudentRequest studentRequest);
    StudentResponse updateStudent(Long id , StudentRequest studentRequest);
    void deleteStudent(Long id);
    StudentResponse updatePatchStudent(Long id , StudentPatchRequest studentPatchRequest);





}
