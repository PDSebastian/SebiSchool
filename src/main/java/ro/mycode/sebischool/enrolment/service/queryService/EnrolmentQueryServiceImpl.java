package ro.mycode.sebischool.enrolment.service.queryService;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.repository.EnrolmentRepository;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentResponse;
import ro.mycode.sebischool.enrolment.service.mappers.EnrolmentMapper;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.mapper.StudentMapper;

import java.util.List;

@Component
public class EnrolmentQueryServiceImpl implements  EnrolmentQueryService {
    private final StudentMapper studentMapper;
    EnrolmentRepository enrolmentRepository;
    EnrolmentMapper enrolmentMapper;
    public EnrolmentQueryServiceImpl(EnrolmentRepository enrolmentRepository, EnrolmentMapper enrolmentMapper, StudentMapper studentMapper) {
        this.enrolmentRepository = enrolmentRepository;
        this.enrolmentMapper = enrolmentMapper;
        this.studentMapper = studentMapper;
    }


    @Override
    public List<EnrolmentResponse> getAllEnrolments() {
        return enrolmentRepository.findAll().stream().map(enrolmentMapper::toDto).toList();
    }

    @Override
    public List<EnrolmentResponse> getAllEnrolmentsByCourseId(Long courseID) {
        List<Enrolment> enrolments = enrolmentRepository.findByCourseId(courseID);
        return enrolments.stream().map(enrolmentMapper::toDto).toList();


    }

    @Override
    public List<StudentSummaryResponse> getStudentsByCourseId(Long courseID) {
        List<Enrolment> enrolments = enrolmentRepository.findByCourseId(courseID);
        return enrolments.stream().map(enrolment -> studentMapper.toDto(enrolment.getStudent())).toList();
    }


}
