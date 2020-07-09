# Tools für die Computerinsel

Dieses Programm beinhaltet verschiedene Tools, welche für die Computerinsel benötigt werden.

Dazu zählen:
- Reparaturaufträge verwalten
- Rechnungen verwalten
- Angebote verwalten
- Bestellungen verwalten
- Kunden verwalten
- Kassenbücher verwalten
- Kassenstand berechnen
- Statistiken erstellen
- Inventar verwalten
- Einkäufe tätigen

Um allgemein auf die Tools zugreifen zu können wird ein gültiger Login benötigt.
Die verschiedenen Seiten und Funktionen sind durch Berechtigungen, die den Mitarbeitern zugeteilt werden müssen, abgesichert.

## Reparaturen

Die Seite Reparaturen unterteilt sich in die zwei Unterseiten **Erstellen** und **Reparaturen**.

### Erstellen
Um diese Seite öffnen zu können wird das Recht *ZUGRIFF_REPARATUR_ERSTELLEN* benötigt.
Um den Reparaturauftrag nach der Erstellung ansehen und drucken zu können, wird das Recht *REPARATUR* benötigt.

Auf dieser Seite befinden sich verschiedene Felder die beim Erstellen eines Reparaturauftrages zu befüllen sind.
Nach dem Speichern öffnet sich eine neue Seite für den Druck des Auftrages.
Ebenfalls wird ein Hinweis geöffnet, dass das DSGVO-Informationsblatt vom Kunden zu Unterzeichnen ist, wenn er dies noch nicht getan hat.

#### Abholdatum/-zeit
Das Abholdatum und die Abholzeit werden dabei automatisch gefüllt.
Das Standardabholdatum wird dabei auf das Datum in **3** Werktagen (Samstag ausgenommen) und die Abholzeit auf **16:30 Uhr** gesetzt.

Wird die Expressbearbeitung aktiviert und es ist vor **13:30 Uhr** wird das Abholdatum auf den aktuellen Tag um **18:30 Uhr** gesetzt. Ist es nach 13:30 Uhr, wird das Abholdatum auf den nächsten Tag **13:30 Uhr** gesetzt.

Ist das berechnete Abholdatum zu 5 oder mehr nicht erledigten anderen Reparaturaufträgen hinterlegt, wird dieses auf den nächsten Tag gesetzt, an dem weniger
Abholungen stattfinden sollen.

Fällt das Abholdatum auf einen ein Sonn- oder Feiertag, wird dieses auf den nächst verfügbaren Werktag gesetzt. Dabei werden die sächsischen gesetzlichen Feiertage berücksichtigt.

#### Kunde wählen
Um den Kunden zu wählen muss neben dem Label *Kunde* das *Kunde-Icon* geklickt werden. Anschließend öffnet sich ein Dialog in dem der entsprechende Kunde gesucht werden kann.
Durch Klick auf das *OK-Icon* wird der Kunde ausgewählt und der Dialog schließt sich.

Ist das Recht *KUNDEN_VERWALTEN* gesetzt, können in diesem Dialog neue Kunden angelegt oder bestehende Kunden editiert werden.

#### Gerät geprüft
Bevor ein Reparaturauftrag erstellt werden kann, muss das Gerät auf Funktionsfähigkeit geprüft werden. Wird die Option *Gerät funktioniert nicht* gewählt, können im Bemerkungsfeld weitere Informationen diesbezüglich hinterlegt werden. Die Begründung erscheint dann ebenfalls auf dem Reparaturauftrag.

### Reparaturen
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_REPARATUR_UEBERSICHT* benötigt.
Das Recht *REPARATUR_VERWALTEN* wird benötigt um Reparaturaufträge zu bearbeiten, zu löschen oder zu erledigen.

Auf dieser Seite werden alle Reparaturaufträge aufgelistet. Mit Hilfe der Suchfilter können die Ergebnisse weiter eingeschränkt werden.

Durch Klick auf das *Öffnen-Icon* kann der Reparaturauftrag in der Druckansicht angezeigt werden. Dazu wird das Recht *REPARATUR* benötigt.
Durch Klick auf das *Bearbeiten-Icon* kann der Reparaturauftrag beliebig bearbeitet werden.

Durch Klick auf das *Smilie-Icon* kann der Reparaturauftrag erledigt oder geöffnet werden. Durch das Erledigen des Auftrags wird das Gerätepasswort gelöscht und eine Bearbeitung ist nicht mehr möglich.
Das erneute Öffnen des Auftrages stellt das Gerätepasswort **nicht** wieder her.

Durch Klick auf das *E-Mail-Icon* kann der Kunde informiert werden, dass der Auftrag abgeschlossen ist und dieser in die Filiale kommen soll.
Dieses Icon ist nur sichtbar, wenn zum Kunden eine E-Mail-Adresse hinterlegt ist.

Durch Klick auf das *Löschen-Icon* wird nach erneuter Bestätigung der Reparaturauftrag komplett gelöscht.

## Rechnungen
Die Seite Rechnungen unterteilt sich in die drei Unterseiten **Erstellen**, **Rechnungen** und **Export**.

### Erstellen
Um diese Seite öffnen zu können wird das Recht *ZUGRIFF_RECHNUNG_ERSTELLEN* benötigt.
Um die Rechnung nach der Erstellung ansehen und drucken zu können, wird das Recht *RECHNUNG* benötigt.

Auf dieser Seite befinden sich verschiedene Felder die beim Erstellen einer Rechnung zu befüllen sind.
Nach dem Speichern öffnet sich eine neue Seite für den Druck der Rechnung.
Ebenfalls wird ein Hinweis geöffnet, dass das DSGVO-Informationsblatt vom Kunden zu Unterzeichnen ist, wenn er dies noch nicht getan hat.

In der oberen Tabelle werden alle vorhandenen Produkte aufgezeigt. Mit Hilfe der Suchfilter können diese weiter eingeschränkt werden. Durch Drücken der Enter-Taste wird eine Suche ausgelöst.
Dabei ist zu beachten, dass sobald das Suchfeld EAN ausgefüllt ist, die weiteren Suchfelder ignoriert werden.

Wenn ein Produkt gewählt werden soll, kann das *Doppel-Häkchen-Icon* geklickt werden für eine Direktübernahme in die gewählten Posten. Alternativ kann das *OK-Icon* geklickt werden, um die Menge, den Rabatt, die Bezeichnung, die Seriennummer und den Hinweis vor der Übernahme in die gewählten Posten anzupassen.

In der darunterliegenden Tabelle befinden sich die gewählten Posten, welche auf der Rechnung erscheinen.
Mit dem *Plus-Icon* kann die Menge des Produktes um 1 erhöht werden.
Mit dem *Minus-Icon* kann die Menge des Produktes um 1 verringert werden. Wird das *Minus-Icon* geklickt, während sich der Posten nur noch ein mal auf der Liste befindet, wird dieser wieder entfernt.

Durch Klick auf das *Bearbeiten-Icon* besteht nochmals die Möglichkeit die Menge, den Rabatt, die Bezeichnung, die Seriennummer und den Hinweis zu bearbeiten.

Durch Klick auf das *Euro-Icon* öffnet sich der Rabattrechner, mit dessen Hilfe ein Rabatt festgelegt werden kann. Dabei wird der Einkaufspreis des Produktes berücksichtigt, insofern dieser hinterlegt ist und der Gewinn angezeigt.

Unterhalb der Tabelle der gewählten Posten kann ein prozentualer oder fester Gesamtrabatt gegeben werden.
Zusätzlich wird der summierte Einkaufsbruttopreis aller gewählten Posten angezeigt (insofern hinterlegt).
Aus dem Gesamtbetrag, Rabatt und dem Einkaufspreis wird die Differenz als Gewinn angezeigt.
Dabei wird ein Verlust durch Rotfärbung des Gewinnfeldes angezeigt.
Ist der Gewinn kleiner als 1 EUR wird das Gewinnfeld gelb eingefärbt.

Zuletzt muss noch die Zahlart gewählt werden. Dabei ist zu beachten, dass bei der Zahlart *ÜBERWEISUNG* der *Kunde* ausgewählt werden muss.

Mit des Kontrollkästchens kann gewählt werden, ob der Kundenname auf die Rechnung gedruckt werden soll. Ist kein Kunde ausgewählt, ist dieses Feld obsolet.

Nach dem Speichern wird im Inventar der Bestand des Produktes um die entsprechende Menge reduziert.

#### Angebot wählen
Um ein Angebot zu wählen muss neben dem Label *Angebot* das *Angebot-Icon* geklickt werden.
Anschließend öffnet sich ein Dialog in dem das entsprechende Angebot gesucht werden kann.
Durch Klick auf das *OK-Icon* wird das Angebot gewählt und der Dialog schließt sich.
Durch Wahl des Angebots wird automatisch der Kunde gewählt und die Rechnungsposten mit den Angebotsposten aufgefüllt.

Zudem wird der Rabatt übertragen.
Da dieser jedoch bei dem Angebot vom Netto-Betrag abgezogen wird und bei der Rechnung vom Brutto-Betrag,
wird der Rabatt umgerechnet, sodass der Gesamtbetrag der Rechnung und des Angebots übereinstimmen.
Kleine Abweichungen im Centbereich können dabei entstehen und müssen manuell korrigiert werden.

Wird das Angebot wieder abgewählt, bleiben die aus dem Angebot übernommenen Posten bestehen!

#### Kunde wählen
Um den Kunden zu wählen muss neben dem Label *Kunde* das *Kunde-Icon* geklickt werden. Anschließend öffnet sich ein Dialog in dem der entsprechende Kunde gesucht werden kann.
Durch Klick auf das *OK-Icon* wird der Kunde ausgewählt und der Dialog schließt sich.

Ist das Recht *KUNDEN_VERWALTEN* gesetzt, können in diesem Dialog neue Kunden angelegt oder bestehende Kunden editiert werden.

#### Reparaturauftrag wählen
Um den Reparaturauftrag zu wählen muss neben dem Label *Reparaturauftrag* das *Reparatur-Icon* geklickt werden. Anschließend öffnet sich ein Dialog in dem der entsprechende Auftrag gesucht werden kann.
Durch Klick auf das *OK-Icon* wird der Auftrag ausgewählt und der Dialog schließt sich.

Durch Wahl des Auftrages wird automatisch der Kunde gewählt.

### Rechnungen
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_RECHNUNG_UEBERSICHT* benötigt.
Das Recht *RECHNUNG_VERWALTEN* wird benötigt um Rechnungen zu bearbeiten oder zu löschen.

Auf dieser Seite befindet sich eine Auflistung aller Rechnungen. Mit Hilfe der Suchfilter kann die Anzeige weiter eingeschränkt werden.
Sind die Suchfilter nach den Kontrollkästchen *Mit Angeboten*, *Mit Bestellungen* und *Mit Reparaturaufträgen* aktiv, werden die darüberliegenden Suchfilter ignoriert.
Ist das Suchfeld *Rechnungsposten durchsuchen nach Bezeichnung / Seriennummer / Hinweis* gefüllt, werden die darüberliegenden Suchfilter ignoriert.

Durch Klick auf das *Öffnen-Icon* kann die Rechnung in der Druckansicht angezeigt werden. Dazu wird das Recht *RECHNUNG* benötigt.

Durch Klick auf das *Bearbeiten-Icon* kann die Rechnung bearbeitet werden.
Dabei ist es nur möglich das Rechnungsdatum, die Zahlart, den Kunden und Reparaturauftrag zu ändern.
Zudem darf die Menge, Bezeichnung, der Hinweis, die Seriennummer und der Rabatt von bestehenden Posten geändert werden. Es dürfen jedoch keine neuen Posten hinzugefügt oder bestehende entfernt werden.
Das Bearbeiten der Menge nimmt keinen Einfluss auf den Bestand im Inventar. Dies wird lediglich beim Erstellen beeinflusst.

Durch Klick auf das *Lieferschein-Icon* kann zur Rechnung ein Lieferschein in der Druckansicht angezeigt werden. Dazu wird das Recht *RECHNUNG* benötigt.

Durch Klick auf das *E-Mail-Icon* kann dem Kunden die Rechnung zugesendet werden. Dazu muss die Rechnung zuerst als PDF gedruckt und im
sich öffnenden Dialog gewählt werden.
Dieses Icon ist nur sichtbar, wenn zum Kunden eine E-Mail-Adresse hinterlegt ist.

Durch Klick auf das *Löschen-Icon* kann eine Rechnung nach nochmaliger Bestätigung gelöscht werden.

### Export
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_RECHNUNG_EXPORT* benötigt.
Das Recht *RECHNUNG_EXPORT* wird benötigt, um die Exportieren-Schaltfläche zu aktivieren.

Auf dieser Seite muss der Monat und das Jahr gewählt werden.
Es wird immer der aktuelle Monat und das aktuelle Jahr vorgeblendet.

Nach dem Klick auf die Exportieren-Schaltfläche werden alle Rechnungen mit dem Rechnungsdatum im gewählten Monat/Jahr exportiert.
Zusätzlich werden alle Posten aus den Kassenbüchern des Monats exportiert, welche nicht identisch mit einer Rechnungsnummer in dem Monat sind.
Aus diesem Grund ist es notwendig, mehrfach vorhandene Kassenbucheinträge für einen Tag zu löschen, sodass je Tag nur ein Eintrag vorhanden ist.

Das Dateiformat ist hierbei TXT und besitzt ein fest definiertes Muster.
Die Konten werden aus den zur Rechnungen hinterlegten Filialen ermittelt.
Die Konten können in den Einstellung angepasst werden.

## Angebote
Die Seite Angebote unterteilt sich in die zwei Unterseiten **Erstellen** und **Angebote**.

### Erstellen
Um diese Seite öffnen zu können wird das Recht *ZUGRIFF_ANGEBOT_ERSTELLEN* benötigt.
Um das Angebot nach der Erstellung ansehen und drucken zu können, wird das Recht *ANGEBOT* benötigt.

Auf dieser Seite befinden sich eine Tabelle in welcher die einzelnen Posten des Angebots anzugeben sind.
Die Preise sind dabei in Netto anzugeben.

Unterhalb der Tabelle der gewählten Posten kann ein prozentualer oder fester Gesamtrabatt gegeben werden.
Um die MwSt korrekt auszuweisen, wird dieser vom Netto-Betrag abgezogen.
Zusätzlich wird der summierte Einkaufsbruttopreis aller gewählten Posten angezeigt (insofern hinterlegt).
Aus dem Gesamtbetrag, Rabatt und dem Einkaufspreis wird die Differenz als Gewinn angezeigt.
Dabei wird ein Verlust durch Rotfärbung des Gewinnfeldes angezeigt.
Ist der Gewinn kleiner als 1 EUR wird das Gewinnfeld gelb eingefärbt.

Nach dem Speichern öffnet sich eine neue Seite für den Druck des Angebots.
Ebenfalls wird ein Hinweis geöffnet, dass das DSGVO-Informationsblatt vom Kunden zu Unterzeichnen ist, wenn er dies noch nicht getan hat.

#### Kunde wählen
Um den Kunden zu wählen muss neben dem Label *Kunde* das *Kunde-Icon* geklickt werden. Anschließend öffnet sich ein Dialog in dem der entsprechende Kunde gesucht werden kann.
Durch Klick auf das *OK-Icon* wird der Kunde ausgewählt und der Dialog schließt sich.

Ist das Recht *KUNDEN_VERWALTEN* gesetzt, können in diesem Dialog neue Kunden angelegt oder bestehende Kunden editiert werden.

### Angebote
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_ANGEBOT_UEBERSICHT* benötigt.
Das Recht *ANGEBOT_VERWALTEN* wird benötigt um Aufträge zu bearbeiten oder zu löschen.

Auf dieser Seite befindet sich eine Auflistung aller Angebote. Mit Hilfe der Suchfilter kann die Anzeige weiter eingeschränkt werden.
Ist das Suchfeld *Angebotsposten-Bezeichnung* gefüllt, werden alle anderen Suchfelder ignoriert. Mit Hilfe dieses Suchfeldes können die Angebotsposten durchsucht werden.

Durch Klick auf das *Öffnen-Icon* kann das Angebot in der Druckansicht angezeigt werden. Dazu wird das Recht *ANGEBOT* benötigt.

Durch Klick auf das *Bearbeiten-Icon* kann das Angebot bearbeitet werden.

Durch Klick auf das *Duplizieren-Icon* kann das Angebot dupliziert werden.

Durch Klick auf das *E-Mail-Icon* kann dem Kunden das Angebot und weitere PDF-Dokumente zugesendet werden. Dazu muss das Angebot zuerst als PDF gedruckt und im
sich öffnenden Dialog gewählt werden. Alle weiteren Dokumente müssen sich ebenfalls in diesem Ordner befinden und können dort gewählt werden.
Der Dateiname der Angebotsdatei und der Anhänge ist hier relevant, da diese 1:1 an den Kunden so übermittelt werden.
Die Sortierung der Dateien in der Liste der Anhänge ist abhängig davon, wie die Dateien im Auswahlexplorer sortiert sind.
Die Reihenfolge in der Dokumente angeklickt werden ist nicht relevant.
Dieses Icon ist nur sichtbar, wenn zum Kunden eine E-Mail-Adresse hinterlegt ist.

Durch Klick auf das *Löschen-Icon* kann ein Angebot nach nochmaliger Bestätigung gelöscht werden.

## Bestellung
Die Seite Bestellung unterteilt sich in die zwei Unterseiten **Erstellen** und **Bestellungen**.

### Erstellen
Um diese Seite öffnen zu können wird das Recht *ZUGRIFF_BESTELLUNG_ERSTELLEN* benötigt.
Um die Bestellung nach der Erstellung ansehen und drucken zu können, wird das Recht *BESTELLUNG* benötigt.

Auf dieser Seite befinden sich verschiedene Felder die beim Erstellen einer Bestellung zu befüllen sind.
Nach dem Speichern öffnet sich eine neue Seite für den Druck der Bestellung.
Ebenfalls wird ein Hinweis geöffnet, dass das DSGVO-Informationsblatt vom Kunden zu Unterzeichnen ist, wenn er dies noch nicht getan hat.

#### Angebot wählen
Um das Angebot zu wählen muss neben dem Label *Angebot* das *Angebot-Icon* geklickt werden. Anschließend öffnet sich ein Dialog in dem das entsprechende Angebot gesucht werden kann.
Durch Klick auf das *OK-Icon* wird das Angebot ausgewählt und der Dialog schließt sich.

#### Kunde wählen
Um den Kunden zu wählen muss neben dem Label *Kunde* das *Kunde-Icon* geklickt werden. Anschließend öffnet sich ein Dialog in dem der entsprechende Kunde gesucht werden kann.
Durch Klick auf das *OK-Icon* wird der Kunde ausgewählt und der Dialog schließt sich.

Ist das Recht *KUNDEN_VERWALTEN* gesetzt, können in diesem Dialog neue Kunden angelegt oder bestehende Kunden editiert werden.

### Bestellungen
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_BESTELLUNG_UEBERSICHT* benötigt.
Das Recht *BESTELLUNG_VERWALTEN* wird benötigt um Bestellungen zu bearbeiten oder zu löschen.

Auf dieser Seite befindet sich eine Auflistung aller Bestellungen. Mit Hilfe der Suchfilter kann die Anzeige weiter eingeschränkt werden.

Durch Klick auf das *Öffnen-Icon* kann die Bestellung in der Druckansicht angezeigt werden. Dazu wird das Recht *BESTELLUNG* benötigt.

Durch Klick auf das *Bearbeiten-Icon* kann die Bestellung bearbeitet werden.

Durch Klick auf das *Löschen-Icon* kann eine Bestellung nach nochmaliger Bestätigung gelöscht werden.

## Kunden
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_KUNDEN* benötigt.
Um Kunden Anlegen, Bearbeiten oder Löschen zu können, wird das Recht *KUNDEN_VERWALTEN* benötigt.
Um Reparaturaufträge zum Kunden öffnen zu können, wird das Recht *KUNDEN_REPARATUR* benötigt.
Um Rechnungen zum Kunden öffnen zu können, wird das Recht *KUNDEN_RECHNUNG* benötigt.

Auf dieser Seite werden alle Kunden aufgelistet. Mit Hilfe der Suchfilter kann die Anzeige weiter eingeschränkt werden.

Durch Klick auf das *Plus-Icon* kann ein neuer Kunde angelegt werden.
Durch Klick auf das *Reparatur-Icon* können alle Reparaturaufträge des Kunden angezeigt werden.
Durch Klick auf das *Euro-Icon* können alle Rechnungen des Kunden angezeigt werden.
Durch Klick auf das *Bearbeiten-Icon* können die Kundendaten bearbeitet werden.

Durch Klick auf das *Duplikat-Icon* werden alle Kundendatensätze mit demselben Namen vorgefiltert.
Durch Auswahl eines der Kundendatensätze werden alle Rechnungen und Reparaturaufträge dieses Kunden in den in der Hauptansicht gewählten
Kundendatensatz übernommen. Der Kundendatensatz wird anschließend gelöscht.

Durch Klick auf das *Löschen-Icon* kann nach erneuter Bestätigung der Kunde gelöscht werden. Dabei werden keine Reparaturaufträge oder Rechnungen gelöscht. Jedoch wird die Verknüpfung zum Kunden entfernt, sodass diese Daten beim Ansehen der Aufträge/Rechnungen nicht mehr ersichtlich sind.

## Inventar
Die Seite Inventar unterteilt sich in die sechs Unterseiten **Produkte**, **Gruppen**, **Kategorien**, **Inventur**, **Export** und **Import**.

### Produkte
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_INVENTAR_PRODUKT* benötigt.
Um Produkte Anlegen, Bearbeiten oder Löschen zu können, wird das Recht *INVENTAR_PRODUKT_VERWALTEN* benötigt.

Auf dieser Seite werden alle Produkte angezeigt. Mit Hilfe der Suchfilter kann die Anzeige weiter eingeschränkt werden.
Dabei ist zu beachten, dass sobald das Suchfeld EAN ausgefüllt ist, die weiteren Suchfelder ignoriert werden.

Wird das Kontrollkästchen *Schnellerfassung* aktiviert, wird nach jedem Drücken der *Enter*-Taste oder Klicken auf *Suchen* die Anzahl des gefunden Produktes
um 1 erhöht, wenn es nur ein einziges Suchergebnis gibt und das Produkt nicht die Einstellung *Unbegrenzt* gesetzt hat.
Zusätzlich ist es nach Aktivieren der *Schnellerfassung* möglich, mit Hilfe der *Entf*-Taste das Feld EAN zu leeren.

Durch Klick auf das *Plus-Icon* kann ein neues Produkt angelegt werden.

Durch Klick auf das *Bearbeiten-Icon* können die Produktdaten bearbeitet werden. Dabei ist es auch möglich, das Produkt einer anderen Produktgruppe oder -kategorie zuzuordnen.

Durch Klick auf das *Duplizieren-Icon* kann das gewählte Produkt dupliziert werden. Erst durch Bestätigen mit *Speichern* wird das Produkt als neuer Datensatz angelegt.

Durch Klick auf das *Löschen-Icon* kann nach erneuter Bestätigung das Produkt gelöscht werden. Dabei werden keine Posten aus den Rechnungen gelöscht. Die Bezeichnung der Posten wird in der Rechnung weiterhin angezeigt, da diese gesondert gespeichert wird.

#### Ungebrenzter Bestand
Dieses Kontrollkästchen sollte nur gesetzt werden, wenn der Bestand dieses Produktes nicht messbar ist. Bspw. ist dies bei Dienstleistungen der Fall. Für diese Produkte macht es somit auch keinen Sinn einen Einkaufspreis anzugeben.

### Gruppen
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_INVENTAR_GRUPPE* benötigt.
Um Gruppen Anlegen, Bearbeiten oder Löschen zu können, wird das Recht *INVENTAR_GRUPPE_VERWALTEN* benötigt.

Auf dieser Seite werden alle Produktgruppen angezeigt. Mit Hilfe der Suchfilter kann die Anzeige weiter eingeschränkt werden.

Jede Gruppe kann nur einer Kategorie zugeordnet werden. Somit ist es auch möglich, dass Gruppennamen mehrfach vorkommen.

Durch Klick auf das *Plus-Icon* kann eine neue Gruppe angelegt werden.

Durch Klick auf das *Bearbeiten-Icon* kann die Gruppe bearbeitet werden. Dabei ist es auch möglich, die Gruppe einer anderen Produktkategorie zuzuordnen.

Durch Klick auf das *Löschen-Icon* kann nach erneuter Bestätigung die Gruppe gelöscht werden. Dabei werden ebenfalls alle Produkte, die zu dieser Gruppe gehören, gelöscht.

### Kategorien
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_INVENTAR_KATEGORIE* benötigt.
Um Kategorien Anlegen, Bearbeiten oder Löschen zu können, wird das Recht *INVENTAR_KATEGORIE_VERWALTEN* benötigt.

Auf dieser Seite werden alle Produktkategorien angezeigt. Mit Hilfe der Suchfilter kann die Anzeige weiter eingeschränkt werden.

Es ist empfehlenswert die Kategoriebezeichnung eindeutig zu halten, um Verwechslungen zu vermeiden.

Durch Klick auf das *Plus-Icon* kann eine neue Kategorie angelegt werden.
Durch Klick auf das *Bearbeiten-Icon* kann die Kategorie bearbeitet werden.

Durch Klick auf das *Löschen-Icon* kann nach erneuter Bestätigung die Kategorie gelöscht werden. Dabei werden ebenfalls alle Produkte und Produktgruppen, die zu dieser Kategorie gehören, gelöscht.

### Inventur
Um diese Seite öffnen zu können und die Inventur durchführen zu können, wird das Recht *ZUGRIFF_INVENTAR_INVENTUR* benötigt.

Auf dieser Seite kann die Inventur durchgeführt werden.
Dabei werden alle Produkte deren Bestand größer 0 ist und welche nicht die Einstellung *Unbegrenzt* berücksichtigt.
Die Anzahl, die Bezeichnung, der Netto-Einkaufspreis und der Netto-Einkaufspreis * Anzahl, werden alphabetisch in einer Excel-Datei ins Ablageverzeichnis abgelegt.
Am Ende der Datei wird die Gesamtsumme angezeigt.

Besitzt das Produkt keinen Netto-Einkaufspreis, wird dieser folgendermaßen errechnet: Netto-Verkaufspreis - 10% des Netto-Verkaufspreis.
Besitzt das Produkt keinen Netto-Verkaufspreis, wird das Produkt nicht berücksichtigt.

Zwischen dem 26. Dezember und 5. Januar wird unabhängig davon, ob die Inventur bereits durchgeführt wurde, eine Erinnerungsmeldung angezeigt.

### Export
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_INVENTAR_EXPORT* benötigt.

Auf dieser Seite werden alle im Zeitraum geänderten Produkte angezeigt, welche eine EAN besitzen.
Diese können durch Klick auf die *Exportieren-Schaltfläche* im JSON-Format in das Ablageverzeichnis exportiert werden.
Um die angezeigten Produkte exportieren zu können, wird das Recht *INVENTAR_EXPORT* benötigt.

Durch Klick auf das *Minus-Icon* können Produkte für den Export abgewählt werden.

### Import
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_INVENTAR_IMPORT* benötigt.

Auf dieser Seite kann eine zuvor exportierte JSON-Datei mit Produkten eingelesen werden.
Nach Auswahl der JSON-Datei werden die Datensätze angezeigt.
Diese können durch Klick auf die *Importieren-Schaltfläche* in den eigenen Produktdatenbestand importiert werden.
Um die angezeigten Produkte importieren zu können, wird das Recht *INVENTAR_IMPORT* benötigt.

Durch Klick auf das *Minus-Icon* können Produkte für den Import abgewählt werden.

Es werden nur Produkte importiert, wenn die EAN einmalig vorhanden ist.
Werden keine oder mehrere Produkte gefunden, findet keine Anpassung statt.
Angepasst werden die Brutto/Netto-Einkaufs/-Verkaufspreise, sowie, ob der Bestand unbegrenzt ist.
Die Bezeichnung des Produktes wird nicht angepasst.

## Kassenbuch
Die Seite Kassenbuch unterteilt sich in die vier Unterseiten **Erstellen**, **Kassenbücher**, **Kassenstand** und **Statistiken**.

### Erstellen
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_KASSENBUCH_ERSTELLEN* benötigt.
Um das Kassenbuch nach der Erstellung ansehen und drucken zu können, wird das Recht *KASSENBUCH* benötigt.

Beim Öffnen der Seite werden automatisch aus den BAR-Rechnungen des aktuellen Tages alle Einträge für das Kassenbuch angezeigt.

Mit Hilfe des Feldes *Rechnungsdatum* können die Einträge auch für andere Tage angezeigt werden.

Der Ausgangsbetrag entspricht dem Kassenstand vom Vortrag und wird nach jedem erzeugtem Kassenbuch gespeichert.

Durch Klick auf das *Plus-Icon* kann eine manuelle Eintragung hinzugefügt werden. Dies kann z.B. die Beschaffung von Büroartikeln sein oder eine stornierte Rechnung.
Diese Eintragungen werden immer am Ende des Kassenbuchs in der Reihenfolge, in welcher sie hinzugefügt wurden, angezeigt.

Unter der Tabelle befindet sich ein Feld, in welchem der Gesamtbetrag des Kassenbuchs (inkl. des Ausgangsbetrags) vorgeblendet wird.
Durch Klick auf die Schaltfläche *Speichern* wird das Kassenbuch gespeichert und für den Druck geöffnet.
Gibt es an diesem Tag keine BAR-Einnahmen-/Ausgaben, kann die Schaltfläche *Keine BAR Einnahmen/Ausgaben* geklickt werden.
Anschließend wird ein leerer Kassenbericht erstellt.

### Kassenbücher
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_KASSENBUCH_UEBERSICHT* benötigt.
Um die Kassenbücher öffnen zu können, wird das Recht *KASSENBUCH* benötigt.
Um ein Kassenbuch löschen zu können, wird das Recht *KASSENBUCH_VERWALTEN* benötigt.

Auf dieser Seite befindet sich eine Übersicht aller erstellten Kassenbücher. Mit Hilfe der Suchfilter kann die Anzeige weiter eingeschränkt werden.

Durch Klick auf das *Öffnen-Icon* kann die Druckansicht des Kassenbuchs geöffnet werden.
Durch Klick auf das *Löschen-Icon* kann nach erneuter Bestätigung das Kassenbuch gelöscht werden.

Das Bearbeiten ist nicht möglich, um eine Manipulation zu verhindern.

### Kassenstand
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_KASSENBUCH_KASSENSTAND* benötigt.

Auf dieser Seite kann geprüft werden, ob der mit dem Kassenbuch errechnete Betrag mit dem Betrag, welcher sich in der Kasse befindet, übereinstimmt.

Im Feld *Gesamt Kassenbuch* wird der über das Kassenbuch berechnete Gesamtbetrag vorgeblendet. Den Kassenstand zu ermitteln, macht demnach erst Sinn, nachdem das Kassenbuch für den aktuellen Tag erstellt wurde.

Mit Hilfe der Pfeiltasten kann in den Feldern der Betrag entsprechend erhöht oder verringert werden. Durch Klick auf die Schaltfläche *Speichern* wird eine Excel-Datei mit dem Kassenstand in das Ablageverzeichnis gelegt und der Kassenstand für den nächsten Tag gemerkt.

### Statistiken
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_KASSENBUCH_STATISTIK* benötigt.

Auf dieser Seite kann eine Statistik über die geschriebenen Rechnungen innerhalb des gewählten Zeitraums erstellt werden. Wird zusätzlich im Feld *Posten* etwas angegeben wird eine Statistik zu diesem Posten innerhalb des Zeitraums erstellt.
Die Statistik wird als Excel-Datei in das Ablageverzeichnis gelegt. Dabei wird je Monat und Zahlart der Ertrag der Rechnung/des Postens aufgelistet.

Alternativ kann mit Hilfe der Schaltfläche *Überweisungen* eine Auflistung aller Rechnungen im angegebenen Zeitraum mit der Zahlart *ÜBERWEISUNG* erstellt werden.
Diese wird ebenfalls in das Ablageverzeichnis gelegt und zeigt die Rechnungsnummer, die Kundenanschrift und Telefonnummer, sowie den Rechnungsbetrag.
Das Feld *Posten* wird dabei nicht berücksichtigt.

## Einkauf
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_EINKAUF* benötigt.

Bei jeder Rechnung die geschrieben wird, werden die verkauften Produkte aus dem Inventar vorgemerkt und mit Anzahl, EAN und Produktbezeichnung vorgeblendet.
Diese Liste kann genutzt werden um die Produkte zum Einkauf hinzuzufügen.

Die Einkaufsliste wird angezeigt und kann bearbeitet und gespeichert werden.
Die zum Mitarbeiter hinterlegte Filiale ist ausschlaggebend, welche Einkaufsliste geladen und bearbeitet wird. Dabei wird der Ortsname der Filiale als Dateiname verwendet.

## Einstellungen
Die Seite Kassenbuch unterteilt sich in die drei Unterseiten **Allgemein**, **Filiale** und **Mitarbeiter**.

### Allgemein
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_EINSTELLUNGEN_ALLGEMEIN* benötigt.

#### Ablageverzeichnis
Das hier definierte Verzeichnis wird verwendet, um die Statistiken und Kassenstände abzulegen.

#### Mehrwertsteuer in Prozent
Hier kann die Mehrwertsteuer verändert werden.
Nach dem Speichern dieser Einstellung wird bei allen neu erstellten Rechnungen und Angeboten mit der neuen Mehrwertsteuer gerechnet.
Zudem wird beim Bearbeiten von Produkten die neue Mehrwertsteuer berücksichtigt.
Zusätzlich steht eine Schaltfläche zur Verfügung um den Netto-Verkaufspreis aller Produkte anzupassen.
Dabei wird der Brutto-Verkaufspreis als Basis verwendet und nicht verändert.
Die Einkaufspreise werden ebenfalls nicht automatisch verändert.

#### FTP-Server
Hier sind die Verbindungsdaten zum FTP-Server anzugeben.
Diese Daten dienen dazu, um die Bestellliste lesen/schreiben zu können.

#### SMTP-Server
Hier sind die Verbindungsdaten zum SMTP-Server anzugeben.
Dabei sind beim Benutzernamen/Passwort die Daten der Filialenadresse anzugeben.
Die Daten werden benötigt, um den Kunden per E-Mail über einen abgeschlossenen Reparaturauftrag zu informieren oder eine Rechnung oder ein Angebot zu senden.
Mit Hilfe der E-Mail-Inhalt-Felder kann allgemein gültig der Inhalt der E-Mail bestimmt werden. Als Platzhalter kann #NUMMER# verwendet werden,
um die Rechnungs-, Reparatur oder Angebotsnummer in den Text einzufügen.
Die Signatur wird immer im Fuß der E-Mail hinzugefügt.

## Filialen
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_EINSTELLUNGEN_FILIALEN* benötigt.
Um Filialen Anlegen oder Bearbeiten zu können, wird das Recht *FILIALEN_VERWALTEN* benötigt.

Auf dieser Seite befindet sich eine Auflistung aller Filialen.

Durch Klick auf das *Plus-Icon* kann eine neue Filiale angelegt werden.
Durch Klick auf das *Bearbeiten-Icon* kann die Filiale bearbeitet werden.

Die Adressdaten, sowie Telefon, E-Mail, Kürzel und der Text "Besuchen Sie uns auch in unserer Filiale in" erscheinen auf den Rechnungen und Reparaturaufträgen.
Eine Änderung dieser Daten wirkt sich auf bestehende Rechnungen und Aufträge aus.

### Rechnungsnummer
Die Rechnungsnummer setzt sich zusammen aus dem Kürzel der Filiale, dem Jahr des Rechnungsdatums und einer fortlaufenden Nummer, welche automatisch immer auf 4 Stellen angepasst wird.

Ist beispielsweise das Rechnungsdatum der 31.12.2018 und die fortlaufende Nummer 455, dann lautet die Rechnungsnummer, wenn das Kürzel der Filiale L ist: *L18-0455*

In diesem Feld muss lediglich die fortlaufende Nummer angegeben werden. Es empfiehlt sich beim Jahreswechsel die Nummer manuell zurückzusetzen auf 0.
Angezeigt wird immer die zuletzt verwendete Nummer (NICHT die nächste Nummer).

### Reparaturnummer
Die Reparaturnummer setzt sich zusammen aus dem Kürzel der Filiale, dem aktuellen Jahr und einer fortlaufenden Nummer, welche automatisch immer auf 4 Stellen angepasst wird.

Ist beispielsweise heute der 31.12.2018 und die fortlaufende Nummer 455, dann lautet die Reparaturnummer, wenn das Kürzel der Filiale L ist: *L180455*

In diesem Feld muss lediglich die fortlaufende Nummer angegeben werden. Es empfiehlt sich beim Jahreswechsel die Nummer manuell zurückzusetzen auf 0.
Angezeigt wird immer die zuletzt verwendete Nummer (NICHT die nächste Nummer).

### Angebotsnummer
Die Angebotsnummer setzt sich zusammen aus dem Kürzel der Filiale, dem aktuellen Jahr und einer fortlaufenden Nummer, welche automatisch immer auf 4 Stellen angepasst wird.

Ist beispielsweise heute der 31.12.2018 und die fortlaufende Nummer 455, dann lautet die Angebotsnummer, wenn das Kürzel der Filiale L ist: *L180455*

In diesem Feld muss lediglich die fortlaufende Nummer angegeben werden. Es empfiehlt sich beim Jahreswechsel die Nummer manuell zurückzusetzen auf 0.
Angezeigt wird immer die zuletzt verwendete Nummer (NICHT die nächste Nummer).

## Mitarbeiter
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_EINSTELLUNGEN_MITARBEITER* benötigt.
Um Mitarbeiter Anlegen, Bearbeiten oder Löschen zu können, wird das Recht *MITARBEITER_VERWALTEN* benötigt.
Um das Passwort eines Mitarbeiters zurücksetzen zu können, wird das Recht *MITARBEITER_RESET* benötigt.
Um die Rechte eines Mitarbeiters bearbeiten zu können, wird das Recht *MITARBEITER_RECHTE* benötigt.

Auf dieser Seite befindet sich eine Auflistung aller Mitarbeiter.

Durch Klick auf das *Plus-Icon* kann ein neuer Mitarbeiter angelegt werden. Dabei setzt sich der Benutzername aus dem ersten Zeichen des Vornamens und dem gesamten Nachnamen zusammen. Ergibt sich somit ein Benutzername, welcher kürzer als 6 Zeichen ist, werden die fehlenden Zeichen am Ende des Benutzernamens mit der Zahl *0* aufgefüllt.
Der Mitarbeiter wird ohne Berechtigungen erstellt.

Durch Klick auf das *Schlüssel-Icon* kann nach erneuter Bestätigung das Passwort des Mitarbeiters zurückgesetzt werden. Dieses wird auf den Benutzernamen gesetzt.

Durch Klick auf das *Bearbeiten-Icon* können die Daten des Mitarbeiters bearbeitet werden.
Die gewählte Filiale wirkt sich dabei auf das Erstellen von Reparaturaufträgen und Rechnungen aus. Je nachdem welche Filiale gewählt wurde, werden auf den
Rechnungen und Reparaturaufträgen andere Adressdaten zur Filiale gedruckt.

Durch Klick auf das *Löschen-Icon* kann der Mitarbeiter nach erneuter Bestätigung gelöscht werden. Dies hat keine Auswirkungen auf bestehende Verknüpfungen zu Rechnungen, Reparaturaufträgen oder Kassenbüchern, da der Mitarbeitername gesondert gespeichert wird.

Durch Klick auf das *Sicherheit-Icon* können die Rechte des Mitarbeiters angepasst werden.

#### Rechte
Folgende Rechte sind verfügbar:

- **ZUGRIFF_KASSENBUCH_ERSTELLEN** - Zugriff auf die Seite: Kassenbuch erstellen
- **ZUGRIFF_KASSENBUCH_UEBERSICHT** - Zugriff auf die Seite: Übersicht Kassenbücher
- **KASSENBUCH** - Ermöglicht das Anzeigen der Druckansicht eines Kassenbuches
- **ZUGRIFF_KASSENBUCH_KASSENSTAND** - Zugriff auf die Seite: Kassenstand berechnen
- **ZUGRIFF_KASSENBUCH_STATISTIK** - Zugriff auf die Seite: Statistik
- **ZUGRIFF_EINSTELLUNGEN_ALLGEMEIN** - Zugriff auf die Seite: Einstellungen allgemein
- **ZUGRIFF_EINSTELLUNGEN_FILIALEN** - Zugriff auf die Seite: Filiale
- **FILIALEN_VERWALTEN** - Filialen können angelegt, bearbeitet oder gelöscht werden
- **ZUGRIFF_EINSTELLUNGEN_MITARBEITER** - Zugriff auf die Seite Mitarbeiter
- **MITARBEITER_VERWALTEN** - Mitarbeiter dürfen angelegt, bearbeitet oder gelöscht werden
- **MITARBEITER_RESET** - Das Passwort von anderen Benutzern darf zurückgesetzt werden
- **MITARBEITER_RECHTE** - Die Berechtigungen von anderen Benutzern dürfen angepasst werden
- **ZUGRIFF_KUNDEN** - Zugriff auf die Seite Kunden
- **KUNDEN_VERWALTEN** - Kunden dürfen angelegt, bearbeitet oder gelöscht werden
- **KUNDEN_REPARATUR** - Ermöglicht das Anzeigen aller Reparaturaufträge eines Kunden
- **ZUGRIFF_REPARATUR_ERSTELLEN** - Zugriff auf die Seite Reparaturauftrag erstellen
- **ZUGRIFF_REPARATUR_UEBERSICHT** - Zugriff auf die Seite Übersicht Reparaturaufträge
- **REPARATUR** - Ermöglicht das Anzeigen der Druckansicht eines Reparaturauftrages
- **REPARATUR_VERWALTEN** - Reparaturaufträge dürfen angelegt, bearbeitet oder gelöscht werden
- **ZUGRIFF_INVENTAR_KATEGORIE** - Zugriff auf die Seite: Kategorie
- **INVENTAR_KATEGORIE_VERWALTEN** - Kategorien können angelegt, bearbeitet oder gelöscht werden
- **ZUGRIFF_INVENTAR_GRUPPE** - Zugriff auf die Seite: Gruppe
- **INVENTAR_GRUPPE_VERWALTEN** - Gruppen können angelegt, bearbeitet oder gelöscht werden
- **ZUGRIFF_INVENTAR_PRODUKT** - Zugriff auf die Seite: Produkt
- **INVENTAR_PRODUKT_VERWALTEN** - Produkte können angelegt, bearbeitet oder gelöscht werden
- **ZUGRIFF_INVENTAR_EXPORT** - Zugriff auf die Seite: Export
- **INVENTAR_EXPORT** - Produkte können exportiert werden
- **ZUGRIFF_INVENTAR_IMPORT** - Zugriff auf die Seite: Import
- **INVENTAR_IMPORT** - Produkte können importiert werden
- **KUNDEN_RECHNUNG** - Ermöglicht das Anzeigen aller Rechnungen eines Kunden
- **ZUGRIFF_RECHNUNG_ERSTELLEN** - Zugriff auf die Seite Rechnung erstellen
- **ZUGRIFF_RECHNUNG_UEBERSICHT** - Zugriff auf die Seite Übersicht Rechnungen
- **ZUGRIFF_RECHNUNG_EXPORT** - Zugriff auf die Seite Übersicht Export (Rechnungen)
- **RECHNUNG_EXPORT** - Rechnungen können exportiert werden
- **RECHNUNG** - Ermöglicht das Anzeigen der Druckansicht einer Rechnung
- **RECHNUNG_VERWALTEN** - Rechnungen dürfen angelegt, bearbeitet oder gelöscht werden
- **ZUGRIFF_EINKAUF** - Zugriff auf die Seite: Einkauf
- **KUNDEN_BESTELLUNGEN** - Ermöglicht das Anzeigen aller Bestellungen eines Kunden
- **ZUGRIFF_BESTELLUNG_ERSTELLEN** - Zugriff auf die Seite Bestellung erstellen
- **ZUGRIFF_BESTELLUNG_UEBERSICHT** - Zugriff auf die Seite Übersicht Bestellungen
- **BESTELLUNG** - Ermöglicht das Anzeigen der Druckansicht einer Bestellung
- **BESTELLUNG_VERWALTEN** - Bestellungen dürfen angelegt, bearbeitet oder gelöscht werden
- **KUNDEN_ANGEBOTE** - Ermöglicht das Anzeigen aller Angebote eines Kunden
- **ZUGRIFF_ANGEBOT_ERSTELLEN** - Zugriff auf die Seite Angebote erstellen
- **ZUGRIFF_ANGEBOT_UEBERSICHT** - Zugriff auf die Seite Übersicht Angebote
- **ANGEBOT** - Ermöglicht das Anzeigen der Druckansicht eines Angebots
- **ANGEBOT_VERWALTEN** - Angebote dürfen angelegt, bearbeitet oder gelöscht werden

## Mein Profil
Jeder Mitarbeiter kann diese Seite rechts oben auf jeder Seite aufrufen.

Im Reiter Allgemein kann der Name, sowie weitere persönliche Daten angepasst werden.
Zudem kann die zugehörige Filiale angepasst werden. Die Daten der Filiale werden in Rechnungen und Reparaturaufträgen dargestellt.

Ist die Einstellung *Druckansicht in neuem Fenster öffnen* aktiv, wird beim Öffnen der Druckansicht von Rechnungen, Reparaturaufträgen und Kassenbüchern
ein neues Browserfenster geöffnet. Ist die Einstellung deaktiviert, öffnet sich die Druckansicht in einem neuen Browsertab.

Ist die Einstellung *Druckdialog beim Öffnen der Druckansicht anzeigen* aktiv, wird beim Öffnen der Druckansicht von Rechnungen, Reparaturaufträgen und Kassenbüchern der Drucken-Dialog des Browsers geöffnet. Ist die Einstellung deaktiviert, wird der Drucken-Dialog nicht geöffnet.

Im Reiter Anmeldedaten kann der Benutzername und das Passwort geändert werden. Dabei muss im Feld *Passwort* zur Bestätigung immer das aktuelle Passwort angegeben werden.

## Anleitung zum Einspielen eines Backups
Vor dem Update auf eine neue Version wird ein Backup des Programms und der Datenbank angefertigt.
Sollte eine Arbeit aufgrund von Fehlern nicht möglich sein, kann das Backup eingespielt werden.

Durch das Einspielen des Backups geht der Bearbeitungsstand im Zeitraum nach dem Backup verloren.

Folgende Schritte müssen der Reihe nach durchgeführt werden:
### Stoppen des Tomcat-Servers
- Folgende EXE öffnen: "C:/CI-Tools Server/Tomcat/bin/Tomcat9w.exe"
- Unter "General" die Schaltfläche "Stop" betätigen und warten bis der Dienst gestoppt wurde (Start-Schaltfläche wird dann aktiv)

### Backup der Datenbank wiederherstellen
- Das Verzeichnis "C:/CI-Tools Server/Datenbank" zippen und gesondert ablegen
- Das Verzeichnis "C:/CI-Tools Server/Datenbank" löschen
- Aus dem Verzeichnis "C:/CI-Tools Server/Backup" die ZIP-Datei "Datenbank.zip" nach "C:/CI-Tools Server/" kopieren und entpacken in den Ordner "Datenbank".
In dem Verzeichnis "Datenbank" müssen, wenn alles richtig durchgeführt wurde, ein Ordner und mehrere Dateien sich befinden.

### Temporäre Daten im Tomcat-Server bereinigen
- Den Inhalt der Ordner "C:/CI-Tools Server/Tomcat/work" und "C:/CI-Tools Server/Tomcat/temp" löschen. Die Ordner selbst müssen bestehen bleiben.

### Vorherige Programmversion wiederherstellen
- Das Verzeichnis "C:/CI-Tools Server/Tomcat/webapps/ROOT" zippen und gesondert ablegen
- Das Verzeichnis "C:/CI-Tools Server/Tomcat/webapps/ROOT" löschen
- Aus dem Verzeichnis "C:/CI-Tools Server/Backup" die ZIP-Datei "ROOT.zip" nach "C:/CI-Tools Server/Tomcat/webapps/" kopieren und entpacken in den Ordner "ROOT"
Im Verzeichnis "ROOT" müssen, wenn alles richtig durchgeführt wurde, drei Unterordner sich befinden.

### Starten des Tomcat-Servers
- Folgende EXE öffnen: "C:/CI-Tools Server/Tomcat/bin/Tomcat9w.exe"
- Unter "General" die Schaltfläche "Start" betätigen und warten bis der Dienst gestartet wurde (Stop-Schaltfläche wird dann aktiv)
