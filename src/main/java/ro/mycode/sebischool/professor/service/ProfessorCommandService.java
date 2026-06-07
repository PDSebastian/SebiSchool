package ro.mycode.sebischool.professor.service;

import ro.mycode.sebischool.professor.dtos.ProfessorPatchRequest;
import ro.mycode.sebischool.professor.dtos.ProfessorRequest;
import ro.mycode.sebischool.professor.dtos.ProfessorResponse;
import ro.mycode.sebischool.professor.dtos.ProfessorSummaryResponse;

public interface ProfessorCommandService {
    ProfessorResponse addProfessor(ProfessorRequest professorRequest);
    ProfessorResponse updateProfessor(Long id, ProfessorRequest professorRequest);
    ProfessorResponse patchProfessor (Long id, ProfessorPatchRequest  professorPatchRequest);
    void deleteProfessor(Long id);
    ProfessorResponse assignCourse (Long id,Long courseId);
    ProfessorResponse unassignCourse (Long id,Long courseId);








}
