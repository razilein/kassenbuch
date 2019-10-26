ALTER TABLE kunde ADD COLUMN anrede TINYINT;
ALTER TABLE kunde ADD COLUMN akademischer_titel VARCHAR(50);

UPDATE kunde SET akademischer_titel = '';

UPDATE kunde SET
  nachname = TRIM(SUBSTRING(nachname, 6, LENGTH(nachname))),
  akademischer_titel = akademischer_titel + 'Prof. '
WHERE
  nachname LIKE 'Prof.%';

UPDATE kunde SET
  vorname = TRIM(SUBSTRING(vorname, 6, LENGTH(vorname))),
  akademischer_titel = akademischer_titel + 'Prof. '
WHERE
  nachname LIKE 'Prof.%';

UPDATE kunde SET
  nachname = TRIM(SUBSTRING(nachname, 4, LENGTH(nachname))),
  akademischer_titel = akademischer_titel + 'Dr. '
WHERE
  nachname LIKE 'Dr.%';

UPDATE kunde SET
  vorname = TRIM(SUBSTRING(vorname, 4, LENGTH(vorname))),
  akademischer_titel = akademischer_titel + 'Dr. '
WHERE
  nachname LIKE 'Dr.%';

UPDATE kunde SET akademischer_titel = TRIM(akademischer_titel);
UPDATE kunde SET akademischer_titel = NULL WHERE akademischer_titel = '';
