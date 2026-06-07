package ro.mycode.sebischool.professor.service;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.professor.dtos.ProfessorResponse;
import ro.mycode.sebischool.professor.exceptions.ProfessorNotFoundException;
import ro.mycode.sebischool.professor.mapper.ProfessorMapper;
import ro.mycode.sebischool.professor.model.Professor;
import ro.mycode.sebischool.professor.repository.ProfessorRepository;

import java.util.List;
@Component
public class ProfessorQueryServiceImpl implements ProfessorQueryService{

    private final ProfessorRepository professorRepository;

    public ProfessorQueryServiceImpl(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public List<ProfessorResponse> getAllProfessors() {
        return professorRepository.findAll()
                .stream()
                .map(ProfessorMapper::toDto)
                .toList();
    }

    @Override
    public ProfessorResponse getProfessorById(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException());
        return ProfessorMapper.toDto(professor);
    }

    @Override
    public ProfessorResponse getProfessorByFirstAndLastName(String firstName, String lastName) {
        Professor professor = professorRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new ProfessorNotFoundException());
        return ProfessorMapper.toDto(professor);
    }
}
