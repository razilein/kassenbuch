DROP VIEW vrechnung;

CREATE VIEW vrechnung (
  id,
  bestellung_id,
  kunde_id,
  reparatur_id,
  angebot_id,
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
  reparatur_nr,
  angebot_nr,
  gesamtrabatt,
  vorlage,
  zusatztext
) AS 
SELECT
  id,
  bestellung_id,
  kunde_id,
  reparatur_id,
  angebot_id,
  filiale_id,
  art,
  bezahlt,
  datum,
  name_drucken,
  ersteller,
  nummer,
  CASE
    WHEN rechnung.rabatt_p > 0 THEN
      (SELECT SUM(rp.menge * rp.preis - rp.rabatt) FROM rechnungsposten rp WHERE rp.rechnung_id = rechnung.id) * (100.00 - rechnung.rabatt_p) / 100
    ELSE
      (SELECT SUM(rp.menge * rp.preis - rp.rabatt) FROM rechnungsposten rp WHERE rp.rechnung_id = rechnung.id) - COALESCE(rechnung.rabatt, 0)
  END,
  erstellt_am,
  CASE
    WHEN angebot_id IS NOT NULL OR (SELECT COUNT(*) FROM bestellung WHERE bestellung.id = rechnung.bestellung_id AND bestellung.angebot_id IS NOT NULL) > 0
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
  CASE
    WHEN vorlage = 1 THEN 'keine'
    ELSE filiale.kuerzel + LEFT(TO_CHAR(rechnung.nummer), 2) + '-' + SUBSTRING(TO_CHAR(rechnung.nummer), 3, LENGTH(TO_CHAR(rechnung.nummer)))
  END,
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
  END,
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
        angebot.id = rechnung.angebot_id
    )
  END,
  CASE
    WHEN rechnung.rabatt IS NOT NULL AND rechnung.rabatt > 0 THEN 1
    WHEN rechnung.rabatt_p IS NOT NULL AND rechnung.rabatt_p > 0 THEN 1
    ELSE 0
  END,
  rechnung.vorlage,
  rechnung.zusatztext
FROM
  rechnung
  JOIN filiale ON filiale.id = rechnung.filiale_id;
