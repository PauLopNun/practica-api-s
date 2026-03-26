package com.exampleinyection.clase2parte2.controller;
import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.exception.InvalidUserException;
import com.exampleinyection.clase2parte2.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(UserController.class)

class ExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private AppConfig appConfig;
    @Test
    void triggerInvalidUser() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new InvalidUserException("Invalid"));
        mockMvc.perform(get("/api/1"))
                .andExpect(status().is(417));
    }

    @Test
    void triggerUserNotFound() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new com.exampleinyection.clase2parte2.exception.UserNotFoundException("Not found"));
        mockMvc.perform(get("/api/1"))
                .andExpect(status().is(404));
    }

    @Test
    void triggerResponseStatusException() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_GATEWAY, "Reason"));
        mockMvc.perform(get("/api/1"))
                .andExpect(status().is(502));
    }

    @Test
    void triggerGenericException() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new RuntimeException("Generic error"));
        mockMvc.perform(get("/api/1"))
                .andExpect(status().is(500));
    }

    @Test
    void triggerMethodArgumentNotValid() throws Exception {
        org.springframework.validation.BindException bindException = new org.springframework.validation.BindException(new Object(), "userRequest");
        bindException.addError(new org.springframework.validation.ObjectError("userRequest", "Validation failed"));

        org.springframework.core.MethodParameter methodParameter = new org.springframework.core.MethodParameter(
                UserController.class.getMethod("createUser", com.exampleinyection.clase2parte2.dto.UserRequest.class), 0);

        org.springframework.web.bind.MethodArgumentNotValidException ex =
                new org.springframework.web.bind.MethodArgumentNotValidException(methodParameter, bindException);

        com.exampleinyection.clase2parte2.exception.GlobalExceptionHandler handler = new com.exampleinyection.clase2parte2.exception.GlobalExceptionHandler();
        org.springframework.http.ResponseEntity<java.util.Map<String, Object>> response = handler.handleValidation(ex);

        org.junit.jupiter.api.Assertions.assertEquals(400, response.getStatusCode().value());
        org.junit.jupiter.api.Assertions.assertEquals("Validation failed", response.getBody().get("message"));
    }
}
