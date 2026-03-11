package ro.mycode.sebischool.enrolment.service.queryService;

import jdk.jfr.Category;
import org.springframework.stereotype.Component;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.repository.EnrolmentRepository;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentResponse;
import ro.mycode.sebischool.enrolment.service.mappers.EnrolmentMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EnrolmentQueryServiceImpl implements  EnrolmentQueryService {
    EnrolmentRepository enrolmentRepository;
    EnrolmentMapper enrolmentMapper;
    public EnrolmentQueryServiceImpl(EnrolmentRepository enrolmentRepository, EnrolmentMapper enrolmentMapper) {
        this.enrolmentRepository = enrolmentRepository;
        this.enrolmentMapper = enrolmentMapper;

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


}
