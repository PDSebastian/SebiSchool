    package ro.mycode.sebischool.professor.service;

    import ro.mycode.sebischool.professor.dtos.ProfessorResponse;

    import java.util.List;

    public interface ProfessorQueryService {
        List<ProfessorResponse> getAllProfessors();
        ProfessorResponse getProfessorById(Long id);
        ProfessorResponse getProfessorByFirstAndLastName(String firstName, String lastName);
    }
