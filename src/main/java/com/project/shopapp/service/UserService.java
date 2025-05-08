package com.project.shopapp.service;

import com.project.shopapp.dto.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.Role;
import com.project.shopapp.model.User;
import com.project.shopapp.respository.RoleRespository;
import com.project.shopapp.respository.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRespository userRespository;
    private final RoleRespository roleRespository;
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
//                String encoded = Base64.getEncoder().encodeToString(password.getBytes());
//                user.setPassword(encoded);
            }
            return userRespository.save(user);
    }

    @Override
    public String login(String phoneNumber, String password) {
        // lien quan den security doi
        return null;
    }
}
