package com.eren.assignment.sahibinden.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eren.assignment.sahibinden.entity.User;
import com.eren.assignment.sahibinden.service.UserService;
import com.eren.assignment.sahibinden.test.TestDataBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:**/servlet-context.xml")
public class LoginControllerTest {
    
    private MockMvc mockMvc;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private LoginController loginController;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private SecurityContext securityContext;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                                .setControllerAdvice(new ExceptionHandlerController())
                                .build();
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    public void testGetUser() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("testUser");
        
        mockMvc.perform(get("/user")
                .principal(mockPrincipal))
               .andExpect(status().isOk());
    }
    
    @Test
    public void testLogoutWithExistingUser() throws Exception {
        when(authentication.getName()).thenReturn("testUser");
        
        User user = TestDataBuilder.createUser(1L, "testUser", "Test", "User", "A");
        when(userService.getUserWithUserName("testUser")).thenReturn(user);
        
        mockMvc.perform(post("/logout"))
               .andExpect(status().isOk())
               .andExpect(content().string("redirect:/main.html?logout"));
        
        verify(userService, times(1)).getUserWithUserName("testUser");
    }
    
    @Test
    public void testLogoutWithNonExistingUser() throws Exception {
        when(authentication.getName()).thenReturn("nonExistingUser");
        when(userService.getUserWithUserName("nonExistingUser")).thenReturn(null);
        
        mockMvc.perform(post("/logout"))
               .andExpect(status().isOk())
               .andExpect(content().string("redirect:/main.html?logout"));
        
        verify(userService, times(1)).getUserWithUserName("nonExistingUser");
    }
}