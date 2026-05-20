package ro.mycode.sebischool.auth.authService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.auth.dtos.AuthLoginRequest;
import ro.mycode.sebischool.auth.dtos.AuthLoginResponse;
import ro.mycode.sebischool.auth.dtos.AuthRegisterRequest;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.users.dtos.UserResponse;
import ro.mycode.sebischool.users.exceptions.UserAlreadyExistsException;
import ro.mycode.sebischool.users.exceptions.UserNotFoundException;
import ro.mycode.sebischool.users.jwt.JWTTokenProvider;
import ro.mycode.sebischool.users.model.User;
import ro.mycode.sebischool.users.repository.Userrepository;
import ro.mycode.sebischool.users.security.UserPermissions;

import java.util.Set;

@Component
public class AuthServiceImpl implements AuthService {
    private final Userrepository userRepository;
    private final StudentRepository studentRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    public  AuthServiceImpl(Userrepository userRepository,
                            StudentRepository studentRepository,
                            AuthenticationManager authenticationManager,
                            JWTTokenProvider jwtTokenProvider,
                            PasswordEncoder passwordEncoder)
    {

        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AuthLoginResponse login(AuthLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException());

        return new AuthLoginResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPermissions(),
                jwtTokenProvider.generateToken(user)
        );
    }

    @Override
    @Transactional
    public UserResponse register(AuthRegisterRequest request) {
        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new UserAlreadyExistsException();
        }

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(StudentNotFoundException::new);

        if (!student.getEmail().equals(request.email())) {
            throw new IllegalStateException("Emailul userului trebuie sa fie acelasi cu emailul studentului.");
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .build();

        user.setPassword(passwordEncoder.encode(request.password()));

        user.setPermissions(Set.of(UserPermissions.USER_ADD, UserPermissions.USER_DELELTE, UserPermissions.USER_EDIT));
        User savedUser = userRepository.save(user);

        linkStudentToUser(student, savedUser);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail()
        );
    }

    private void linkStudentToUser(Student student, User user) {
        if (student.getUser() != null && !student.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Studentul este deja asociat altui user.");
        }

        student.setUser(user);
        studentRepository.save(student);
    }
}
