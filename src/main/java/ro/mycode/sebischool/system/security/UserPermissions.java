package ro.mycode.sebischool.system.security;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum UserPermissions {
    USER_ADD("User_Add"),
    USER_EDIT("User_Edit"),
    USER_DELETE("User_Delete"),
    COURSE_READ("COURSE_READ"),
    COURSE_WRITE("COURSE_WRITE"),



    COURSE_DELETE("course:delete"),
    COURSE_ADD("course:add"),
    COURSE_EDIT("course:edit"),



    ENROLMENT_DELETE("ENROLMENT_DELETE"),
    ENROLMENT_ADD("ENTOLMENT_ADD");





    private final String permission;
    public String getPermission() {
        return permission;
    }
}
