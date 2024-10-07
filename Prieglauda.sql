CREATE SCHEMA ruki9058;

CREATE TABLE ruki9058.Gyvunai (
    ID                      INTEGER         NOT NULL,
    Vardas                  VARCHAR(30)     NOT NULL,
    Rusis                   VARCHAR(42)     NOT NULL,
    Veisle                  VARCHAR(31)     DEFAULT 'Misrunas',
    Spalva                  VARCHAR(20)     NOT NULL,
    Lytis                   VARCHAR(7)      NOT NULL,
    Pgr_priziuretojo_ID     INTEGER         NOT NULL,
    Medicinines_info_ID     INTEGER         NOT NULL,
    Globejo_ID              INTEGER,
    PRIMARY KEY (ID),
    CONSTRAINT IMedicinine_informacija FOREIGN KEY (Medicinines_info_ID) REFERENCES ruki9058.Medicinine_informacija ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT IGlobejai FOREIGN KEY (Globejo_ID) REFERENCES ruki9058.Globejai ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE ruki9058.Priziuretojai (
    ID                      INTEGER         NOT NULL,
    Vardas                  VARCHAR(30)     NOT NULL,
    Pavarde                 VARCHAR(30)     NOT NULL,
    Pareigos                VARCHAR(30)     DEFAULT 'Pagalbinis darbuotojas',
    Telefono_numeris        CHAR(12)        NOT NULL    CONSTRAINT Val_Tel_numeris
                                                        CHECK(Telefono_numeris LIKE '+370%'),
    El_pastas               VARCHAR(30)     NOT NULL    CONSTRAINT Val_Pastas
                                                        CHECK(El_pastas LIKE '%_@%_.%_'),
    Adresas                 VARCHAR(30),
    PRIMARY KEY (ID) 
);

CREATE TABLE ruki9058.Medicinine_informacija (
    ID                                  INTEGER      NOT NULL,
    Sveikatos_bukle                     VARCHAR(20)  NOT NULL,
    Paskutines_med_apziuros_data        DATE         NOT NULL CONSTRAINT Val_Vakcinacijos_data
                                                              CHECK(Paskutines_med_apziuros_data <= CURRENT_DATE),
    Paskutines_vakcinacijos_data        DATE         CONSTRAINT Val_Vakcinacijos_data
                                                     CHECK(Paskutines_vakcinacijos_data <= Paskutines_med_apziuros_data),
    Sterializacijos_statusas            VARCHAR(4)   NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE ruki9058.Globejai (
    ID                      SERIAL          NOT NULL,
    Vardas                  VARCHAR(30)     NOT NULL,
    Pavarde                 VARCHAR(30)     NOT NULL,
    Telefono_numeris        CHAR(12)        NOT NULL    CONSTRAINT Val_Tel_numeris
                                                        CHECK(Telefono_numeris LIKE '+370%'),
    El_pastas               VARCHAR(30)                  CONSTRAINT Val_Pastas
                                                        CHECK(El_pastas LIKE '%_@%_.%_'),
    Adresas                 VARCHAR(30)     NOT NULL,
    PRIMARY KEY (ID)
);

CREATE VIEW ruki9058.Priglaudimo_informacija 
                    (Gyvunai.ID, 
                    Gyvunai.Vardas, 
                    Globejai.ID, 
                    Globejai.Vardas, 
                    Sutarties_tvirtintojo_ID)
AS SELECT Gyvunai.ID, 
          Gyvunai.Vardas,
          Globejai.ID,
          Globejai.Vardas,
          Gyvunai.Pgr_priziuretojo_ID
FROM Gyvunai,
     Globejai
WHERE Gyvunai.Globejo_ID IS NOT NULL AND
      Gyvunai.Globejo_ID = Globejai.ID;


CREATE VIEW ruki9058.Kasdienines_prieziuros_informacija 
                    (Gyvunai.ID, 
                    Gyvunai.Vardas, 
                    Sveikatos_bukle, 
                    Paskutines_med_apziuros_data, 
                    Paskutines_vakcinacijos_data)
AS SELECT Gyvunai.ID, 
          Gyvunai.Vardas, 
          Sveikatos_bukle, 
          Paskutines_med_apziuros_data, 
          Paskutines_vakcinacijos_data
FROM Gyvunai, 
     Medicinine_informacija
WHERE Gyvunai.Medicinines_info_ID = Medicinine_informacija.ID AND 
      Gyvunai.Globejo_ID IS NULL;

CREATE MATERIALIZED VIEW ruki9058.Informacija_veterinarui
                                (Medicinine_informacija.ID,
                                Gyvunai.Vardas,
                                Gyvunai.Veisle,
                                Gyvunai.Lytis,
                                Medicinine_informacija.Paskutines_vakcinacijos_data,
                                Medicinine_informacija.Sterializacijos_statusas)
AS SELECT Medicinine_informacija.ID,
          Gyvunai.Vardas,
          Gyvunai.Veisle,
          Gyvunai.Lytis,
          Medicinine_informacija.Paskutines_vakcinacijos_data,
          Medicinine_informacija.Sterializacijos_statusas
FROM Gyvunai,
     Medicinine_informacija
WHERE Gyvunai.Medicinines_info_ID = Medicinine_informacija.ID AND
      Gyvunai.Globejo_ID IS NULL
WITH DATA;

REFRESH MATERIALIZED VIEW ruki9058.Informacija_veterinarui;

CREATE UNIQUE INDEX Telefono_numeris ON ruki9058.Priziuretojai;
CREATE INDEX Vardas ON ruki9058.Gyvunai;

CREATE TRIGGER Gyvunu_limitas_priglausti()
BEFORE INSERT OR UPDATE OF Globejo_ID ON ruki9058.Gyvunai
FOR EACH ROW
EXECUTE PROCEDURE Gyvunu_limitas_priglausti();

CREATE FUNCTION Gyvunu_limitas_priglausti()
RETURNS TRIGGER AS
BEGIN
IF (SELECT COUNT(*) FROM Gyvunai
 WHERE Gyvunai.Globejo_ID = NEW.Globejo_ID) >= 50
THEN
 RAISE EXCEPTION "Asmuo negali pasiimti daugiau, nei 50 gyvunu";
END IF;
RETURN NEW;
END; 
LANGUAGE plpgsql;

CREATE TRIGGER Gyvunu_limitas_priziureti_pridedant
BEFORE INSERT ON ruki9058.Gyvunai
REFERENCING NEW AS New_Gyvunai
FOR EACH ROW
WHEN ((SELECT COUNT(*) FROM Gyvunai
WHERE Gyvunai.Pgr_priziuretojo_ID = New_Gyvunai.Pgr_priziuretojo_ID AND
      Gyvunai.Globejo_ID = NULL) >= 45)
SIGNAL SQLSTATE '70001'
SET MESSAGE_TEXT = "Priziuretojas negali buti atsakingas uz daugiau, nei 45 gyvunus vienu metu"; 

CREATE TRIGGER Gyvunu_limitas_priziureti_pakeiciant
BEFORE UPDATE OF Pgr_priziuretojo_ID ON ruki9058.Gyvunai
REFERENCING NEW AS New_Gyvunai
FOR EACH ROW
WHEN ((SELECT COUNT(*) FROM Gyvunai
WHERE Gyvunai.Pgr_priziuretojo_ID = New_Gyvunai.Pgr_priziuretojo_ID AND
      Gyvunai.Globejo_ID IS NULL) >= 45)
SIGNAL SQLSTATE '70002'
SET MESSAGE_TEXT = "Priziuretojas negali buti atsakingas uz daugiau, nei 45 gyvunus vienu metu";