CREATE TABLE bestellung (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  produkt_id INT,
  menge TINYINT NOT NULL,
  FOREIGN KEY (produkt_id) REFERENCES produkt(id) ON DELETE CASCADE
);

INSERT INTO rolle (name, beschreibung) VALUES ('ROLE_ZUGRIFF_BESTELLUNG', 'Zugriff auf die Seite: Bestellung');
