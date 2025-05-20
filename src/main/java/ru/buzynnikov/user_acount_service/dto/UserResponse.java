package ru.buzynnikov.user_acount_service.dto;


import java.io.Serializable;

public record UserResponse(Long id, String name, String birthday) implements Serializable {
}
