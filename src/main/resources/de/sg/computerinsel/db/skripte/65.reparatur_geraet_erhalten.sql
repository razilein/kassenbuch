ALTER TABLE reparatur ADD COLUMN geraet_erhalten BIT DEFAULT 0 NOT NULL;
UPDATE reparatur SET geraet_erhalten = 1;