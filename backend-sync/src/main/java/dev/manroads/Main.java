package dev.manroads;

import dev.manroads.repositories.RefreshTokenRepository;
import dev.manroads.repositories.RoleRepository;
import dev.manroads.repositories.UserRepository;
import dev.manroads.security.jwt.models.ERole;
import dev.manroads.security.jwt.models.RefreshToken;
import dev.manroads.security.jwt.models.Role;
import dev.manroads.security.jwt.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dev.manroads.security.jwt.models.ERole.*;
import static java.lang.Thread.sleep;

@SpringBootApplication
public class Main implements CommandLineRunner {

    final static Logger logger = LoggerFactory.getLogger(Main.class);

    RefreshTokenRepository repository;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder encoder;

    public Main(RefreshTokenRepository repository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Populate Roles
        Role user = new Role(ROLE_USER);
        roleRepository.save(user);
        Role moderator = new Role(ROLE_MODERATOR);
        roleRepository.save(moderator);
        Role admin = new Role(ROLE_ADMIN);
        roleRepository.save(admin);

        // Populate users
        Set<Role> hash_Set = new HashSet<>();
        hash_Set.add(user);
        User user1 = new User("aap1", encoder.encode( "staart1"), hash_Set);
        User user2 = new User("aap2", encoder.encode("staart2"), hash_Set   );
        userRepository.save(user1);
        userRepository.save(user2);

        /*

        RefreshToken refreshToken = new RefreshToken(user,"ABC", Instant.now());
        repository.save(refreshToken);
        RefreshToken refreshToken2 = new RefreshToken(user2,"DEF", Instant.now().plusMillis(5000));
        repository.save(refreshToken2);
        logger.info("tokens: " + refreshToken + " " + refreshToken2);
        RefreshToken refreshToken3 = repository.findByRefreshToken("ABC").get();
        logger.info("token 3: " + refreshToken3);

         */



    }
}
