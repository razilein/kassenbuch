CREATE VIEW vangebot (
  id,
  kunde_id,
  filiale_id,
  nummer,
  erledigt,
  erledigungsdatum,
  ersteller,
  erstellt_am,
  gesamtbetrag,
  gesamtbetrag_netto
) AS 
SELECT
  id,
  kunde_id,
  filiale_id,
  nummer,
  erledigt,
  erledigungsdatum,
  ersteller,
  erstellt_am,
  (SELECT SUM(ap.menge * ap.preis) FROM angebotsposten ap WHERE ap.angebot_id = angebot.id),
  (SELECT SUM(ap.menge * ap.preis / 1.19) FROM angebotsposten ap WHERE ap.angebot_id = angebot.id)
FROM
  angebot;
