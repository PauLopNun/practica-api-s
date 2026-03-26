package com.exampleinyection.clase2parte2.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class UserTest {
    @Test
    void testUser() {
        User user = new User(1L, "Pepe", 20, null);
        assertEquals(1L, user.getId());
        assertEquals("Pepe", user.getName());
        assertEquals(20, user.getAge());
        assertEquals(null, user.getAllergies());
        user.setId(2L);
        user.setName("Paco");
        user.setAge(22);
        user.setAllergies(java.util.List.of());
        assertEquals(2L, user.getId());
        assertEquals("Paco", user.getName());
        assertEquals(22, user.getAge());
        user.toString();
        user.hashCode();
        user.equals(new User(2L, "Paco", 22, java.util.List.of()));
    }
}
