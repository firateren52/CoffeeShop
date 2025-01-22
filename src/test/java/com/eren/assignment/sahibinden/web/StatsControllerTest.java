package com.eren.assignment.sahibinden.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

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
import com.eren.assignment.sahibinden.entity.Condiment;
import com.eren.assignment.sahibinden.entity.OrderItem;
import com.eren.assignment.sahibinden.entity.OrderItemDetail;
import com.eren.assignment.sahibinden.service.BeverageService;
import com.eren.assignment.sahibinden.service.CondimentService;
import com.eren.assignment.sahibinden.service.OrderService;
import com.eren.assignment.sahibinden.service.model.EntityStats;
import com.eren.assignment.sahibinden.test.TestDataBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:**/servlet-context.xml")
public class StatsControllerTest {
    
    private MockMvc mockMvc;
    
    @Mock
    private OrderService orderService;
    
    @Mock
    private CondimentService condimentService;
    
    @Mock
    private BeverageService beverageService;
    
    @InjectMocks
    private StatsController statsController;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(statsController)
                                .setControllerAdvice(new ExceptionHandlerController())
                                .build();
    }
    
    @Test
    public void testGetStatsForUsers() throws Exception {
        EntityStats stats1 = TestDataBuilder.createEntityStats(1L, "user1", 5);
        EntityStats stats2 = TestDataBuilder.createEntityStats(2L, "user2", 3);
        
        when(orderService.getStatsforUsers()).thenReturn(Arrays.asList(stats1, stats2));
        
        mockMvc.perform(get("/stats/users"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].name", is("user1")))
               .andExpect(jsonPath("$[0].count", is(5)))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].name", is("user2")))
               .andExpect(jsonPath("$[1].count", is(3)));
        
        verify(orderService, times(1)).getStatsforUsers();
    }
    
    @Test
    public void testGetStatsForBeverages() throws Exception {
        EntityStats stats1 = TestDataBuilder.createEntityStats(1L, "Coffee", 10);
        EntityStats stats2 = TestDataBuilder.createEntityStats(2L, "Tea", 7);
        
        when(orderService.getStatsforBeverages()).thenReturn(Arrays.asList(stats1, stats2));
        
        mockMvc.perform(get("/stats/beverages"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].name", is("Coffee")))
               .andExpect(jsonPath("$[0].count", is(10)))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].name", is("Tea")))
               .andExpect(jsonPath("$[1].count", is(7)));
        
        verify(orderService, times(1)).getStatsforBeverages();
    }
    
    @Test
    public void testGetStatsForCondiments() throws Exception {
        EntityStats stats1 = TestDataBuilder.createEntityStats(1L, "Milk", 15);
        EntityStats stats2 = TestDataBuilder.createEntityStats(2L, "Sugar", 20);
        
        when(orderService.getStatsforCondiments()).thenReturn(Arrays.asList(stats1, stats2));
        
        mockMvc.perform(get("/stats/condiments"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].name", is("Milk")))
               .andExpect(jsonPath("$[0].count", is(15)))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].name", is("Sugar")))
               .andExpect(jsonPath("$[1].count", is(20)));
        
        verify(orderService, times(1)).getStatsforCondiments();
    }
    
    @Test
    public void testGetOrders() throws Exception {
        Beverage beverage = TestDataBuilder.createBeverage(1L, "Coffee", 5);
        Condiment condiment = TestDataBuilder.createCondiment(1L, "Milk", 2);
        
        when(beverageService.getBeverage(1L)).thenReturn(beverage);
        when(condimentService.getCondiment(1L)).thenReturn(condiment);
        
        mockMvc.perform(get("/orders"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].beverage.id", is(1)))
               .andExpect(jsonPath("$[0].beverage.name", is("Coffee")))
               .andExpect(jsonPath("$[0].orderItemDetails", hasSize(1)))
               .andExpect(jsonPath("$[0].orderItemDetails[0].condiment.id", is(1)))
               .andExpect(jsonPath("$[0].orderItemDetails[0].condiment.name", is("Milk")))
               .andExpect(jsonPath("$[0].orderItemDetails[0].count", is(2)));
        
        verify(beverageService, times(1)).getBeverage(1L);
        verify(condimentService, times(1)).getCondiment(1L);
    }
}