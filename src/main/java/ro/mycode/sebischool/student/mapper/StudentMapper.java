package ro.mycode.sebischool.student.mapper;

import ro.mycode.sebischool.books.mapper.BookMapper;
import ro.mycode.sebischool.course.mapper.CourseMapper;
import ro.mycode.sebischool.student.dtos.StudentDetailResponse;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;


public class StudentMapper {

    public static StudentSummaryResponse StudentToStudentSummaryResponse(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentSummaryResponse(
                student.getId(),
                student.getUser().getFirstName(),
                student.getUser().getLastName(),
                student.getUser().getEmail(),
                student.getAge()
        );
    }


    public static StudentDetailResponse StudentToStudentDetailResponse(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentDetailResponse(
                student.getId(),
                student.getUser().getFirstName(),
                student.getUser().getLastName(),
                student.getUser().getEmail(),
                student.getEnrolments().stream().map(enrolment -> CourseMapper.toDto(enrolment.getCourse())).toList(),
                student.getBooks().stream().map(BookMapper::toDto).toList()
        );
    }
}
