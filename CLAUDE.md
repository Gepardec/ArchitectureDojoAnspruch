# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Was dieses Repo ist

Ein **Architektur-Dojo** (siehe `doc/README.md`), kein Produktivsystem. Ziel ist das Erproben von
Paketstrukturen und Abhängigkeiten anhand vereinfachter UseCases der österreichischen
Sozialversicherung. Details wie ExceptionHandling, Persistenz oder Edge-Cases sind bewusst
nachrangig -- die Paketstruktur und die Richtung der Abhängigkeiten sind das eigentliche Artefakt.

Dokumentation und Code sind auf Deutsch (Fachbegriffe, Klassennamen, Kommentare). Das beibehalten.

### Drei Lösungsversionen

Dasselbe Dojo ist dreimal gelöst. Der Vergleich steht in `result.adoc` (AsciiDoc, Diagramme
brauchen Kroki bzw. asciidoctor-diagram).

| Branch | Autor | Variante | Iterationen |
|---|---|---|---|
| `feature/dojo1` | Implementierer-Gruppe | Clean Architecture (Eschhold) | 1--5 |
| `feature/solution1` | Sensei (Referenzlösung) | Hexagonal (Cockburn) | 1--7 |
| `feature/onion` | -- | **Onion (Palermo)** | 1--7 |

**Dieser Branch ist `feature/onion`.** Die Variantenwahl ist in `result.adoc` im Kapitel
"Empfehlung: Welche Variante passt zu dieser Fachlichkeit?" begründet. Wer hier arbeitet, arbeitet
an der Onion-Lösung -- die Beschreibungen unten gelten nicht für die beiden anderen Branches.

## Module

- `dojo-external-domains` -- Stubs für externe Systeme (`ZeitenService`/MVB, `PersonenService`/ZPV,
  `AngehoerigeService`, `SvnrValidator`, `TestData`). **Hier wird nichts geändert.** Die Services
  liefern hartcodierte Daten für die Familie Leonhardsberger aus `doc/testcases.md`.
- `dojo-leistung` -- die Lösung.

## Build & Test

```bash
mvn clean install                                    # Vollbuild beider Module
mvn test                                             # alle Tests (8 + 68)
mvn -pl dojo-leistung test                           # nur das Lösungsmodul
mvn -pl dojo-leistung test -Dtest=KindAnspruchTest   # eine Testklasse
mvn -pl dojo-leistung test -Dtest=ArchitectureRulesTest   # nur die Architekturregeln
```

Java 17, JUnit 5, AssertJ, Mockito, ArchUnit. Der Integrationstest heißt `LeistungIntegrationTest`
und läuft bei `mvn test` **mit** -- keine `SIT`-Endung, kein Failsafe nötig. (Auf `feature/dojo1`
war das anders und der einzige aussagekräftige Integrationstest lief deshalb nie.)

`-pl dojo-leistung` setzt voraus, dass `dojo-external-domains` installiert ist; sonst vorher
`mvn install -DskipTests`.

## Architektur: Onion (Palermo)

```
at.gepardec.dojo.leistung
├── shared/domain          Svnr  -- Shared Kernel, von beiden Kontexten genutzt
├── anspruch
│   ├── domain             AnspruchService, RegelwerkParameterFehltException
│   │   ├── model          Versicherungszeit
│   │   ├── port           ZeitenPort, PersonenPort, AngehoerigePort,
│   │   │                  RegelwerkPort, ZeitPort      <-- Ports IM Domänenring
│   │   └── rule           Anspruch, EigenAnspruch, KindAnspruch
│   ├── application        PruefeAnspruchUseCase (Input Port), PruefeAnspruchService
│   └── infrastructure     ZeitenAdapter, PersonenAdapter, AngehoerigeAdapter,
│                          RegelwerkAdapter, SystemZeitAdapter, FixerZeitAdapter,
│                          AnspruchWebCheck
├── au
│   ├── domain             AuMeldungService
│   │   ├── model          AuMeldung, MeldungsErgebnis
│   │   └── port           AuMeldungPort, AnspruchPruefungPort
│   ├── application        ErstelleAuMeldungUseCase, ErstelleAuMeldungService
│   └── infrastructure     AuMeldungAdapter, AnspruchPruefungAdapter
└── infrastructure         CompositionRoot
```

### Die Regeln, die nicht gebrochen werden dürfen

**`ArchitectureRulesTest` erzwingt sie im Build.** Wer eine dieser Regeln verletzt, bekommt einen
roten Test, keinen Kommentar im Review. Vor größeren Umbauten die Regeln lesen -- sie sind die
ausführbare Fassung der Architekturentscheidung.

1. **Ports liegen im Domänenring** (`*.domain.port`), nicht darüber. Das ist das Definitionsmerkmal
   von Onion und der Punkt, an dem sich diese Lösung von beiden anderen unterscheidet.
2. **Domänenklassen dürfen ihre Ports direkt nutzen.** `EigenAnspruch`, `KindAnspruch` und
   `AuMeldungService` tun das. Das ist kein Versehen, sondern der fachliche Grund für die
   Variantenwahl: Eine Regel weiß selbst, welche Daten sie braucht.
3. **`*.domain` hängt an nichts außerhalb** -- außer `shared.domain` und `java..`.
4. **Keine Systemuhr außerhalb von `SystemZeitAdapter`.** Kein `LocalDate.now()` sonstwo. Stichtage
   werden durchgereicht.
5. **`au` kennt `anspruch` nur aus `au.infrastructure`** -- über `AnspruchPruefungPort` und
   `AnspruchPruefungAdapter` (Anti-Corruption Layer). `au.domain` und `au.application` dürfen
   `anspruch.*` nicht importieren.
6. **`SvnrValidator` ist die einzige zugelassene Ausnahme** aus `dojo-external-domains` im
   Domänenring, genutzt nur von `shared.domain.Svnr`. Eine benannte Ausnahme, keine Lücke.

### Weitere Konventionen

- **Constructor Injection überall.** Keine statische Factory, kein Service Locator, kein globaler
  Zustand. `CompositionRoot` ist die einzige Klasse, die alle Ringe kennt -- deshalb liegt sie im
  äußersten. (Auf `feature/solution1` liegt die Factory im Port-Package und importiert die Adapter:
  ein Zyklus vom inneren zum äußeren Ring, der schwerste Befund des gesamten Vergleichs.)
- **Neue Anspruchsart = neue `Anspruch`-Implementierung** plus eine Zeile in der `CompositionRoot`.
  Bestehende Klassen bleiben unberührt. Wenn eine Änderung das nicht einhält, stimmt der Schnitt
  nicht.
- **Ports werden aus dem Bedarf der Regel abgeleitet, nicht vorab entworfen.** Erst die Regel
  schreiben, das gewünschte Interface dabei erfinden, dann herausziehen. Sonst entstehen technische
  Signaturen (`getVersicherungsZeiten(String)`) statt fachlicher (`versicherungszeiten(Svnr)`).
- **Keine Umsystem-Typen in der Domäne.** Adapter mappen (`VersicherungsZeit` → `Versicherungszeit`,
  `Person` → `Optional<LocalDate>`).
- **Geschäftsregeln gehören in `domain`, nicht in `application`.** Die Application-Services sind
  dünne Orchestrierer ohne fachliches `if`.

## Fachliche Regeln (Iterationen 1--7)

Anspruch besteht, sobald **eine** Regel greift:

- **Eigenanspruch:** aktive Versicherungszeit zum Stichtag (`bis == null` heißt aktiv).
- **Kindanspruch:** unterhalb der Altersgrenze **und** mindestens ein Elternteil mit
  *Eigenanspruch* (nicht: mit Anspruch). Die Altersgrenze kommt aus dem `RegelwerkPort`, fachlicher
  Vorgabewert 18. "Unter" heißt: am Tag des Erreichens endet der Anspruch.

AU-Meldung: wird nur gespeichert, wenn Anspruch besteht. Das Ergebnis ist ein `MeldungsErgebnis`
(`Gespeichert` / `Abgelehnt` mit Grund), kein `void` -- damit Nebenszenario 1 (Error-Queue)
anschließbar wäre. Die Queue selbst ist nicht umgesetzt.

Erwartete Testfälle zum Stichtag 05.08.2026: Kurt hat Anspruch (aktive Beschäftigung), Angie hat
Anspruch (16, Kind von Kurt), Eberhard hat keinen (24), Maria hat keinen (Ehepartnerin ohne eigene
Zeit -- Ehepartner-Mitversicherung ist keine Regel dieser Iterationen).

**Achtung bei den Testdaten:** Die SVNR-Konstanten in `at.gepardec.dojo.test.TestData` weichen von
den in `doc/testcases.md` genannten Nummern ab (`SVNR_EBERHARD`/`SVNR_ANGIE` sind gegenüber der
Doku vertauscht). Die Stub-Services sind in sich konsistent mit `TestData` -- immer die Konstanten
verwenden, nie die Nummern aus der Doku abtippen.

## Arbeitsweise

### Dojo-Ablauf

Der Sensei stellt pro Iteration neue Geschäftsregeln vor. Diese werden **zuerst in die Doku-Dateien
eingefügt** (`doc/UseCase_*.md`, `doc/GeschäftsRegeln_*.md`), dann implementiert, und am Ende der
Iteration wird alles gemeinsam committed -- Doku + Code in einem Commit. Der Zweck ist
Nachvollziehbarkeit: welche Pakete mussten für welche Anforderungsänderung angefasst werden.
Niemals Code ohne die zugehörige Doku-Änderung committen.

### OpenSpec

Dieser Branch nutzt OpenSpec (`openspec/`, CLI installiert). Die drei umgesetzten Changes liegen
unter `openspec/changes/archive/`, die abgeleiteten Capability-Specs unter `openspec/specs/`
(`anspruch-pruefung`, `au-meldung`).

```bash
openspec list                 # aktive Changes
openspec list --specs         # Capabilities
openspec validate --all --strict
openspec new change "<name>"  # Artefakte: proposal → specs → design → tasks
openspec archive <name>       # erst dadurch wandern Deltas in openspec/specs/
```

Für eine neue Iteration: eigener Change, nicht in einen bestehenden hineinschreiben. Ein Change pro
Anforderungsänderung ist genau das, was den Dojo-Zweck (welche Pakete ändern sich?) messbar macht.
Bei `MODIFIED Requirements` muss die Überschrift **exakt** der bestehenden entsprechen, sonst geht
beim Archivieren Inhalt verloren.

### Tests

Jede Testmethode prüft etwas -- keine leeren Rümpfe als Platzhalter. Randfälle, die in den anderen
Lösungen falsch sind und hier abgesichert bleiben müssen: genau an der Altersgrenze (kein
Anspruch), unbekannte SVNR (kein Anspruch statt NullPointerException), Stichtag in der Zukunft.

ArchUnit-Hinweis: `src/test/resources/archunit.properties` setzt `archRule.failOnEmptyShould=false`,
damit Regeln auf noch leere Pakete zutreffen dürfen.
