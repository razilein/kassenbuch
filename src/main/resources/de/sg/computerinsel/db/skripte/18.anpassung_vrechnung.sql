DROP VIEW vrechnung;
CREATE VIEW vrechnung (
  id,
  auftrag_id,
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
  erstellt_am
) AS 
SELECT
  id,
  auftrag_id,
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
  erstellt_am
FROM
  rechnung;
