ALTER TABLE angebotsposten ADD produkt_id INT;
ALTER TABLE angebotsposten ADD FOREIGN KEY (produkt_id) REFERENCES produkt(id) ON DELETE SET NULL;
