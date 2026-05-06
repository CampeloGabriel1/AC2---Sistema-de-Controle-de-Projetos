package com.example.controleprojetos.dto;

import java.time.LocalDate;

public record ProjetoDTO(
    String descricao,
    LocalDate dataInicio,
    LocalDate dataFim
) { }