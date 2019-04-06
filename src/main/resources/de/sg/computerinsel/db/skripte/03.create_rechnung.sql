CREATE TABLE rechnung (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  kunde_id INT,
  reparatur_id INT,
  filiale_id INT NOT NULL,
  art TINYINT NOT NULL,
  bezahlt BIT DEFAULT 0 NOT NULL,
  datum DATE NOT NULL,
  name_drucken BIT DEFAULT 0 NOT NULL,
  ersteller VARCHAR(200) NOT NULL,
  nummer INT NOT NULL,
  FOREIGN KEY (kunde_id) REFERENCES kunde(id) ON DELETE SET NULL,
  FOREIGN KEY (reparatur_id) REFERENCES reparatur(id) ON DELETE SET NULL,
  FOREIGN KEY (filiale_id) REFERENCES filiale(id)
);

CREATE TABLE rechnungsposten (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  rechnung_id INT NOT NULL,
  produkt_id INT,
  position TINYINT NOT NULL,
  menge TINYINT NOT NULL,
  bezeichnung VARCHAR(500) NOT NULL,
  seriennummer VARCHAR(100),
  hinweis VARCHAR(100),
  preis DECIMAL(9,2) NOT NULL,
  rabatt DECIMAL(9,2),
  FOREIGN KEY (rechnung_id) REFERENCES rechnung(id) ON DELETE CASCADE,
  FOREIGN KEY (produkt_id) REFERENCES produkt(id) ON DELETE SET NULL,
);

ALTER TABLE kunde ADD COLUMN nummer INT NOT NULL;

CREATE SEQUENCE PUBLIC.K_NUMMER_SEQUENCE START WITH 7000 INCREMENT BY 1;

INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_KUNDEN_RECHNUNG', 'Ermöglicht das Anzeigen aller Rechnungen eines Kunden');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_RECHNUNG_ERSTELLEN', 'Zugriff auf die Seite Rechnung erstellen');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_RECHNUNG_UEBERSICHT', 'Zugriff auf die Seite Übersicht Rechnungen');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_RECHNUNG', 'Ermöglicht das Anzeigen der Druckansicht einer Rechnung');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_RECHNUNG_VERWALTEN', 'Rechnungen dürfen angelegt, bearbeitet oder gelöscht werden');
