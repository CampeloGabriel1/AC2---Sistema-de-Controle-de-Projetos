package com.example.controleprojetos.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.controleprojetos.dtos.DadosFuncionarioDTO;
import com.example.controleprojetos.dtos.DadosProjetoDTO;
import com.example.controleprojetos.dtos.FuncionarioDTO;
import com.example.controleprojetos.service.FuncionarioService;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionar(@RequestBody FuncionarioDTO funcionarioDTO) {
        funcionarioService.adicionar(funcionarioDTO);
    }

    @GetMapping
    public java.util.List<DadosFuncionarioDTO> listarFuncionarios() {
        return funcionarioService.listarFuncionarios();
    }

    @GetMapping("/{idFuncionario}/projetos")
    public List<DadosProjetoDTO> buscarProjetos(@PathVariable Integer idFuncionario) {
        return funcionarioService.buscarProjetos(idFuncionario);
    }
}