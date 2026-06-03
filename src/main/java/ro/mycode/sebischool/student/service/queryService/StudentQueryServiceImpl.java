package ro.mycode.sebischool.student.service.queryService;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.student.dtos.StudentDetailResponse;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.mapper.StudentMapper;

import java.util.List;
import java.util.Optional;


@Component
public class StudentQueryServiceImpl implements StudentQueryService{


    StudentRepository studentRepository;

    public StudentQueryServiceImpl(StudentRepository studentRepository) {


        this.studentRepository = studentRepository;

    }


    @Override
    public List<StudentSummaryResponse> getAllStudents() {
        List<Student> s = studentRepository.findAll();
       return s.stream().map(StudentMapper::StudentToStudentSummaryResponse).toList();
    }

    @Override
    public List<StudentSummaryResponse> getStudentByEmail(String email) {
        return studentRepository.findByUserEmail(email)
                .map(StudentMapper::StudentToStudentSummaryResponse)
                .map(List::of)
                .orElseGet(List::of);
    }

    @Override
    public Optional<StudentSummaryResponse> getStudentByFirstNameAndLastName(String firstName, String lastName) {
        // FirstName + LastName traiesc pe User. Daca e nevoie, adauga findByUserFirstNameAndUserLastName in StudentRepository.
        return Optional.empty();
    }

    @Override
    public StudentDetailResponse getStudentById(Long studentID) {
        Student s = studentRepository.findById(studentID).orElseThrow(StudentNotFoundException::new);
        return StudentMapper.StudentToStudentDetailResponse(s);
    }

    @Override
    public List<StudentSummaryResponse> getStudentsByFirstName(String firstName) {
        return studentRepository.findByUserFirstName(firstName)
                .stream()
                .map(StudentMapper::StudentToStudentSummaryResponse)
                .toList();
    }

    @Override
    public StudentSummaryResponse getStudentWithEnrolments(Long studentID) {
        Student s=studentRepository.findById(studentID).orElseThrow(()->new StudentNotFoundException());
        return StudentMapper.StudentToStudentSummaryResponse(s);
    }


}
