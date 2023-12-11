package dev.manroads.security.service;

import dev.manroads.controllers.AuthController;
import dev.manroads.repositories.UserRepository;
import dev.manroads.security.jwt.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Class UserDetailsServiceImpl offers 1 method that accepts a username and returns Userdetails, which
 * Spring uses for authentication and validation
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Retrieve user from DB
        User user = userRepository.findByUserName(username).get();
        logger.info("user " + user);


        // Build Userdetails and return
        return UserDetailsImpl.build(user);
    }
}
