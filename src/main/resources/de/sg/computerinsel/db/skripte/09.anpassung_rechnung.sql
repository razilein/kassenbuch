ALTER TABLE "PUBLIC"."RECHNUNGSPOSTEN" ALTER COLUMN HINWEIS VARCHAR(300);
ALTER TABLE rechnung ADD COLUMN erstellt_am DATETIME DEFAULT CURRENT_DATE NOT NULL;

UPDATE rechnung SET erstellt_am = (
  SELECT MAX(protokoll.datum) FROM protokoll
  WHERE
    typ = 1
    AND tablename = 'I'
    AND rechnung.id = protokoll.table_id
);

ALTER TABLE rechnung ADD COLUMN name_drucken_bei_firma BIT DEFAULT 1 NOT NULL;
ALTER TABLE kunde ADD COLUMN name_drucken_bei_firma BIT DEFAULT 1 NOT NULL;
