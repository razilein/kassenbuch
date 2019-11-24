ALTER TABLE filiale ADD COLUMN zaehler_auftrag INT;
UPDATE filiale SET zaehler_auftrag = (SELECT MAX(id) FROM auftrag WHERE filiale.id = filiale_id);
UPDATE filiale SET zaehler_auftrag = 0 WHERE zaehler_auftrag IS NULL;

ALTER TABLE auftrag ADD COLUMN nummer INT;
UPDATE auftrag SET nummer = id + 19000;
