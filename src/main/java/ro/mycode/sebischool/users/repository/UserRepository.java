package ro.mycode.sebischool.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ro.mycode.sebischool.users.model.User;

import java.util.Optional;

public interface Userrepository  extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);


}
