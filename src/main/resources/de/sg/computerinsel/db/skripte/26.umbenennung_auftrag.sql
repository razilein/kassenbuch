UPDATE rolle SET name = 'ROLE_ZUGRIFF_BESTELLUNG_ERSTELLEN', beschreibung = 'Zugriff auf die Seite Bestellung erstellen' WHERE name = 'ROLE_ZUGRIFF_AUFTRAG_ERSTELLEN';
UPDATE rolle SET name = 'ROLE_ZUGRIFF_BESTELLUNG_UEBERSICHT', beschreibung = 'Zugriff auf die Seite Übersicht Bestellungen' WHERE name = 'ROLE_ZUGRIFF_AUFTRAG_UEBERSICHT';
UPDATE rolle SET name = 'ROLE_BESTELLUNG', beschreibung = 'Ermöglicht das Anzeigen der Druchansicht einer Bestellung' WHERE name = 'ROLE_AUFTRAG';
UPDATE rolle SET name = 'ROLE_BESTELLUNG_VERWALTEN', beschreibung = 'Bestellungen dürfen angelegt, bearbeitet oder gelöscht werden' WHERE name = 'ROLE_AUFTRAG_VERWALTEN';
UPDATE rolle SET name = 'ROLE_KUNDEN_BESTELLUNGEN', beschreibung = 'Ermöglicht das Anzeigen aller Bestellungen eines Kunden' WHERE name = 'ROLE_KUNDEN_AUFTRAEGE';

DROP VIEW vauftraege_je_tag;
DROP VIEW vkunde;

ALTER TABLE PUBLIC.AUFTRAG RENAME TO BESTELLUNG;

DROP VIEW vrechnung;

ALTER TABLE PUBLIC.RECHNUNG ALTER COLUMN AUFTRAG_ID RENAME TO BESTELLUNG_ID;
ALTER TABLE PUBLIC.FILIALE ALTER COLUMN ZAEHLER_AUFTRAG RENAME TO ZAEHLER_BESTELLUNG;
