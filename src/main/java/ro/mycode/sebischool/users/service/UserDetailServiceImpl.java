package ro.mycode.sebischool.users.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ro.mycode.sebischool.users.exceptions.UserNotFoundException;
import ro.mycode.sebischool.users.repository.Userrepository;
@Component
public class UserDetailServiceImpl implements UserDetailsService {

  private  Userrepository  userrepository;
    public UserDetailServiceImpl(Userrepository userrepository) {
        this.userrepository = userrepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) userrepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException());


    }
}
