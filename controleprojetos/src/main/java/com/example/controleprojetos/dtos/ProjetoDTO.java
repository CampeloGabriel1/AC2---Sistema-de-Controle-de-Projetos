package com.example.controleprojetos.dtos;

import java.time.LocalDate;

public record ProjetoDTO(
    String descricao,
    LocalDate dataInicio,
    LocalDate dataFim
) { }