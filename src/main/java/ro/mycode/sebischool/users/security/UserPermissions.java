package ro.mycode.sebischool.users.security;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum UserPermissions {
    USER_EDIT("USER_EDIT"),
    USER_DELELTE("USER_DELELTE"),
    USER_ADD("USER_ADD"),
    COURSE_VIEW("COURSE_VIEW"),
    COURSE_MANAGE("COURSE_MANAGE"),
    ENROL_SELF("ENROL_SELF");

    private final String permission;
    public String getPermission() {
        return permission;
    }
}
