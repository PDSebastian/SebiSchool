package ro.mycode.sebischool.users.security;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserPermissions {
    USER_EDIT("USER_EDIT"),
    USER_DELELTE("USER_DELELTE"),
    USER_ADD("USER_ADD");

    private String permission;
    public String getPermission() {
        return permission;
    }
}
