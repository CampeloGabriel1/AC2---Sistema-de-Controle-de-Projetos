package com.example.controleprojetos.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.controleprojetos.models.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {
    // a. Retorna projeto com funcionários vinculados
    @Query("SELECT p FROM Projeto p LEFT JOIN FETCH p.funcionarios WHERE p.id = :id")
    Optional<Projeto> findByIdWithFuncionarios(@Param("id") Integer id);

    // b. Retorna projetos com início no intervalo
    @Query("SELECT p FROM Projeto p WHERE p.dataInicio BETWEEN :inicio AND :fim")
    List<Projeto> findByDataInicioBetween(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}