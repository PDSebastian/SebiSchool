package ro.mycode.sebischool.student.mapper;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.course.service.mapper.CourseMapper;
import ro.mycode.sebischool.enrolment.service.mappers.EnrolmentMapper;
import ro.mycode.sebischool.student.dtos.StudentDetailResponse;
import ro.mycode.sebischool.student.dtos.StudentListResponse;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.model.Student;

@Component
public class StudentMapperListTODetail {
   private EnrolmentMapper enrolmentMapper;
   private CourseMapper  courseMapper;
   public  StudentMapperListTODetail(EnrolmentMapper enrolmentMapper,CourseMapper  courseMapper){
       this.enrolmentMapper=enrolmentMapper;
       this.courseMapper=courseMapper;

   }

    public StudentDetailResponse toDto(Student student){
        if(student==null){
            return null;
        }
        return new StudentDetailResponse(
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getEnrolments().stream().map(enrolment -> enrolmentMapper.toDto(enrolment)).toList(),
                student.getEnrolments().stream().map(enrolment -> courseMapper.toDto(enrolment.getCourse())).toList()
        );

    }




}
