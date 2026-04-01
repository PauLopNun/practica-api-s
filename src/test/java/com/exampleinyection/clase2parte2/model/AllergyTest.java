package com.exampleinyection.clase2parte2.model;

import org.junit.jupiter.api.Test;

import java.util.List;

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
        allergy.setUsers(List.of(user));

        assertEquals(1L, allergy.getId());
        assertEquals("Pollen", allergy.getName());
        assertEquals(3, allergy.getSeverity());
        assertEquals(1, allergy.getUsers().size());
        assertEquals(2L, allergy.getUsers().get(0).getId());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User();
        user.setId(1L);
        Allergy allergy = new Allergy(1L, "Cats", 5, List.of(user));

        assertEquals(1L, allergy.getId());
        assertEquals("Cats", allergy.getName());
        assertEquals(5, allergy.getSeverity());
        assertNotNull(allergy.getUsers());
        assertEquals(1, allergy.getUsers().size());
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
    void testUsersRelationshipNullByDefault() {
        Allergy allergy = new Allergy("Pollen", 1);
        assertNull(allergy.getUsers());
    }

    @Test
    void testAllArgsConstructorWithNullUsers() {
        Allergy allergy = new Allergy(1L, "Pollen", 3, null);
        assertEquals(1L, allergy.getId());
        assertNull(allergy.getUsers());
    }

    @Test
    void testEqualsWithDifferentIds() {
        Allergy idNull = new Allergy("Dust", 2);            // id = null
        Allergy idOne  = new Allergy(1L, "Dust", 2, null);  // id = 1L
        Allergy idTwo  = new Allergy(2L, "Dust", 2, null);  // id = 2L

        // null id vs non-null id → cubre rama: this.id==null && other.id!=null
        assertNotEquals(idNull, idOne);
        // non-null id vs distinto non-null id → cubre rama: this.id!=null && !equals(other.id)
        assertNotEquals(idOne, idTwo);
    }

    @Test
    void testEqualsAndHashCodeWithUsersField() {
        User user = new User();
        user.setId(1L);

        Allergy withUsers     = new Allergy(1L, "Pollen", 1, List.of(user));
        Allergy sameUsers     = new Allergy(1L, "Pollen", 1, List.of(user));
        Allergy withoutUsers  = new Allergy(1L, "Pollen", 1, null);
        Allergy differentUser = new Allergy(1L, "Pollen", 1, List.of(new User()));

        // non-null users == non-null users (same list) → equal
        assertEquals(withUsers, sameUsers);
        assertEquals(withUsers.hashCode(), sameUsers.hashCode());

        // non-null users vs null → not equal (both directions)
        assertNotEquals(withUsers, withoutUsers);
        assertNotEquals(withoutUsers, withUsers);

        // non-null users vs different non-null users → not equal
        assertNotEquals(withUsers, differentUser);
    }
}
