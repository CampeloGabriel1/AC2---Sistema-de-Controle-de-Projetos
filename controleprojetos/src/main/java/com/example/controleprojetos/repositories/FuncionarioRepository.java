package com.example.controleprojetos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.controleprojetos.models.Funcionario;
import com.example.controleprojetos.models.Projeto;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    @Query("SELECT p FROM Projeto p JOIN p.funcionarios f WHERE f.id = :idFuncionario")
    List<Projeto> findProjetosByFuncionarioId(@Param("idFuncionario") Integer idFuncionario);
}