package com.example.controleprojetos.dto;

import java.time.LocalDate;
import java.util.List;

public record DadosProjetoDTO(
    Integer id,
    String descricao,
    LocalDate dataInicio,
    LocalDate dataFim,
    List<DadosFuncionarioDTO> funcionarios
) { }