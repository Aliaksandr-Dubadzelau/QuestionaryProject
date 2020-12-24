package by.questionary.controller;

import by.questionary.domain.User;
import by.questionary.security.entity.UserDetailsImpl;
import by.questionary.security.jwt.JwtUtils;
import by.questionary.security.payload.request.LoginRequest;
import by.questionary.security.payload.request.SignupRequest;
import by.questionary.security.payload.response.JwtResponse;
import by.questionary.security.payload.response.MessageResponse;
import by.questionary.service.impl.MailSenderServiceImpl;
import by.questionary.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/authentication")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final MailSenderServiceImpl mailSenderServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final JwtUtils jwtUtils;

    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> authenticateUser(
            @Valid
            @RequestBody LoginRequest loginRequest) {

        log.info("/signIn loginRequest - {}", loginRequest);

        String loginName = loginRequest.getName();
        String loginPassword = loginRequest.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginName, loginPassword);

        log.info("loginRequest - {}, token - {}", loginRequest, token);

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        log.info("loginRequest - {}, jwt - {}", loginRequest, jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        long id = userDetails.getId();
        String name = userDetails.getUsername();
        String email = userDetails.getEmail();
        boolean activated = userDetails.isEnabled();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse(jwt, id, name, email, activated, roles);

        log.info("loginRequest - {}, jwtResponse - {}", loginRequest, jwtResponse);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signUp")
    public ResponseEntity<MessageResponse> registerUser(
            @Valid
            @RequestBody SignupRequest signUpRequest) {

        log.info("/signUp signUpRequest - {}", signUpRequest);

        String signUpUserName = signUpRequest.getName();
        String signUpUserEmail = signUpRequest.getEmail();
        String signUpUserPassword = signUpRequest.getPassword();
        String signUpUserRepeatedPassword = signUpRequest.getRepeatedPassword();

        boolean existedUserByName = userServiceImpl.existsUserByName(signUpUserName);
        boolean existedUserByEmail = userServiceImpl.existsUserByEmail(signUpUserEmail);

        if (existedUserByName) {

            log.warn("signUpRequest - {}. User with the same name is existed", signUpRequest);

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (existedUserByEmail) {

            log.warn("signUpRequest - {}. User with the same email is existed", signUpRequest);

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (userServiceImpl.comparePasswords(signUpUserPassword, signUpUserRepeatedPassword)) {

            log.warn("signUpRequest - {}. Passwords are different", signUpRequest);

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Passwords mustn't be different!"));
        }

        User user = userServiceImpl.createUserByRequest(signUpRequest);
        userServiceImpl.prepareUserToSaving(user);
        userServiceImpl.saveUser(user);
        mailSenderServiceImpl.sendActivationMail(user);

        log.info("loginRequest - {}. User has been saved", signUpRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}