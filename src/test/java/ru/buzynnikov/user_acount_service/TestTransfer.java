package ru.buzynnikov.user_acount_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class TestTransfer {
    @Autowired
    private MockMvc mockMvc;



    @Test
    void successAuth() throws Exception {
        String request = """
                {
                    "email": "ivanov@example.com",
                    "password": "securePass123"
                }
                """;


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());


    }
    @Transactional
    @Test
    void successTransferAndCheckBalance() throws Exception {
        String requestFirstUser = """
                {
                    "email": "ivanov@example.com",
                    "password": "securePass123"
                }
                """;

        ObjectMapper mapper = new ObjectMapper();

        MvcResult authResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestFirstUser))
                .andExpect(status().isOk())
                .andReturn();
        String token = mapper.readTree(authResult.getResponse().getContentAsString())
                .get("token").asText();

        String requestSecondUser = """
                                {
                                    "email": "smirnov@example.com",
                                    "password": "alexSmirnov78"
                                }
                                """;

        MvcResult authResultSecondUser = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestSecondUser))
                .andExpect(status().isOk())
                .andReturn();
        String tokenSecondUser = mapper.readTree(authResultSecondUser.getResponse().getContentAsString())
                .get("token").asText();

        String requestTransfer = """
                                    {
                                        "toId": 3,
                                        "amount": 100
                                    }
                                 """;
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(requestTransfer))
                .andExpect(status().isNoContent());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/balance")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1400.75"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/balance")
                .header("Authorization", "Bearer " + tokenSecondUser))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("600.50"));
    }
    @Test
    void failedTransferAndCheckBalance() throws Exception {
        String requestFirstUser = """
                {
                    "email": "ivanov@example.com",
                    "password": "securePass123"
                }
                """;

        ObjectMapper mapper = new ObjectMapper();

        MvcResult authResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestFirstUser))
                .andExpect(status().isOk())
                .andReturn();
        String token = mapper.readTree(authResult.getResponse().getContentAsString())
                .get("token").asText();

        String requestSecondUser = """
                                {
                                    "email": "smirnov@example.com",
                                    "password": "alexSmirnov78"
                                }
                                """;

        MvcResult authResultSecondUser = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestSecondUser))
                .andExpect(status().isOk())
                .andReturn();
        String tokenSecondUser = mapper.readTree(authResultSecondUser.getResponse().getContentAsString())
                .get("token").asText();

        //Слишком малая сумма перевода
        String requestTransfer = """
                                    {
                                        "toId": 3,
                                        "amount": 8
                                    }
                                 """;
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(requestTransfer))
                .andExpect(status().isBadRequest());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/balance")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1500.75"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/balance")
                .header("Authorization", "Bearer " + tokenSecondUser))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("500.50"));

        //Несуществующий id получателя
        String requestTransfer2 = """
                                    {
                                        "toId": 4,
                                        "amount": 100
                                    }
                                 """;
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(requestTransfer2))
                .andExpect(status().isNotFound());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/balance")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1500.75"));

        //Нет средств на счете пользователя
        String requestTransfer3 = """
                                    {
                                        "toId": 2,
                                        "amount": 10000
                                    }
                                 """;
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(requestTransfer3))
                .andExpect(status().isConflict());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/balance")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1500.75"));
    }
}
