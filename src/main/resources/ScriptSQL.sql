--Criação de tabela de demonstracoes_contabeis com a tipagem correta.

CREATE TABLE demonstracoes_contabeis (

    data DATE NOT NULL,
    reg_ans INTEGER NOT NULL,
    cd_conta_contabil INTEGER NOT NULL,
    descricao TEXT NOT NULL,
    vl_saldo_inicial NUMERIC(15,2) NOT NULL,
    vl_saldo_final NUMERIC(15,2) NOT NULL
);

--Criação de tabela de demonstracoes_contabeis com a tipagem text para ajustar depois.
CREATE TABLE demonstracoes_contabeis_temp (
    data TEXT,
    reg_ans TEXT,
    cd_conta_contabil TEXT,
    descricao TEXT,
    vl_saldo_inicial TEXT,
    vl_saldo_final TEXT
);

--Importa os dados das planilhas como o exemplo a baixo de 1T2024.csv
COPY demonstracoes_contabeis_temp
FROM 'C:/tabelas/1T2024/1T2024.csv'
DELIMITER ';'
CSV HEADER
ENCODING 'UTF8';


--insere as informaçãoes tratando as datas e os valores de saldo inicial e final.
INSERT INTO demonstracoes_contabeis (data, reg_ans, cd_conta_contabil, descricao, vl_saldo_inicial, vl_saldo_final)
SELECT
    CASE
        WHEN data LIKE '__/__/____' THEN TO_DATE(data, 'DD/MM/YYYY')
        WHEN data LIKE '____-__-__' THEN TO_DATE(data, 'YYYY-MM-DD')
        ELSE NULL
    END,
    reg_ans::INTEGER,
    cd_conta_contabil::INTEGER,
    descricao,
    REPLACE(vl_saldo_inicial, ',', '.')::NUMERIC,
    REPLACE(vl_saldo_final, ',', '.')::NUMERIC
FROM demonstracoes_contabeis_temp;

-verifica valores
SELECT * FROM demonstracoes_contabeis;

--Criação de tabela relatorio_cadop

CREATE TABLE IF NOT EXISTS public.relatorio_cadop
(
    registro_ans text COLLATE pg_catalog."default",
    cnpj text COLLATE pg_catalog."default",
    razao_social text COLLATE pg_catalog."default",
    nome_fantasia text COLLATE pg_catalog."default",
    modalidade text COLLATE pg_catalog."default",
    logradouro text COLLATE pg_catalog."default",
    numero text COLLATE pg_catalog."default",
    complemento text COLLATE pg_catalog."default",
    bairro text COLLATE pg_catalog."default",
    cidade text COLLATE pg_catalog."default",
    uf character(2) COLLATE pg_catalog."default",
    cep text COLLATE pg_catalog."default",
    ddd text COLLATE pg_catalog."default",
    telefone text COLLATE pg_catalog."default",
    fax text COLLATE pg_catalog."default",
    endereco_eletronico text COLLATE pg_catalog."default",
    representante text COLLATE pg_catalog."default",
    cargo_representante text COLLATE pg_catalog."default",
    regiao_de_comercializacao text COLLATE pg_catalog."default",
    data_registro_ans date
)

--para importar os dados

COPY public.relatorio_cadop
FROM 'C:/tabelas/Relatorio_cadop.csv'
DELIMITER ';'
CSV HEADER
ENCODING 'UTF8';

--queries de busca

WITH datas_disponiveis AS (
    SELECT DISTINCT data
    FROM demonstracoes_contabeis
),
ultima_data AS (
    SELECT MAX(data) AS data_mais_recente
    FROM datas_disponiveis
),
trimestre_alvo AS (
    SELECT
        DATE_TRUNC('quarter', data_mais_recente) AS inicio_trimestre,
        (DATE_TRUNC('quarter', data_mais_recente) + INTERVAL '3 months - 1 day')::DATE AS fim_trimestre
    FROM ultima_data
)
SELECT
    rc.razao_social,
    SUM(dc.vl_saldo_final - dc.vl_saldo_inicial) AS total_despesa
FROM demonstracoes_contabeis dc
JOIN relatorio_cadop rc ON LPAD(dc.reg_ans::TEXT, 6, '0') = LPAD(rc.registro_ans, 6, '0')
JOIN trimestre_alvo ta ON dc.data BETWEEN ta.inicio_trimestre AND ta.fim_trimestre
WHERE UPPER(TRIM(dc.descricao)) = 'EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS  DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR'
GROUP BY rc.razao_social
ORDER BY total_despesa DESC
LIMIT 10;

SELECT
    rc.razao_social,
    SUM(dc.vl_saldo_final - dc.vl_saldo_inicial) AS total_despesa
FROM demonstracoes_contabeis dc
JOIN relatorio_cadop rc ON LPAD(dc.reg_ans::TEXT, 6, '0') = LPAD(rc.registro_ans, 6, '0')
WHERE UPPER(TRIM(dc.descricao)) = 'EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS  DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR'
  AND dc.data >= CURRENT_DATE - INTERVAL '1 year'
GROUP BY rc.razao_social
ORDER BY total_despesa DESC
LIMIT 10;