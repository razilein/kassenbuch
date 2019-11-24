ALTER TABLE kunde ADD suchfeld_name VARCHAR(400);

UPDATE kunde SET suchfeld_name = REPLACE(COALESCE(firmenname, '') + COALESCE(vorname, ''), ' ', '') + COALESCE(nachname, '');
