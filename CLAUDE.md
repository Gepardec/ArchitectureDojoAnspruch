# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Was dieses Repo ist

Ein **Architektur-Dojo** (siehe `doc/README.md`), kein Produktivsystem. Ziel ist das Erproben von
Paketstrukturen und Abhängigkeiten (hexagonale Architektur / Ports & Adapters) anhand vereinfachter
UseCases der österreichischen Sozialversicherung. Details wie ExceptionHandling, Persistenz oder
Edge-Cases sind bewusst nachrangig — die Paketstruktur und die Richtung der Abhängigkeiten sind das
eigentliche Artefakt.

Dokumentation und Code sind auf Deutsch (Fachbegriffe, Klassennamen, Kommentare). Das beibehalten.

## Ablauf (wichtig für jede Änderung)

Der Sensei stellt pro Iteration neue Geschäftsregeln vor. Diese werden **zuerst in die Doku-Dateien
eingefügt** (`doc/UseCase_*.md`, `doc/GeschäftsRegeln_*.md`), dann implementiert, und am Ende der
Iteration wird alles gemeinsam committed — Doku + Code in einem Commit, Commit-Message im Stil
`iteration N: <kurze Beschreibung>`. Der Zweck ist Nachvollziehbarkeit: welche Pakete/Klassen mussten
für welche Anforderungsänderung angefasst werden. Also niemals Code ohne die zugehörige Doku-Änderung
committen.

## Module

- `dojo-external-domains` — Stubs für externe Systeme (ZeitenService/MVB, PersonenService/ZPV,
  AngehoerigeService, SvnrValidator, TestData). **Hier wird nichts geändert.** Die Services liefern
  hartcodierte Daten für die Familie Leonhardsberger aus `doc/testcases.md`.
- `dojo-leistung` — hier wird die Lösung implementiert.

## Build & Test

```bash
mvn clean install          # Vollbuild beider Module
mvn test                   # alle Unit-Tests
mvn -pl dojo-leistung test # nur das Lösungsmodul
mvn -pl dojo-leistung test -Dtest=VersicherterTest          # eine Testklasse
mvn -pl dojo-leistung test -Dtest=VersicherterTest#testIsVersichert_containedDate
mvn -pl dojo-leistung test -Dtest=PruefeLeistungsanspruchUseCaseSIT
```

Java 17, JUnit 5 + AssertJ. Kein Failsafe-Plugin konfiguriert: Klassen mit Endung `SIT`
(derzeit nur `PruefeLeistungsanspruchUseCaseSIT`) werden von Surefire **nicht** automatisch
ausgeführt und müssen explizit angestoßen werden — mit `-pl dojo-leistung`, sonst scheitert der
Reaktor an "No tests were executed" in `dojo-external-domains`. Tests mit Endung `Test` unter
`src/test/java/it/...` laufen dagegen im normalen `mvn test` mit.

Konsequenz: die Iteration-5-Fälle (Angie, Kurt, Eberhard über den echten Objektgraph) sind
**ausschließlich** in diesem SIT abgedeckt und laufen bei `mvn test` nicht mit. Nach Änderungen an
Anspruchslogik oder Adaptern den SIT explizit ausführen.

## Architektur in `dojo-leistung`

Zwei fachliche Bounded Contexts, je mit identischem hexagonalem Schnitt:

```
at.gepardec.dojo.leistung.
├── anspruch/          (UC-ANSP-01 Anspruch Web Check)
│   ├── api/           REST-Einstieg (LeistungsanspruchRESTService)
│   ├── application/   QueryHandler + application/port/ (UseCase-Interface, Repository-Interface)
│   ├── domain/        Versicherter, AVersicherungszeit — reine Fachlogik
│   └── infrastructure/VersicherterAdapter → externe Services
├── au/                (UC-AU-01 Elektronische Krankmeldung)
│   ├── api/ application/ domain/ infrastructure/  (analog)
└── shared/domain/     Svnr (kontextübergreifendes Value Object)
```

Regeln, die der Code aktuell durchhält und die beibehalten werden sollen:

- **Abhängigkeitsrichtung:** `application.port` ist der nach innen gerichtete Vertrag. `api` kennt nur
  das UseCase-Interface aus `application.port`, `application` implementiert es und hängt an `domain`,
  `infrastructure` implementiert die Repository-/Port-Interfaces. Niemand außer `infrastructure`
  greift auf die Umsysteme `at.gepardec.dojo.{zeiten,personen,angehoerige}` zu. `domain` importiert
  nichts außerhalb des eigenen Kontexts (außer `shared.domain`).
- **Ausnahme `at.gepardec.dojo.svnr.SvnrValidator`:** wird als reine Utility behandelt, nicht als
  Umsystem, und darf deshalb aus `shared.domain.Svnr` heraus verwendet werden. Nur diese eine
  externe Klasse hat diesen Status.
- **Kontext-zu-Kontext:** `au` ruft `anspruch` **nicht** direkt auf. Stattdessen definiert `au` einen
  eigenen Port `au.application.port.LeistungsanspruchPruefungPort`, den
  `au.infrastructure.LeistungsanspruchPruefungAdapter` auf `anspruch`s UseCase-Interface übersetzt.
  Neue kontextübergreifende Zugriffe genauso bauen. (`au/api/AuRESTController` importiert derzeit
  ungenutzt `anspruch.api.Response` — ein Überbleibsel, kein Vorbild.)
- **Externe Datenmodelle nie durchreichen:** `VersicherterAdapter` mappt `VersicherungsZeit` → `AVersicherungszeit`
  und `Person` → Geburtsdatum. Domänentypen sehen die externen Records nie.
- **Verdrahtung passiert in den Tests**, es gibt keinen DI-Container und keine `main`-Klasse —
  die Integrationstests (`src/test/java/it/...`) bauen den Objektgraph von Hand zusammen und sind
  damit die faktische Composition Root.

## Fachliche Regeln (Stand: Iteration 5)

Anspruch (`Versicherter.isVersichert`):
- Eigenanspruch bei aktiver Versicherungszeit (`bis == null` heißt aktiv).
- Kinder unter 18: mitversichert, wenn mindestens ein Elternteil Eigenanspruch hat
  (Elternteil = `AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL`).

AU-Meldung: wird nur gespeichert, wenn Anspruch besteht. Achtung auf den Stichtag — geprüft wird
aktuell `LocalDate.now()` ("ist der Versicherte *heute* versichert?"), **nicht** `command.auBeginn()`.
Das war schon einmal ein Missverständnis (Commit `1369d83`); vor einer Änderung klären, welcher
Stichtag fachlich gemeint ist.

Erwartete Testfälle: Kurt hat Anspruch (aktive Beschäftigung), Angie hat Anspruch (16, Kind von Kurt),
Eberhard hat keinen Anspruch (24, nie beschäftigt).

Achtung bei den Testdaten: die SVNR-Konstanten in `at.gepardec.dojo.test.TestData` weichen von den in
`doc/testcases.md` genannten Nummern ab (SVNR_EBERHARD/SVNR_ANGIE sind gegenüber der Doku vertauscht).
Die Stub-Services sind in sich konsistent mit `TestData` — immer die Konstanten verwenden, nie die
Nummern aus der Doku abtippen.

## Test-Konventionen

Testmethoden folgen `// given / // when / // then`. Mehrere Testklassen sind bewusst leer und
enthalten nur Kommentare als Platzhalter für noch zu schreibende Fälle (z.B.
`ErstelleAuMeldungCommandHandlerTest`, `AuMeldungTest`) — diese Kommentare sind Aufgabenbeschreibung,
nicht toter Code. Mockito ist derzeit **nicht** als Dependency eingebunden, obwohl Kommentare es
vorsehen; bei Bedarf ergänzen.
