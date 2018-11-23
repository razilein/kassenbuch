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
   passwort VARCHAR(50) NOT NULL,
   nachname VARCHAR(50) NOT NULL,
   vorname VARCHAR(50) NOT NULL,
   email VARCHAR(50),
   email_privat VARCHAR(100),
   telefon VARCHAR(50)
);

CREATE TABLE reparatur (
   id INTEGER IDENTITY NOT NULL PRIMARY KEY,
   mitarbeiter_id INTEGER NOT NULL,
   kunde_id INTEGER NOT NULL,
   nummer VARCHAR(20),
   art TINYINT NOT NULL,
   geraet VARCHAR(200),
   seriennummer VARCHAR(200),
   symptome VARCHAR(1000),
   aufgaben VARCHAR(1000),
   geraetepasswort VARCHAR(50),
   expressbeabeitung BIT DEFAULT 0 NOT NULL,
   abholdatum DATE,
   abholzeit TIME,
   kostenvoranschlag VARCHAR(100),
   erledigt BIT DEFAULT 0 NOT NULL,,
   erledigungsdatum DATETIME,
   erstellt_am DATETIME,
   bemerkung VARCHAR(4000),
   FOREIGN KEY (mitarbeiter_id) REFERENCES mitarbeiter(id),
   FOREIGN KEY (kunde_id) REFERENCES kunde(id)
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
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_KASSENBUCH_ERSTELLEN', 'Bestimmt, ob auf die Seite Kassenbuch erstellen zugegriffen werden darf.');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_KASSENBUCH_KASSENSTAND', 'Bestimmt, ob auf die Seite Kassenstand berechnen zugegriffen werden darf.');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_KASSENBUCH_STATISTIK', 'Bestimmt, ob auf die Seite Statistik zugegriffen werden darf.');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_EINSTELLUNGEN_ALLGEMEIN', 'Bestimmt, ob auf die Seite Einstellungen allgemein zugegriffen werden darf.');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_EINSTELLUNGEN_FILIALEN', 'Bestimmt, ob auf die Seite Filiale zugegriffen werden darf.');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_EINSTELLUNGEN_MITARBEITER', 'Bestimmt, ob auf die Seite Mitarbeiter zugegriffen werden darf.');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_REPARATUR_KUNDEN', 'Bestimmt, ob auf die Seite Kunden zugegriffen werden darf.');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_REPARATUR_ERSTELLEN', 'Bestimmt, ob auf die Seite Reparaturauftrag erstellen werden darf.');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_REPARATUR_UEBERSICHT', 'Bestimmt, ob auf die Seite Übersicht Reparaturaufträge zugegriffen werden darf.');

CREATE TABLE mitarbeiter_rolle (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  rolle_id INTEGER NOT NULL,
  mitarbeiter_id INTEGER NOT NULL,
  FOREIGN KEY (rolle_id) REFERENCES rolle(id),
  FOREIGN KEY (mitarbeiter_id) REFERENCES mitarbeiter(id)
);

CREATE SEQUENCE PUBLIC.R_NUMMER_SEQUENCE START WITH 5000 INCREMENT BY 1;