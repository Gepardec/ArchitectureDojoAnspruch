# au-meldung Specification

## Purpose
TBD - created by archiving change add-onion-anspruch-baseline. Update Purpose after archive.
## Requirements
### Requirement: Entgegennahme einer elektronischen Krankmeldung

Das System SHALL eine elektronische Arbeitsunfähigkeitsmeldung entgegennehmen, die aus dem
AU-Beginn und der Versicherungsnummer des Patienten besteht (UC-AU-01).

Beide Angaben MUST vorhanden sein; eine Meldung ohne AU-Beginn oder ohne Versicherungsnummer
MUST zurückgewiesen werden.

#### Scenario: Vollständige Meldung

- **WHEN** eine Meldung mit AU-Beginn und gültiger Versicherungsnummer eintrifft
- **THEN** wird sie zur Gültigkeitsprüfung angenommen

#### Scenario: Unvollständige Meldung

- **WHEN** eine Meldung ohne AU-Beginn oder ohne Versicherungsnummer eintrifft
- **THEN** wird sie mit einem Fehler zurückgewiesen und NOT gespeichert

### Requirement: AU-Meldung nur bei bestehendem Anspruch speichern

Eine AU-Meldung SHALL nur dann gültig sein und gespeichert werden, wenn für die gemeldete
Versicherungsnummer ein Leistungsanspruch besteht. Besteht kein Anspruch, MUST die Meldung
verworfen werden.

#### Scenario: Anspruch vorhanden

- **WHEN** Kurt am 06.08.2026 krankgemeldet wird und Anspruch hat
- **THEN** wird die AU-Meldung gespeichert

#### Scenario: Kein Anspruch

- **WHEN** Eberhard krankgemeldet wird und keinen Anspruch hat
- **THEN** wird die AU-Meldung NOT gespeichert

### Requirement: Ergebnis der Meldungsverarbeitung ist auswertbar

Die Verarbeitung einer AU-Meldung SHALL dem Aufrufer ein unterscheidbares Ergebnis liefern:
gespeichert oder abgelehnt, im Ablehnungsfall mit Begründung.

Ein reiner Protokolleintrag SHALL NOT als Ergebnis genügen, da Nebenszenario 1 des Use Case eine
Weiterverarbeitung ungültiger Meldungen in einer Fehler-Queue vorsieht.

#### Scenario: Ablehnung ist erkennbar

- **WHEN** eine AU-Meldung mangels Anspruch abgelehnt wird
- **THEN** erhält der Aufrufer ein Ergebnis, das die Ablehnung samt Grund erkennen lässt

#### Scenario: Speicherung ist erkennbar

- **WHEN** eine AU-Meldung gespeichert wurde
- **THEN** erhält der Aufrufer ein Ergebnis, das die erfolgreiche Speicherung erkennen lässt

### Requirement: Persistenz hinter einem Port

Die Speicherung einer AU-Meldung SHALL über einen Port erfolgen, sodass die konkrete
Speichertechnologie austauschbar ist und die Fachlogik ohne Datenbank testbar bleibt.

#### Scenario: Fachlogik ohne Datenbank testbar

- **WHEN** die Verarbeitung einer AU-Meldung getestet wird
- **THEN** genügt eine Test-Implementierung des Persistenz-Ports; eine Datenbank ist NOT nötig

### Requirement: Anspruchsprüfung über eigenen Port des AU-Kontexts

Der AU-Kontext SHALL die Anspruchsprüfung über einen von ihm selbst definierten Port aufrufen.
Klassen des AU-Kontexts MUST NOT direkt auf Klassen des Anspruchskontexts zugreifen; die
Übersetzung MUST in einem Adapter des AU-Kontexts stattfinden.

#### Scenario: Kontextgrenze eingehalten

- **WHEN** der AU-Kontext den Anspruch einer Person prüft
- **THEN** erfolgt der Aufruf über einen Port des AU-Kontexts, den ein Adapter auf den
  Anspruchskontext abbildet

#### Scenario: Direkter Zugriff verhindert

- **WHEN** eine Klasse in `…leistung.au.domain` oder `…leistung.au.application` eine Klasse aus
  `…leistung.anspruch` importiert
- **THEN** schlägt der Architekturtest im Build fehl

