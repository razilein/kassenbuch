CREATE TABLE kategorie (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  bezeichnung VARCHAR(100) NOT NULL
);

INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_INVENTAR_KATEGORIE', 'Zugriff auf die Seite: Kategorie');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_INVENTAR_KATEGORIE_VERWALTEN', 'Kategorien können angelegt, bearbeitet oder gelöscht werden');

CREATE TABLE gruppe (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  kategorie_id INTEGER NOT NULL,
  bezeichnung VARCHAR(100) NOT NULL,
  FOREIGN KEY (kategorie_id) REFERENCES kategorie(id) ON DELETE CASCADE
);

INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_INVENTAR_GRUPPE', 'Zugriff auf die Seite: Gruppe');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_INVENTAR_GRUPPE_VERWALTEN', 'Gruppen können angelegt, bearbeitet oder gelöscht werden');

CREATE TABLE produkt (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  gruppe_id INTEGER NOT NULL,
  ean VARCHAR(100),
  bezeichnung VARCHAR(500) NOT NULL,
  hersteller VARCHAR(100),
  bestand_unendlich BIT DEFAULT 0 NOT NULL,
  bestand INTEGER DEFAULT 0 NOT NULL,
  preis_ek DECIMAL(9,2),
  preis_vk DECIMAL(9,2),
  FOREIGN KEY (gruppe_id) REFERENCES gruppe(id) ON DELETE CASCADE
);

INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_INVENTAR_PRODUKT', 'Zugriff auf die Seite: Produkt');
INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_INVENTAR_PRODUKT_VERWALTEN', 'Produkte können angelegt, bearbeitet oder gelöscht werden');
