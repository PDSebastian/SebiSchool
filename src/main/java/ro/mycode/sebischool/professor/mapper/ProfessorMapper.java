package ro.mycode.sebischool.professor.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.mycode.sebischool.professor.dtos.ProfessorResponse;
import ro.mycode.sebischool.professor.dtos.ProfessorSummaryResponse;
import ro.mycode.sebischool.professor.model.Professor;

@Slf4j
@Component
public class ProfessorMapper {





    public static ProfessorSummaryResponse toSummary(Professor professor) {
        if(professor == null) return null;

        return new ProfessorSummaryResponse(
                professor.getId(),
                professor.getUser().getFirstName(),
                professor.getUser().getLastName(),
                professor.getUser().getEmail(),
                professor.getSpecialty(),
                professor.getDepartament(),
                professor.getYearExperience(),
               professor.getCourses()





        );


    }
    public static ProfessorResponse toWithCourses(Professor professor) {
        if(professor == null) return null;
        return new ProfessorResponse(
                professor.getId(),
                professor.getUser().getFirstName(),
                professor.getUser().getLastName(),
                professor.getUser().getEmail(),
                professor.getSpecialty(),
                professor.getDepartament(),
                professor.getYearExperience(),
                professor.getCourses()
        );
    }


}
