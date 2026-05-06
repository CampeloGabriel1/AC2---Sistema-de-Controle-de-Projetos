package com.example.controleprojetos.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.controleprojetos.dtos.DadosSetorDTO;
import com.example.controleprojetos.dtos.SetorDTO;
import com.example.controleprojetos.service.SetorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/setores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SetorController {

    private final SetorService setorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionar(@RequestBody SetorDTO setorDTO) {
        setorService.adicionar(setorDTO);
    }

    @GetMapping
    public java.util.List<DadosSetorDTO> listarSetores() {
        return setorService.listarSetores();
    }

    @GetMapping("/{idSetor}")
    public DadosSetorDTO buscarSetorPorId(@PathVariable Integer idSetor) {
        return setorService.buscarSetorPorId(idSetor);
    }
}