## ADDED Requirements

### Requirement: Stichtag ist von außen vorgebbar

Das System SHALL den Stichtag der Anspruchsprüfung über einen Port beziehen, dessen
Implementierung austauschbar ist.

Im Regelbetrieb SHALL der Stichtag dem aktuellen Tagesdatum entsprechen. Für Tests MUST ein
festes Datum vorgebbar sein, ohne dass Produktionscode geändert wird.

Ein Aufruf der Systemuhr MUST ausschließlich im dafür vorgesehenen Adapter stattfinden. Kein
anderes Paket SHALL `LocalDate.now()` oder eine gleichwertige Quelle aufrufen.

#### Scenario: Regelbetrieb

- **WHEN** die Anspruchsprüfung ohne besondere Konfiguration ausgeführt wird
- **THEN** verwendet sie das aktuelle Tagesdatum als Stichtag

#### Scenario: Test mit festem Stichtag

- **WHEN** die Anwendung mit einem festen Stichtag 05.08.2026 verdrahtet wird
- **THEN** liefert jede Anspruchsprüfung dasselbe Ergebnis, unabhängig vom tatsächlichen
  Kalenderdatum der Testausführung

#### Scenario: Kein Umschalten zur Laufzeit über den Produktivvertrag

- **WHEN** der Vertrag des Zeit-Ports betrachtet wird
- **THEN** enthält er ausschließlich eine lesende Operation und NOT Methoden zum Setzen oder
  Zurücksetzen des Datums

#### Scenario: Systemuhr nur im Adapter

- **WHEN** eine Klasse außerhalb des Systemuhr-Adapters `LocalDate.now()` aufruft
- **THEN** schlägt der Architekturtest im Build fehl

### Requirement: Zeitstabile Testfälle des Dojos

Die fachlichen Testfälle SHALL mit einem festen Stichtag ausgeführt werden, damit ihr Ergebnis
über die Jahre unverändert bleibt.

#### Scenario: Angie am festen Stichtag

- **WHEN** Angie (geboren 24.07.2010) zum festen Stichtag 05.08.2026 geprüft wird und ihr Vater
  Kurt Eigenanspruch hat
- **THEN** besteht Anspruch -- unabhängig davon, in welchem Jahr der Test läuft

#### Scenario: Angie nach Erreichen der Altersgrenze

- **WHEN** dieselbe Prüfung mit dem Stichtag 05.08.2029 ausgeführt wird
- **THEN** besteht kein Anspruch mehr, da Angie die Altersgrenze überschritten hat

#### Scenario: Eberhard am festen Stichtag

- **WHEN** Eberhard (geboren 01.04.2002) zum festen Stichtag 05.08.2026 geprüft wird
- **THEN** besteht kein Anspruch
