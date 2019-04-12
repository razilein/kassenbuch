CREATE TABLE kunde (
   id INTEGER IDENTITY NOT NULL PRIMARY KEY,
   nachname VARCHAR(100) NOT NULL,
   vorname VARCHAR(50),
   strasse VARCHAR(100),
   plz VARCHAR(8),
   ort VARCHAR(50),
   telefon VARCHAR(50),
   email VARCHAR(100),
   dsgvo BIT DEFAULT 0 NOT NULL,
   erstellt_am DATETIME,
   bemerkung VARCHAR(4000)
);

CREATE TABLE filiale (
   id INTEGER IDENTITY NOT NULL PRIMARY KEY,
   kuerzel VARCHAR(3) NOT NULL,
   name VARCHAR(50) NOT NULL,
   strasse VARCHAR(100) NOT NULL,
   plz VARCHAR(8) NOT NULL,
   ort VARCHAR(50) NOT NULL,
   email VARCHAR(100) NOT NULL,
   telefon VARCHAR(50) NOT NULL
);

CREATE TABLE mitarbeiter (
   id INTEGER IDENTITY NOT NULL PRIMARY KEY,
   benutzername VARCHAR(50) NOT NULL,
   passwort VARCHAR(500) NOT NULL,
   nachname VARCHAR(50) NOT NULL,
   vorname VARCHAR(50) NOT NULL,
   email VARCHAR(50),
   email_privat VARCHAR(100),
   telefon VARCHAR(50)
);

CREATE TABLE reparatur (
   id INTEGER IDENTITY NOT NULL PRIMARY KEY,
   mitarbeiter VARCHAR(200) NOT NULL,
   kunde_id INTEGER,
   filiale_id INTEGER NOT NULL,
   nummer VARCHAR(20),
   art TINYINT NOT NULL,
   geraet VARCHAR(500),
   seriennummer VARCHAR(500),
   symptome VARCHAR(1000),
   aufgaben VARCHAR(1000),
   geraetepasswort VARCHAR(50),
   expressbearbeitung BIT DEFAULT 0 NOT NULL,
   abholdatum DATE,
   abholzeit TIME,
   kostenvoranschlag VARCHAR(300),
   erledigt BIT DEFAULT 0 NOT NULL,
   erledigungsdatum DATETIME,
   erstellt_am DATETIME,
   bemerkung VARCHAR(4000),
   FOREIGN KEY (kunde_id) REFERENCES kunde(id) ON DELETE SET NULL,
   FOREIGN KEY (filiale_id) REFERENCES filiale(id)
);

CREATE TABLE einstellungen (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(500),
  wert VARCHAR(1000)
);

CREATE TABLE rolle (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  beschreibung VARCHAR(200) NOT NULL
);
--Müssen mit ROLE_ starten, da spring-security diese sonst nicht erkennt
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_KASSENBUCH_ERSTELLEN', 'Zugriff auf die Seite: Kassenbuch erstellen');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_KASSENBUCH_UEBERSICHT', 'Zugriff auf die Seite: Übersicht Kassenbücher');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_KASSENBUCH', 'Ermöglicht das Anzeigen der Druckansicht eines Kassenbuches');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_KASSENBUCH_KASSENSTAND', 'Zugriff auf die Seite: Kassenstand berechnen');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_KASSENBUCH_STATISTIK', 'Zugriff auf die Seite: Statistik');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_EINSTELLUNGEN_ALLGEMEIN', 'Zugriff auf die Seite: Einstellungen allgemein');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_EINSTELLUNGEN_FILIALEN', 'Zugriff auf die Seite: Filiale');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_FILIALEN_VERWALTEN', 'Filialen können angelegt, bearbeitet oder gelöscht werden');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_EINSTELLUNGEN_MITARBEITER', 'Zugriff auf die Seite Mitarbeiter');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_MITARBEITER_VERWALTEN', 'Mitarbeiter dürfen angelegt, bearbeitet oder gelöscht werden');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_MITARBEITER_RESET', 'Das Passwort von anderen Benutzern darf zurückgesetzt werden');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_MITARBEITER_RECHTE', 'Die Berechtigungen von anderen Benutzern dürfen angepasst werden');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_KUNDEN', 'Zugriff auf die Seite Kunden');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_KUNDEN_VERWALTEN', 'Kunden dürfen angelegt, bearbeitet oder gelöscht werden');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_KUNDEN_REPARATUR', 'Ermöglicht das Anzeigen aller Reparaturaufträge eines Kunden');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_REPARATUR_ERSTELLEN', 'Zugriff auf die Seite Reparaturauftrag erstellen');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_REPARATUR_UEBERSICHT', 'Zugriff auf die Seite Übersicht Reparaturaufträge');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_REPARATUR', 'Ermöglicht das Anzeigen der Druckansicht eines Reparaturauftrages');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_REPARATUR_VERWALTEN', 'Reparaturaufträge dürfen angelegt, bearbeitet oder gelöscht werden');

CREATE TABLE mitarbeiter_rolle (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  rolle_id INTEGER NOT NULL,
  mitarbeiter_id INTEGER NOT NULL,
  FOREIGN KEY (rolle_id) REFERENCES rolle(id) ON DELETE CASCADE,
  FOREIGN KEY (mitarbeiter_id) REFERENCES mitarbeiter(id) ON DELETE CASCADE
);

CREATE TABLE protokoll (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  table_id INTEGER NULL,
  tablename VARCHAR(50) NULL,
  bemerkung VARCHAR(1000) NULL,
  datum DATETIME NOT NULL,
  mitarbeiter VARCHAR(200) NOT NULL,
  typ TINYINT NOT NULL
);
