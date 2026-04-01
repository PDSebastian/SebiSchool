package ro.mycode.sebischool.student.service.commandService;

import ro.mycode.sebischool.student.dtos.StudentPatchRequest;
import ro.mycode.sebischool.student.dtos.StudentRequest;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;

public interface StudentCommandService {
    StudentSummaryResponse addStudent(StudentRequest studentRequest);
    StudentSummaryResponse updateStudent( StudentRequest studentRequest);
    void deleteStudent(Long id);
    StudentSummaryResponse updatePatchStudent(Long id , StudentPatchRequest studentPatchRequest);





}
