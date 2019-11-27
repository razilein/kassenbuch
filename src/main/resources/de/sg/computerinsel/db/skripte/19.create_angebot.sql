CREATE TABLE angebot (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  kunde_id INT,
  filiale_id INT NOT NULL,
  nummer INT NOT NULL,
  erledigt BIT DEFAULT 0 NOT NULL,
  erledigungsdatum DATETIME,
  ersteller VARCHAR(200) NOT NULL,
  erstellt_am DATETIME DEFAULT CURRENT_DATE NOT NULL,
  FOREIGN KEY (kunde_id) REFERENCES kunde(id) ON DELETE SET NULL,
  FOREIGN KEY (filiale_id) REFERENCES filiale(id)
);

CREATE TABLE angebotsposten (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  angebot_id INT NOT NULL,
  position TINYINT NOT NULL,
  menge TINYINT NOT NULL,
  bezeichnung VARCHAR(500) NOT NULL,
  preis DECIMAL(9,2) NOT NULL,
  FOREIGN KEY (angebot_id) REFERENCES angebot(id) ON DELETE CASCADE
);

INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_KUNDEN_ANGEBOTE', 'Ermöglicht das Anzeigen aller Angebote eines Kunden');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_ANGEBOT_ERSTELLEN', 'Zugriff auf die Seite Angebot erstellen');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_ANGEBOT_UEBERSICHT', 'Zugriff auf die Seite Übersicht Angebote');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ANGEBOT', 'Ermöglicht das Anzeigen der Druckansicht eines Angebots');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ANGEBOT_VERWALTEN', 'Angebote dürfen angelegt, bearbeitet oder gelöscht werden');

ALTER TABLE filiale ADD COLUMN zaehler_angebot INT;
UPDATE filiale SET zaehler_angebot = 0;

ALTER TABLE auftrag ADD COLUMN angebot_id INT;
ALTER TABLE auftrag ADD FOREIGN KEY (angebot_id) REFERENCES angebot(id) ON DELETE SET NULL;
