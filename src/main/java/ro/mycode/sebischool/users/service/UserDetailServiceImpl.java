package ro.mycode.sebischool.users.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ro.mycode.sebischool.users.repository.UserRepository;
@Component
public class UserDetailServiceImpl implements UserDetailsService {

  private UserRepository userrepository;
    public UserDetailServiceImpl(UserRepository userrepository) {
        this.userrepository = userrepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userrepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


    }
}
