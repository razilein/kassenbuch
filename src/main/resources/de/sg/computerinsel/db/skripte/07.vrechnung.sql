CREATE VIEW vrechnung (
  id,
  kunde_id,
  reparatur_id,
  filiale_id,
  art,
  bezahlt,
  datum,
  name_drucken,
  ersteller,
  nummer,
  rechnungsbetrag
) AS 
SELECT
  id,
  kunde_id,
  reparatur_id,
  filiale_id,
  art,
  bezahlt,
  datum,
  name_drucken,
  ersteller,
  nummer,
  (SELECT SUM(rp.menge * rp.preis - rp.rabatt) FROM rechnungsposten rp WHERE rp.rechnung_id = rechnung.id)
FROM
  rechnung;

  