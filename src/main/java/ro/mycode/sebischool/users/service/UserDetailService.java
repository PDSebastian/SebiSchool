package ro.mycode.sebischool.users.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.mycode.sebischool.users.repository.Userrepository;

public interface UserDetailService {
    UserDetails loadUserByUsername(String email);
}
