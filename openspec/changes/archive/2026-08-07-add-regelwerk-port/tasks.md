## 1. Voraussetzungen und Einordnung in die Reihenfolge


> **Empfohlene Reihenfolge: `add-stichtag-port` zuerst, dieser Change danach** -- entgegen der
> Nummerierung der Iterationen in der Aufgabenstellung. Grund ist Task 5.2 dieses Changes: Der
> Nachweis "Altersgrenze 27 → Eberhard hat Anspruch" hängt an Eberhards Alter am Stichtag. Läuft
> der Test gegen `LocalDate.now()`, ist Eberhard (geboren 01.04.2002) heute 24 und der Test grün,
> ab April 2029 aber 27 -- und der Test kippt lautlos.
>
> Wird die Reihenfolge der Aufgabenstellung beibehalten, ist Task 5.5 verpflichtend.

- [x] 1.1 Prüfen, dass `add-onion-anspruch-baseline` archiviert ist
      (`openspec/specs/anspruch-pruefung/spec.md` existiert) -- der `MODIFIED`-Block dieses
      Changes überschreibt eine dort definierte Requirement
- [x] 1.2 Feststellen, ob `add-stichtag-port` bereits umgesetzt wurde -- davon hängt ab, ob Task
      5.5 nötig ist

## 2. Port im Domänenring

- [x] 2.1 `anspruch/domain/port/RegelwerkPort.java` mit fachlich benannter Methode
      `int altersgrenzeMitversicherung()` -- kein generisches `String get(String key)`
- [x] 2.2 Fehlerfall festlegen: eigene `RegelwerkParameterFehltException` im Domänenring, die den
      fehlenden Parameter benennt

## 3. Regel umstellen

- [x] 3.1 Konstante `18` aus `KindAnspruch` entfernen
- [x] 3.2 `RegelwerkPort` als Konstruktorparameter ergänzen und die Grenze beim Auswerten lesen
- [x] 3.3 Prüfen, dass die Vergleichslogik weiterhin "unter der Grenze" bedeutet -- der Randfall
      "genau an der Grenze" darf keinen Anspruch ergeben
- [x] 3.4 Prüfen, dass die ArchUnit-Regeln unverändert grün sind: Der neue Port liegt im
      Domänenring und braucht keine Sonderbehandlung

## 4. Adapter

- [x] 4.1 `anspruch/infrastructure/RegelwerkAdapter.java` gegen eine Ablage, die eine Datenbank
      stellvertritt (In-Memory oder Properties)
- [x] 4.2 Vorgabewert 18 in der Ablage hinterlegen, NICHT im Adaptercode als Fallback
- [x] 4.3 Adapter-Test: vorhandener Parameter, fehlender Parameter

## 5. Tests

- [x] 5.1 Bestehende `KindAnspruch`-Tests auf ein Test-Double des `RegelwerkPort` umstellen
- [x] 5.2 Neuer Test: Altersgrenze 27 → Eberhard (24 am Stichtag 05.08.2026) hat Anspruch
- [x] 5.3 Neuer Test: fehlender Parameter führt zum Fehler statt zu einem stillen Vorgabewert
- [x] 5.4 Integrationstest der Baseline muss unverändert grün bleiben
- [x] 5.5 Entfällt -- `add-stichtag-port` wurde vorgezogen, der Stichtag ist bereits fest. Ursprünglich: **Nur falls `add-stichtag-port` noch nicht umgesetzt ist:** In Test 5.2 den Stichtag
      explizit auf 05.08.2026 setzen statt `LocalDate.now()` zu verwenden, und den Test in
      `add-stichtag-port` auf den `FixerZeitAdapter` umstellen

## 6. Verdrahtung

- [x] 6.1 Composition Root um den neuen Adapter erweitern
- [x] 6.2 Sicherstellen, dass `application` und der `au`-Kontext unverändert bleiben

## 7. Nachweis und Abschluss

- [x] 7.1 Berührte Dateien gegenüber dem Vorstand -- Erwartung erfüllt:
      * NEU `anspruch/domain/port/RegelwerkPort.java`,
        `anspruch/domain/RegelwerkParameterFehltException.java`
      * NEU `anspruch/infrastructure/RegelwerkAdapter.java`
      * GEÄNDERT `anspruch/domain/rule/KindAnspruch.java` (Konstante → Port)
      * GEÄNDERT `infrastructure/CompositionRoot.java` (eine Zeile Verdrahtung)
      * GEÄNDERT `KindAnspruchTest`
      * **UNVERÄNDERT: die gesamte `application`-Schicht und der `au`-Kontext.**
        Das ist das eigentliche Ergebnis: Unter Clean Architecture hätte der Interactor den
        Regelwert beschaffen und dafür den Datenbedarf dieser Regel kennen müssen.
- [x] 7.2 Ergebnis in `result.adoc` eintragen: was Iteration 6 in der Onion-Variante gekostet hat,
      gegenübergestellt der Umsetzung in `feature/solution1` (Port vorhanden, Wert hartcodiert)
      und der Schätzung für `feature/dojo1` (Konstante mitten in der Domäne)
- [x] 7.3 `doc/UseCase_AnspruchWebCheck.md` um den Iterationsabschnitt 6 ergänzen
- [x] 7.4 `openspec archive add-regelwerk-port`
