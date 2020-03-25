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
  mobiltelefon,
  suchfeld_telefon,
  suchfeld2_telefon,
  email,
  bemerkung,
  dsgvo,
  erstellt_am,
  name_drucken_bei_firma,
  anzahl_reparaturen,
  anzahl_rechnungen,
  anzahl_bestellungen,
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
  mobiltelefon,
  CASE
    WHEN COALESCE(telefon, '') = '' AND COALESCE(mobiltelefon, '') = '' THEN NULL
    WHEN COALESCE(telefon, '') != '' AND COALESCE(mobiltelefon, '') = '' THEN telefon
    WHEN COALESCE(telefon, '') = '' AND COALESCE(mobiltelefon, '') != '' THEN mobiltelefon
    ELSE telefon + ' | ' + mobiltelefon
  END,
  CASE
    WHEN COALESCE(telefon, '') = '' AND COALESCE(mobiltelefon, '') = '' THEN NULL
    WHEN COALESCE(telefon, '') != '' AND COALESCE(mobiltelefon, '') = '' THEN REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(telefon, ' ', ''), '/', ''), '-', ''), '\', ''), ')', ''), '(', '')
    WHEN COALESCE(telefon, '') = '' AND COALESCE(mobiltelefon, '') != '' THEN REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(mobiltelefon, ' ', ''), '/', ''), '-', ''), '\', ''), ')', ''), '(', '')
    ELSE REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(telefon, ' ', ''), '/', ''), '-', ''), '\', ''), ')', ''), '(', '') + ' | ' + REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(mobiltelefon, ' ', ''), '/', ''), '-', ''), '\', ''), ')', ''), '(', '')
  END,
  email,
  bemerkung,
  dsgvo,
  erstellt_am,
  name_drucken_bei_firma,
  (SELECT COUNT(*) FROM reparatur WHERE kunde_id = kunde.id),
  (SELECT COUNT(*) FROM rechnung WHERE kunde_id = kunde.id),
  (SELECT COUNT(*) FROM bestellung WHERE kunde_id = kunde.id),
  (SELECT COUNT(*) FROM angebot WHERE kunde_id = kunde.id)
FROM
  kunde;
