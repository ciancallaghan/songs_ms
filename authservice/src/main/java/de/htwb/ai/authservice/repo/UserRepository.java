package de.htwb.ai.authservice.repo;

import de.htwb.ai.authservice.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, String> {
    User getByUserid(String userid);
}