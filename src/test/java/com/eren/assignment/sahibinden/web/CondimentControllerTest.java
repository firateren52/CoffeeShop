package com.eren.assignment.sahibinden.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eren.assignment.sahibinden.entity.Condiment;
import com.eren.assignment.sahibinden.exception.ResponseCodes;
import com.eren.assignment.sahibinden.exception.ServiceResponse;
import com.eren.assignment.sahibinden.service.CondimentService;
import com.eren.assignment.sahibinden.test.TestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:**/servlet-context.xml")
public class CondimentControllerTest {
    
    private MockMvc mockMvc;
    
    @Mock
    private CondimentService condimentService;
    
    @InjectMocks
    private CondimentController condimentController;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(condimentController)
                                .setControllerAdvice(new ExceptionHandlerController())
                                .build();
    }
    
    @Test
    public void testGetAllCondiments() throws Exception {
        Condiment condiment1 = TestDataBuilder.createCondiment(1L, "Milk", 2);
        Condiment condiment2 = TestDataBuilder.createCondiment(2L, "Sugar", 1);
        
        when(condimentService.getAllCondiments()).thenReturn(Arrays.asList(condiment1, condiment2));
        
        mockMvc.perform(get("/condiments"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].name", is("Milk")))
               .andExpect(jsonPath("$[0].cost", is(2)))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].name", is("Sugar")))
               .andExpect(jsonPath("$[1].cost", is(1)));
        
        verify(condimentService, times(1)).getAllCondiments();
    }
    
    @Test
    public void testGetCondiment() throws Exception {
        Condiment condiment = TestDataBuilder.createCondiment(1L, "Milk", 2);
        
        when(condimentService.getCondiment(1L)).thenReturn(condiment);
        
        mockMvc.perform(get("/condiments/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.name", is("Milk")))
               .andExpect(jsonPath("$.cost", is(2)));
        
        verify(condimentService, times(1)).getCondiment(1L);
    }
    
    @Test
    public void testCreateNewCondiment() throws Exception {
        Condiment condiment = TestDataBuilder.createCondiment(null, "Milk", 2);
        
        ServiceResponse response = new ServiceResponse();
        response.setErrorCode(ResponseCodes.SUCCESS.getErrorCode());
        
        when(condimentService.createNewCondiment(any(Condiment.class))).thenReturn(response);
        
        mockMvc.perform(post("/condiments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(condiment)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.errorCode", is(ResponseCodes.SUCCESS.getErrorCode())));
        
        verify(condimentService, times(1)).createNewCondiment(any(Condiment.class));
    }
    
    @Test
    public void testUpdateCondiment() throws Exception {
        Condiment condiment = TestDataBuilder.createCondiment(1L, "Milk", 3);
        
        ServiceResponse response = new ServiceResponse();
        response.setErrorCode(ResponseCodes.SUCCESS.getErrorCode());
        
        when(condimentService.updateCondiment(any(Condiment.class))).thenReturn(response);
        
        mockMvc.perform(put("/condiments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(condiment)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.errorCode", is(ResponseCodes.SUCCESS.getErrorCode())));
        
        verify(condimentService, times(1)).updateCondiment(any(Condiment.class));
    }
    
    @Test
    public void testRemoveCondiment() throws Exception {
        ServiceResponse response = new ServiceResponse();
        response.setErrorCode(ResponseCodes.SUCCESS.getErrorCode());
        
        when(condimentService.removeCondiment(1L)).thenReturn(response);
        
        mockMvc.perform(delete("/condiments/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.errorCode", is(ResponseCodes.SUCCESS.getErrorCode())));
        
        verify(condimentService, times(1)).removeCondiment(1L);
    }
}