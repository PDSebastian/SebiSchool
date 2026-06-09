package ro.mycode.sebischool.auth.authService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.auth.dtos.AuthLoginRequest;
import ro.mycode.sebischool.auth.dtos.AuthLoginResponse;
import ro.mycode.sebischool.auth.dtos.AuthRegisterRequest;
import ro.mycode.sebischool.auth.exceptions.AuthValidationException;
import ro.mycode.sebischool.professor.model.Professor;
import ro.mycode.sebischool.professor.repository.ProfessorRepository;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.users.dtos.UserResponse;
import ro.mycode.sebischool.users.exceptions.UserAlreadyExistsException;
import ro.mycode.sebischool.users.exceptions.UserNotFoundException;
import ro.mycode.sebischool.users.jwt.JWTTokenProvider;
import ro.mycode.sebischool.users.model.User;
import ro.mycode.sebischool.users.model.UserType;
import ro.mycode.sebischool.users.repository.UserRepository;
import ro.mycode.sebischool.users.security.UserPermissions;

import java.util.Set;

@Component
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    public  AuthServiceImpl(UserRepository userRepository,
                            StudentRepository studentRepository,
                            ProfessorRepository professorRepository,
                            AuthenticationManager authenticationManager,
                            JWTTokenProvider jwtTokenProvider,
                            PasswordEncoder passwordEncoder)
    {

        this.userRepository = userRepository;
        this.professorRepository = professorRepository;
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
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .userType(request.userType())
                .build();

        user.setPermissions(permissionsForType(request.userType()));
        User savedUser = userRepository.save(user);

        if (request.userType() == UserType.STUDENT) {
            createStudentForUser(savedUser, request);
        }

        if (request.userType() == UserType.PROFESSOR) {
            createProfessorForUser(savedUser, request);
        }

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail()
        );
    }

    private void createStudentForUser(User user, AuthRegisterRequest request) {
        if (request.age() == null) {
            throw new AuthValidationException("age is required when registering as STUDENT");
        }
        Student student = Student.builder()
                .age(request.age())
                .user(user)
                .build();
        studentRepository.save(student);
    }

    private void createProfessorForUser(User user, AuthRegisterRequest request) {
        if(request.specialty()==null|| request.departament()==null) {
            throw new AuthValidationException("speciality or departament is required");
        }
        Professor professor = Professor.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .specialty(request.specialty())
                .departament(request.departament())
                .yearExperience(request.yearExperience())
                .user(user)
                .build();

        professorRepository.save(professor);


    }


    private Set<UserPermissions> permissionsForType(UserType type) {
        if (type == UserType.PROFESSOR) {
            return Set.of(
                    UserPermissions.USER_VIEW,
                    UserPermissions.USER_EDIT,
                    UserPermissions.COURSE_VIEW,
                    UserPermissions.COURSE_MANAGE,
                    UserPermissions.STUDENT_VIEW
            );
        }
        return Set.of(
                UserPermissions.USER_VIEW,
                UserPermissions.USER_EDIT,
                UserPermissions.COURSE_VIEW,
                UserPermissions.ENROL_SELF,
                UserPermissions.STUDENT_VIEW
        );
    }
}
