package com.eren.assignment.sahibinden.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.security.Principal;
import java.util.ArrayList;

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
import com.eren.assignment.sahibinden.entity.OrderItem;
import com.eren.assignment.sahibinden.exception.ResponseCodes;
import com.eren.assignment.sahibinden.exception.ServiceResponse;
import com.eren.assignment.sahibinden.service.OrderService;
import com.eren.assignment.sahibinden.service.ShoppingCartService;
import com.eren.assignment.sahibinden.service.model.ShoppingCart;
import com.eren.assignment.sahibinden.test.TestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:**/servlet-context.xml")
public class ShoppingCartControllerTest {
    
    private MockMvc mockMvc;
    
    @Mock
    private ShoppingCartService shoppingCartService;
    
    @Mock
    private OrderService orderService;
    
    @Mock
    private ShoppingCart cart;
    
    @InjectMocks
    private ShoppingCartController shoppingCartController;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shoppingCartController)
                                .setControllerAdvice(new ExceptionHandlerController())
                                .build();
    }
    
    @Test
    public void testGetCart() throws Exception {
        mockMvc.perform(get("/cart/"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
    
    @Test
    public void testAddToCart() throws Exception {
        Beverage beverage = TestDataBuilder.createBeverage(1L, "Coffee", 5);
        OrderItem orderItem = TestDataBuilder.createOrderItem(null, beverage);
        
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("testUser");
        
        ShoppingCart updatedCart = new ShoppingCart();
        when(shoppingCartService.addToCart(eq("testUser"), any(ShoppingCart.class), any(OrderItem.class)))
            .thenReturn(updatedCart);
        
        mockMvc.perform(post("/cart/items")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderItem)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        
        verify(shoppingCartService, times(1)).addToCart(eq("testUser"), any(ShoppingCart.class), any(OrderItem.class));
    }
    
    @Test
    public void testRemoveFromCart() throws Exception {
        ShoppingCart updatedCart = new ShoppingCart();
        when(shoppingCartService.removeFromCart(any(ShoppingCart.class), eq(0)))
            .thenReturn(updatedCart);
        
        mockMvc.perform(delete("/cart/items/0"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        
        verify(shoppingCartService, times(1)).removeFromCart(any(ShoppingCart.class), eq(0));
    }
    
    @Test
    public void testCheckoutOrder() throws Exception {
        doNothing().when(orderService).checkoutOrder(any(ShoppingCart.class));
        
        mockMvc.perform(post("/cart/orders"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.errorCode", is(ResponseCodes.SUCCESS.getErrorCode())));
        
        verify(orderService, times(1)).checkoutOrder(any(ShoppingCart.class));
    }
}