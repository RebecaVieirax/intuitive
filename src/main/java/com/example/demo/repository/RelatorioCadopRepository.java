package com.example.demo.repository;

import com.example.demo.model.RelatorioCadop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelatorioCadopRepository extends JpaRepository<RelatorioCadop, String> {

    // Busca por representante (ignora maiúsculas e minúsculas, e retorna nomes similares)
    List<RelatorioCadop> findByRepresentanteContainingIgnoreCase(String representante);

    // Busca exata pelo CNPJ
    List<RelatorioCadop> findByCnpj(String cnpj);

    // Busca por modalidade (retorna todas as operadoras com a modalidade especificada)
    List<RelatorioCadop> findByModalidadeContainingIgnoreCase(String modalidade);


}
