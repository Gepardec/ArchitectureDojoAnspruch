## Why

Die Testfälle des Dojos sind an konkrete Kalenderdaten gebunden: Angie (geboren 24.07.2010) hat
2026 Anspruch, 2029 nicht mehr. Solange die Anwendung den Stichtag aus der Systemuhr nimmt,
laufen diese Tests in einigen Jahren still falsch -- Angie wird volljährig, ohne dass sich der
Code geändert hat.

Iteration 7 der Aufgabenstellung verlangt deshalb, dass das Tagesdatum für Tests vorgegeben
werden kann; als Testwert ist der 05.08.2026 genannt.

Wie `add-regelwerk-port` ist dieser Change bewusst getrennt, damit der Diff zeigt, was diese
Architekturvorgabe in der Onion-Variante strukturell kostet.

## What Changes

- Neuer Port `ZeitPort` mit einer Methode `heute()`.
- `PruefeAnspruchService` bezieht den Stichtag über diesen Port statt über `LocalDate.now()`.
- Zwei Adapter: einer gegen die Systemuhr für den Produktivbetrieb, einer mit festem Datum für
  Tests.
- Die bestehenden Testfälle werden auf den festen Stichtag 05.08.2026 umgestellt und damit
  dauerhaft stabil.
- Die Architekturregel "keine Systemuhr im Domänenring" wird auf die Application-Schicht
  ausgeweitet: `LocalDate.now()` darf ab diesem Change nur noch im Systemuhr-Adapter stehen.

Ausdrücklich **nicht** enthalten:

- Kein umschaltbares Systemdatum zur Laufzeit über Setter im Produktivport. Diese Lösung wählt
  `feature/solution1` (`SystemDaten.setToday()`/`resetToday()`); sie stellt einen Testbelang in
  den Produktionsvertrag und erlaubt jedem Produktionscode, das Systemdatum zu verstellen.
- Keine Zeitzonen- oder Uhrzeitbehandlung; die Fachlichkeit arbeitet auf Tagesebene.

## Capabilities

### New Capabilities

<!-- keine -->

### Modified Capabilities

- `anspruch-pruefung`: Der Stichtag der Anspruchsprüfung ist von außen vorgebbar; die Anwendung
  ermittelt ihn über einen Port statt direkt über die Systemuhr.

## Impact

- **Neu:** `…leistung/anspruch/domain/port/ZeitPort.java`
- **Neu:** `…leistung/anspruch/infrastructure/SystemZeitAdapter.java`
- **Neu:** `…leistung/anspruch/infrastructure/FixerZeitAdapter.java` (Testunterstützung)
- **Geändert:** `…leistung/anspruch/application/PruefeAnspruchService.java` -- der einzige
  verbleibende `LocalDate.now()`-Aufruf entfällt
- **Geändert:** Composition Root -- eine zusätzliche Verdrahtung
- **Geändert:** Integrationstests -- fester Stichtag 05.08.2026 statt Systemuhr
- **Geändert:** `ArchitectureRulesTest` -- die Systemuhr-Regel gilt nun für alle Pakete außer dem
  Systemuhr-Adapter
- **Nicht betroffen:** Der Domänenring. Er bekommt den Stichtag seit der Baseline als Parameter
  und ändert sich durch diese Iteration nicht -- das ist das erwartete Ergebnis und der
  Gegenpunkt zu `feature/dojo1`, wo `Versicherter.isUnter18()` die Uhr selbst ruft.
