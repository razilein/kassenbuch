ALTER TABLE reparatur ADD COLUMN bestellung_id INTEGER NULL;
ALTER TABLE reparatur ADD FOREIGN KEY (bestellung_id) REFERENCES bestellung(id) ON DELETE SET NULL;
