package com.exampleinyection.clase2parte2.repository;

import com.exampleinyection.clase2parte2.model.Allergy;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT a FROM Allergy a")
    List<Allergy> findAllWithAllergies();
}
