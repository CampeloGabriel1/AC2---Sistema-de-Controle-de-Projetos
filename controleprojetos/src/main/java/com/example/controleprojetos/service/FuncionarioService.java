package com.example.controleprojetos.service;

import org.springframework.stereotype.Service;

import com.example.controleprojetos.dto.DadosFuncionarioDTO;
import com.example.controleprojetos.dto.DadosProjetoDTO;
import com.example.controleprojetos.dto.FuncionarioDTO;
import com.example.controleprojetos.models.Funcionario;
import com.example.controleprojetos.models.Projeto;
import com.example.controleprojetos.repositories.FuncionarioRepository;
import com.example.controleprojetos.repositories.SetorRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    private final SetorRepository setorRepository;

    @Transactional
    public void adicionar(FuncionarioDTO dto) {
        // Validar se nome não está vazio
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new IllegalArgumentException("Nome do funcionário não pode estar vazio.");
        }
        
        Funcionario funcionario = new Funcionario(dto.nome());
        if (dto.setorId() != null) {
            funcionario.setSetor(setorRepository.findById(dto.setorId())
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado.")));
        }
        funcionarioRepository.save(funcionario);
    }

    public List<DadosFuncionarioDTO> listarFuncionarios() {
        return funcionarioRepository.findAll().stream()
            .map(funcionario -> new DadosFuncionarioDTO(funcionario.getId(), funcionario.getNome()))
            .toList();
    }

    public List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario) {
        // Validar se funcionário existe
        if (!funcionarioRepository.existsById(idFuncionario)) {
            throw new EntityNotFoundException("Funcionário não encontrado.");
        }
        
        // Buscar projetos vinculados ao funcionário
        List<Projeto> projetos = funcionarioRepository.findProjetosByFuncionarioId(idFuncionario);
        
        // Mapear para DTO de saída
        return projetos.stream()
            .map(p -> new DadosProjetoDTO(
                p.getId(),
                p.getDescricao(),
                p.getDataInicio(),
                p.getDataFim(),
                p.getFuncionarios().stream()
                    .map(f -> new DadosFuncionarioDTO(f.getId(), f.getNome()))
                    .toList()
            ))
            .toList();
    }
}