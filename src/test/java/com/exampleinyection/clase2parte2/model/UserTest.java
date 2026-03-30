package com.exampleinyection.clase2parte2.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private static class UserChild extends User {
        UserChild(Long id, String name, int age, List<Allergy> allergies) {
            super(id, name, age, allergies);
        }
    }

    private static class UserChildStrict extends User {
        UserChildStrict(Long id, String name, int age, List<Allergy> allergies) {
            super(id, name, age, allergies);
        }

        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }

    @Test
    void testGettersAndSetters() {
        User user = new User(1L, "Pepe", 20, null);

        assertEquals(1L, user.getId());
        assertEquals("Pepe", user.getName());
        assertEquals(20, user.getAge());
        assertNull(user.getAllergies());

        user.setId(2L);
        user.setName("Paco");
        user.setAge(22);
        user.setAllergies(List.of());

        assertEquals(2L, user.getId());
        assertEquals("Paco", user.getName());
        assertEquals(22, user.getAge());
        assertEquals(List.of(), user.getAllergies());
    }

    @Test
    void testEqualsAndHashCode() {
        List<Allergy> allergies = List.of(new Allergy("Pollen", 2));

        User baseUser = new User(1L, "Pepe", 20, allergies);
        User sameValuesUser = new User(1L, "Pepe", 20, List.of(new Allergy("Pollen", 2)));
        User nullAllergiesA = new User(1L, "Pepe", 20, null);
        User nullAllergiesB = new User(1L, "Pepe", 20, null);
        User nullFieldsA = new User(null, null, 20, null);
        User nullFieldsB = new User(null, null, 20, null);

        assertEquals(baseUser, baseUser);
        assertEquals(baseUser, sameValuesUser);
        assertEquals(baseUser.hashCode(), sameValuesUser.hashCode());

        assertEquals(nullAllergiesA, nullAllergiesB);
        assertEquals(nullFieldsA, nullFieldsB);
        assertEquals(nullFieldsA.hashCode(), nullFieldsB.hashCode());
        assertNotEquals(nullFieldsA, baseUser);
        assertNotEquals(baseUser, nullFieldsA);
        assertNotEquals(nullAllergiesA, baseUser);
        assertNotEquals(baseUser, nullAllergiesA);

        assertNotEquals(baseUser, null);
        assertNotEquals(baseUser, "not-a-user");
        assertNotEquals(baseUser, new User(2L, "Pepe", 20, allergies));
        assertNotEquals(baseUser, new User(1L, "Paco", 20, allergies));
        assertNotEquals(baseUser, new User(1L, "Pepe", 21, allergies));
        assertNotEquals(baseUser, new User(1L, "Pepe", 20, List.of(new Allergy("Dust", 1))));
        assertNotEquals(baseUser, new User(null, "Pepe", 20, allergies));
        assertNotEquals(baseUser, new User(1L, null, 20, allergies));
        assertNotEquals(new User(1L, null, 20, allergies), baseUser);
    }

    @Test
    void testEqualsWithSubclassMatch() {
        User baseUser = new User(1L, "Pepe", 20, List.of(new Allergy("Pollen", 2)));
        User childUser = new UserChild(1L, "Pepe", 20, List.of(new Allergy("Pollen", 2)));

        assertEquals(baseUser, childUser);
        assertEquals(childUser, baseUser);
        assertEquals(baseUser.hashCode(), childUser.hashCode());
    }

    @Test
    void testEqualsWithStrictSubclassMismatch() {
        User baseUser = new User(1L, "Pepe", 20, List.of(new Allergy("Pollen", 2)));
        User strictChildUser = new UserChildStrict(1L, "Pepe", 20, List.of(new Allergy("Pollen", 2)));

        assertFalse(baseUser.equals(strictChildUser));
        assertTrue(strictChildUser.equals(baseUser));
    }

    @Test
    void testToStringContainsEssentialFields() {
        User user = new User(1L, "Pepe", 20, null);
        String toStringOutput = user.toString();

        assertTrue(toStringOutput.contains("User"));
        assertTrue(toStringOutput.contains("Pepe"));
        assertTrue(toStringOutput.contains("20"));
    }
}
