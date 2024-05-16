CREATE TABLE musico (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50),
    descricao TEXT,
    senha VARCHAR(100),
    cache NUMERIC(10, 2),
    instrumento VARCHAR(50),
    objetivo VARCHAR(100),
    estilo VARCHAR(50)
);


INSERT INTO musico (nome, descricao, senha, cache, instrumento, objetivo, estilo)
VALUES 
    ('Arthur', 'Arthur, 25 anos, guitarrista talentoso buscando novos sons e experiências.', 'senha123', 1200.00, 'Guitarra', 'Para tocar músicas acústicas', 'Rock');


INSERT INTO musico (nome, descricao, senha, cache, instrumento, objetivo, estilo)
VALUES 
    ('Breno', 'Breno, 22 anos, tecladista profissional procurando inovação e qualidade sonora.', 'tecl@123', 800.50, 'Teclado', 'Produção musical e performances ao vivo', 'Pop');


INSERT INTO musico (nome, descricao, senha, cache, instrumento, objetivo, estilo)
VALUES 
    ('Tinoco', 'Tinoco, 18 anos, baterista experiente em busca de equipamento completo e versátil.', 'bateria456', 2500.75, 'Bateria', 'Para músicos profissionais e ensaios intensivos', 'Metal');


CREATE TABLE banda (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50),
    descricao TEXT,
    senha VARCHAR(100),
    cache NUMERIC(10, 2),
    dataCriacao DATE,
    objetivo VARCHAR(100),
    estilo VARCHAR(50) -- Correção: removi o caractere especial ' ' que estava antes de VARCHAR
);

INSERT INTO banda (nome, descricao, senha, cache, dataCriacao, objetivo, estilo) 
VALUES ('The Thunderbirds', 'Uma banda de rock energético e poderoso.', 'thunder123', 700.00, '2023-05-10', 'Fazer turnê nacional', 'Rock');

INSERT INTO banda (nome, descricao, senha, cache, dataCriacao, objetivo, estilo) 
VALUES ('Neon Dreams', 'Uma banda de pop com vibes futurísticas.', 'neon2024!', 1200.00, '2022-09-25', 'Lançar um EP autoral', 'Pop');


INSERT INTO banda (nome, descricao, senha, cache, dataCriacao, objetivo, estilo) 
VALUES ('Midnight Serenade', 'Uma banda de jazz suave e envolvente.', 'serenade@midnight', 400.75, '2024-02-12', 'Realizar concertos intimistas', 'Jazz');

CREATE TABLE casadeshows (
    id SERIAL PRIMARY KEY,
    nomeCasa VARCHAR(100),
    nomeDono VARCHAR(50),
    valor NUMERIC(10, 2),
    endereco TEXT,
    telefone VARCHAR(20),
    horario TIME
);

INSERT INTO casadeshows (nomeCasa, nomeDono, valor, endereco, telefone, horario) 
VALUES 
    ('Rock Palace', 'João Silva', 50.00, 'Rua das Estrelas, 123', '(31) 98765-4321', '20:00'),
    ('Pop Arena', 'Maria Souza', 40.00, 'Av. dos Sonhos, 456', '(31) 12345-6789', '19:30'),
    ('Jazz Lounge', 'Carlos Oliveira', 60.00, 'Praça das Notas, 789', '(31) 55555-9999', '21:00'); 

SELECT * FROM banda;
SELECT * FROM casadeshows;
SELECT * FROM banda;
