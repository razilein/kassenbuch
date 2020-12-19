ALTER TABLE rechnungsposten ADD COLUMN storno BIT DEFAULT 0 NOT NULL;

CREATE TABLE stornierung (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  rechnung_id INT NOT NULL,
  kunde_id INT,
  datum DATE NOT NULL,
  ersteller VARCHAR(200) NOT NULL,
  grund VARCHAR(1000) NULL,
  filiale_id INT NOT NULL,
  nummer INT NOT NULL,
  vollstorno BIT DEFAULT 0 NOT NULL,
  FOREIGN KEY (kunde_id) REFERENCES kunde(id) ON DELETE SET NULL,
  FOREIGN KEY (rechnung_id) REFERENCES rechnung(id) ON DELETE CASCADE,
  FOREIGN KEY (filiale_id) REFERENCES filiale(id)
);

CREATE TABLE stornierungposten (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  stornierung_id INT NOT NULL,
  rechnungsposten_id INT NOT NULL,
  FOREIGN KEY (rechnungsposten_id) REFERENCES rechnungsposten(id) ON DELETE CASCADE,
  FOREIGN KEY (stornierung_id) REFERENCES stornierung(id) ON DELETE CASCADE
);

ALTER TABLE filiale ADD COLUMN zaehler_stornierung INT;
UPDATE filiale SET zaehler_stornierung = 0;

INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_RECHNUNG_STORNO', 'Ermöglicht das Anzeigen der Druckansicht eines Stornobelegs für eine Rechnung');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_RECHNUNG_STORNO_UEBERSICHT', 'Zugriff auf die Seite Übersicht Stornierungen zu einer Rechnungen');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_RECHNUNG_STORNO_VERWALTEN', 'Stornierungen zu Rechnungen dürfen gelöscht werden');
