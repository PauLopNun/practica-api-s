package com.exampleinyection.clase2parte2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Allergy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int severity;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private User user;

    public Allergy(String name, int severity) {
        this.name = name;
        this.severity = severity;
    }
    
    public Allergy toDTO(Allergy allergy) {
        if (allergy == null) {
            return null;
        }

        Allergy dto = new Allergy();
        dto.setId(allergy.getId());
        dto.setName(allergy.getName());
        dto.setSeverity(allergy.getSeverity());
        return dto;
    }
}