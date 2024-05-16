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
