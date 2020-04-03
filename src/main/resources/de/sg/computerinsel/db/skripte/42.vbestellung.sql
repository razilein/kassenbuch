CREATE VIEW vbestellung (
  id,
  angebot_id,
  kunde_id,
  filiale_id,
  nummer,
  beschreibung,
  erledigt,
  erledigungsdatum,
  anzahlung,
  kosten,
  datum,
  ersteller,
  erstellt_am,
  bestellung_nr,
  angebot_nr,
  kunde_nr
) AS 
SELECT
  id,
  angebot_id,
  kunde_id,
  filiale_id,
  nummer,
  beschreibung,
  erledigt,
  erledigungsdatum,
  anzahlung,
  kosten,
  datum,
  ersteller,
  erstellt_am,
  filiale.kuerzel + LEFT(TO_CHAR(bestellung.nummer), 2) + '-B' + SUBSTRING(TO_CHAR(bestellung.nummer), 3, LENGTH(TO_CHAR(bestellung.nummer))),
  CASE
    WHEN angebot_id IS NULL THEN NULL
    ELSE (
      SELECT
        f.kuerzel
        + LEFT(TO_CHAR(angebot.nummer), 2)
        + '-A'
        + SUBSTRING(TO_CHAR(angebot.nummer), 3, LENGTH(TO_CHAR(angebot.nummer)))
      FROM
        angebot
        JOIN filiale f ON f.id = angebot.filiale_id
      WHERE
        angebot.id = bestellung.angebot_id
    )
  END,
  CASE
    WHEN kunde_id IS NULL THEN NULL
    ELSE (SELECT TO_CHAR(kunde.nummer) FROM kunde WHERE kunde.id = bestellung.kunde_id)
  END
FROM
  bestellung
  JOIN filiale ON filiale.id = bestellung.filiale_id;
