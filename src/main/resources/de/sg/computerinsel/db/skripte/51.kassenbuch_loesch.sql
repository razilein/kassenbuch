ALTER TABLE kassenbuch ADD COLUMN geloescht BIT DEFAULT 0 NOT NULL;
ALTER TABLE kassenbuch ADD COLUMN datum_geloescht DATETIME NULL;
ALTER TABLE kassenbuch ADD COLUMN loescher VARCHAR(200) NULL;