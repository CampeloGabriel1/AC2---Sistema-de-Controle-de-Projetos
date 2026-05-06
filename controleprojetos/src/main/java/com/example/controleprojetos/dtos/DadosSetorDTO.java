package com.example.controleprojetos.dtos;

import java.util.List;

public record DadosSetorDTO(
    Integer id,
    String nome,
    List<DadosFuncionarioDTO> funcionarios
) { }