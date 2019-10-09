package com.thanhld.server959.controller;

import com.thanhld.server959.exception.ResourceNotFoundException;
import com.thanhld.server959.model.User;
import com.thanhld.server959.repository.UserRepository;
import com.thanhld.server959.security.CurrentUser;
import com.thanhld.server959.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal){
        return userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User","id",userPrincipal.getId()));
    }
}
