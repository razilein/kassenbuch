CREATE TABLE kunde (
   id INTEGER IDENTITY NOT NULL PRIMARY KEY,
   nachname VARCHAR(100) NOT NULL,
   kuerzel VARCHAR(3) NOT NULL,
   vorname VARCHAR(50),
   strasse VARCHAR(100),
   plz VARCHAR(8),
   ort VARCHAR(50),
   telefon VARCHAR(50),
   email VARCHAR(100),
   erstellt_am DATETIME,
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
   nachname VARCHAR(50),
   vorname VARCHAR(50),
   email VARCHAR(100)
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
   expressbeabeitung BIT,
   abholdatum DATE,
   abholzeit TIME,
   kostenvoranschlag VARCHAR(10),
   erledigt BIT DEFAULT 0,
   erledigungsdatum DATETIME,
   erstellt_am DATETIME,
   FOREIGN KEY (mitarbeiter_id) REFERENCES mitarbeiter(id),
   FOREIGN KEY (kunde_id) REFERENCES kunde(id)
);

CREATE TABLE einstellungen (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(500),
  wert VARCHAR(1000)
);

CREATE SEQUENCE PUBLIC.R_NUMMER_SEQUENCE START WITH 5000 INCREMENT BY 1;