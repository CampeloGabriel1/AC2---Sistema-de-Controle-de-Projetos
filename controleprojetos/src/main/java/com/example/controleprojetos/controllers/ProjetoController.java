package com.example.controleprojetos.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.controleprojetos.dto.DadosProjetoDTO;
import com.example.controleprojetos.dto.ProjetoDTO;
import com.example.controleprojetos.service.ProjetoService;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionar(@RequestBody ProjetoDTO projetoDTO) {
        projetoService.adicionar(projetoDTO);
    }

    @GetMapping
    public List<DadosProjetoDTO> listarProjetos() {
        return projetoService.listarProjetos();
    }

    @GetMapping("/{id}")
    public DadosProjetoDTO buscarProjetoPorId(@PathVariable Integer id) {
        return projetoService.buscarProjetoPorId(id);
    }


    @PostMapping("/{idProjeto}/funcionarios/{idFuncionario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vincularFuncionario(
            @PathVariable Integer idProjeto,
            @PathVariable Integer idFuncionario) {
        projetoService.vincularFuncionario(idProjeto, idFuncionario);
    }
}