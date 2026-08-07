## Context

Das Repository enthält bereits zwei Lösungsversionen desselben Dojos. Die Analyse in
`result.adoc` ordnet sie ein: `feature/dojo1` folgt Clean Architecture (Eschhold-Adaption),
`feature/solution1` folgt Hexagonal Architecture (Cockburn). Beide decken die fachlichen
Iterationen 1--5 ab; `solution1` zusätzlich die Architekturvorgaben 6 und 7.

Aus der Analyse ergab sich die Empfehlung, für diese Fachlichkeit **Onion (Palermo)** zu wählen.
Die Begründung ist im Kapitel "Empfehlung: Welche Variante passt zu dieser Fachlichkeit?" von
`result.adoc` ausgeführt und wird hier nicht wiederholt, sondern umgesetzt.

Rahmenbedingungen aus `doc/README.md`: Im Modul `dojo-external-domains` darf nichts geändert
werden. Die Aufmerksamkeit gilt Paketstrukturen und Abhängigkeiten, nicht Exception-Handling oder
Edge Cases.

## Goals / Non-Goals

**Goals:**

- Die Onion-Variante so umsetzen, dass ihre Definitionsmerkmale am Quelltext ablesbar sind:
  Ports im Domänenring, Dependency Rule nach innen, Domain Services dürfen Ports nutzen.
- Den Regelkatalog so schneiden, dass eine weitere Anspruchsart additiv hinzukommt.
- Die Architekturentscheidung maschinell prüfbar machen (ArchUnit), nicht nur dokumentieren.
- Vergleichbarkeit mit den beiden bestehenden Branches: gleicher fachlicher Umfang
  (Iterationen 1--5), gleiche Testdaten, gleiches Maven-Modul.

**Non-Goals:**

- Keine echte Datenbank, kein Web-Framework, kein DI-Container. Persistenz und REST bleiben
  Attrappen hinter Ports -- wie in beiden bestehenden Lösungen auch.
- Keine Fehler-Queue (Nebenszenario 1 des AU-Use-Case). Der Change stellt lediglich sicher, dass
  das Ergebnis der Verarbeitung auswertbar ist, sodass die Queue später anschließbar wäre.
- Iterationen 6 und 7 sind ausgeklammert und je ein eigener Change.
- Keine Änderung an `dojo-external-domains`.

## Decisions

### D1: Ports liegen im Domänenring (`domain.port`)

Das ist das Definitionsmerkmal von Onion (§2.3 des Referenz-Guides) und der Punkt, an dem sich
diese Lösung von beiden bestehenden unterscheidet: `dojo1` legt die Ports eine Schale weiter
außen, `solution1` in ein Geschwister-Package neben der Domäne.

Konsequenz: Die Regelklassen dürfen ihre Ports direkt nutzen. Genau das ist der fachliche Grund
für die Variantenwahl -- eine Regel weiß selbst, welche Daten sie braucht.

### D2: Anspruchsregeln als Strategie-Muster in `domain.rule`

`Anspruch` als Interface mit `boolean anspruch(Svnr svnr, LocalDate stichtag)`, Implementierungen
`EigenAnspruch` und `KindAnspruch`. Eine Domänenklasse `AnspruchService` wertet die registrierten
Regeln aus und liefert Anspruch, sobald eine greift.

Übernommen aus `feature/solution1`, wo dieses Muster die beste Einzelidee des Vergleichs war.
Anders als dort bekommen die Regeln ihre Ports per Konstruktor statt aus einer statischen Factory.

### D3: Der Stichtag wird durchgereicht, die Domäne kennt keine Uhr

Alle Regeln erhalten den Stichtag als Parameter. Auch schon in diesem Baseline-Change, obwohl
Iteration 7 erst später kommt -- die Alternative (`LocalDate.now()` in der Domäne) ist der Fehler,
den `dojo1` gemacht hat, und sie wäre in `add-stichtag-port` teuer zu korrigieren.

Woher die Anwendung den Stichtag nimmt, ist in diesem Change bewusst simpel: `LocalDate.now()` im
Application Service. `add-stichtag-port` ersetzt das durch einen Port. Der Unterschied im Diff
ist dann exakt die Antwort auf die Frage, was Iteration 7 strukturell kostet.

### D4: `Svnr` als Shared Kernel in `…leistung.shared.domain`

Beide Kontexte brauchen das Wertobjekt. Läge es in `anspruch.domain`, müsste `au` darauf
zugreifen -- der Fehler aus `feature/solution1`. Ein bewusst winziger Shared Kernel ist die
kleinere Kopplung.

`Svnr` validiert im Konstruktor über `SvnrValidator` aus `dojo-external-domains` und ist ein
`record` (Wertsemantik inklusive `equals`/`hashCode` -- in `solution1` fehlte beides).

Damit hängt `shared.domain` an einer Klasse aus einem fremden Modul. Das ist eine bewusste,
hier dokumentierte Ausnahme: `SvnrValidator` ist eine zustandslose Utility, kein Umsystem. Die
ArchUnit-Regel führt sie als benannte Ausnahme, nicht als Lücke.

### D5: Kontextgrenze `au` → `anspruch` über Port und Adapter

`au.domain.port.AnspruchPruefungPort` wird von
`au.infrastructure.AnspruchPruefungAdapter` implementiert, der auf den Application Service des
Anspruchskontexts abbildet.

Übernommen aus `feature/dojo1`, wo dies die wertvollste Einzelentscheidung war. Onion ist bei
Input Ports schweigsam, verbietet sie aber nicht; bei zwei treibenden Technologien (Web für ANSP,
Messaging für AU) und einer Kontextgrenze zahlt sich der Port aus.

### D6: Composition Root in `infrastructure`, kein Service Locator

Alle Abhängigkeiten per Konstruktor. Eine explizite Klasse `infrastructure.CompositionRoot` baut
den Objektgraphen; Tests bauen ihn wahlweise selbst.

Abgrenzung zu `feature/solution1`: Dort liegt die Factory im Port-Package und importiert alle
Adapter -- ein Zyklus vom inneren zum äußeren Ring und der schwerste Befund des gesamten
Vergleichs. Die Composition Root gehört in den äußersten Ring, weil nur dort beide Seiten bekannt
sein dürfen.

### D7: ArchUnit als Fitness Function

Zwei Regeln genügen für die wesentlichen Aussagen:

```java
classes().that().resideInAPackage("..leistung.anspruch.domain..")
    .should().onlyDependOnClassesThat()
    .resideInAnyPackage("..leistung.anspruch.domain..", "..leistung.shared.domain..", "java..");

noClasses().that().resideInAPackage("..leistung..domain..")
    .should().callMethod(LocalDate.class, "now");
```

Die erste hätte in `solution1` den Zyklus `ports → adapters` gemeldet, die zweite in `dojo1` den
Systemuhr-Zugriff in `Versicherter.isUnter18()`. Dazu kommt eine dritte Regel für die
Kontextgrenze (`au` darf `anspruch` nur aus `au.infrastructure` sehen).

### D8: Tests mit Assertions, Integrationstests laufen im Build

Jede Testmethode prüft etwas. Keine leeren Rümpfe als Platzhalter -- der schwerwiegendste Befund
gegen `feature/dojo1` war ein grüner Build ohne Aussagekraft.

Integrationstests laufen unter `mvn test` mit; falls die Endung `IT`/`SIT` verwendet wird, MUSS
das Failsafe-Plugin konfiguriert sein. In `dojo1` lief der einzige aussagekräftige
Integrationstest mangels Konfiguration nie.

## Risks / Trade-offs

**Onion erlaubt der Domäne Port-Zugriff -- das ist Absicht, aber es kostet Disziplin.**
Die Grenze zwischen "Regel holt sich ihre Fachdaten" und "Regel macht Infrastrukturarbeit" ist
weicher als bei Clean. Die ArchUnit-Regel fängt nur Paketverstöße, nicht schlechten Geschmack.
Gegenmaßnahme: Ports sind fachlich geschnitten (`ZeitenPort.aktiveZeiten(svnr)`), nicht technisch
(`Database.query(...)`).

**Der Shared Kernel kann wachsen.** Heute enthält `shared.domain` genau eine Klasse. Jede weitere
Aufnahme sollte eine bewusste Entscheidung sein, sonst entsteht dort ein gemeinsamer
Datenmodell-Klumpen, der beide Kontexte aneinanderbindet.

**Ein dritter Branch erhöht den Pflegeaufwand.** Ändert der Sensei eine Geschäftsregel, gibt es
drei Stellen. Bewusst in Kauf genommen: Der Vergleich ist das Produkt des Dojos, nicht die
Anwendung.

**Ein Application Service ohne Input Port wäre einfacher.** Onion verlangt ihn nicht. Er wird
trotzdem eingeführt (D5), weil die Kontextgrenze ihn braucht. Wer die Lösung streng nach Palermo
liest, kann das als überflüssige Zeremonie sehen -- die Begründung ist die zweite treibende
Technologie, nicht das Muster an sich.
