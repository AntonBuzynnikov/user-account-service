package ru.buzynnikov.user_acount_service.controllers;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.buzynnikov.user_acount_service.dto.UserResponse;
import ru.buzynnikov.user_acount_service.exceptions.NotAuthenticatedException;

import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;
import ru.buzynnikov.user_acount_service.services.interfasces.UserService;



/**
 * Контроллер для работы с пользователями. Предназначен для поиска пользователей
 * по различным критериям фильтрации с поддержкой постраничного вывода результатов.
 */

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final String DATE_OF_BIRTH_REGEX = "^(?:(?:31\\.(0?[13578]|1[02])\\.|(29|30)\\." +
            "(0?[13-9]|1[0-2])\\.)((1[6-9]|[2-9]\\d)\\d{2})|29\\.0?2\\.((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|" +
            "[13579][26])|((16|[2468][048]|[3579][26])00))|(0?[1-9]|1\\d|2[0-8])\\.(0?[1-9]|1[0-2])\\.((1[6-9]|[2-9]\\d)\\d{2}))$";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Метод для поиска пользователей по множеству критериев фильтрации:
     * email, дате рождения, телефону и имени.
     *
     * @param email       необязательный параметр для фильтра по адресу электронной почты
     * @param dateOfBirth необязательный параметр для фильтра по дате рождения
     * @param phone       необязательный параметр для фильтра по номеру телефона
     * @param name        необязательный параметр для фильтра по имени пользователя
     * @param pageable    объект для пагинации результатов
     * @return объект ResponseEntity, содержащий страницу результатов с объектами UserResponse
     */
    @GetMapping("/find")
    public ResponseEntity<Page<UserResponse>> findAll(@Pattern(regexp = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)", message = "Поле email должно быть валидным адресом электронной почты")
                                                      @RequestParam(value = "email", required = false) String email,
                                                      @Pattern(regexp = DATE_OF_BIRTH_REGEX, message = "Поле даты рождения должно быть в формате dd.mm.yyyy")
                                                      @RequestParam(value = "date-of-birth", required = false) String dateOfBirth,
                                                      @Pattern(regexp = "^7\\d{8,12}$", message = "Поле номера телефона должно быть в формате 7xxxxxxxxx от 9 до 13 цифр")
                                                      @RequestParam(value = "phone", required = false) String phone,
                                                      @NotNull(message = "Поле имя пользователя не может быть пустым")
                                                      @Size(min = 3, message = "Минимальная длина имени должна быть не менее 3 символов")
                                                      @RequestParam(value = "name", required = false) String name,
                                                      Pageable pageable) {
        return ResponseEntity.ok(userService.findUser(email, name, phone, dateOfBirth, pageable));
    }
    @PatchMapping("/update-date/{dateOfBirth}")
    public ResponseEntity<Void> updateDateOfBirth(@PathVariable String dateOfBirth, Authentication authentication) {
        if (authentication instanceof UserAuthDTO dto){
            userService.updateDateOfBird(dto.getUserId(), dateOfBirth);
        } else {
            throw new NotAuthenticatedException("Вы не авторизованы.");
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update-name/{name}")
    public ResponseEntity<Void> updateName(@PathVariable String name, Authentication authentication) {
        if (authentication instanceof UserAuthDTO dto){
            userService.updateName(dto.getUserId(), name);
        } else {
            throw new NotAuthenticatedException("Вы не авторизованы.");
        }
        return ResponseEntity.noContent().build();
    }

}
