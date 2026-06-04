package ro.mycode.sebischool.professor.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.professor.dtos.ProfessorRequest;
import ro.mycode.sebischool.professor.dtos.ProfessorResponse;
import ro.mycode.sebischool.professor.exceptions.ProfessorAlreadyExistsException;
import ro.mycode.sebischool.professor.exceptions.ProfessorNotFoundException;
import ro.mycode.sebischool.professor.mapper.ProfessorMapper;
import ro.mycode.sebischool.professor.model.Professor;
import ro.mycode.sebischool.professor.repository.ProfessorRepository;
import ro.mycode.sebischool.users.model.User;
import ro.mycode.sebischool.users.repository.UserRepository;

import java.util.Optional;

@Component
public class ProfessorCommandServiceImpl implements ProfessorCommandService {
    private ProfessorRepository professorRepository;
    private UserRepository userRepository;
    public ProfessorCommandServiceImpl(ProfessorRepository professorRepository, UserRepository userRepository) {
        this.professorRepository = professorRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ProfessorResponse addProfessor(ProfessorRequest professorRequest) {
        Optional<Professor> professor=professorRepository.findByFirstNameAndLastName(professorRequest.firstName(),professorRequest.lastName());
        if(professor!=null){
            new ProfessorAlreadyExistsException();
        }
        Professor p=ProfessorMapper.toEntity(professorRequest);
        p=professorRepository.save(p);
        return ProfessorMapper.toDto(p);
    }

    @Override
    @Transactional
    public ProfessorResponse updateProfessor(Long id,ProfessorRequest professorRequest) {
       Professor professor=professorRepository.findById(id).get();
       professor.setFirstName(professorRequest.firstName());
       professor.setLastName(professorRequest.lastName());
       professor.setDepartament(professorRequest.departament());
       professor.setSpecialty(professorRequest.specialty());
       professorRepository.save(professor);
       return ProfessorMapper.toDto(professor);
    }

    @Override
    @Transactional
    public void deleteProfessor(Long id) {
        Professor professor=professorRepository.findById(id).orElseThrow(()->new ProfessorNotFoundException());
        professorRepository.delete(professor);

    }
}
