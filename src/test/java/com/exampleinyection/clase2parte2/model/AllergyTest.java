package com.exampleinyection.clase2parte2.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class AllergyTest {
    @Test
    void testAllergy() {
        Allergy allergy = new Allergy("Pollen", 5);
        assertEquals("Pollen", allergy.getName());
        assertEquals(5, allergy.getSeverity());
        allergy.setName("Dust");
        allergy.setSeverity(2);
        assertEquals("Dust", allergy.getName());
        assertEquals(2, allergy.getSeverity());
        allergy.toString();
        allergy.hashCode();
        allergy.equals(new Allergy("Dust", 2));
    }
}
