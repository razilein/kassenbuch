****************************
* 1. Kassenbuch erstellen  *
****************************
Alle Rechnungen aus "Rechnungsverzeichnis" werden gelesen.

Folgende Felder werden aus Rechnungen mit Zahlungsweg BAR herausgefiltert, welche im angegebenen Rechnungszeitraum liegen (Rechnungsdatum von, Rechnungsdatum bis:
Rechnungsnummer, Rechnungsdatum und Gesamtsumme der Rechnung

Eine CSV und PDF-Datei wird erstellt mit folgendem Inhalt:
* Ausgangsbetrag
* Rechnungen (Rechnungsdatum, Rechnungsnummer, Einnahmen, Ausgaben, Gesamtbetrag)
* Gesamtbetrag

Je Tag wird der Ausgangsbetrag und Gesamtbetrag berechnet und angezeigt.
Der erste Ausgangsbetrag kann mit Hilfe des Feldes "Ausgangsbetrag" festgelegt werden.

****************************
* 2. Kassenbuch editieren  *
****************************
Im Feld "Zu bearbeitende CSV-Datei (Dateipfad)" steht automatisch das zuletzt bearbeitete oder erstellte Kassenbuch.
Jedoch kann dieses Feld auch frei befüllt werden.

Durch Ausfüllen der Felder "Verwendungszweck", "Eintragungsdatum" und "Betrag" wird nach dem Bestätigen von "Eintrag hinzufügen" der
Eintrag im gewählten Kassenbuch an entsprechender Stelle ergänzt.

Durch wählen eines der Radiobuttons "-" bzw. "+" kann festgelegt werden, ob es sich um eine Ausgabe bzw. Einnahme handelt.

****************************
* 3. Kassenstand berechnen *
****************************
Durch befüllen der Felder ergibt sich ein Gesamtbetrag. Das nächste Feld kann mit Hilfe von Tab oder Enter fokussiert werden.
Mit Hilfe der Pfeiltasten oder durch tippen kann die Anzahl der Scheine/Münzen angepasst werden.

Pfeiltaste oben: + 1
Pfeiltaste unten: - 1
Pfeiltaste rechts: + 10
Pfeiltaste links: - 10

Der Gesamtbetrag aus dem Kassenbuch welches im Feld "Zu bearbeitende CSV-Datei (Dateipfad)" hinterlegt ist wird zur Berechnung der Differenz genutzt.
Ist dieses Feld leer, wird aus dem Ablageverzeichnis die zuletzt erstellte Datei verwendet.

Farbliche Bedeutung der Differenz:
* rot -> Der Rechnungsbetrag ist höher als das in der Kasse befindliche Geld
* gelb -> Der Rechnungsbetrag ist niedriger als das in der Kasse befindliche Geld
* grün -> Der Rechnungsbetrag stimmt mit dem Kassenbetrag überein

****************************
* 4. Einstellungen         *
****************************
Hier muss das Ablageverzeichnis für erstellte Kassenbüche, sowie das Rechnungsverzeichnis in dem sich alle Rechnungen befinden angegeben werden.
Durch Bestätigen der "Einstellungen speichern"-Schaltfläche, werden die Einstellungen in der Datei config.properties gesichert.


****************************
* 4. Releasenotes          *
****************************
V 1.0.1:

** 5 - Farbe Tabellenkopf in PDF-Datei anpassen **
- Es wurde für den Tabellenkopf in den PDF-Dateien nun ein hellerer Grauton gewählt.

** 7 - Icon für Programm hinzufügen **
- Für das Programm wurde ein Kassenicon hinzugefügt.

** 13 - Bilder für Münzen/Scheine überarbeiten **
- Das Dateiformat für Bilder für Münzen wurden zu png geändert.
- Für Münzen wird der Hintergrund nun transparent dargestellt.