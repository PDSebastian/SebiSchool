package ro.mycode.sebischool.student.service.queryService;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.books.service.dtos.BookResponse;
import ro.mycode.sebischool.books.service.mapper.BookMapper;
import ro.mycode.sebischool.course.service.dtos.CourseResponse;
import ro.mycode.sebischool.course.service.mapper.CourseMapper;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.mapper.StudentMapperListTODetail;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.student.dtos.StudentDetailResponse;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.mapper.StudentMapper;

import java.util.List;
import java.util.Optional;


@Component
public class StudentQueryServiceImpl implements StudentQueryService{
    private final CourseMapper courseMapper;
    private final BookMapper bookMapper;
   StudentMapperListTODetail studentMapperListTODetail;
    StudentRepository studentRepository;
    StudentMapper studentMapper;
    public StudentQueryServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper, CourseMapper courseMapper, BookMapper bookMapper,
                                   StudentMapperListTODetail studentMapperListTODetail) {


        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.courseMapper = courseMapper;
        this.bookMapper = bookMapper;
        this.studentMapperListTODetail = studentMapperListTODetail;
    }


    @Override
    public List<StudentSummaryResponse> getAllStudents() {
        List<Student> s = studentRepository.findAll();
       return s.stream().map(student -> studentMapper.toDto(student)).toList();
    }

    @Override
    public Optional<StudentSummaryResponse> getStudentByEmail(String email) {
//        return studentRepository.findByEmail(email).map(student -> studentMapper.toDto(student));
        return null;
    }

    @Override
    public Optional<StudentSummaryResponse> getStudentByFirstNameAndLastName(String firstName, String lastName) {
//        return studentRepository.findByFirstNameAndLastName(firstName,lastName).map(student -> studentMapper.toDto(student));
        return null;
    }

    @Override
    public StudentDetailResponse getCourseAndBooksByStudentID(Long studentID) {
        Student s=studentRepository.findById(studentID).orElseThrow(()->new StudentNotFoundException("Student not found"));
        return studentMapperListTODetail.toDto(s);

    }

    @Override
    public List<StudentSummaryResponse> getStudentsByFirstName(String firstName) {
        return studentRepository.findStudentByFirstName(firstName)
                .stream()
                .map(s->studentMapper.toDto(s))
                .toList();
    }





}
