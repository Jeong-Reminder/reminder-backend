package com.example.backend.service;


import com.example.backend.dto.CustomUserDetails;
import com.example.backend.model.entity.Member;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member memberData = userRepository.findByName(username);

        if (memberData != null) {


            return new CustomUserDetails(memberData);

        }

        return null;
    }
}
