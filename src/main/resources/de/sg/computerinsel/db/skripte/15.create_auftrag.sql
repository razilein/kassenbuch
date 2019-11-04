CREATE TABLE auftrag (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  kunde_id INT,
  filiale_id INT NOT NULL,
  beschreibung VARCHAR(2000),
  erledigt BIT DEFAULT 0 NOT NULL,
  erledigungsdatum DATETIME,
  anzahlung VARCHAR(300) NOT NULL,
  kosten VARCHAR(300) NOT NULL,
  datum DATE NOT NULL,
  ersteller VARCHAR(200) NOT NULL,
  erstellt_am DATETIME DEFAULT CURRENT_DATE NOT NULL,
  FOREIGN KEY (kunde_id) REFERENCES kunde(id) ON DELETE SET NULL,
  FOREIGN KEY (filiale_id) REFERENCES filiale(id)
);

INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_KUNDEN_AUFTRAEGE', 'Ermöglicht das Anzeigen aller Aufträge eines Kunden');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_AUFTRAG_ERSTELLEN', 'Zugriff auf die Seite Auftrag erstellen');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_AUFTRAG_UEBERSICHT', 'Zugriff auf die Seite Übersicht Aufträge');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_AUFTRAG', 'Ermöglicht das Anzeigen der Druckansicht eines Auftrags');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_AUFTRAG_VERWALTEN', 'Aufträge dürfen angelegt, bearbeitet oder gelöscht werden');

ALTER TABLE rechnung ADD COLUMN auftrag_id INT;
ALTER TABLE rechnung ADD FOREIGN KEY (auftrag_id) REFERENCES auftrag(id) ON DELETE SET NULL;
