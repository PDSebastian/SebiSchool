    package ro.mycode.sebischool.professor.service;

    import org.springframework.transaction.annotation.Transactional;
    import ro.mycode.sebischool.professor.dtos.ProfessorResponse;

    import java.util.List;
@Transactional(readOnly = true)
    public interface ProfessorQueryService {
        List<ProfessorResponse> getAllProfessors();
        ProfessorResponse getProfessorById(Long id);

    }
