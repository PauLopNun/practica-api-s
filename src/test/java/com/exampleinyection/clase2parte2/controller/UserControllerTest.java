package com.exampleinyection.clase2parte2.controller;
import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.dto.UserRequest;
import com.exampleinyection.clase2parte2.exception.InvalidUserException;
import com.exampleinyection.clase2parte2.exception.UserNotFoundException;
import com.exampleinyection.clase2parte2.model.User;
import com.exampleinyection.clase2parte2.service.UserService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(UserController.class)

class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private AppConfig appConfig;
    @Test
    void getUsers() throws Exception {
        when(userService.getPaginatedUsers(1, 10)).thenReturn(List.of(new User(1L, "Pepe", 20, null)));
        mockMvc.perform(get("/api?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pepe"));
    }
    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(new User(1L, "Pepe", 20, null));
        mockMvc.perform(get("/api/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pepe"));
    }
    @Test
    void createUser() throws Exception {
        when(userService.saveUser(any())).thenReturn(new User(1L, "Pepe", 20, null));
        UserRequest request = new UserRequest("Pepe", 20, null);
        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pepe"));
    }
    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/1"))
                .andExpect(status().isNoContent());
        verify(userService).deleteUser(1L);
    }
    @Test
    void deleteAllUsers() throws Exception {
        mockMvc.perform(delete("/api/all"))
                .andExpect(status().isNoContent());
        verify(userService).deleteAllUsers();
    }
    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(eq(1L), any())).thenReturn(new User(1L, "Paco", 22, null));
        UserRequest request = new UserRequest("Paco", 22, null);
        mockMvc.perform(put("/api/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Paco"));
    }
    @Test
    void searchUsers() throws Exception {
        when(userService.searchByName("pa")).thenReturn(List.of(new User(1L, "Paco", 22, null)));
        mockMvc.perform(get("/api/user/search?nombre=pa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Paco"));
    }
    @Test
    void createMultipleUsers() throws Exception {
        when(userService.saveMultipleUsers(any())).thenReturn(List.of(new User(1L, "Pepe", 20, null)));
        mockMvc.perform(post("/api/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(List.of(new UserRequest("Pepe", 20, null)))))
                .andExpect(status().isOk());
    }
    @Test
    void getConfig() throws Exception {
        mockMvc.perform(get("/api/config"))
                .andExpect(status().isOk());
    }
    @Test
    void exceptionUserNotFound() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new UserNotFoundException("no"));
        mockMvc.perform(get("/api/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("no"));
    }
    @Test
    void exceptionMethodArgumentNotValid() throws Exception {
        UserRequest req = new UserRequest("", null, null); // invalid
        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isOk());
    }
    @Test
    void exceptionGeneric() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new RuntimeException("Error interno"));
        mockMvc.perform(get("/api/1"))
                .andExpect(status().isInternalServerError());
    }
    @Test
    void exceptionResponseStatus() throws Exception {
        when(userService.updateUser(any(), any())).thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Disabled"));
        UserRequest req = new UserRequest("Pepe", 20, null);
        mockMvc.perform(put("/api/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }
}
