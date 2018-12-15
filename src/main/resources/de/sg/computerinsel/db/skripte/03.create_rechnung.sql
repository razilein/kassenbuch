CREATE TABLE rechnung (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  kunde_id INT,
  reparatur_id INT,
  art TINYINT NOT NULL,
  datum DATE NOT NULL,
  name_drucken BIT DEFAULT 0 NOT NULL,
  ersteller VARCHAR(200) NOT NULL,
  nummer INT NOT NULL,
  FOREIGN KEY (kunde_id) REFERENCES kunde(id) ON DELETE SET NULL,
  FOREIGN KEY (reparatur_id) REFERENCES reparatur(id) ON DELETE SET NULL
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
