package ru.buzynnikov.user_acount_service.repositories;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @Query("""
            SELECT u.id, u.name, u.dateOfBirth, u.password
            FROM User u
            JOIN email_data e ON e.user_id = u.id
            WHERE e.email = :email
          """)
    Optional<User> findByEmail(String email);
}
