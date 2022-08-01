package com.kantboot.system.user.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultPasswordEncoder {

    /**
     * 进行BCrypt加密
     */
    public String encode(String charSequence) {
        return new BCryptPasswordEncoder().encode(charSequence.toString());
    }

    /**
     * 进行密码对比
     */
    public boolean matches(String charSequence, String encodePassword) {
        return new BCryptPasswordEncoder().matches(charSequence,encodePassword);
    }

}
