CREATE TABLE kassenbuch (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  ausgangsbetrag DECIMAL(9,2),
  datum DATE NOT NULL,
  ersteller VARCHAR(200) NOT NULL
);

CREATE TABLE kassenbuchposten (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  kassenbuch_id INT NOT NULL,
  betrag DECIMAL(9,2),
  verwendungszweck VARCHAR(200) NOT NULL,
  FOREIGN KEY (kassenbuch_id) REFERENCES kassenbuch(id) ON DELETE CASCADE
);
