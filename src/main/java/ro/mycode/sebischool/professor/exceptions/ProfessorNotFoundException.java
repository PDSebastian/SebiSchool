package ro.mycode.sebischool.professor.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class ProfessorNotFoundException extends RuntimeException {
    public ProfessorNotFoundException() {
        super(ErrorConstants.PROFESSOR_NOT_FOUND_ERROR );
    }
}
