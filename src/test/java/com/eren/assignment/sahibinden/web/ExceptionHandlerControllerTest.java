package com.eren.assignment.sahibinden.web;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.eren.assignment.sahibinden.exception.ResponseCodes;
import com.eren.assignment.sahibinden.exception.ServiceResponse;
import com.eren.assignment.sahibinden.exception.ServiceRuntimeException;

public class ExceptionHandlerControllerTest {
    
    @Mock
    private HttpServletRequest request;
    
    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testHandleServiceRuntimeException() {
        // Given
        ServiceRuntimeException exception = new ServiceRuntimeException(ResponseCodes.ERROR);
        
        // When
        ServiceResponse response = exceptionHandlerController.handleServiceRuntimeException(request, exception);
        
        // Then
        assertNotNull(response);
        assertEquals(ResponseCodes.ERROR.getErrorCode(), response.getErrorCode());
    }
    
    @Test
    public void testHandleUnexpectedException() {
        // Given
        Exception exception = new RuntimeException("Unexpected error");
        
        // When
        ServiceResponse response = exceptionHandlerController.handleUnexpectedException(request, exception);
        
        // Then
        assertNotNull(response);
        assertEquals(ResponseCodes.ERROR.getErrorCode(), response.getErrorCode());
    }
    
    @Test
    public void testHandleServiceRuntimeExceptionWithCustomCode() {
        // Given
        ServiceRuntimeException exception = new ServiceRuntimeException(ResponseCodes.INVALID_PARAMETER);
        
        // When
        ServiceResponse response = exceptionHandlerController.handleServiceRuntimeException(request, exception);
        
        // Then
        assertNotNull(response);
        assertEquals(ResponseCodes.INVALID_PARAMETER.getErrorCode(), response.getErrorCode());
    }
}