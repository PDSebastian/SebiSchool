package ro.mycode.sebischool.system.security;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum UserPermissions {

    USER_ADD("User_Add"),
    USER_EDIT("User_Edit"),
    USER_DELETE("User_Delete"),


    COURSE_READ("COURSE_READ"),
    COURSE_WRITE("COURSE_WRITE"),
    COURSE_DELETE("COURSE_DELETE"),
    COURSE_ADD("COURSE_ADD"),
    COURSE_EDIT("COURSE_EDIT"),

    ENROLMENT_DELETE("ENROLMENT_DELETE"),
    ENROLMENT_ADD("ENTOLMENT_ADD");





    private final String permission;
    public String getPermission() {
        return permission;
    }
}
