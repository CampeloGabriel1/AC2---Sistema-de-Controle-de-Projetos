package com.example.controleprojetos.service;

import com.example.controleprojetos.dtos.DadosSetorDTO;
import com.example.controleprojetos.dtos.SetorDTO;
import java.util.List;

public interface SetorService {
    void adicionar(SetorDTO dto);
    DadosSetorDTO buscarSetorPorId(Integer id);
    List<DadosSetorDTO> listarSetores();
}