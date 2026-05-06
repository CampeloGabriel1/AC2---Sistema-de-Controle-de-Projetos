package com.example.controleprojetos.service;

import org.springframework.stereotype.Service;

import com.example.controleprojetos.dto.DadosFuncionarioDTO;
import com.example.controleprojetos.dto.DadosProjetoDTO;
import com.example.controleprojetos.dto.ProjetoDTO;
import com.example.controleprojetos.models.Funcionario;
import com.example.controleprojetos.models.Projeto;
import com.example.controleprojetos.repositories.FuncionarioRepository;
import com.example.controleprojetos.repositories.ProjetoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetoService {
    private final ProjetoRepository projetoRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Transactional
    public List<DadosProjetoDTO> listarProjetos() {
        return projetoRepository.findAll().stream()
            .map(projeto -> new DadosProjetoDTO(
                projeto.getId(),
                projeto.getDescricao(),
                projeto.getDataInicio(),
                projeto.getDataFim(),
                projeto.getFuncionarios() == null ? List.of() : projeto.getFuncionarios().stream()
                    .map(funcionario -> new DadosFuncionarioDTO(funcionario.getId(), funcionario.getNome()))
                    .toList()
            ))
            .toList();
    }

    @Transactional
    public void adicionar(ProjetoDTO dto) {
        // Validar se data de início não é posterior à data fim
        if (dto.dataInicio().isAfter(dto.dataFim())) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data final.");
        }
        
        // Criar novo projeto com funcionários inicializados
        Projeto projeto = new Projeto(dto.descricao(), dto.dataInicio(), dto.dataFim());
        projeto.setFuncionarios(new ArrayList<>()); // Inicializar lista para evitar NPE
        projetoRepository.save(projeto);
    }

    public DadosProjetoDTO buscarProjetoPorId(Integer id) {
        // Buscar projeto com funcionários usando query personalizada
        Projeto projeto = projetoRepository.findByIdWithFuncionarios(id)
            .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado."));
        
        // Mapear funcionários para DTO
        var funcionariosDTO = projeto.getFuncionarios().stream()
            .map(f -> new DadosFuncionarioDTO(f.getId(), f.getNome()))
            .toList();
            
        return new DadosProjetoDTO(
            projeto.getId(), 
            projeto.getDescricao(), 
            projeto.getDataInicio(), 
            projeto.getDataFim(), 
            funcionariosDTO
        );
    }

    @Transactional
    public void vincularFuncionario(Integer idProjeto, Integer idFuncionario) {
        // Buscar projeto
        Projeto projeto = projetoRepository.findById(idProjeto)
            .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado."));
        
        // Buscar funcionário
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
            .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado."));

        // Validar se já não está vinculado
        if (!projeto.getFuncionarios().contains(funcionario)) {
            projeto.getFuncionarios().add(funcionario);
            projetoRepository.save(projeto);
        } else {
            throw new IllegalArgumentException("Funcionário já está vinculado a este projeto.");
        }
    }
}