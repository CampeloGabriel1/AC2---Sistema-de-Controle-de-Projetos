package com.example.controleprojetos.dtos;

import java.time.LocalDate;
import java.util.List;

public record DadosProjetoDTO(
    Integer id,
    String descricao,
    LocalDate dataInicio,
    LocalDate dataFim,
    List<DadosFuncionarioDTO> funcionarios
) { }