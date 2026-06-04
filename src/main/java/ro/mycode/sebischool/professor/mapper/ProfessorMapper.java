package ro.mycode.sebischool.professor.mapper;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.professor.dtos.ProfessorRequest;
import ro.mycode.sebischool.professor.dtos.ProfessorResponse;
import ro.mycode.sebischool.professor.model.Professor;

@Component
public class ProfessorMapper {
    public static ProfessorResponse toDto(Professor professor) {
        if (professor == null) {
            return null;
        }
        return  ProfessorResponse.builder()
                .id(professor.getId())
                .firstName(professor.firstName)
                .lastName(professor.lastName)
                .specialty(professor.specialty)
                .departament(professor.getDepartament())
                .build();
    }
    public static Professor toEntity(ProfessorRequest professorRequest) {
        if (professorRequest == null) {
            return null;
        }
        return Professor.builder()
                .firstName(professorRequest.firstName())
                .lastName(professorRequest.lastName())
                .specialty(professorRequest.specialty())
                .departament(professorRequest.departament())
                .yearExperience(professorRequest.yearExperience())
                .build();
    }
}
