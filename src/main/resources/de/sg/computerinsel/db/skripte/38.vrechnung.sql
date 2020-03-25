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
  mit_reparatur
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
  END
FROM
  rechnung;
