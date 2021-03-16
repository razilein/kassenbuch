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
  kunde_nr,
  zusatztext
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
  CASE
    WHEN angebot.rabatt_p > 0 THEN
      ((SELECT SUM(ap.menge * ap.preis * 100 / (mwst + 100.00)) FROM angebotsposten ap JOIN angebot a ON a.id = ap.angebot_id WHERE a.id = angebot.id)
      * ((100.00 - angebot.rabatt_p) / 100) * (mwst + 100.00) / 100)
    ELSE
      (((SELECT SUM(ap.menge * ap.preis * 100 / (mwst + 100.00)) FROM angebotsposten ap JOIN angebot a ON a.id = ap.angebot_id WHERE a.id = angebot.id)
      - COALESCE(angebot.rabatt, 0)) * (mwst + 100.00) / 100)
  END,
  CASE
    WHEN angebot.rabatt_p > 0 THEN
      ((SELECT SUM(ap.menge * ap.preis * 100 / (mwst + 100.00)) FROM angebotsposten ap JOIN angebot a ON a.id = ap.angebot_id WHERE a.id = angebot.id)
      * (100.00 - angebot.rabatt_p) / 100)
    ELSE
      ((SELECT SUM(ap.menge * ap.preis * 100 / (mwst + 100.00)) FROM angebotsposten ap JOIN angebot a ON a.id = ap.angebot_id WHERE a.id = angebot.id)
      - COALESCE(angebot.rabatt, 0))
  END,
  filiale.kuerzel + LEFT(TO_CHAR(angebot.nummer), 2) + '-A' + SUBSTRING(TO_CHAR(angebot.nummer), 3, LENGTH(TO_CHAR(angebot.nummer))),
  CASE
    WHEN kunde_id IS NULL THEN NULL
    ELSE (SELECT TO_CHAR(kunde.nummer) FROM kunde WHERE kunde.id = angebot.kunde_id)
  END,
  angebot.zusatztext
FROM
  angebot
  JOIN filiale ON filiale.id = angebot.filiale_id;
