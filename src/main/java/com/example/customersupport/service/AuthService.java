package com.example.customersupport.service;

import com.example.customersupport.dto.request.AgentRequestDto;
import com.example.customersupport.dto.request.LoginRequestDto;
import com.example.customersupport.dto.request.UserRequestDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.Agent;
import com.example.customersupport.entity.Customer;
import com.example.customersupport.entity.Invite;
import com.example.customersupport.entity.User;
import com.example.customersupport.enums.InviteStatus;
import com.example.customersupport.enums.Roles;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.AgentRepo;
import com.example.customersupport.repo.CustomerRepo;
import com.example.customersupport.repo.UserRepo;
import com.example.customersupport.util.AuthorityUtil;
import com.example.customersupport.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AgentRepo agentRepo;
    private final InviteService inviteService;
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final AuthorityUtil authorityUtil;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepo customerRepo;

    public GenericResponse tokenRegister(String token, @Valid AgentRequestDto agentRequestDto) {

        Invite invite = inviteService.validate(agentRequestDto.email(), token);
        log.info("registering the agent into application");
        User user = User.builder()
                .firstName(agentRequestDto.firstName())
                .lastName(agentRequestDto.lastName())
                .email(agentRequestDto.email())
                .phoneNumber(agentRequestDto.phone())
                .password(passwordEncoder.encode(agentRequestDto.password()))
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .role(Roles.AGENT)
                .build();

        Agent agent = Agent.builder()
                .user(user)
                .supportType(invite.getSupportType())
                .build();

        try {
            agentRepo.save(agent);
            inviteService.updateStatus(invite, InviteStatus.APPROVED);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "there is problem in saving the data");
        }
        log.info("registering the agent  in to the application is successful");
        return new GenericResponse(HttpStatus.CREATED, "registering the agent  in to the application is successful");
    }

    public GenericResponse register(@Valid UserRequestDto userRequestDto) {

        User user = User.builder()
                .firstName(userRequestDto.firstName())
                .lastName(userRequestDto.lastName())
                .email(userRequestDto.email())
                .phoneNumber(userRequestDto.phone())
                .password(passwordEncoder.encode(userRequestDto.password()))
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .role(Roles.CUSTOMER)
                .build();

        Customer customer = Customer.builder()
                .user(user)
                .build();
        try {
            customerRepo.save(customer);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
        return new GenericResponse(HttpStatus.CREATED, "new registration is successful");
    }

    public GenericResponse login(@Valid LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password());
        Authentication authentication = authenticationManager.authenticate(token);

        if (authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            assert user != null;
            if(user.isDeleted()) throw new CustomException(HttpStatus.UNAUTHORIZED,"invalid request");
            String result = "access token : " + jwtUtil.generateToken(loginRequestDto.email()) + "\n refresh token : " + refreshTokenService.createRefreshToken(user);
            log.info("User logged in successfully");
            return new GenericResponse(HttpStatus.OK, result);

        } else throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid username or password");


    }

    public GenericResponse logout() {
        User user = authorityUtil.checkUser();
        String result = refreshTokenService.delete(refreshTokenService.getToken(user));
        return new GenericResponse(HttpStatus.NO_CONTENT,result);
    }

    public GenericResponse signOut() {
        User user = authorityUtil.checkUser();
        user.setDeleted(true);
        logout();
        userRepo.save(user);
        return new GenericResponse(HttpStatus.NO_CONTENT,"sign out successful");
    }
}
