package com.thanhld.server959.service.user;

import com.thanhld.server959.model.user.User;
import com.thanhld.server959.repository.UserRepository;
import com.thanhld.server959.security.UserPrincipal;
import com.thanhld.server959.web.rest.errors.BadRequestAlertException;
import com.thanhld.server959.web.rest.errors.ErrorConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User getCurrentUser(UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new BadRequestAlertException(ErrorConstants.ENTITY_NOT_FOUND, "userId not found", "userId", ErrorConstants.USER_NOT_FOUND));
    }
}
