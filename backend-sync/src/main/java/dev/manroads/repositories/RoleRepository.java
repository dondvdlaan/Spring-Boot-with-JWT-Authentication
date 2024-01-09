package dev.manroads.repositories;

import dev.manroads.security.jwt.models.Role;
import dev.manroads.security.jwt.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

}
