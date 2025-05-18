package ru.buzynnikov.user_acount_service.dto;

public class JwtAuthResponse {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public JwtAuthResponse(String token) {
        this.token = token;
    }

    public JwtAuthResponse() {
    }
}
