package com.example.controleprojetos.service;

import org.springframework.stereotype.Service;

import com.example.controleprojetos.dtos.DadosFuncionarioDTO;
import com.example.controleprojetos.dtos.DadosProjetoDTO;
import com.example.controleprojetos.dtos.FuncionarioDTO;
import com.example.controleprojetos.exceptions.RegraNegocioException;
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
public class FuncionarioServiceImpl implements FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    private final SetorRepository setorRepository;

    @Transactional
    public void adicionar(FuncionarioDTO dto) {
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new RegraNegocioException("Nome do funcionário não pode estar vazio.");
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
        if (!funcionarioRepository.existsById(idFuncionario)) {
            throw new EntityNotFoundException("Funcionário não encontrado.");
        }

        List<Projeto> projetos = funcionarioRepository.findProjetosByFuncionarioId(idFuncionario);

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
