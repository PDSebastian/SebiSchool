package ro.mycode.sebischool.professor.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.course.exceptions.CourseNotFoundException;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.professor.dtos.ProfessorPatchRequest;
import ro.mycode.sebischool.professor.dtos.ProfessorRequest;
import ro.mycode.sebischool.professor.dtos.ProfessorResponse;
import ro.mycode.sebischool.professor.exceptions.ProfessorNotFoundException;
import ro.mycode.sebischool.professor.mapper.ProfessorMapper;
import ro.mycode.sebischool.professor.model.Professor;
import ro.mycode.sebischool.professor.repository.ProfessorRepository;
import ro.mycode.sebischool.users.repository.UserRepository;

import java.util.Optional;

@Component
public class ProfessorCommandServiceImpl implements ProfessorCommandService {

    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    Course course;
    public ProfessorCommandServiceImpl(ProfessorRepository professorRepository, UserRepository userRepository,CourseRepository courseRepository) {
        this.professorRepository = professorRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public ProfessorResponse addProfessor(ProfessorRequest professorRequest) {
       throw  new UnsupportedOperationException("Use /api/v2/auth/register with userType=PROFESOR");


    }

    @Override
    @Transactional
    public ProfessorResponse updateProfessor(Long id, ProfessorRequest professorRequest) {

        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException());

        professor.setDepartament(professorRequest.departament());
        professor.setSpecialty(professorRequest.specialty());
        professor.setYearExperience(professorRequest.yearExperience());

        professorRepository.save(professor);

        return ProfessorMapper.toWithCourses(professor);
    }

    @Override
    @Transactional
    public ProfessorResponse patchProfessor(Long id, ProfessorPatchRequest professorPatchRequest) {

        Professor professor=professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException());

        professor.setSpecialty(professorPatchRequest.specialty());
        professorRepository.save(professor);
        return ProfessorMapper.toWithCourses(professor);


    }

    @Override
    @Transactional
    public void deleteProfessor(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException());

        professor.getCourses();
        course.setProfessor(null);


        professorRepository.delete(professor);
    }

    @Override
    @Transactional
    public ProfessorResponse assignCourse(Long id, Long courseId) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException());

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException());

        course.setProfessor(professor);
        professor.getCourses().add(course);
        courseRepository.save(course);
        return ProfessorMapper.toWithCourses(professor);
    }

    @Override
    @Transactional
    public ProfessorResponse unassignCourse(Long id, Long courseId) {
        Professor professor=professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException());

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException());

        course.setProfessor(null);
        professor.getCourses().remove(course);
        courseRepository.save(course);
        return ProfessorMapper.toWithCourses(professor);
    }
}
