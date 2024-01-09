package dev.manyroads.service;

import dev.manyroads.models.User;
import dev.manyroads.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Class UserDetailsServiceImpl offers 1 method that accepts a username and returns Userdetails, which
 * Spring uses for authentication and validation
 */
@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
/*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Retrieve user from DB
        User user = userRepository.findByUserName(username).get();
        logger.info("user " + user);


        // Build Userdetails and return
        return UserDetailsImpl.build(user);
    }

 */

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        // Retrieve user from DB
        Mono<User> monoUser = userRepository.findByUserName(username);

        logger.info("user " + monoUser.subscribe(System.out::println));

        // Build Userdetails and return
        return monoUser.map(u -> (UserDetailsImpl.build(u)));
    }
}
