package com.example.zerakiassessment.repository;


import com.example.zerakiassessment.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<Institution,Long> {
    Optional<Institution> findTopByNameEqualsIgnoreCase(String name);

    List<Institution> findAllByOrderByNameDesc();

    List<Institution> findAllByNameContainingIgnoreCase(String name);

    boolean existsByNameEqualsIgnoreCaseAndIdNot(String name,long id);




}
