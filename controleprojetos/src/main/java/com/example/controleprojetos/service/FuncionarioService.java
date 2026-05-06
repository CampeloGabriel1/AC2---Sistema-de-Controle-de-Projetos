package com.example.controleprojetos.service;

import com.example.controleprojetos.dtos.DadosFuncionarioDTO;
import com.example.controleprojetos.dtos.DadosProjetoDTO;
import com.example.controleprojetos.dtos.FuncionarioDTO;
import java.util.List;

public interface FuncionarioService {
    void adicionar(FuncionarioDTO dto);
    List<DadosFuncionarioDTO> listarFuncionarios();
    List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario);
}