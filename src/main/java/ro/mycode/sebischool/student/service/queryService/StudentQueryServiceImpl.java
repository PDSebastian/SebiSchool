package ro.mycode.sebischool.student.service.queryService;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.student.service.dtos.StudentResponse;
import ro.mycode.sebischool.student.service.mapper.StudentMapper;

import java.util.List;
import java.util.Optional;


@Component
public class StudentQueryServiceImpl implements StudentQueryService{
    StudentRepository studentRepository;
    StudentMapper studentMapper;
    public StudentQueryServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;

    }


    @Override
    public List<StudentResponse> getAllStudents() {
        List<Student> s = studentRepository.findAll();
       return s.stream().map(student -> studentMapper.toDto(student)).toList();
    }

    @Override
    public Optional<StudentResponse> getStudentByEmail(String email) {
//        return studentRepository.findByEmail(email).map(student -> studentMapper.toDto(student));
        return null;
    }

    @Override
    public Optional<StudentResponse> getStudentByFirstNameAndLastName(String firstName, String lastName) {
//        return studentRepository.findByFirstNameAndLastName(firstName,lastName).map(student -> studentMapper.toDto(student));
        return null;
    }

    @Override
    public List<StudentResponse> getStudentsByFirstName(String firstName) {
        return studentRepository.findStudentByFirstName(firstName)
                .stream()
                .map(s->studentMapper.toDto(s))
                .toList();
    }
}
