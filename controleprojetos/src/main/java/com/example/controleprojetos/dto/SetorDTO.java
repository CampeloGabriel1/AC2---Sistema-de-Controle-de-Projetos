package com.example.controleprojetos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SetorDTO {
    private Integer id;
    private String nome;
    
    public SetorDTO(String nome) {
        this.nome = nome;
    }
}
