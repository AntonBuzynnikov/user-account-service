package ru.buzynnikov.user_acount_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.buzynnikov.user_acount_service.models.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
