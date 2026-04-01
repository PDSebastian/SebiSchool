package ro.mycode.sebischool.student.service.commandService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.student.exceptions.InvalidStudentAgeException;
import ro.mycode.sebischool.student.exceptions.StudentAlreadyExistsException;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.student.dtos.StudentPatchRequest;
import ro.mycode.sebischool.student.dtos.StudentRequest;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.mapper.StudentMapper;

@Component
public class StudentCommandServiceImpl implements StudentCommandService {
    StudentRepository studentRepository;


    StudentCommandServiceImpl(StudentRepository studentRepository ) {
        this.studentRepository = studentRepository;
    }


    @Override
    @Transactional
    public StudentSummaryResponse addStudent(StudentRequest studentRequest) {
        studentRepository.findByEmail(studentRequest.getEmail()).ifPresent(student -> {
            throw new StudentAlreadyExistsException();
        });
        if(studentRequest.getAge()>100){
            throw new InvalidStudentAgeException();
        }

        Student student = StudentMapper.StudentRequesttoStudent(studentRequest);
        Student savedStudent = studentRepository.save(student);
        return StudentMapper.StudentToStudentSummaryResponse(savedStudent);
    }

    @Override
    @Transactional
    public StudentSummaryResponse updateStudent( StudentRequest studentRequest) {
        Student s = studentRepository.findByEmail(studentRequest.getEmail())
                .orElseThrow(() -> new StudentNotFoundException());
        s.setFirstName(studentRequest.getFirstName());
        s.setLastName(studentRequest.getLastName());
        s.setEmail(studentRequest.getEmail());
        s.setAge(studentRequest.getAge());
        studentRepository.save(s);
        return StudentMapper.StudentToStudentSummaryResponse(s);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException();
        }
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public StudentSummaryResponse updatePatchStudent(Long id, StudentPatchRequest studentRequest) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException());

        if (studentRequest.firstName() != null) {
            s.setFirstName(studentRequest.firstName());
        }

        if (studentRequest.lastName() != null) {
            s.setLastName(studentRequest.lastName());
        }

        if (studentRequest.email() != null) {
            s.setEmail(studentRequest.email());
        }

        if (studentRequest.age() != null) {
            s.setAge(studentRequest.age());
        }
        studentRepository.save(s);
        return StudentMapper.StudentToStudentSummaryResponse(s);
    }
}
