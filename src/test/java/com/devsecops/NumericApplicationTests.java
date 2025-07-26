package com.devsecops;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NumericApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NumericController numericController;

    @Test
    public void smallerThanOrEqualToFiftyMessage() throws Exception {
        this.mockMvc.perform(get("/compare/49")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Smaller than or equal to 50"));
    }

    @Test
    public void greaterThanFiftyMessage() throws Exception {
        this.mockMvc.perform(get("/compare/51")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Greater than 50"));
    }
    
    @Test
    public void welcomeMessage() throws Exception {
         this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void testIncrementEndpoint() throws Exception {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(numericController).build();

        // Mock the external service response
        int testValue = 5;
        String mockResponse = "6";
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
            .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Test the endpoint
        mockMvc.perform(get("/increment/" + testValue))
               .andExpect(status().isOk())
               .andExpect(content().string(mockResponse));
    }

    @Test
    public void testIncrementEndpointWithNegativeValue() throws Exception {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(numericController).build();

        // Mock the external service response for negative value
        int testValue = -3;
        String mockResponse = "-2";
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
            .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Test the endpoint
        mockMvc.perform(get("/increment/" + testValue))
               .andExpect(status().isOk())
               .andExpect(content().string(mockResponse));
    }

    @Test
    public void testIncrementEndpointWhenServiceFails() throws Exception {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(numericController).build();

        // Mock service failure
        int testValue = 10;
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
            .thenThrow(new RuntimeException("Service unavailable"));

        // Test the endpoint
        mockMvc.perform(get("/increment/" + testValue))
               .andExpect(status().is5xxServerError());
    }
    

}