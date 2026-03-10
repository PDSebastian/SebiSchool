package ro.mycode.sebischool.student.service.commandService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.student.exceptions.StudentAlreadyExistsException;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.student.service.dtos.StudentPatchRequest;
import ro.mycode.sebischool.student.service.dtos.StudentRequest;
import ro.mycode.sebischool.student.service.dtos.StudentResponse;
import ro.mycode.sebischool.student.service.mapper.StudentMapper;

@Component
public class StudentCommandServiceImpl implements StudentCommandService {
    StudentRepository studentRepository;
    StudentMapper studentMapper;

    StudentCommandServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }


    @Override
    @Transactional
    public StudentResponse addStudent(StudentRequest studentRequest) {
        studentRepository.findByEmail(studentRequest.getEmail()).ifPresent(student -> {
            throw new StudentAlreadyExistsException("Student already exists");
        });

        Student student = studentMapper.toEntity(studentRequest);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDto(savedStudent);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest studentRequest) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        s.setFirstName(studentRequest.getFirstName());
        s.setLastName(studentRequest.getLastName());
        s.setEmail(studentRequest.getEmail());
        s.setAge(studentRequest.getAge());
        studentRepository.save(s);
        return studentMapper.toDto(s);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student not found");
        }
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public StudentResponse updatePatchStudent(Long id, StudentPatchRequest studentRequest) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        if (studentRequest.getFirstName() != null) {
            s.setFirstName(studentRequest.getFirstName());
        }

        if (studentRequest.getLastName() != null) {
            s.setLastName(studentRequest.getLastName());
        }

        if (studentRequest.getEmail() != null) {
            s.setEmail(studentRequest.getEmail());
        }

        if (studentRequest.getAge() != null) {
            s.setAge(studentRequest.getAge());
        }
        studentRepository.save(s);
        return studentMapper.toDto(s);
    }
}
