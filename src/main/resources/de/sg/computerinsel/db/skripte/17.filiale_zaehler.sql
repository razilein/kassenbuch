ALTER TABLE filiale ADD COLUMN zaehler_rechnung INT;
ALTER TABLE filiale ADD COLUMN zaehler_reparaturauftrag INT;
ALTER TABLE filiale ADD COLUMN ausgangsbetrag DECIMAL(12,2);

UPDATE filiale SET
  zaehler_rechnung = (SELECT wert FROM einstellungen WHERE name = 'rechnung.nummer'),
  zaehler_reparaturauftrag = (SELECT wert FROM einstellungen WHERE name = 'reparatur.nummer'),
  ausgangsbetrag = (SELECT wert FROM einstellungen WHERE name = 'kassenbuch.ausgangsbetrag');

UPDATE filiale SET ausgangsbetrag = 0 WHERE ausgangsbetrag IS NULL;
