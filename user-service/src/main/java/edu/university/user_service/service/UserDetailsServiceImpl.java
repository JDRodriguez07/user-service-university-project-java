package edu.university.user_service.service;

import edu.university.user_service.exceptions.UserNotFoundException;
import edu.university.user_service.model.User;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Usuario no encontrado con email: " + email));

                String springRole = "ROLE_" + user.getRole().getName().toUpperCase();

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(springRole));

                return new org.springframework.security.core.userdetails.User(
                                user.getEmail(),
                                user.getPassword(),
                                authorities);
        }
}
