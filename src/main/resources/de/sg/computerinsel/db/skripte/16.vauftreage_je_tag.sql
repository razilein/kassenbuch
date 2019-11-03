CREATE VIEW vauftraege_je_tag (
  id,
  datum,
  anzahl_reparatur,
  anzahl_auftrag,
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
), auftrag_je_tag (datum, anzahl) AS (
  SELECT
    datum,
    COUNT(*)
  FROM
    auftrag
  WHERE
    erledigt = 0
  GROUP BY
    datum
)
SELECT
  rownum(),
  datum,
  SUM(anzahl_reparatur),
  SUM(anzahl_auftrag),
  SUM(anzahl_gesamt)
FROM (
  SELECT
    datum,
    anzahl AS anzahl_reparatur,
    0 AS anzahl_auftrag,
    anzahl AS anzahl_gesamt
  FROM
    reparatur_je_tag
  UNION ALL
  SELECT
    datum,
    0 AS anzahl_reparatur,
    anzahl AS anzahl_auftrag,
    anzahl AS anzahl_gesamt
  FROM
    auftrag_je_tag
)
GROUP BY
  datum;
