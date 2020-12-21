package by.questionary.repository;

import by.questionary.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByName(String name);

    boolean existsAllByName(String name);

    boolean existsByEmail(String email);

}
