## Context

Nach der Baseline reicht die Application-Schicht den Stichtag an den Domänenring durch und
ermittelt ihn selbst per `LocalDate.now()`. Der Domänenring ist damit bereits zeitfrei -- das war
eine bewusste Vorwegnahme (Entscheidung D3 in `add-onion-anspruch-baseline/design.md`).

Iteration 7 verlangt, dass das Tagesdatum für Tests vorgegeben werden kann. Zu erledigen bleibt
deshalb genau eine Sache: den letzten Uhrzugriff hinter einen Port bringen.

Zum Vergleich der bestehenden Lösungen:

- `feature/solution1` erfüllt die Vorgabe über einen Port `SystemDaten` mit
  `today()`/`setToday()`/`resetToday()` und einem statischen, veränderlichen Singleton.
- `feature/dojo1` erfüllt sie gar nicht; dort ruft zusätzlich `Versicherter.isUnter18()` die Uhr
  mitten in der Domäne.

## Goals / Non-Goals

**Goals:**

- Den Stichtag über einen Port beziehen, dessen Produktivvertrag ausschließlich lesend ist.
- Die Testfälle des Dojos dauerhaft stabil machen (fester Stichtag 05.08.2026).
- Nachweisen, dass diese Iteration in der Onion-Variante ausschließlich Application und
  Infrastructure berührt und den Domänenring unangetastet lässt.

**Non-Goals:**

- Keine Uhrzeit-, Zeitzonen- oder Sommerzeitbehandlung. Die Fachlichkeit arbeitet auf Tagesebene.
- Keine Möglichkeit, das Datum im laufenden Produktivbetrieb umzuschalten.
- Kein Ersatz von `java.time.Clock` durch eine eigene Zeitabstraktion über das hier Nötige hinaus.

## Decisions

### D1: Der Port ist ausschließlich lesend

`ZeitPort` bietet genau `LocalDate heute()`. Kein `setToday()`, kein `resetToday()`.

Die Testvorgabe erfolgt durch **Auswahl einer anderen Implementierung** bei der Verdrahtung, nicht
durch Umschalten eines gemeinsamen Zustands. Zwei Adapter: `SystemZeitAdapter` (ruft die
Systemuhr) und `FixerZeitAdapter` (liefert ein im Konstruktor übergebenes Datum).

Abgrenzung zu `feature/solution1`: Dort stehen `setToday`/`resetToday` im Portinterface. Das hat
zwei Folgen -- jeder produktive Adapter muss Methoden implementieren, die er nicht braucht, und
jeder Produktionscode kann das Systemdatum verstellen. Zusätzlich zwingt das veränderliche
statische Singleton die Tests zu `@BeforeEach`/`@AfterEach`-Aufräumarbeiten und macht sie
reihenfolgeabhängig und nicht parallelisierbar.

### D2: `ZeitPort` liegt in `anspruch.domain.port`, obwohl ihn nur die Application nutzt

Alle Ports dieser Lösung liegen im Domänenring; das hält die ArchUnit-Dependency-Regel bei einer
einzigen Zeile. Ein Zeit-Port im Domänenring, den der Domänenring selbst nie ruft, ist auf den
ersten Blick schief.

Die Alternative wäre `anspruch.application.port` -- dann bräuchte es eine zweite Paketregel und
zwei Orte, an denen man nach Ports sucht. Bei der aktuellen Größe wiegt die Einheitlichkeit
schwerer. **Der Umschlagpunkt ist benannt:** Sobald mehrere Ports ausschließlich von der
Application genutzt werden, gehören sie in ein eigenes Portpaket dieser Schicht.

### D3: Der Domänenring bleibt unverändert

Die Regeln bekommen den Stichtag weiterhin als Methodenparameter. Sie erfahren nicht, woher er
stammt.

Das ist der inhaltlich wichtigste Punkt dieses Changes für den Dojo-Vergleich: Der Diff soll
zeigen, dass eine Vorwegnahme in der Baseline (Stichtag als Parameter statt Uhrzugriff) diese
Iteration von einer Domänenänderung zu einer reinen Verdrahtungsänderung macht.

### D4: Die Architekturregel wird verschärft, nicht ergänzt

Die bestehende Regel "keine Systemuhr im Domänenring" wird ausgeweitet auf "keine Systemuhr
außerhalb des Systemuhr-Adapters". Erst dadurch ist die Vorgabe maschinell abgesichert -- eine
Regel, die nur den Domänenring prüft, hätte den `LocalDate.now()`-Aufruf im Application Service
durchgelassen, den dieser Change gerade entfernt.

### D5: Testverdrahtung über die Composition Root

Integrationstests bauen den Objektgraphen mit `FixerZeitAdapter(LocalDate.of(2026, 8, 5))`. Kein
globaler Zustand, keine Aufräumarbeiten, parallelisierbar.

## Risks / Trade-offs

**Ein Port im Domänenring, der dort nicht genutzt wird, ist eine Unschärfe.** Sie ist in D2
bewusst eingegangen und mit einem Umschlagpunkt versehen. Wer die Lösung streng liest, wird sie
anmerken -- zu Recht.

**Zwei Adapter für eine triviale Operation wirken überdimensioniert.** Bei einer Anwendung dieser
Größe stimmt das. Der Nutzen liegt nicht in der Zeitabstraktion selbst, sondern darin, dass die
Testvorgabe ohne veränderlichen globalen Zustand auskommt -- was die Alternative in
`feature/solution1` nicht schafft.

**`java.time.Clock` wäre die Standardlösung.** Ein eigener `ZeitPort` ist redundant zu einer
JDK-Abstraktion, die genau dieses Problem löst. Bewusst trotzdem gewählt: Ein fachlich benannter
Port passt zur Portsprache der übrigen Lösung, und im Dojo geht es um Portstruktur, nicht um
JDK-Kenntnis. In einem Produktivsystem wäre `Clock` die naheliegendere Wahl.

**Der feste Stichtag kann Fehler verdecken.** Läuft alles gegen 05.08.2026, fällt eine
zeitabhängige Regressionsursache nicht auf. Gegenmaßnahme: Mindestens ein Testfall prüft
ausdrücklich einen zweiten Stichtag (05.08.2029), damit die Zeitabhängigkeit selbst getestet ist
und nicht nur wegkonfiguriert.
