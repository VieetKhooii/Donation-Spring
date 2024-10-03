package com.gabriel.donation.security;

import com.gabriel.donation.entity.Role;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;

    @Autowired
    public CustomUserDetailsService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepo.findByPhone(phone).orElseThrow(()-> new UsernameNotFoundException("Phone not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getPhone(),
                user.getPassword(),
                mapRoleToAuthorities(user.getRole())
        );    }

    private Collection<GrantedAuthority> mapRoleToAuthorities(Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getName()));
    }
}
