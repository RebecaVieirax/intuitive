package com.example.demo.controller;

import com.example.demo.model.RelatorioCadop;
import com.example.demo.service.RelatorioCadopService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operadoras")
public class RelatorioCadopController {

    private final RelatorioCadopService operadoraService;

    public RelatorioCadopController(RelatorioCadopService operadoraService) {
        this.operadoraService = operadoraService;
    }

    // Busca todas as operadoras
    @GetMapping
    public List<RelatorioCadop> buscarTodasOperadoras() {
        return operadoraService.buscarTodos();
    }

    // Busca por representante (busca aproximada)
    @GetMapping("/representante")
    public List<RelatorioCadop> buscarPorRepresentante(@RequestParam String nome) {
        return operadoraService.buscarPorRepresentante(nome);
    }

    // Busca por CNPJ (busca exata)
    @GetMapping("/cnpj")
    public List<RelatorioCadop> buscarPorCnpj(@RequestParam String cnpj) {
        return operadoraService.buscarPorCnpj(cnpj);
    }

    // Busca por modalidade (busca exata, mas sem diferenciar maiúsculas e minúsculas)
    @GetMapping("/modalidade")
    public List<RelatorioCadop> buscarPorModalidade(@RequestParam String modalidade) {
        return operadoraService.buscarPorModalidade(modalidade);
    }
}
