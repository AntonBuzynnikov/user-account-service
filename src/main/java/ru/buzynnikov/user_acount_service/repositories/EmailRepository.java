package ru.buzynnikov.user_acount_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.buzynnikov.user_acount_service.models.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
}
