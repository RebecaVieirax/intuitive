# Intuitive - Backend para Case Técnico ANS

Este repositório contém toda a implementação **backend** do case técnico proposto, utilizando Java com Spring Boot e PostgreSQL.

## 📌 Descrição dos Testes Atendidos

### 1. Teste de Web Scraping
- Implementado o acesso automatizado ao site da ANS.
- Download dos anexos I e II (PDFs).
- Compactação dos arquivos em um único `.zip`.

### 2. Teste de Transformação de Dados
- Extração completa dos dados da tabela "Rol de Procedimentos e Eventos em Saúde" (Anexo I - PDF).
- Salvamento dos dados em um arquivo `.csv`.
- Compactação do `.csv` em um `.zip` nomeado como `Teste_Rebeca.zip`.
- Substituição das siglas OD e AMB pelas descrições completas.

### 3. Teste de Banco de Dados
- Scripts SQL compatíveis com PostgreSQL para:
  - Estruturação das tabelas.
  - Importação dos dados de demonstrações contábeis e dados cadastrais de operadoras.
- Queries analíticas que respondem:
  - As 10 operadoras com maiores despesas em “EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS DE ASSISTÊNCIA A SAÚDE MÉDICO HOSPITALAR” no **último trimestre**.
  - As 10 operadoras com maiores despesas nessa categoria no **último ano**.

### 4. Teste de API
- API RESTful construída com Spring Boot.
- Endpoints para busca de operadoras por:
  - Nome do representante (busca aproximada).
  - CNPJ (busca exata).
  - Modalidade (busca exata, case-insensitive).
- Exemplo de consumo com `curl` disponível na documentação do projeto ou no Postman.

## 🚀 Como rodar o projeto

1. **Pré-requisitos:**
   - Java 17+
   - Maven 3+
   - PostgreSQL

2. **Clone o projeto:**
   ```bash
   git clone https://github.com/RebecaVieirax/intuitive.git
   cd intuitive
