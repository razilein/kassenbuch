CREATE VIEW vreparatur (
  id,
  bestellung_id,
  kunde_id,
  filiale_id,
  nummer,
  art,
  geraet,
  seriennummer,
  symptome,
  aufgaben,
  geraetepasswort,
  geraetepasswort_art,
  funktionsfaehig,
  expressbearbeitung,
  abholdatum,
  abholzeit,
  kostenvoranschlag,
  erledigt,
  erledigungsdatum,
  erstellt_am,
  mitarbeiter,
  bemerkung,
  bestellung_nr,
  kunde_nr,
  reparatur_nr
) AS 
SELECT
  id,
  bestellung_id,
  kunde_id,
  filiale_id,
  nummer,
  art,
  geraet,
  seriennummer,
  symptome,
  aufgaben,
  geraetepasswort,
  geraetepasswort_art,
  funktionsfaehig,
  expressbearbeitung,
  abholdatum,
  abholzeit,
  kostenvoranschlag,
  erledigt,
  erledigungsdatum,
  erstellt_am,
  mitarbeiter,
  bemerkung,
  CASE
    WHEN bestellung_id IS NULL THEN NULL
    ELSE (
      SELECT
        f.kuerzel
        + LEFT(TO_CHAR(bestellung.nummer), 2)
        + '-B'
        + SUBSTRING(TO_CHAR(bestellung.nummer), 3, LENGTH(TO_CHAR(bestellung.nummer)))
      FROM
        bestellung
        JOIN filiale f ON f.id = bestellung.filiale_id
      WHERE
        bestellung.id = reparatur.bestellung_id
    )
  END,
  CASE
    WHEN kunde_id IS NULL THEN NULL
    ELSE (SELECT TO_CHAR(kunde.nummer) FROM kunde WHERE kunde.id = reparatur.kunde_id)
  END,
  filiale.kuerzel + reparatur.nummer
FROM
  reparatur
  JOIN filiale ON filiale.id = reparatur.filiale_id;
