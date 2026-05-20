package ro.mycode.sebischool.auth.authService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.mycode.sebischool.student.dtos.StudentDetailResponse;
import ro.mycode.sebischool.student.dtos.StudentRequest;
import ro.mycode.sebischool.auth.dtos.AuthLoginRequest;
import ro.mycode.sebischool.auth.dtos.AuthLoginResponse;
import ro.mycode.sebischool.users.dtos.UserRequest;
import ro.mycode.sebischool.users.dtos.UserResponse;
import ro.mycode.sebischool.users.exceptions.UserAlreadyExistsException;
import ro.mycode.sebischool.users.exceptions.UserNotFoundException;
import ro.mycode.sebischool.users.jwt.JWTTokenProvider;
import ro.mycode.sebischool.users.mapper.UserMapper;
import ro.mycode.sebischool.users.model.User;
import ro.mycode.sebischool.users.repository.Userrepository;
import ro.mycode.sebischool.users.security.UserPermissions;

import java.util.Set;

@Component
public class AuthServiceImpl implements AuthService {
    private final Userrepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    public  AuthServiceImpl(Userrepository userRepository, UserMapper userMapper, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder)
    {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AuthLoginResponse login(UserRequest request) {
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
    public UserResponse register(UserRequest request) {
        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new UserAlreadyExistsException();
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));

        user.setPermissions(Set.of(UserPermissions.USER_ADD, UserPermissions.USER_DELELTE, UserPermissions.USER_EDIT));
        return UserMapper.toDTO(userRepository.save(user));
    }
}
