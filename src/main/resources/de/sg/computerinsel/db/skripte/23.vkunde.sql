DROP VIEW vkunde;

CREATE VIEW vkunde (
  id,
  anrede,
  akademischer_titel,
  nummer,
  nachname,
  vorname,
  firmenname,
  suchfeld_name,
  strasse,
  plz,
  ort,
  telefon,
  email,
  bemerkung,
  dsgvo,
  erstellt_am,
  name_drucken_bei_firma,
  anzahl_reparaturen,
  anzahl_rechnungen,
  anzahl_auftraege,
  anzahl_angebote
) AS
SELECT
  id,
  anrede,
  akademischer_titel,
  nummer,
  nachname,
  vorname,
  firmenname,
  suchfeld_name,
  strasse,
  plz,
  ort,
  telefon,
  email,
  bemerkung,
  dsgvo,
  erstellt_am,
  name_drucken_bei_firma,
  (SELECT COUNT(*) FROM reparatur WHERE kunde_id = kunde.id),
  (SELECT COUNT(*) FROM rechnung WHERE kunde_id = kunde.id),
  (SELECT COUNT(*) FROM auftrag WHERE kunde_id = kunde.id),
  (SELECT COUNT(*) FROM angebot WHERE kunde_id = kunde.id)
FROM
  kunde;