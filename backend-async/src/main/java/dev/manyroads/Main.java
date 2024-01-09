package dev.manyroads;

import dev.manyroads.config.ProjectConfig;
import dev.manyroads.models.ERole;
import dev.manyroads.models.Role;
import dev.manyroads.models.User;
import dev.manyroads.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

import static dev.manyroads.models.ERole.ROLE_USER;

@SpringBootApplication
public class Main implements CommandLineRunner {

    UserRepository repository;

    public Main(UserRepository repository) {
        this.repository = repository;
    }

    private static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class);

        // Checking the MySQL DB connection with the DOCKER MySQL container
        String mysqlUri = ctx.getEnvironment().getProperty("spring.datasource.url");

        logger.info("Connected to MySQL: " + mysqlUri);

        // Checking the Mongo DB connection with the DOCKER Mongo container
        String mongoHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");

        logger.info("Connected to Mongo: " + mongoHost);

    }

    @Override
    public void run(String... args) throws Exception {

        logger.debug("Filling DB");

        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ROLE_USER));

        Mono<User> monoUser = repository.save(new User(
                "tedje",
                "vanes",
                roles));

        monoUser.subscribe(m-> System.out.println(m));
    }
}
