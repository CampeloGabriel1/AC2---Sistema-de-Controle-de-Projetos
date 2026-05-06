package com.example.controleprojetos.service;

import org.springframework.stereotype.Service;

import com.example.controleprojetos.dto.DadosFuncionarioDTO;
import com.example.controleprojetos.dto.DadosSetorDTO;
import com.example.controleprojetos.dto.SetorDTO;
import com.example.controleprojetos.models.Setor;
import com.example.controleprojetos.repositories.SetorRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SetorService {
    private final SetorRepository setorRepository;

    public java.util.List<DadosSetorDTO> listarSetores() {
        return setorRepository.findAllWithFuncionarios().stream()
            .map(setor -> new DadosSetorDTO(
                setor.getId(),
                setor.getNome(),
                setor.getFuncionarios() == null ? java.util.List.of() : setor.getFuncionarios().stream()
                    .map(funcionario -> new DadosFuncionarioDTO(funcionario.getId(), funcionario.getNome()))
                    .toList()
            ))
            .toList();
    }

    @Transactional
    public void adicionar(SetorDTO dto) {
        // Validar se nome não está vazio
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do setor não pode estar vazio.");
        }
        
        Setor setor = new Setor(dto.getNome());
        setorRepository.save(setor);
    }

    public DadosSetorDTO buscarSetorPorId(Integer id) {
        // Buscar setor com funcionários vinculados
        Setor setor = setorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado."));
        
        // Mapear funcionários para DTO
        var funcionariosDTO = setor.getFuncionarios().stream()
            .map(f -> new DadosFuncionarioDTO(f.getId(), f.getNome()))
            .toList();
        
        return new DadosSetorDTO(setor.getId(), setor.getNome(), funcionariosDTO);
    }
}