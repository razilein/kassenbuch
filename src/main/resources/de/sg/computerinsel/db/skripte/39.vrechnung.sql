DROP VIEW vrechnung;

CREATE VIEW vrechnung (
  id,
  bestellung_id,
  kunde_id,
  reparatur_id,
  filiale_id,
  art,
  bezahlt,
  datum,
  name_drucken,
  ersteller,
  nummer,
  rechnungsbetrag,
  erstellt_am,
  mit_angebot,
  mit_bestellung,
  mit_reparatur,
  bestellung_nr,
  kunde_nr,
  rechnung_nr,
  reparatur_nr
) AS 
SELECT
  id,
  bestellung_id,
  kunde_id,
  reparatur_id,
  filiale_id,
  art,
  bezahlt,
  datum,
  name_drucken,
  ersteller,
  nummer,
  (SELECT SUM(rp.menge * rp.preis - rp.rabatt) FROM rechnungsposten rp WHERE rp.rechnung_id = rechnung.id),
  erstellt_am,
  CASE
    WHEN (SELECT COUNT(*) FROM bestellung WHERE bestellung.id = rechnung.bestellung_id AND bestellung.angebot_id IS NOT NULL) > 0
    THEN 1
    ELSE 0
  END,
  CASE
    WHEN bestellung_id IS NULL THEN 0
    ELSE 1
  END,
  CASE
    WHEN reparatur_id IS NULL THEN 0
    ELSE 1
  END,
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
        bestellung.id = rechnung.bestellung_id
    )
  END,
  CASE
    WHEN kunde_id IS NULL THEN NULL
    ELSE (SELECT TO_CHAR(kunde.nummer) FROM kunde WHERE kunde.id = rechnung.kunde_id)
  END,
  filiale.kuerzel + LEFT(TO_CHAR(rechnung.nummer), 2) + '-' + SUBSTRING(TO_CHAR(rechnung.nummer), 3, LENGTH(TO_CHAR(rechnung.nummer))),
  CASE
    WHEN reparatur_id IS NULL THEN NULL
    ELSE (
      SELECT
        f.kuerzel + reparatur.nummer
      FROM
        reparatur
        JOIN filiale f ON f.id = reparatur.filiale_id
      WHERE
        reparatur.id = rechnung.reparatur_id
    )
  END
FROM
  rechnung
  JOIN filiale ON filiale.id = rechnung.filiale_id;
