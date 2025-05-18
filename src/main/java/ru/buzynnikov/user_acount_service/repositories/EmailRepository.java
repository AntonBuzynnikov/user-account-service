package ru.buzynnikov.user_acount_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buzynnikov.user_acount_service.models.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    @Transactional
    boolean existsByEmail(String email);

    @Transactional
    boolean existsByEmailAndUserId(String email, Long userId);

    @Transactional
    @Query("SELECT COUNT(e) FROM Email e WHERE e.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Transactional
    void deleteByEmail(String email);

}
