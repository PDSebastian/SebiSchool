package ro.mycode.sebischool.professor.service;

import ro.mycode.sebischool.professor.dtos.ProfessorRequest;
import ro.mycode.sebischool.professor.dtos.ProfessorResponse;

public interface ProfessorCommandService {
    ProfessorResponse addProfessor(ProfessorRequest professorRequest);
    ProfessorResponse updateProfessor(Long id, ProfessorRequest professorRequest);
    void deleteProfessor(Long id);








}
