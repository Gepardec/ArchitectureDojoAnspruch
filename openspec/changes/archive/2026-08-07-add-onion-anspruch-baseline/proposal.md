## Why

Für das Architektur-Dojo existieren zwei Lösungsversionen: `feature/dojo1` setzt Clean
Architecture (Eschhold-Adaption) um, `feature/solution1` Hexagonal Architecture (Cockburn) --
siehe die Analyse in `result.adoc`. Die dort begründete Empfehlung für diese Fachlichkeit lautet
**Onion Architecture (Palermo)**, weil der Anspruchsbereich konfigurationsgetrieben ist und einen
wachsenden Regelkatalog hat. Eine dritte Lösungsversion macht diese Empfehlung überprüfbar statt
behauptet.

Dieser Change liefert die Vergleichsbasis: dieselben fachlichen Iterationen 1--5, die auch die
beiden bestehenden Branches abdecken, umgesetzt in der Onion-Variante.

## What Changes

- Neuer Lösungsbaum `at.gepardec.dojo.leistung` im Modul `dojo-leistung`, aufgebaut nach Onion:
  `domain` (innerster Ring, inkl. Ports) -- `application` -- `infrastructure`.
- Fachliche Abdeckung der Iterationen 1--5:
  - Anspruchsauskunft über Selbstbedienung (UC-ANSP-01)
  - Eigenanspruch bei aktiver Versicherungszeit
  - Mitversicherung für Kinder unter 18 Jahren
  - Entgegennahme elektronischer Krankmeldungen (UC-AU-01)
  - AU-Meldung nur bei bestehendem Anspruch speichern
- Anspruchsregeln als Strategie-Muster (`domain.rule`), damit jede weitere Anspruchsart eine
  zusätzliche Klasse statt einer Änderung an bestehendem Code ist.
- Kontextgrenze `au` → `anspruch` über einen eigenen Port plus Adapter (Anti-Corruption Layer)
  statt eines direkten Aufrufs.
- ArchUnit als Fitness Function für die Onion-Dependency-Regel -- die Zielvariante wird
  maschinell geprüft, nicht nur dokumentiert.

Bewusst **nicht** enthalten (kommen als eigene Changes):

- Altersgrenze aus dem Regelwerk (Iteration 6) → `add-regelwerk-port`
- Vorgebbares Tagesdatum (Iteration 7) → `add-stichtag-port`

Diese Trennung ist Absicht: `doc/README.md` nennt als Zweck des Dojos, nachvollziehbar zu machen,
welche Pakete für welche Anforderungsänderung angefasst werden mussten. Werden die beiden
Architekturvorgaben in dieselbe Änderung gepackt, ist genau diese Information nicht mehr ablesbar.

## Capabilities

### New Capabilities

- `anspruch-pruefung`: Feststellen, ob eine Person zu einem Stichtag Anspruch auf
  Versicherungsleistungen hat, und Auskunft darüber erteilen.
- `au-meldung`: Entgegennahme und Speicherung elektronischer Arbeitsunfähigkeitsmeldungen
  einschließlich der Gültigkeitsprüfung gegen den Leistungsanspruch.

### Modified Capabilities

<!-- keine - dies ist der erste Change des Branches -->

## Impact

- **Neu:** `dojo-leistung/src/main/java/at/gepardec/dojo/leistung/**` (Domain, Application,
  Infrastructure für beide Kontexte)
- **Neu:** `dojo-leistung/src/test/java/at/gepardec/dojo/leistung/**` inkl.
  `ArchitectureRulesTest`
- **Geändert:** `dojo-leistung/pom.xml` -- Abhängigkeiten ArchUnit, AssertJ, Mockito
- **Unverändert:** `dojo-external-domains` -- laut `doc/README.md` darf in diesem Modul nichts
  geändert werden. Die Umsysteme `ZeitenService`, `PersonenService`, `AngehoerigeService` und
  `SvnrValidator` werden ausschließlich über Adapter angebunden.
- **Keine Datenbank, kein Web-Framework, kein DI-Container.** Persistenz und REST bleiben
  Attrappen hinter Ports, die Verdrahtung übernimmt eine explizite Composition Root.
