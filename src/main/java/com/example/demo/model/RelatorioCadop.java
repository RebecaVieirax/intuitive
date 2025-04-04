package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "relatorio_cadop", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RelatorioCadop {

    @Id
    @Column(name = "registro_ans", columnDefinition = "TEXT")
    private String registroAns;

    @Column(name = "cnpj", columnDefinition = "TEXT", nullable = false)
    private String cnpj;

    @Column(name = "razao_social", columnDefinition = "TEXT", nullable = false)
    private String razaoSocial;

    @Column(name = "nome_fantasia", columnDefinition = "TEXT")
    private String nomeFantasia;

    @Column(name = "modalidade", columnDefinition = "TEXT")
    private String modalidade;

    @Column(name = "logradouro", columnDefinition = "TEXT")
    private String logradouro;

    @Column(name = "numero", columnDefinition = "TEXT")
    private String numero;

    @Column(name = "complemento", columnDefinition = "TEXT")
    private String complemento;

    @Column(name = "bairro", columnDefinition = "TEXT")
    private String bairro;

    @Column(name = "cidade", columnDefinition = "TEXT")
    private String cidade;

    @Column(name = "uf", length = 2)
    private String uf;

    @Column(name = "cep", columnDefinition = "TEXT")
    private String cep;

    @Column(name = "ddd", columnDefinition = "TEXT")
    private String ddd;

    @Column(name = "telefone", columnDefinition = "TEXT")
    private String telefone;

    @Column(name = "fax", columnDefinition = "TEXT")
    private String fax;

    @Column(name = "endereco_eletronico", columnDefinition = "TEXT")
    private String enderecoEletronico;

    @Column(name = "representante", columnDefinition = "TEXT")
    private String representante;

    @Column(name = "cargo_representante", columnDefinition = "TEXT")
    private String cargoRepresentante;

    @Column(name = "regiao_de_comercializacao", columnDefinition = "TEXT")
    private String regiaoDeComercializacao;

    @Column(name = "data_registro_ans")
    private LocalDate dataRegistroAns;
}

