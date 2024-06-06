BandaMusico

CREATE TABLE BandaMusico (
    id SERIAL PRIMARY KEY,
    banda_id INT REFERENCES Banda(id),
    musico_id INT REFERENCES Musico(id),
    UNIQUE (banda_id, musico_id)
);

CasaMusico


CREATE TABLE CasaMusico (
    id SERIAL PRIMARY KEY,
    casa_id INT REFERENCES CasadeShows(id),
    musico_id INT REFERENCES Musico(id),
    UNIQUE (casa_id, musico_id)
);

Adicionar Coluna capacidade nas Tabelas

ALTER TABLE Banda
ADD COLUMN capacidade INT NOT NULL DEFAULT 0;

ALTER TABLE CasadeShows
ADD COLUMN capacidade INT NOT NULL DEFAULT 0;

ALTER TABLE Musico
ADD COLUMN capacidade INT NOT NULL DEFAULT 0;


Funções de Verificação
Verificação de Banda Cheia

CREATE OR REPLACE FUNCTION verificar_banda_cheia() 
RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT COUNT(*) FROM BandaMusico WHERE banda_id = NEW.banda_id) >= 
       (SELECT capacidade FROM Banda WHERE id = NEW.banda_id) THEN
        RAISE EXCEPTION 'Banda está cheia';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION verificar_casa_cheia() 
RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT COUNT(*) FROM CasaMusico WHERE casa_id = NEW.casa_id) >= 
       (SELECT capacidade FROM CasadeShows WHERE id = NEW.casa_id) THEN
        RAISE EXCEPTION 'Casa de show está cheia';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION verificar_musico_cheio() 
RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT COUNT(*) FROM BandaMusico WHERE musico_id = NEW.musico_id) >= 
       (SELECT capacidade FROM Musico WHERE id = NEW.musico_id) THEN
        RAISE EXCEPTION 'Músico já está em capacidade máxima de bandas';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

Triggers

CREATE TRIGGER verificar_banda_cheia_trigger
BEFORE INSERT ON BandaMusico
FOR EACH ROW
EXECUTE FUNCTION verificar_banda_cheia();

CREATE TRIGGER verificar_casa_cheia_trigger
BEFORE INSERT ON CasaMusico
FOR EACH ROW
EXECUTE FUNCTION verificar_casa_cheia();

CREATE TRIGGER verificar_musico_cheio_trigger
BEFORE INSERT ON BandaMusico
FOR EACH ROW
EXECUTE FUNCTION verificar_musico_cheio();


SELECT * FROM centraldomusico.Banda;
