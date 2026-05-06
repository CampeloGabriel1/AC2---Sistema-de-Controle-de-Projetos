package com.example.controleprojetos.service;

import org.springframework.stereotype.Service;

import com.example.controleprojetos.dtos.DadosFuncionarioDTO;
import com.example.controleprojetos.dtos.DadosProjetoDTO;
import com.example.controleprojetos.dtos.ProjetoDTO;
import com.example.controleprojetos.exceptions.RegraNegocioException;
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
public class ProjetoServiceImpl implements ProjetoService {
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
        if (dto.dataInicio().isAfter(dto.dataFim())) {
            throw new RegraNegocioException("A data de início não pode ser posterior à data final.");
        }

        Projeto projeto = new Projeto(dto.descricao(), dto.dataInicio(), dto.dataFim());
        projeto.setFuncionarios(new ArrayList<>());
        projetoRepository.save(projeto);
    }

    public DadosProjetoDTO buscarProjetoPorId(Integer id) {
        Projeto projeto = projetoRepository.findByIdWithFuncionarios(id)
            .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado."));

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
        Projeto projeto = projetoRepository.findById(idProjeto)
            .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado."));

        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
            .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado."));

        if (!projeto.getFuncionarios().contains(funcionario)) {
            projeto.getFuncionarios().add(funcionario);
            projetoRepository.save(projeto);
        } else {
            throw new RegraNegocioException("Funcionário já está vinculado a este projeto.");
        }
    }
}
