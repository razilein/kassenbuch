CREATE VIEW vauftraege_je_tag (
  id,
  datum,
  anzahl_reparatur,
  anzahl_bestellung,
  anzahl_gesamt
) AS
WITH reparatur_je_tag (datum, anzahl) AS (
  SELECT
    abholdatum,
    COUNT(*)
  FROM
    reparatur
  WHERE
    erledigt = 0
  GROUP BY
    abholdatum
), bestellung_je_tag (datum, anzahl) AS (
  SELECT
    datum,
    COUNT(*)
  FROM
    bestellung
  WHERE
    erledigt = 0
  GROUP BY
    datum
)
SELECT
  rownum(),
  datum,
  SUM(anzahl_reparatur),
  SUM(anzahl_bestellung),
  SUM(anzahl_gesamt)
FROM (
  SELECT
    datum,
    anzahl AS anzahl_reparatur,
    0 AS anzahl_bestellung,
    anzahl AS anzahl_gesamt
  FROM
    reparatur_je_tag
  UNION ALL
  SELECT
    datum,
    0 AS anzahl_reparatur,
    anzahl AS anzahl_bestellung,
    anzahl AS anzahl_gesamt
  FROM
    bestellung_je_tag
)
GROUP BY
  datum;
