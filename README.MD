# Versionsdokumentation

## Version 2.8.1

* Batch-Datei zum Sichern und Speichern des Programmverzeichnisses erstellt: kassenprogramm-backup.cmd (administrativ auszuführen)
* Bei Informationsmeldung zu nicht bezahlten Rechnungen keine Rechnungsvorlagen mehr beachten
* Kontrollkästchen "Vorlage nach dem Erstellen der Rechnung behalten" standardmäßg deaktivieren beim Bearbeiten von Rechnungsvorlagen
* Reparaturaufträge - Länge des Feldes "Gerätepasswort" auf 200 Zeichen erhöht
* Reparaturaufträge - Noch nicht geöffnete Aufträge mit Statusicon "Neu" markieren
* Automatische Reparaturaufträge - CSV-Dateistruktur abgeglichen, Felder kürzen, wenn die Maximalgröße überschritten wurden.
* Automatische Reparaturaufträge - E-Mail-Benachrichtigungen mit gesonderter Absender-Adresse versenden
* Automatische Reparaturaufträge - Information per E-Mail, wenn ein neuer Auftrag eingeht
* Automatische Reparaturaufträge - Kunden nicht mehrfach anlegen

## Version 2.8.0

* Reparatur drucken - Buchstabe P oben rechts anzeigen, wenn der Kunde als problematisch markiert wurde (Kombinierbar mit E für Express)
* Hinweis auf problematischen Kunden auch bei Auswahl von Angeboten, Bestellungen und Reparaturaufträgen in allen Ansichten anzeigen
* Reparaturübersicht - Erledigtsymbol von Smilies zu Kontrollkästchen geändert (rot und leer = nicht erledigt, schwarz und angehakt = erledigt)
* Inhalt der E-Mail vor dem Versenden anzeigen und bearbeitbar machen
* Teilstornierungen korrekt im Kassenbuch ausweisen
* Vereinfachtes Öffnen durch Verlinkungen von Rechnungs-, Bestellungs-, Angebots-, Reparatur- und Kundennummer in Tabellen
* Rechnungsvorlagen nicht in Rechnungsansicht anzeigen
* Auflistung aller Rechnungsvorlagen unter Rechnung - Vorlagen
* Neues Feld Zusatztext auf dem Ausdruck auf Rechnungen oder Angeboten ausgeben
* Automatisches Einlesen und Anlegen von Reparaturaufträgen vom FTP-Server (separat einzurichten und zu aktivieren)

## Version 2.7.1

* Rechnungen zu einem Kunden wieder korrekt anzeigen.

## Version 2.7.0

* Stornoicon geändert, da verwechselbar mit Löschen-Funktion
* Beschriftung für Lieferdatum bei Rechnungen zu 'Liefer- / Leistungsdatum' geändert
* Rechnungen als Vorlage speichern zur späteren Weiterbearbeitung oder Kopie

## Version 2.6.0

* Stornierung und Teilstornierung von Rechnungen ermöglichen
* Rotes Informationsicon in Kundenansicht anzeigen, wenn es sich um einen Problemkunden handelt
* Bezahltstatus beim Ändern der Zahlart beim Ändern einer Rechnung anpassen

* Export/Import von Produkten - Hersteller, Kategorie und Gruppe ebenfalls exportieren
  * Kann die EAN nicht gefunden werden, wird das Produkt angelegt.
  * Kann die Gruppe in der Kategorie nicht gefunden werden, wird diese angelegt.
  * Kann die Kategorie nicht gefunden werden, wird diese angelegt.

* Datum auf Bestellungsausdruck anhand des Erstelldatums der Bestellung ableiten, anstatt das Tagesdatum zu verwenden
* Sortierung in Produkttabelle nach Preisen erlauben. Es wird nach dem VK Bto sortiert
* Seiten erst anzeigen, wenn diese vollständig gerendert sind
* Suchfeld für Straße in Kundenansicht ergänzt mit dynamischer Suche
* Tabellenkopf beim Scrollen nach unten mitbewegen

## Version 2.5.4

* Festen Rabatt von Rechnungen im Kassenbericht beachten
* Prozentualen Rabatt von Rechnungen in Kassenbericht und Lexware-Export beachten

## Version 2.5.3

* Dialog zu nicht bezahlten Rechnungen nach Auswahl des Kunden korrekt in Reparaturansicht anzeigen

## Version 2.5.2

* Vorwahl 0341 nur bei Telefonnummern unter 9 Zeichen Länge hinzufügen, wenn diese nicht mit 0 oder + startet
* Nummern welche mit 00 oder + beginnen in internationaler Formatierung anzeigen (z.B. +7 für russische Nummern)
* Beim Auswählen eines Kunden in den Erstellansichten erscheint eine Hinweismeldung, wenn noch offene Rechnungen bestehen
* Problematische Kunden markierbar, bei Auswahl des Kunden erfolgt eine Warnung mit dem Inhalt des Bemerkungstextes

* Rundungsfehler bei Brutto-Betrag bei Angeboten behoben
  * Berechnung erfolgt auf Basis des Nettobetrags, um den Rabatt von diesem korrekt abzuziehen. Zuletzt wird der Gesamtbruttopreis anhand des Gesamtnetto berechnet. Dazu wird nun der ungerundete Gesamtnettobetrag verwendet, zuvor wurde der gerundete Nettobetrag verwendet, was zu Rundungsfehlern führte.

## Version 2.5.1

* Gesamtrabatt in EUR oder Prozent auf Rechnungen erlauben
** Gesamt Brutto Einkaufspreis aller Produkt (wo vorhanden) sowie Gewinn anzeigen

* Gesamtrabatt (Netto) in EUR oder Prozent auf Angeboten erlauben
  * Gesamt Brutto Einkaufspreis aller Produkte (wo vorhanden) sowie Gewinn anzeigen

* Erstellen von leeren Kassenberichten erlauben, wenn keine BAR Einnahmen/Ausgaben an diesem Tag
* Kassenbücher beim Löschen nicht tatsächlich löschen sondern nur ausblenden

## Version 2.5.0

* Mitarbeiter - Tabelle - Korrekte Beschriftung der Spalten Nachname und Vorname
* Startzeitraum beim Export von Produkten ausgehend vom aktuellen Jahr anstatt fest 2019

* MwSt in Einstellungen einstellbar
  * MwSt wird je Rechnung und Angebot anhand der aktuellen Einstellung gespeichert für bisherige Fälle 19%
  * Einstellung wird berücksichtigt bei Rechnungen, Angeboten und Produkten
  * Schaltfläche zum Anpassen aller VK-Netto-Preise aller Produkte hinzugefügt

## Version 2.4.2

* Übersicht Rechnungen - Suche nach Rechnnungsposten wiederhergestellt, Suche nach Kunde deaktiviert, da fehlerhaft
* Rechnung E-Mail-Versand - Keinen Druckdialog öffnen, da die Rechnung kurz vorher manuell als PDF gedruckt wird
* Druckansicht Bestellungen - Schriftgröße verkleinern um bis zu 19 Zeilen Bestelltext anzeigen zu können
* Übersicht Bestellungen - Spalte Beschreibung hinzugefügt, nach 350px mit ... abschneiden
* Übersicht Reparturen - Spalte Gerät nach 350px mit ... abschneiden
* Import von Produkten - Bezeichnung ebenfalls aktualisieren
* Anzeige von Tabellenfunktionen - Fehler behoben von sporadisch fehlenden Icons beim Laden der Seite, Ursache: Laden von Rechten erfolgte manchmal verzögert

## Version 2.4.1

* Text zu Labelkey general.uhrzeit hinzugefügt
* Funktion des einfachen Häkchens beim Angebotsposten aus Inventar wählen wiederhergestellt
* Angebot - Abbrechen beim Hinzufügen von neuen Posten führt nun nicht mehr zu einem leeren Posten
* Mehrere PDFs beim Senden von Angeboten wählbar machen 
Die Dateien müssen im selben Verzeichnis liegen und sinnvolle Dateinamen bekommen, da diese 1:1 übermittelt werden.

* Sinnvolle Dateinamen Rechnungsdateien beim E-Mail senden vergeben
* Übersicht/Suche Rechnungen, Angebote, Bestellungen, Reparaturen - Anzeige und Suche nach Nummern, wie sie auf dem Ausdruck dargestellt werden
* Rechnungen aus Angeboten erstellen ermöglicht
* Korrekte Vorbefüllung der Nummernfelder der Suchdialoge nach Angeboten, Bestellungen und Reparaturen

## Version 2.4.0

* Ändern des Kunden beim Bearbeiten von Angeboten ermöglicht
* Angebote per E-Mail versenden ermöglicht
* E-Mails generell in Gesendet-Ordner ablegen
* Reparaturauftrag drucken - Schriftgröße für Felder Gerät, SN, Symptome, Geplante Aufgaben everringert, um mehr Platz zu bekommen
* Druckansicht von Bestellungen korrekt darstellen, wenn Kunde zur Bestellung gelöscht wurde
* Bestellungen um Anruf- und Warenbestellinformationen erweitert
* Lokalisierung und Aulagerung von Text/Labels an zentraler Stelle
* Verbesserung Suche nach Telefonnummer - Platzhalter nur wie in Suchfeld angegeben verwenden
* Duplizieren von Angeboten ermöglichen
* Produkt suchen-Abschnitt aus Rechnungen in eigene Komponente zur Wiedververwendung ausgelagert
* Angebote zusätzlich über Produkte aus dem Inventar befüllbar machen
* Rechnungssuche erweitert um Kontrollkästchen, ob Angebote, Reparaturen, Bestellungen zur Rechnung hinterlegt sind
* Bessere hervorhebung welche Suchfelder gemeinsam benutzt werden beim Suchen von Rechnungen/Angeboten
* Funktion in Rechnungsübersicht um Lieferschein aus Rechnung zu erstellen

## Version 2.3.1


## Version 2.3.0

* Anzahl Exemplare bei Rechnungen anpassen
  * Bei Überweisungen immer nur 1 Exemplar drucken
  * Beim Nutzen der E-Mail Funktion immer nur 1 Exemplar drucken
  * Anzahl der Exemplare bei Rechnungen in der URL frei definierbar

* Bestellung bei Reparaturauftrag auswählbar
  * Selektiert den Kunden und die Kosten vor
  * Die Bestellung wird beim Speichern des Reparaturauftrags erledigt
  * Wird beim Rechnung erstellen eine Reparatur mit hinterlegter Bestellung gewählt, wird die Bestellung ebenfalls gewählt

* Auswahlfeld für Ersteller beim Erstellen von Angeboten und Bestellungen hinzugefügt
* Rechnungsnummer auf Rechnungen mehr hervorheben, Auftrags-/Bestellnummern weniger hervorheben
* Informationen bei Überweisungen um Verwendungszweck erweitert

## Version 2.2.6

* Telefonnummerformatierung - Vorwahl nur anhängen, wenn Telefonnummer nicht leer
* Rechnungen, Seriennummern beim Scannen kommagetrennt darstellen
Das letzte Komma wird beim Verlassen des Feldes (z.B. durch Klick auf Speichern) automatisch entfernt

## Version 2.2.5

* Nach dem Anlegen/Bearbeiten von Kunden nur Suchfelder Firmenname/Nachname/Vorname vorbefüllen
* Neues Feld beim Kunden 'Bemerkung zur Telefonnummer' hinzugefügt, erscheint auf Reparaturauftrag, Bestellung
 
## Version 2.2.4

* Leerzeichen am Anfang/Ende beim Suchen/Speichern von Kunden (Firmenname, Nachname, Vorname) entfernen
* Angebotspositionen nach Positionsnummer sortieren beim nachträglichen Bearbeiten
* Angebotspositionen mit kleiner oder gleich 0 Eur ermöglichen, um Rabatte zu erlauben
* Trennlinien zwischen Angebotspositionen beim Druck hinzugefügt
* Tabellenanzeige für Anzahl Datensätze beim ersten Laden der Seite korrigiert
* Bestellung aus Angebot erstellen - Korrekte Anzeige der Preise