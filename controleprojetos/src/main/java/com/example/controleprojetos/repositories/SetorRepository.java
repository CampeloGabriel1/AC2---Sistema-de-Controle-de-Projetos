package com.example.controleprojetos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.controleprojetos.models.Setor;

public interface SetorRepository extends JpaRepository<Setor, Integer> {
    // d. Lista todos os setores com seus funcionários vinculados
    @Query("SELECT DISTINCT s FROM Setor s LEFT JOIN FETCH s.funcionarios")
    List<Setor> findAllWithFuncionarios();
}