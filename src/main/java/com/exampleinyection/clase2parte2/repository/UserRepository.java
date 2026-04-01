package com.exampleinyection.clase2parte2.repository;

import com.exampleinyection.clase2parte2.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    int updateNameById(@Param("id") Long id, @Param("name") String name);
    @EntityGraph(attributePaths = {"allergies"})
    @org.springframework.data.jpa.repository.Query("SELECT u FROM User u")
    List<User> findAllWithAllergies();

    List<User> findByNameContainingIgnoreCase(String name);
}
