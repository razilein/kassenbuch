DROP VIEW vangebot;
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
  gesamtbetrag_netto,
  angebot_nr,
  kunde_nr
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
  (SELECT SUM(ap.menge * ap.preis / 1.19) FROM angebotsposten ap WHERE ap.angebot_id = angebot.id),
  filiale.kuerzel + LEFT(TO_CHAR(angebot.nummer), 2) + '-A' + SUBSTRING(TO_CHAR(angebot.nummer), 3, LENGTH(TO_CHAR(angebot.nummer))),
  CASE
    WHEN kunde_id IS NULL THEN NULL
    ELSE (SELECT TO_CHAR(kunde.nummer) FROM kunde WHERE kunde.id = angebot.kunde_id)
  END
FROM
  angebot
  JOIN filiale ON filiale.id = angebot.filiale_id;
