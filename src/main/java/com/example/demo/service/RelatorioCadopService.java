package com.example.demo.service;

import com.example.demo.exception.RecursoNaoEncontradoException;
import com.example.demo.model.RelatorioCadop;
import com.example.demo.repository.RelatorioCadopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelatorioCadopService {

    private final RelatorioCadopRepository relatorioCadopRepository;

    public RelatorioCadopService(RelatorioCadopRepository relatorioCadopRepository) {
        this.relatorioCadopRepository = relatorioCadopRepository;
    }

    // Buscar todas as operadoras cadastradas
    public List<RelatorioCadop> buscarTodos() {
        List<RelatorioCadop> operadoras = relatorioCadopRepository.findAll();
        if (operadoras.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhuma operadora encontrada.");
        }
        return operadoras;
    }

    // Buscar por representante (busca aproximada, ignorando maiúsculas e minúsculas)
    public List<RelatorioCadop> buscarPorRepresentante(String nome) {
        List<RelatorioCadop> operadoras = relatorioCadopRepository.findByRepresentanteContainingIgnoreCase(nome);
        if (operadoras.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhuma operadora encontrada para o representante: " + nome);
        }
        return operadoras;
    }

    // Buscar por CNPJ (busca exata)
    public List<RelatorioCadop> buscarPorCnpj(String cnpj) {
        List<RelatorioCadop> operadoras = relatorioCadopRepository.findByCnpj(cnpj);
        if (operadoras.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhuma operadora encontrada com o CNPJ: " + cnpj);
        }
        return operadoras;
    }

    // Buscar por modalidade (busca aproximada, ignorando maiúsculas e minúsculas)
    public List<RelatorioCadop> buscarPorModalidade(String modalidade) {
        List<RelatorioCadop> operadoras = relatorioCadopRepository.findByModalidadeContainingIgnoreCase(modalidade);
        if (operadoras.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhuma operadora encontrada para a modalidade: " + modalidade);
        }
        return operadoras;
    }
}
