package org.example.libraryapi;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data is magic. It will automatically write the SQL query
    // to find a user by their username just by reading this method name!
    Optional<User> findByUsername(String username);
}