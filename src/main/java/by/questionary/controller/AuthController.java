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

        log.info("New SignIn request");

        String loginName = loginRequest.getName();
        String loginPassword = loginRequest.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginName, loginPassword);

        log.info("Token is created - {}", token);

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        log.info("JWT is created - {}", jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        long id = userDetails.getId();
        String name = userDetails.getUsername();
        String email = userDetails.getEmail();
        boolean activated = userDetails.isEnabled();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse(jwt, id, email, name, activated, roles);

        log.info("JWT response is sent");

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signUp")
    public ResponseEntity<MessageResponse> registerUser(
            @Valid
            @RequestBody SignupRequest signUpRequest) {

        log.info("New SignUp request");

        String signUpUserName = signUpRequest.getName();
        String signUpUserEmail = signUpRequest.getEmail();
        String signUpUserPassword = signUpRequest.getPassword();
        String signUpUserRepeatedPassword = signUpRequest.getRepeatedPassword();

        boolean existedUserByName = userServiceImpl.existsUserByName(signUpUserName);
        boolean existedUserByEmail = userServiceImpl.existsUserByEmail(signUpUserEmail);

        if (existedUserByName) {

            log.warn("User with the same name is existed. Name - {}", signUpRequest.getName());

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (existedUserByEmail) {

            log.warn("User with the same email is existed. Email - {}", signUpRequest.getEmail());

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (!userServiceImpl.comparePasswords(signUpUserPassword, signUpUserRepeatedPassword)) {

            log.warn("Passwords are different");

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Passwords mustn't be different!"));
        }

        User user = userServiceImpl.createUserByRequest(signUpRequest);
        userServiceImpl.prepareUserToSaving(user);
        userServiceImpl.saveUser(user);
        mailSenderServiceImpl.sendActivationMail(user);

        log.info("User \" {} \" has been saved", signUpRequest.getName());

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<MessageResponse> activate(
            @PathVariable String code) {

        log.info("New Activation request. Code - {}", code);

        boolean activated = userServiceImpl.activateUser(code);

        if (activated){
            return ResponseEntity.ok(new MessageResponse("User activated successfully !"));
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Activation code is not found!"));
    }

//    @PostMapping("/resetPassword")
//    public
//
//    @PostMapping("")
}