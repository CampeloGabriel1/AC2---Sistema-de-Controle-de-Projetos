package com.example.controleprojetos.service;

import com.example.controleprojetos.dtos.DadosProjetoDTO;
import com.example.controleprojetos.dtos.ProjetoDTO;
import java.util.List;

public interface ProjetoService {
    List<DadosProjetoDTO> listarProjetos();
    void adicionar(ProjetoDTO dto);
    DadosProjetoDTO buscarProjetoPorId(Integer id);
    void vincularFuncionario(Integer idProjeto, Integer idFuncionario);
}