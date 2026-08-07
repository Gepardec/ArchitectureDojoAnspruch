## 1. Voraussetzungen und Einordnung in die Reihenfolge

> **Empfohlene Reihenfolge: dieser Change vor `add-regelwerk-port`** -- entgegen der Nummerierung
> der Iterationen in der Aufgabenstellung. Beide sind technisch unabhängig voneinander, aber
> `add-regelwerk-port` bringt als Nachweis den Testfall "Altersgrenze 27 → Eberhard hat Anspruch".
> Eberhard (geboren 01.04.2002) ist am Stichtag 05.08.2026 vierundzwanzig, also anspruchsberechtigt
> -- läuft dieser Test aber noch gegen `LocalDate.now()`, kippt er im April 2029 lautlos, weil
> Eberhard dann 27 ist. Genau der Fehler, den dieser Change abstellt.
>
> Wer die Reihenfolge der Aufgabenstellung beibehalten will, kann das tun: dann in
> `add-regelwerk-port` den Stichtag in den neuen Tests von Hand festsetzen und hier auf den
> `FixerZeitAdapter` umstellen.

- [x] 1.1 Prüfen, dass `add-onion-anspruch-baseline` archiviert ist
      (`openspec/specs/anspruch-pruefung/spec.md` existiert)
- [x] 1.2 Feststellen, ob `add-regelwerk-port` bereits umgesetzt wurde -- falls ja, dessen Tests in
      Schritt 5 mit umstellen

## 2. Port

- [x] 2.1 `anspruch/domain/port/ZeitPort.java` mit genau einer Methode `LocalDate heute()`
- [x] 2.2 Sicherstellen, dass der Vertrag keine schreibenden Operationen enthält -- kein
      `setToday()`, kein `resetToday()`

## 3. Adapter

- [x] 3.1 `anspruch/infrastructure/SystemZeitAdapter.java` -- der einzige verbleibende Aufruf von
      `LocalDate.now()` im gesamten Modul
- [x] 3.2 `anspruch/infrastructure/FixerZeitAdapter.java` -- Datum per Konstruktor, unveränderlich

## 4. Application umstellen

- [x] 4.1 `LocalDate.now()` aus `PruefeAnspruchService` entfernen, `ZeitPort` als
      Konstruktorparameter aufnehmen
- [x] 4.2 Prüfen, dass der Domänenring unverändert bleibt -- kein Diff unterhalb von
      `anspruch/domain/`, ausgenommen der neue Port aus Schritt 2
- [x] 4.3 Composition Root um den `SystemZeitAdapter` erweitern

## 5. Tests stabilisieren

- [x] 5.1 Integrationstests auf `FixerZeitAdapter(LocalDate.of(2026, 8, 5))` umstellen
- [x] 5.2 Bestehende Fälle bestätigen: Kurt Anspruch, Angie Anspruch, Eberhard kein Anspruch,
      Maria kein Anspruch
- [x] 5.3 Neuer Test mit Stichtag 05.08.2029: Angie hat keinen Anspruch mehr -- damit ist die
      Zeitabhängigkeit selbst geprüft und nicht nur wegkonfiguriert
- [x] 5.4 Sicherstellen, dass kein Test globalen Zustand setzt oder zurücksetzt

## 6. Architekturregel verschärfen

- [x] 6.1 Bestehende Regel "keine Systemuhr im Domänenring" ausweiten auf "keine Systemuhr
      außerhalb `…anspruch.infrastructure.SystemZeitAdapter`"
- [x] 6.2 Gegenprobe: Regel gegen den Stand vor Schritt 4 laufen lassen und prüfen, dass sie den
      Aufruf im Application Service meldet

## 7. Nachweis und Abschluss

- [x] 7.1 Berührte Dateien gegenüber dem Baseline-Stand -- Erwartung erfüllt, der Domänenring
      bleibt bis auf den neuen Port unverändert:
      * NEU `anspruch/domain/port/ZeitPort.java`
      * NEU `anspruch/infrastructure/SystemZeitAdapter.java`, `FixerZeitAdapter.java`
      * GEÄNDERT `anspruch/application/PruefeAnspruchService.java` (Uhr → Port)
      * GEÄNDERT `infrastructure/CompositionRoot.java` (zweiter Konstruktor)
      * GEÄNDERT `ArchitectureRulesTest`, `LeistungIntegrationTest`
      * UNVERÄNDERT: `domain/model`, `domain/rule`, `domain/AnspruchService`, gesamter au-Kontext
- [x] 7.2 In `result.adoc` festhalten, was Iteration 7 in der Onion-Variante gekostet hat,
      gegenübergestellt der Umsetzung in `feature/solution1` (Port mit `setToday`/`resetToday` auf
      veränderlichem Singleton) und der Schätzung für `feature/dojo1`
- [x] 7.3 `doc/UseCase_AnspruchWebCheck.md` um den Iterationsabschnitt 7 ergänzen
- [x] 7.4 `openspec archive add-stichtag-port`
