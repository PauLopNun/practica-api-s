package com.exampleinyection.clase2parte2.model;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String nombre;
    private int edad;
    @Valid
    private List<Allergy> allergy;
}