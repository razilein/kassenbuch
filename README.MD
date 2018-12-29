

# Tools für die Computerinsel

Dieses Programm beinhaltet verschiedene Tools, welche für die Computerinsel benötigt werden.

Dazu zählen:
- Reparaturaufträge verwalten
- Rechnungen verwalten
- Kunden verwalten
- Kassenbücher verwalten
- Kassenstand berechnen
- Statistiken erstellen
- Inventar verwalten

Um allgemein auf die Tools zugreifen zu können wird ein gültiger Login benötigt.
Die verschiedenen Seiten und Funktionen sind durch Berechtigungen, die den Mitarbeitern zugeteilt werden müssen, abgesichert.

## Reparaturen

Die Seite Reparaturen unterteilt sich in die zwei Unterseiten **Erstellen** und **Reparaturen**.

### Erstellen
Um diese Seite öffnen zu können wird das Recht *ZUGRIFF_REPARATUR_ERSTELLEN* benötigt.
Um den Reparaturauftrag nach der Erstellung ansehen und drucken zu können, wird das Recht *REPARATUR* benötigt.

Auf dieser Seite befinden sich verschiedene Felder die beim Erstellen eines Reparaturauftrages zu befüllen sind.
Nach dem Speichern öffnet sich eine neue Seite für den Druck des Auftrages.
Ebenfalls wird ein Informationsblatt zur DSGVO geöffnet, wenn der Kunde diese noch nicht erhalten hat.

#### Abholdatum/-zeit
Das Abholdatum und die Abholzeit werden dabei automatisch gefüllt.
Das Standardabholdatum wird dabei auf das Datum in **3** Tagen und die Abholzeit auf **16:30 Uhr** gesetzt.

Wird die Expressbearbeitung aktiviert und es ist vor **13:30 Uhr** wird das Abholdatum auf den aktuellen Tag um **18:30 Uhr** gesetzt. Ist es nach 13:30 Uhr, wird das Abholdatum auf den nächsten Tag **13:30 Uhr** gesetzt.

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

Durch Klick auf das *Löschen-Icon* wird nach erneuter Bestätigung der Reparaturauftrag komplett gelöscht.

## Rechnungen
Die Seite Rechnungen unterteilt sich in die zwei Unterseiten **Erstellen** und **Rechnungen**.

### Erstellen
Um diese Seite öffnen zu können wird das Recht *ZUGRIFF_RECHNUNG_ERSTELLEN* benötigt.
Um die Rechnung nach der Erstellung ansehen und drucken zu können, wird das Recht *RECHNUNG* benötigt.

Auf dieser Seite befinden sich verschiedene Felder die beim Erstellen einer Rechnung zu befüllen sind.
Nach dem Speichern öffnet sich eine neue Seite für den Druck der Rechnung.
Ebenfalls wird ein Informationsblatt zur DSGVO geöffnet, wenn der Kunde diese noch nicht erhalten hat.

In der oberen Tabelle werden alle vorhandenen Produkte aufgezeigt. Mit Hilfe der Suchfilter können diese weiter eingeschränkt werden. Durch Drücken der Enter-Taste wird eine Suche ausgelöst.

Wenn ein Produkt gewählt werden soll, kann das *Doppel-Häkchen-Icon* geklickt werden für eine Direktübernahme in die gewählten Posten. Alternativ kann das *OK-Icon* geklickt werden, um die Menge, den Rabatt, die Bezeichnung, die Seriennummer und den Hinweis vor der Übernahme in die gewählten Posten anzupassen.

In der darunterliegenden Tabelle befinden sich die gewählten Posten, welche auf der Rechnung erscheinen.
Mit dem *Plus-Icon* kann die Menge des Produktes um 1 erhöht werden.
Mit dem *Minus-Icon* kann die Menge des Produktes um 1 verringert werden. Wird das *Minus-Icon* geklickt, während sich der Posten nur noch ein mal auf der Liste befindet, wird dieser wieder entfernt.

Durch Klick auf das *Bearbeiten-Icon* besteht nochmals die Möglichkeit die Menge, den Rabatt, die Bezeichnung, die Seriennummer und den Hinweis zu bearbeiten.

Durch Klick auf das *Euro-Icon* öffnet sich der Rabattrechner,  mit dessen Hilfe ein Rabatt festgelegt werden kann. Dabei wird der Einkaufspreis des Produktes berücksichtigt, insofern dieser hinterlegt ist und der Gewinn angezeigt.

Zuletzt muss noch die Zahlart gewählt werden. Dabei ist zu beachten, dass bei der Zahlart *ÜBERWEISUNG* der *Kunde* ausgewählt werden muss.

Mit des Kontrollkästchens kann gewählt werden, ob der Kundenname auf die Rechnung gedruckt werden soll. Ist kein Kunde ausgewählt, ist dieses Feld obsolet.

Nach dem Speichern wird im Inventar der Bestand des Produktes um die entsprechende Menge reduziert.

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

Durch Klick auf das *Öffnen-Icon* kann die Rechnung in der Druckansicht angezeigt werden. Dazu wird das Recht *RECHNUNG* benötigt.

Durch Klick auf das *Bearbeiten-Icon* kann die Rechnung bearbeitet werden.
Dabei ist es nur möglich das Rechnungsdatum, die Zahlart, den Kunden und Reparaturauftrag zu ändern.
Zudem darf die Menge, Bezeichnung, der Hinweis, die Seriennummer und der Rabatt von bestehenden Posten geändert werden. Es dürfen jedoch keine neuen Posten hinzugefügt oder bestehende entfernt werden.
Das Bearbeiten der Menge nimmt keinen Einfluss auf den Bestand im Inventar. Dies wird lediglich beim Erstellen beeinflusst.

Durch Klick auf das *Löschen-Icon* kann eine Rechnung nach nochmaliger Bestätigung gelöscht werden.

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

Durch Klick auf das *Löschen-Icon* kann nach erneuter Bestätigung der Kunde gelöscht werden. Dabei werden keine Reparaturaufträge oder Rechnungen gelöscht. Jedoch wird die Verknüpfung zum Kunden entfernt, sodass diese Daten beim Ansehen der Aufträge/Rechnungen nicht mehr ersichtlich sind.

## Inventar
Die Seite Inventar unterteilt sich in die drei Unterseiten **Produkte**, **Gruppen** und **Kategorien**.

### Produkte
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_INVENTAR_PRODUKT* benötigt.
Um Produkte Anlegen, Bearbeiten oder Löschen zu können, wird das Recht *INVENTAR_PRODUKT_VERWALTEN* benötigt.

Auf dieser Seite werden alle Produkte angezeigt. Mit Hilfe der Suchfilter kann die Anzeige weiter eingeschränkt werden.

Durch Klick auf das *Plus-Icon* kann ein neues Produkt angelegt werden.

Durch Klick auf das *Bearbeiten-Icon* können die Produktdaten bearbeitet werden. Dabei ist es auch möglich, das Produkt einer anderen Produktgruppe oder -kategorie zuzuordnen.

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

### Kassenbücher
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_KASSENBUCH_UEBERSICHT* benötigt.
Um die Kassenbücher öffnen zu können, wird das Recht *KASSENBUCH* benötigt.

Auf dieser Seite befindet sich eine Übersicht aller erstellten Kassenbücher. Mit Hilfe der Suchfilter kann die Anzeige weiter eingeschränkt werden.

Durch Klick auf das *Öffnen-Icon* kann die Druckansicht des Kassenbuchs geöffnet werden.

Das Bearbeiten oder Löschen ist nicht möglich, um eine Manipulation zu verhindern.

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

## Einstellungen
Die Seite Kassenbuch unterteilt sich in die drei Unterseiten **Allgemein**, **Filiale** und **Mitarbeiter**.

### Allgemein
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_EINSTELLUNGEN_ALLGEMEIN* benötigt.

#### Ablageverzeichnis
Das hier definierte Verzeichnis wird verwendet, um die Statistiken und Kassenstände abzulegen. Zudem muss das DSGVO-Informationsblatt ausgehend dieses Verzeichnisses eine Ebene höher mit dem Dateinamen **Einwilligung_DSGVO.pdf** abgelegt werden.

#### Rechnungsnummer
Die Rechnungsnummer setzt sich zusammen aus dem Kürzel der Filiale, dem Jahr des Rechnungsdatums und einer fortlaufenden Nummer, welche automatisch immer auf 4 Stellen angepasst wird.

Ist beispielsweise das Rechnungsdatum der 31.12.2018 und die fortlaufende Nummer 455, dann lautet die Rechnungsnummer, wenn das Kürzel der Filiale L ist: *L20180455*

In diesem Feld muss lediglich die fortlaufende Nummer angegeben werden. Es empfiehlt sich beim Jahreswechsel die Nummer manuell zurückzusetzen auf 0.

#### Auftragsnummer
Die Auftragsnummer setzt sich zusammen aus dem Kürzel der Filiale, dem aktuellen Jahr und einer fortlaufenden Nummer, welche automatisch immer auf 4 Stellen angepasst wird.

Ist beispielsweise heute der 31.12.2018 und die fortlaufende Nummer 455, dann lautet die Auftragsnummer, wenn das Kürzel der Filiale L ist: *L20180455*

In diesem Feld muss lediglich die fortlaufende Nummer angegeben werden. Es empfiehlt sich beim Jahreswechsel die Nummer manuell zurückzusetzen auf 0.

#### Filiale
In diesem Feld ist die Filiale zu wählen, dabei handelt es sich um eine globale Einstellung, welche für alle Mitarbeiter gilt.
Diese Einstellung wirkt sich darauf aus, welche Adressdaten auf den Rechnungen und Reparaturaufträgen angezeigt werden.
Ist in der Auswahlliste keine Filiale verfügbar, muss zuerst unter *Einstellungen* > *Filialen* eine Filiale definiert werden.

## Filialen
Um diese Seite öffnen zu können, wird das Recht *ZUGRIFF_EINSTELLUNGEN_FILIALEN* benötigt.
Um Filialen Anlegen oder Bearbeiten zu können, wird das Recht *FILIALEN_VERWALTEN* benötigt.

Auf dieser Seite befindet sich eine Auflistung aller Filialen.

Durch Klick auf das *Plus-Icon* kann eine neue Filiale angelegt werden.
Durch Klick auf das *Bearbeiten-Icon* kann die Filiale bearbeitet werden.

Die Adressdaten, sowie Telefon, E-Mail und das Kürzel erscheinen auf den Rechnungen und Reparaturaufträgen. Eine Änderung dieser Daten wirkt sich auf bestehende Rechnungen und Aufträge aus.

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
- **KUNDEN_RECHNUNG** - Ermöglicht das Anzeigen aller Rechnungen eines Kunden
- **ZUGRIFF_RECHNUNG_ERSTELLEN** - Zugriff auf die Seite Rechnung erstellen
- **ZUGRIFF_RECHNUNG_UEBERSICHT** - Zugriff auf die Seite Übersicht Rechnungen
- **RECHNUNG** - Ermöglicht das Anzeigen der Druckansicht einer Rechnung
- **RECHNUNG_VERWALTEN** - Rechnungen dürfen angelegt, bearbeitet oder gelöscht werden

 ## Mein Profil
 Jeder Mitarbeiter kann diese Seite rechts oben auf jeder Seite aufrufen.
 
 Im Reiter Allgemein kann der Name, sowie weitere persönliche Daten angepasst werden.
 Im Reiter Anmeldedaten kann der Benutzername und das Passwort geändert werden. Dabei muss im Feld *Passwort* zur Bestätigung immer das aktuelle Passwort angegeben werden.
 