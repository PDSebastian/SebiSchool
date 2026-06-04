package ro.mycode.sebischool.professor.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class ProfessorAlreadyExistsException extends RuntimeException {
    public ProfessorAlreadyExistsException() {
        super(ErrorConstants.PROFESSOR_ALREADY_EXISTS_ERROR);
    }
}
