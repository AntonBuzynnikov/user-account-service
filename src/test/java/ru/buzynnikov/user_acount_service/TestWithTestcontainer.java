package ru.buzynnikov.user_acount_service;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Duration;


//@TestPropertySource(locations = "classpath:src/main/resources/application.yaml")
//@Testcontainers
//@SpringBootTest
public class TestWithTestcontainer {

//    @Autowired
    private MockMvc mockMvc;
    private static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9" +
            ".eyJpZCI6MSwic3ViIjoiaXZhbm92QGV4YW1wbGUuY29tIn0" +
            ".mLs4LEFU_dXiQfAgZ34BBiUJtSJxLtZKHYfe9ArlJEueDkR6HlNrjebzMa8dB6cSu4623bdRe5WQuvXl3R0Stg";

//    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password")
            .withExposedPorts(5432)
            .withCommand("postgres -c datestyle='german'")
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 1)
                    .withStartupTimeout(Duration.ofMinutes(2)));

//    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }
//    @Test
    void findAll_shouldReturnPageOfUsers() throws Exception {



        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/find")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").isNumber());
    }

//    @Test
    void findAll_withEmail_shouldValidateEmailFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/find")
                        .param("email", "invalid-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Поле email должно быть валидным адресом электронной почты"));
    }

//    @Test
    void findAll_withPhone_shouldValidatePhoneFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/find")
                        .param("phone", "123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("Поле номера телефона должно быть в формате 7xxxxxxxxx от 9 до 13 цифр"));
    }

//    @Test
    void findAll_withDateOfBirth_shouldValidateDateFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/find")
                        .param("date-of-birth", "2023-13-01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value("Поле даты рождения должно быть в формате dd.mm.yyyy"));
    }

//    @Test
    void findAll_withShortName_shouldValidateNameLength() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/find")
                        .param("name", "ab")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Минимальная длина имени должна быть не менее 3 символов"));
    }

//    @Test
    void findAll_withAllValidParams_shouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/find")
                        .param("name", "Алексей Смирнов")
                        .param("email", "smirnov@example.com")
                        .param("phone", "79001002030")
                        .param("date-of-birth", "15.10.1990")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray());
    }
}
