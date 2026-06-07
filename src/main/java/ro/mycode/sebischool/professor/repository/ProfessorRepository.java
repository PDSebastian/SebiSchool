package ro.mycode.sebischool.professor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.mycode.sebischool.professor.dtos.ProfessorSummaryResponse;
import ro.mycode.sebischool.professor.model.Professor;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByFirstNameAndLastName(String firstName, String lastName);



}
