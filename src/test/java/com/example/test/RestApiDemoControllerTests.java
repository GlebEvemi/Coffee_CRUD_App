package com.example.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestApiDemoController.class)
class RestApiDemoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoffeeRepository coffeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCoffees() throws Exception {
        given(coffeeRepository.findAll())
                .willReturn(Arrays.asList(new Coffee("1", "Latte"), new Coffee("2", "Espresso")));

        mockMvc.perform(get("/coffees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Latte")))
                .andExpect(jsonPath("$[1].name", is("Espresso")));
    }

    @Test
    void testGetCoffeeById() throws Exception {
        given(coffeeRepository.findById("1"))
                .willReturn(Optional.of(new Coffee("1", "Latte")));

        mockMvc.perform(get("/coffees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Latte")));
    }

    @Test
    void testPostCoffee() throws Exception {
        Coffee newCoffee = new Coffee(null, "Cappuccino");
        Coffee savedCoffee = new Coffee("123", "Cappuccino");

        given(coffeeRepository.save(any(Coffee.class)))
                .willReturn(savedCoffee);

        mockMvc.perform(post("/coffees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCoffee)))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.id", is("123")))
                .andExpect(jsonPath("$.name", is("Cappuccino")));
    }

    @Test
    void testPutCoffeeUpdate() throws Exception {
        Coffee updatedCoffee = new Coffee("1", "Americano");

        given(coffeeRepository.existsById("1")).willReturn(true);
        given(coffeeRepository.save(any(Coffee.class))).willReturn(updatedCoffee);

        mockMvc.perform(put("/coffees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCoffee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Americano")));
    }

    @Test
    void testPutCoffeeCreate() throws Exception {
        Coffee newCoffee = new Coffee("99", "Flat White");

        given(coffeeRepository.existsById("99")).willReturn(false);
        given(coffeeRepository.save(any(Coffee.class))).willReturn(newCoffee);

        mockMvc.perform(put("/coffees/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCoffee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Flat White")));
    }

    @Test
    void testDeleteCoffee() throws Exception {
        doNothing().when(coffeeRepository).deleteById("1");

        mockMvc.perform(delete("/coffees/1"))
                .andExpect(status().isOk());
    }
}
