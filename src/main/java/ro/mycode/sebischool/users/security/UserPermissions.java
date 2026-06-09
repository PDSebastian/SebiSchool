package ro.mycode.sebischool.users.security;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum UserPermissions {
    USER_EDIT("USER_EDIT"),
    USER_VIEW("USER_VIEW"),
    USER_DELETE("USER_DELETE"),
    USER_ADD("USER_ADD"),
    COURSE_VIEW("COURSE_VIEW"),
    COURSE_MANAGE("COURSE_MANAGE"),
    ENROL_SELF("ENROL_SELF"),
    STUDENT_VIEW("STUDENT_VIEW");

    private final String permission;
    public String getPermission() {
        return permission;
    }
}
