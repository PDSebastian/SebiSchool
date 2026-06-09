package ro.mycode.sebischool.professor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.mycode.sebischool.professor.dtos.ProfessorSummaryResponse;
import ro.mycode.sebischool.professor.model.Professor;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findBySpecialty(String specialty);
    Optional<Professor>  findByUserEmail(String email);


}
