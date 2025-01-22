package com.eren.assignment.sahibinden.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Date;

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

import com.eren.assignment.sahibinden.entity.Beverage;
import com.eren.assignment.sahibinden.exception.ResponseCodes;
import com.eren.assignment.sahibinden.exception.ServiceResponse;
import com.eren.assignment.sahibinden.service.BeverageService;
import com.eren.assignment.sahibinden.test.TestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:**/servlet-context.xml")
public class BeverageControllerTest {
    
    private MockMvc mockMvc;
    
    @Mock
    private BeverageService beverageService;
    
    @InjectMocks
    private BeverageController beverageController;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(beverageController)
                                .setControllerAdvice(new ExceptionHandlerController())
                                .build();
    }
    
    @Test
    public void testGetAllBeverages() throws Exception {
        Beverage beverage1 = TestDataBuilder.createBeverage(1L, "Coffee", 5);
        Beverage beverage2 = TestDataBuilder.createBeverage(2L, "Tea", 3);
        
        when(beverageService.getAllBeverages()).thenReturn(Arrays.asList(beverage1, beverage2));
        
        mockMvc.perform(get("/beverages"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].name", is("Coffee")))
               .andExpect(jsonPath("$[0].cost", is(5)))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].name", is("Tea")))
               .andExpect(jsonPath("$[1].cost", is(3)));
        
        verify(beverageService, times(1)).getAllBeverages();
    }
    
    @Test
    public void testGetBeverage() throws Exception {
        Beverage beverage = TestDataBuilder.createBeverage(1L, "Coffee", 5);
        
        when(beverageService.getBeverage(1L)).thenReturn(beverage);
        
        mockMvc.perform(get("/beverages/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.name", is("Coffee")))
               .andExpect(jsonPath("$.cost", is(5)));
        
        verify(beverageService, times(1)).getBeverage(1L);
    }
    
    @Test
    public void testCreateNewBeverage() throws Exception {
        Beverage beverage = TestDataBuilder.createBeverage(null, "Coffee", 5);
        
        ServiceResponse response = new ServiceResponse();
        response.setErrorCode(ResponseCodes.SUCCESS.getErrorCode());
        
        when(beverageService.createNewBeverage(any(Beverage.class))).thenReturn(response);
        
        mockMvc.perform(post("/beverages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beverage)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.errorCode", is(ResponseCodes.SUCCESS.getErrorCode())));
        
        verify(beverageService, times(1)).createNewBeverage(any(Beverage.class));
    }
    
    @Test
    public void testUpdateBeverage() throws Exception {
        Beverage beverage = TestDataBuilder.createBeverage(1L, "Coffee", 6);
        
        ServiceResponse response = new ServiceResponse();
        response.setErrorCode(ResponseCodes.SUCCESS.getErrorCode());
        
        when(beverageService.updateBeverage(any(Beverage.class))).thenReturn(response);
        
        mockMvc.perform(put("/beverages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beverage)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.errorCode", is(ResponseCodes.SUCCESS.getErrorCode())));
        
        verify(beverageService, times(1)).updateBeverage(any(Beverage.class));
    }
    
    @Test
    public void testRemoveBeverage() throws Exception {
        ServiceResponse response = new ServiceResponse();
        response.setErrorCode(ResponseCodes.SUCCESS.getErrorCode());
        
        when(beverageService.removeBeverage(1L)).thenReturn(response);
        
        mockMvc.perform(delete("/beverages/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.errorCode", is(ResponseCodes.SUCCESS.getErrorCode())));
        
        verify(beverageService, times(1)).removeBeverage(1L);
    }
}