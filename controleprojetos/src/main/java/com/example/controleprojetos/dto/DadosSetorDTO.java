package com.example.controleprojetos.dto;

import java.util.List;

public record DadosSetorDTO(
    Integer id,
    String nome,
    List<DadosFuncionarioDTO> funcionarios
) { }