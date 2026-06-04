package ro.mycode.sebischool.student.service.commandService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.student.exceptions.InvalidStudentAgeException;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.student.dtos.StudentPatchRequest;
import ro.mycode.sebischool.student.dtos.StudentRequest;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.mapper.StudentMapper;
import ro.mycode.sebischool.users.model.User;

@Component
public class StudentCommandServiceImpl implements StudentCommandService {
    StudentRepository studentRepository;


    public StudentCommandServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Override
    @Transactional
    public StudentSummaryResponse addStudent(StudentRequest studentRequest) {
        // Studentii se creeaza acum prin POST /api/v2/auth/register (cu userType=STUDENT).
        // Acest endpoint nu mai are sens — identitatea (firstName/lastName/email) traieste pe User,
        // iar User-ul are nevoie de parola la creare.
        throw new UnsupportedOperationException("Use POST /api/v2/auth/register with userType=STUDENT");
    }

    @Override
    @Transactional
    public StudentSummaryResponse updateStudent(Long id, StudentRequest studentRequest) {
        Student s = studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
        if (studentRequest.getAge() < 0 || studentRequest.getAge() > 100){
            throw new InvalidStudentAgeException();

        }

        User u = s.getUser();
        u.setFirstName(studentRequest.getFirstName());
        u.setLastName(studentRequest.getLastName());
        u.setEmail(studentRequest.getEmail());
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
                .orElseThrow(StudentNotFoundException::new);

        User u = s.getUser();
        if (studentRequest.firstName() != null) {
            u.setFirstName(studentRequest.firstName());
        }
        if (studentRequest.lastName() != null) {
            u.setLastName(studentRequest.lastName());
        }
        if (studentRequest.email() != null) {
            u.setEmail(studentRequest.email());
        }
        if (studentRequest.age() != null) {
            s.setAge(studentRequest.age());
        }

        studentRepository.save(s);
        return StudentMapper.StudentToStudentSummaryResponse(s);
    }
}
