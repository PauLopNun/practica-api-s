package com.exampleinyection.clase2parte2.dto;

import com.exampleinyection.clase2parte2.model.Allergy;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UserRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Min(value = 0, message = "La edad no puede ser negativa")
        int edad,

        List<Allergy> allergy
) {}