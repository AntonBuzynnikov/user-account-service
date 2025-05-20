package ru.buzynnikov.user_acount_service.utils;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ru.buzynnikov.user_acount_service.models.Email;
import ru.buzynnikov.user_acount_service.models.Phone;
import ru.buzynnikov.user_acount_service.models.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Набор вспомогательных спецификаций для динамического формирования запросов в Spring Data JPA.
 * Каждая спецификация описывает отдельное условие фильтрации, применяемое к объектам User.
 */
public class UserSpecification {

    /**
     * Спецификация для выбора пользователей по адресу электронной почты.
     *
     * @param email адрес электронной почты для поиска
     * @return спецификацию для критерия поиска по email
     */
    public static Specification<User> hasEmail(String email){
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->{
            if(email==null || email.isEmpty()) {
                return builder.conjunction();
            }

            Join<User, Email> emailJoin = root.join("emails", JoinType.INNER);

            return builder.equal(emailJoin.get("email"), email);
        };
    }
    /**
     * Спецификация для выбора пользователей по дате рождения позже указанной.
     *
     * @param dateOfBirth строка с датой рождения в формате dd.MM.yyyy
     * @return спецификацию для критерия поиска по дате рождения
     */
    public static Specification<User> byDateOfBirth(String dateOfBirth){
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->{
            if(dateOfBirth==null) {
                return builder.conjunction();
            }
            return builder.greaterThan(root.get("dateOfBirth"), LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        };

    }

    /**
     * Спецификация для выбора пользователей по частичному совпадению имени.
     *
     * @param name искомое имя пользователя
     * @return спецификацию для критерия поиска по имени
     */
    public static Specification<User> byNameLike(String name){
        System.out.println(name);
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->{
            if(name==null || name.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(root.get("name"), name+"%");
        };
    }

    /**
     * Спецификация для выбора пользователей по номеру телефона.
     *
     * @param phone номер телефона для поиска
     * @return спецификацию для критерия поиска по телефону
     */
    public static Specification<User> hasPhone(String phone){
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->{
            if(phone==null || phone.isEmpty()) {
                return builder.conjunction();
            }

            Join<User, Phone> emailJoin = root.join("phones", JoinType.INNER);

            return builder.equal(emailJoin.get("phone"), phone);
        };
    }
}

