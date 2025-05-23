package com.project.shopapp.service;

import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.dto.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.Role;
import com.project.shopapp.model.User;
import com.project.shopapp.respository.RoleRespository;
import com.project.shopapp.respository.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRespository userRespository;
    private final RoleRespository roleRespository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        // check phonenumber if it exists
            if(userRespository.existsByPhoneNumber(phoneNumber)) {
            throw new DataNotFoundException("Phone number already exists");
        }
        // convert userDTO => user
            User user = User.builder()
                    .fullName(userDTO.getFullName())
                    .phoneNumber(userDTO.getPhoneNumber())
                    .password(userDTO.getPassword())
                    .address(userDTO.getAddress())
                    .dateOfBirth(userDTO.getDateOfBirth())
                    .facebookAccountId(userDTO.getFacebookAcountId())
                    .googleAccountId(userDTO.getGoogleAcountId())
                    .build();
            Role role = roleRespository.findById(userDTO.getRoleId()).orElseThrow(()->new DataNotFoundException("Role not found"));
            user.setRole(role);
            if(userDTO.getGoogleAcountId() == 0 && userDTO.getFacebookAcountId() == 0) {
                String password = userDTO.getPassword();
                String encodedPassword = passwordEncoder.encode(password);
                user.setPassword(encodedPassword);
            }
            return userRespository.save(user);
    }

    @Override
    public String login(String phoneNumber, String password) throws DataNotFoundException {
        Optional<User> existingUser = userRespository.findByPhoneNumber(phoneNumber);
        if(existingUser.isEmpty()){
            throw new DataNotFoundException("Invalid PhoneNumber/Password");
        }
        User user = existingUser.get();
        if(user.getGoogleAccountId() == 0 && user.getFacebookAccountId() == 0) {
            if(!passwordEncoder.matches(password, user.getPassword())){
                throw new DataNotFoundException("Invalid Password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), password);
        //authenticate w Java Spring
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(user);
    }
}
