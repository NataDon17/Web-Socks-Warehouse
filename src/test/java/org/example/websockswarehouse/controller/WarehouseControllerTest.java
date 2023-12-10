package org.example.websockswarehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.websockswarehouse.constant.InitialData;
import org.example.websockswarehouse.dto.SockDTO;
import org.example.websockswarehouse.model.Sock;
import org.example.websockswarehouse.repository.SocksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.websockswarehouse.constant.SocksConstants.*;
import static org.example.websockswarehouse.model.SocksOperation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
public class WarehouseControllerTest {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private DataSource dataSource;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    SocksRepository socksRepository;
    @Autowired
    InitialData initialData;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanTables() {
        socksRepository.deleteAll();
    }

    @Test
    void testPostgresql() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            assertThat(conn).isNotNull();
        }
    }

    @Test
    public void arrivalSocksInWarehouse_whenNewSocks_returnOk() throws Exception {
        String newSock = objectMapper.writeValueAsString(SOCK_1);

        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newSock))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void arrivalSocksInWarehouse_whenSocksIsPresent_returnOk() throws Exception {
        socksRepository.save(SOCK_3.toSock());
        String nextSock = objectMapper.writeValueAsString(SOCK_4);

        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nextSock))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void arrivalSocksInWarehouse_whenInvalidQuantitySocks_thanBadRequest() throws Exception {
        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initialData.getSockWithNegativeQuantity().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void arrivalSocksInWarehouse_whenInvalidColorSocks_thanBadRequest() throws Exception {
        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initialData.getSockWithEmptyColor().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void arrivalSocksInWarehouse_whenInvalidCottonPartSocks_thanBadRequest() throws Exception {
        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initialData.getSockWithInvalidCottonPart().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void arrivalSocksInWarehouse_whenNegativeCottonPartSocks_thanBadRequest() throws Exception {
        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initialData.getSockWithNegativeCottonPart().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void arrivalSocksInWarehouse_whenEmptyParamSocks_thanBadRequest() throws Exception {
        SockDTO emptySock = new SockDTO();

        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptySock.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void dispatchSocksInWarehouse_whenSocksIsPresent_returnOk() throws Exception {
        socksRepository.save(SOCK_1.toSock());
        String sock = objectMapper.writeValueAsString(SOCK_1);

        mockMvc.perform(post("/api/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sock))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void dispatchSocksInWarehouse_whenNonSocks_thenNotFound() throws Exception {
        socksRepository.save(SOCK_1.toSock());
        String sock = objectMapper.writeValueAsString(SOCK_2);

        mockMvc.perform(post("/api/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sock))
                .andExpect(status().isNotFound());
    }

    @Test
    public void dispatchSocksInWarehouse_whenEmptyBase_thanBadRequest() throws Exception {
        Sock sock = SOCK_1.toSock();

        mockMvc.perform(post("/api/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sock.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void dispatchSocksInWarehouse_whenInvalidQuantitySocks_thanBadRequest() throws Exception {
        mockMvc.perform(post("/api/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initialData.getSockWithNegativeQuantity().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void dispatchSocksInWarehouse_whenInvalidColorSocks_thanBadRequest() throws Exception {
        mockMvc.perform(post("/api/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initialData.getSockWithEmptyColor().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void dispatchSocksInWarehouse_whenInvalidCottonPartSocks_thanBadRequest() throws Exception {
        mockMvc.perform(post("/api/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initialData.getSockWithInvalidCottonPart().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void dispatchSocksInWarehouse_whenNegativeCottonPartSocks_thanBadRequest() throws Exception {
        mockMvc.perform(post("/api/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initialData.getSockWithNegativeCottonPart().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void dispatchSocksInWarehouse_whenEmptyParamSocks_thanBadRequest() throws Exception {
        SockDTO emptySock = new SockDTO();

        mockMvc.perform(post("/api/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptySock.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findSocksInWarehouse_whenSocksIsPresentAndOperationEqual_thanReturnQuantity() throws Exception {
        socksRepository.save(SOCK_1.toSock());
        String color = SOCK_1.getColor();
        int cottonPart = SOCK_1.getCottonPart();
        mockMvc.perform(get("/api/socks/")
                        .param("color", color)
                        .param("cottonPart", String.valueOf(cottonPart))
                        .flashAttr("operation", equal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().json(String.valueOf(5)));
    }

    @Test
    public void findSocksInWarehouse_whenSocksIsPresentAndOperationMore_thanReturnQuantity() throws Exception {
        socksRepository.save(SOCK_1.toSock());
        String color = "red";
        int cottonPart = 80;
        mockMvc.perform(get("/api/socks/")
                        .param("color", color)
                        .param("cottonPart", String.valueOf(cottonPart))
                        .flashAttr("operation", moreThan)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().json(String.valueOf(5)));
    }

    @Test
    public void findSocksInWarehouse_whenSocksIsPresentAndOperationLess_thanReturnQuantity() throws Exception {
        socksRepository.save(SOCK_1.toSock());
        socksRepository.save(SOCK_2.toSock());
        String color = "red";
        int cottonPart = 100;
        mockMvc.perform(get("/api/socks/")
                        .param("color", color)
                        .param("cottonPart", String.valueOf(cottonPart))
                        .flashAttr("operation", lessThan)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().json(String.valueOf(2)));
    }

    @Test
    public void findSocksInWarehouse_whenNonSocksAndOperationAny_thanReturnQuantity() throws Exception {
        socksRepository.save(SOCK_1.toSock());
        socksRepository.save(SOCK_2.toSock());
        String color = "pink";
        int cottonPart = 0;
        mockMvc.perform(get("/api/socks/")
                        .param("color", color)
                        .param("cottonPart", String.valueOf(cottonPart))
                        .flashAttr("operation", moreThan)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().json(String.valueOf(0)));
    }

    @Test
    public void findSocksInWarehouse_whenEmptyBase_thanNotFound() throws Exception {
        String color = "black";
        int cottonPart = 10;
        mockMvc.perform(get("/api/socks/")
                        .param("color", color)
                        .param("cottonPart", String.valueOf(cottonPart))
                        .flashAttr("operation", moreThan)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
