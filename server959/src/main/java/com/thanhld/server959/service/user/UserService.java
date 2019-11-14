package com.thanhld.server959.service.user;

import com.thanhld.server959.model.user.User;
import com.thanhld.server959.security.UserPrincipal;

public interface UserService {
    User getCurrentUser(UserPrincipal principal);
}
