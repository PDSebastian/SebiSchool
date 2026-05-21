package ro.mycode.sebischool.auth.authService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.mycode.sebischool.auth.dtos.UserLoginrequest;
import ro.mycode.sebischool.users.dtos.UserRequest;
import ro.mycode.sebischool.users.dtos.UserResponse;
import ro.mycode.sebischool.users.exceptions.UserAlreadyExistsException;
import ro.mycode.sebischool.users.exceptions.UserNotFoundException;
import ro.mycode.sebischool.system.jwt.JWTTokenProvider;
import ro.mycode.sebischool.users.mapper.UserMapper;
import ro.mycode.sebischool.users.model.User;
import ro.mycode.sebischool.users.repository.Userrepository;
import ro.mycode.sebischool.system.security.UserPermissions;

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
    public UserResponse login(UserLoginrequest userLoginrequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginrequest.email(), userLoginrequest.password())
        );

        User user = userRepository.findByEmail(userLoginrequest.email())
                .orElseThrow(() -> new UserNotFoundException());

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                jwtTokenProvider.generateToken(user)
        );
    }

    @Override
    public UserResponse register(UserRequest request) {
        if(userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPermissions(Set.of(UserPermissions.USER_ADD, UserPermissions.USER_DELETE));
        User saved=userRepository.save(user);
        return UserMapper.toDTO(saved,jwtTokenProvider.generateToken(user));
    }
}
