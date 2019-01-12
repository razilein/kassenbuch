ALTER TABLE kunde ADD COLUMN firmenname VARCHAR(200);
ALTER TABLE kunde ALTER COLUMN nachname SET NULL;

ALTER TABLE mitarbeiter ADD COLUMN filiale_id INT NOT NULL;
ALTER TABLE mitarbeiter ADD FOREIGN KEY (filiale_id) REFERENCES filiale(id) ON DELETE CASCADE;

ALTER TABLE produkt ALTER COLUMN preis_ek RENAME TO preis_ek_brutto;
ALTER TABLE produkt ALTER COLUMN preis_vk RENAME TO preis_vk_brutto;
ALTER TABLE produkt ADD COLUMN preis_ek_netto DECIMAL(9,2);
ALTER TABLE produkt ADD COLUMN preis_vk_netto DECIMAL(9,2);
