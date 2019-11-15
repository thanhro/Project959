package com.thanhld.server959.web.rest.errors.util;

import com.thanhld.server959.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {
    public static Optional<UserPrincipal> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof UserPrincipal) {
                        return (UserPrincipal) authentication.getPrincipal();
                    }
                    return null;
                });
    }
}
