package ro.mycode.sebischool.student.service.commandService;

import ro.mycode.sebischool.student.service.dtos.StudentRequest;
import ro.mycode.sebischool.student.service.dtos.StudentResponse;

public interface StudentCommandService {
    StudentResponse addStudent(Long id, StudentRequest studentRequest);
    StudentResponse updateStudent(Long id , StudentRequest studentRequest);
    void deleteStudent(Long id);




}
