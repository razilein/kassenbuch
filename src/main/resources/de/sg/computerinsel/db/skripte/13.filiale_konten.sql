CREATE TABLE filiale_konten (
  id INTEGER IDENTITY NOT NULL PRIMARY KEY,
  filiale_id INT,
  haben_bar SMALLINT,
  haben_ec SMALLINT,
  haben_paypal SMALLINT,
  haben_ueberweisung SMALLINT,
  soll_bar SMALLINT,
  soll_ec SMALLINT,
  soll_paypal SMALLINT,
  soll_ueberweisung SMALLINT,
  FOREIGN KEY (filiale_id) REFERENCES filiale(id) ON DELETE CASCADE
);
