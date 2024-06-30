package com.sage.reactive_crud_sample.objects.security;

import com.sage.reactive_crud_sample.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class BasicUserDetails implements UserDetails {

    private User user;

    public BasicUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Role.USER.name());
        return List.of(authority);
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
