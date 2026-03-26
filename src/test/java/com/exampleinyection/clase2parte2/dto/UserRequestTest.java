package com.exampleinyection.clase2parte2.dto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class UserRequestTest {
    @Test
    void testUserRequest() {
        UserRequest userRequest = new UserRequest("Pepe", 20, null);
        assertEquals("Pepe", userRequest.name());
        assertEquals(20, userRequest.age());
        assertEquals(null, userRequest.allergies());
    }
}
