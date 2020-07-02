ALTER TABLE rechnung ADD COLUMN rabatt DECIMAL(9,2) NULL;
ALTER TABLE rechnung ADD COLUMN rabatt_p DECIMAL(5,2) NULL;
UPDATE rechnung SET rabatt = 0, rabatt_p = 0;

ALTER TABLE angebot ADD COLUMN rabatt DECIMAL(9,2) NULL;
ALTER TABLE angebot ADD COLUMN rabatt_p DECIMAL(5,2) NULL;
UPDATE angebot SET rabatt = 0, rabatt_p = 0;
