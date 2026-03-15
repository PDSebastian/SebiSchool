package ro.mycode.sebischool.enrolment.service.queryService;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.repository.EnrolmentRepository;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;
import ro.mycode.sebischool.enrolment.mappers.EnrolmentMapper;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.mapper.StudentMapper;

import java.util.List;

@Component
public class EnrolmentQueryServiceImpl implements  EnrolmentQueryService {
    EnrolmentRepository enrolmentRepository;

    public EnrolmentQueryServiceImpl(EnrolmentRepository enrolmentRepository) {
        this.enrolmentRepository = enrolmentRepository;
    }


    @Override
    public List<EnrolmentResponse> getAllEnrolments() {
        return enrolmentRepository.findAll().stream().map(EnrolmentMapper::toDto).toList();
    }

    @Override
    public List<EnrolmentResponse> getAllEnrolmentsByCourseId(Long courseID) {
        List<Enrolment> enrolments = enrolmentRepository.findByCourseId(courseID);
        return enrolments.stream().map(EnrolmentMapper::toDto).toList();


    }

    @Override
    public List<StudentSummaryResponse> getStudentsByCourseId(Long courseID) {
        List<Enrolment> enrolments = enrolmentRepository.findByCourseId(courseID);
        return enrolments.stream().map(enrolment -> StudentMapper.StudentToStudentSummaryResponse(enrolment.getStudent())).toList();
    }


}
