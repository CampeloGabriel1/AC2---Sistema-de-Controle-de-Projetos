package com.example.controleprojetos.service;

import org.springframework.stereotype.Service;

import com.example.controleprojetos.dtos.DadosFuncionarioDTO;
import com.example.controleprojetos.dtos.DadosSetorDTO;
import com.example.controleprojetos.dtos.SetorDTO;
import com.example.controleprojetos.exceptions.RegraNegocioException;
import com.example.controleprojetos.models.Setor;
import com.example.controleprojetos.repositories.SetorRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SetorServiceImpl implements SetorService {
    private final SetorRepository setorRepository;

    public List<DadosSetorDTO> listarSetores() {
        return setorRepository.findAllWithFuncionarios().stream()
            .map(setor -> new DadosSetorDTO(
                setor.getId(),
                setor.getNome(),
                setor.getFuncionarios() == null ? List.of() : setor.getFuncionarios().stream()
                    .map(funcionario -> new DadosFuncionarioDTO(funcionario.getId(), funcionario.getNome()))
                    .toList()
            ))
            .toList();
    }

    @Transactional
    public void adicionar(SetorDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("Nome do setor não pode estar vazio.");
        }
        Setor setor = new Setor(dto.getNome());
        setorRepository.save(setor);
    }

    public DadosSetorDTO buscarSetorPorId(Integer id) {
        Setor setor = setorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado."));

        var funcionariosDTO = setor.getFuncionarios().stream()
            .map(f -> new DadosFuncionarioDTO(f.getId(), f.getNome()))
            .toList();

        return new DadosSetorDTO(setor.getId(), setor.getNome(), funcionariosDTO);
    }
}
