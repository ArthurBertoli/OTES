package com.financeiro.api.transacao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class TransacaoControllerEncodingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAcceptAccentedCharactersInRequestBody() throws Exception {
        String requestBody = """
                {
                  "descricao": "Café, ação, maçã, São João",
                  "valor": 10.50,
                  "tipo": "ENTRADA",
                  "categoria": "Alimentação",
                  "dataCadastro": "2026-06-26"
                }
                """;

        mockMvc.perform(post("/api/transacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Café, ação, maçã, São João"))
                .andExpect(jsonPath("$.categoria").value("Alimentação"));
    }
}
