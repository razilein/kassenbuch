ALTER TABLE rechnung ADD COLUMN angebot_id INT NULL;
ALTER TABLE rechnung ADD FOREIGN KEY (angebot_id) REFERENCES angebot(id) ON DELETE SET NULL;