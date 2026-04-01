package com.exampleinyection.clase2parte2.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AllergyTest {

    private static class AllergyChild extends Allergy {
        AllergyChild(String name, int severity) {
            super(name, severity);
        }
    }

    private static class AllergyChildStrict extends Allergy {
        AllergyChildStrict(String name, int severity) {
            super(name, severity);
        }

        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }

    @Test
    void testGettersAndSetters() {
        Allergy allergy = new Allergy("Pollen", 5);

        assertEquals("Pollen", allergy.getName());
        assertEquals(5, allergy.getSeverity());

        allergy.setName("Dust");
        allergy.setSeverity(2);

        assertEquals("Dust", allergy.getName());
        assertEquals(2, allergy.getSeverity());
    }

    @Test
    void testAllergyGettersAndSetters() {
        Allergy allergy = new Allergy();
        allergy.setId(1L);
        allergy.setName("Pollen");
        allergy.setSeverity(3);
        User user = new User();
        user.setId(2L);
        allergy.setUser(user);

        assertEquals(1L, allergy.getId());
        assertEquals("Pollen", allergy.getName());
        assertEquals(3, allergy.getSeverity());
        assertEquals(user, allergy.getUser());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User();
        user.setId(1L);
        Allergy allergy = new Allergy(1L, "Cats", 5, user);

        assertEquals(1L, allergy.getId());
        assertEquals("Cats", allergy.getName());
        assertEquals(5, allergy.getSeverity());
        assertNotNull(allergy.getUser());
    }

    @Test
    void testEqualsAndHashCode() {
        Allergy baseAllergy = new Allergy("Dust", 2);
        Allergy sameValuesAllergy = new Allergy("Dust", 2);
        Allergy nullNameA = new Allergy(null, 2);
        Allergy nullNameB = new Allergy(null, 2);

        assertEquals(baseAllergy, baseAllergy);
        assertEquals(baseAllergy, sameValuesAllergy);
        assertEquals(baseAllergy.hashCode(), sameValuesAllergy.hashCode());
        
        assertEquals(nullNameA, nullNameB);
        assertEquals(nullNameA.hashCode(), nullNameB.hashCode());

        assertNotEquals(baseAllergy, null);
        assertNotEquals(baseAllergy, "not-an-allergy");
        assertNotEquals(baseAllergy, new Allergy("Pollen", 2));
        assertNotEquals(baseAllergy, new Allergy("Dust", 3));
        assertNotEquals(baseAllergy, nullNameA);
        assertNotEquals(nullNameA, baseAllergy);
    }

    @Test
    void testEqualsWithSubclassMatch() {
        Allergy baseAllergy = new Allergy("Dust", 2);
        Allergy childAllergy = new AllergyChild("Dust", 2);

        assertEquals(baseAllergy, childAllergy);
        assertEquals(childAllergy, baseAllergy);
        assertEquals(baseAllergy.hashCode(), childAllergy.hashCode());
    }

    @Test
    void testEqualsWithStrictSubclassMismatch() {
        Allergy baseAllergy = new Allergy("Dust", 2);
        Allergy strictChildAllergy = new AllergyChildStrict("Dust", 2);

        assertFalse(baseAllergy.equals(strictChildAllergy));
        assertTrue(strictChildAllergy.equals(baseAllergy));
    }

    @Test
    void testToStringContainsEssentialFields() {
        Allergy allergy = new Allergy("Dust", 2);
        String toStringOutput = allergy.toString();

        assertTrue(toStringOutput.contains("Allergy"));
        assertTrue(toStringOutput.contains("Dust"));
        assertTrue(toStringOutput.contains("2"));
    }
    
    @Test
    void testToDTO() {
        Allergy allergy = new Allergy(1L, "Pollen", 3, new User());
        Allergy dto = allergy.toDTO(allergy);
        
        assertNotNull(dto);
        assertEquals(allergy.getId(), dto.getId());
        assertEquals(allergy.getName(), dto.getName());
        assertEquals(allergy.getSeverity(), dto.getSeverity());
        assertNull(dto.getUser());
    }

    @Test
    void testToDTONull() {
        Allergy allergy = new Allergy();
        assertNull(allergy.toDTO(null));
    }
}
