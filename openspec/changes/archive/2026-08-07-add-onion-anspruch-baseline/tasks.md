
## 1. Projektgerüst

- [x] 1.1 `dojo-leistung/pom.xml` um AssertJ, Mockito und ArchUnit (`archunit-junit5`, Scope
      `test`) ergänzen
- [x] 1.2 Prüfen, dass `mvn test` grün durchläuft, bevor Fachlogik entsteht
- [x] 1.3 Paketgerüst `at.gepardec.dojo.leistung.{shared,anspruch,au}` anlegen

## 2. Shared Kernel

- [x] 2.1 `shared/domain/Svnr.java` als `record` mit Prüfsummenvalidierung über `SvnrValidator`
- [x] 2.2 `SvnrTest`: gültige Nummer, ungültige Prüfziffer, `null`, falsche Länge

## 3. Fitness Functions -- Grundregeln

> Bewusst früh: Die ArchUnit-Regeln sind die ausführbare Fassung der Zielvariante. Am Ende
> geschrieben wären sie ein Stempel; hier geschrieben schlagen sie beim ersten falschen Import zu.

- [x] 3.1 `ArchitectureRulesTest` anlegen. **Achtung:** ArchUnit lässt seit 0.23 eine Regel
      fehlschlagen, die auf kein einziges Paket zutrifft (`archRule.failOnEmptyShould`). Entweder
      `src/test/resources/archunit.properties` mit `archRule.failOnEmptyShould=false` anlegen oder
      jede Regel erst ergänzen, sobald das betroffene Paket Klassen enthält.
- [x] 3.2 Onion-Dependency-Regel für `…leistung.anspruch.domain` -- erlaubt sind nur das eigene
      Paket, `…leistung.shared.domain` und `java..`
- [x] 3.3 Regel: keine Systemuhr im Domänenring
      (`noClasses()…should().callMethod(LocalDate.class, "now")`)
- [x] 3.4 `SvnrValidator` als einzige zugelassene Ausnahme aus `dojo-external-domains` im
      Domänenring in der Regel benennen und im Test kommentieren -- eine benannte Ausnahme, keine
      stillschweigende Lücke
- [x] 3.5 Gegenprobe im Kleinen: eine Domänenklasse testweise einen Adapter importieren lassen und
      prüfen, dass der Test rot wird

## 4. Anspruch -- Domänenring

> Arbeitsrichtung: **erst die Regel schreiben, das gewünschte Interface dabei erfinden, dann als
> Port herausziehen.** Werden die Ports vorab entworfen, entstehen technische Signaturen
> (`getVersicherungsZeiten(String)`) statt fachlicher (`aktiveZeiten(Svnr)`).

- [x] 4.1 `anspruch/domain/model/Versicherungszeit.java` (record `von`/`bis`, `istAktiv()`,
      `enthaelt(stichtag)`) mit Unit-Test
- [x] 4.2 `anspruch/domain/rule/Anspruch.java` (Interface,
      `boolean anspruch(Svnr svnr, LocalDate stichtag)`)
- [x] 4.3 `anspruch/domain/rule/EigenAnspruch.java` schreiben; den benötigten Zugriff dabei als
      `anspruch/domain/port/ZeitenPort.java` herausziehen
- [x] 4.4 `EigenAnspruchTest` mit Mockito-Double des Ports: aktive Zeit, nur abgelaufene Zeiten,
      keine Zeiten
- [x] 4.5 `anspruch/domain/rule/KindAnspruch.java` schreiben -- setzt 4.3 voraus, da beim
      Elternteil ausschließlich der Eigenanspruch geprüft wird; Altersgrenze vorerst Konstante 18
- [x] 4.6 Benötigte Zugriffe als `anspruch/domain/port/PersonenPort.java` und
      `AngehoerigePort.java` herausziehen
- [x] 4.7 `KindAnspruchTest`: Kind mit berechtigtem Elternteil, volljähriges Kind, Randfall
      **genau 18 Jahre** (kein Anspruch), kein Elternteil mit Eigenanspruch
- [x] 4.8 `anspruch/domain/AnspruchService.java` -- wertet die Regelliste aus, Anspruch sobald
      eine Regel greift
- [x] 4.9 Prüfen, dass die Regeln aus 3.2/3.3 weiterhin grün sind

## 5. Anspruch -- Application und Infrastructure

- [x] 5.1 `anspruch/application/PruefeAnspruchUseCase.java` (Input Port)
- [x] 5.2 `anspruch/application/PruefeAnspruchService.java` -- implementiert den Input Port,
      ermittelt den Stichtag vorerst per `LocalDate.now()` (wird in `add-stichtag-port` ersetzt)
- [x] 5.3 `anspruch/infrastructure/ZeitenAdapter.java` -- mappt `VersicherungsZeit` auf
      `Versicherungszeit`, kein Durchreichen des Umsystem-Typs
- [x] 5.4 `anspruch/infrastructure/PersonenAdapter.java`, `AngehoerigeAdapter.java`
- [x] 5.5 `anspruch/infrastructure/AnspruchWebCheck.java` -- Selbstbedienungseinstieg, liefert
      "Anspruch vorhanden" / "Kein Anspruch"
- [x] 5.6 Randfall absichern: unbekannte SVNR liefert "Kein Anspruch" statt einer
      `NullPointerException` -- dieser Fehler steckt in beiden bestehenden Lösungen
- [x] 5.7 Adapter-Tests gegen die echten Umsystem-Stubs

## 6. AU -- Kontext vollständig

- [x] 6.1 `au/domain/model/AuMeldung.java` (AU-Beginn, Svnr, Pflichtfeldprüfung)
- [x] 6.2 `au/domain/AuMeldungService.java` -- die Gültigkeitsregel "nur bei Anspruch" liegt hier,
      NICHT im Application Service
- [x] 6.3 Benötigte Zugriffe als `au/domain/port/AuMeldungPort.java` und
      `AnspruchPruefungPort.java` herausziehen
- [x] 6.4 Ergebnistyp für "gespeichert" / "abgelehnt mit Grund" statt `void`
- [x] 6.5 `au/application/ErstelleAuMeldungUseCase.java` + Service
- [x] 6.6 `au/infrastructure/AuMeldungAdapter.java` (Attrappe mit Log)
- [x] 6.7 `au/infrastructure/AnspruchPruefungAdapter.java` -- bildet auf
      `anspruch.application.PruefeAnspruchUseCase` ab; setzt 5.1 voraus
- [x] 6.8 ArchUnit-Regel ergänzen: `au` darf `anspruch` ausschließlich aus `au.infrastructure`
      sehen (erst jetzt sinnvoll, da die Pakete nun Klassen enthalten)
- [x] 6.9 Tests: Kurt wird gespeichert, Eberhard abgelehnt, unvollständige Meldung abgelehnt

## 7. Verdrahtung und Integrationstests

- [x] 7.1 `infrastructure/CompositionRoot.java` -- baut den Objektgraphen, kennt als Einzige beide
      Seiten
- [x] 7.2 Durchgängiger Test über den echten Objektgraphen: Kurt, Angie, Eberhard, Maria
- [x] 7.3 Sicherstellen, dass diese Tests bei `mvn test` mitlaufen (bei Endung `IT`/`SIT`
      Failsafe-Plugin konfigurieren) -- in `feature/dojo1` lief der einzige aussagekräftige
      Integrationstest mangels Konfiguration nie
- [x] 7.4 Prüfen, dass keine Testklasse einen leeren Rumpf oder eine Methode ohne Assertion hat

## 8. Gegenprobe und Abschluss

- [x] 8.1 ArchUnit-Regeln testweise gegen `feature/dojo1` und `feature/solution1` laufen lassen
      und prüfen, dass die in `result.adoc` beschriebenen Verstöße gemeldet werden
- [x] 8.2 `doc/UseCase_AnspruchWebCheck.md` und `doc/UseCase_AU_ElektronischeKrankmeldung.md` um
      die Iterationsabschnitte 1--5 ergänzen (Dojo-Ablauf laut `doc/README.md`)
- [x] 8.3 `result.adoc` um die dritte Lösungsversion erweitern: Einordnung nach §0 des
      Referenz-Guides und Gegenüberstellung der Leaks
- [x] 8.4 `openspec archive add-onion-anspruch-baseline` -- erst dadurch entstehen die
      Capability-Specs unter `openspec/specs/`, die die beiden Folge-Changes voraussetzen
