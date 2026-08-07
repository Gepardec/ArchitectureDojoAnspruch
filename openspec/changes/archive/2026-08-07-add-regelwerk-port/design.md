## Context

Nach `add-onion-anspruch-baseline` steht die Altersgrenze als Konstante `18` in
`KindAnspruch`. Iteration 6 der Aufgabenstellung verlangt, dass solche Regelparameter aus einer
Datenbank kommen und von der Fachabteilung geändert werden können.

Dieser Change ist der eigentliche Prüfstein der Variantenwahl. Die Empfehlung für Onion in
`result.adoc` stützt sich maßgeblich darauf, dass eine Regel sich ihre Daten selbst besorgen darf.
Hier zeigt sich, ob das trägt.

Zum Vergleich: In `feature/solution1` existiert bereits ein Port `AnspruchRegeln`, dessen
Implementierung allerdings hartcodiert `18` zurückgibt -- die Naht ist da, die Datenbank nicht.
In `feature/dojo1` steht die `18` mitten in der Domäne, ohne Naht.

## Goals / Non-Goals

**Goals:**

- Die Altersgrenze zur Laufzeit aus einem persistenten Regelwerk beziehen.
- Zeigen, dass diese Änderung ausschließlich Domänenring und Infrastruktur berührt -- die
  Application-Schicht bleibt unangetastet.
- Den Port für mehr als einen Parameter auslegen, da die Vorgabe von einem "umfangreichen
  Regelwerk" spricht.

**Non-Goals:**

- Keine Pflegeoberfläche für die Fachabteilung.
- Kein Caching, keine Invalidierung, keine Versionierung von Regelständen.
- Keine Gültigkeitszeiträume für Regelparameter (fachlich naheliegend, aber nicht gefordert).

## Decisions

### D1: `RegelwerkPort` liegt in `anspruch.domain.port`

Konsistent zu allen anderen Ports dieser Lösung (Onion, §2.3 des Referenz-Guides). Dadurch bleibt
die ArchUnit-Dependency-Regel unverändert -- der neue Port braucht keine Sonderbehandlung.

### D2: `KindAnspruch` ruft den Port selbst

Der Konstruktor bekommt den `RegelwerkPort` zusätzlich zu den bestehenden Ports; die Regel liest
die Altersgrenze beim Auswerten.

Dies ist die Stelle, an der sich die Varianten messbar unterscheiden. Unter Clean Architecture
(Eschhold) dürfte die Regel den Port nicht rufen; nötig wäre stattdessen Decide-then-Act (§3.8):
Die Domäne gibt zurück, *welcher* Parameter gebraucht wird, der Interactor liest ihn und reicht
ihn zurück. Der Interactor müsste dafür den Datenbedarf dieser konkreten Regel kennen -- und mit
jeder weiteren Regel wächst dieses Wissen. Genau das war das Hauptargument der Empfehlung.

**Erwartetes Ergebnis dieses Changes, das im Diff nachweisbar sein soll:** `application` und
`au` bleiben unberührt.

### D3: Fachlicher Portschnitt, kein generischer Key-Value-Zugriff

Nicht `String get(String key)`, sondern benannte Zugriffe, z. B.
`int altersgrenzeMitversicherung()`. Ein generischer Zugriff würde die Fachlichkeit in
Zeichenketten verstecken und Tippfehler erst zur Laufzeit sichtbar machen.

Der Preis: Jeder neue Parameter erweitert das Interface. Das ist bei einem wachsenden Regelwerk
mehr Arbeit als eine generische Signatur -- aber es hält den Portvertrag fachlich lesbar, was in
einem Dojo über Paketstrukturen der wichtigere Wert ist.

### D4: Kein Fallback auf einen Vorgabewert im Code

Fehlt ein Parameter, bricht die Prüfung mit einem Fehler ab. Ein stiller Rückfall auf `18` würde
bedeuten, dass eine fehlerhafte Regelwerkspflege unbemerkt bleibt und im Code doch wieder eine
fachliche Konstante steht -- also genau das, was die Vorgabe abschaffen will.

### D5: Speicher des Regelwerks

Das Dojo hat keine Datenbank (siehe `result.adoc`, Kapitel "Nicht bewertet"). Der Adapter wird
daher gegen eine In-Memory- oder Property-basierte Ablage implementiert, die eine Datenbank
stellvertritt.

Das ist eine Attrappe wie `JpaAuMeldungRepository` in `feature/dojo1` -- mit dem Unterschied, dass
sie hier ehrlich benannt wird. Entscheidend für den Vergleich ist die Portgrenze, nicht die
Speichertechnologie. Wird später eine echte Datenbank ergänzt, ändert sich ausschließlich der
Adapter.

## Risks / Trade-offs

**Die Regel wird zur Laufzeit abhängig von Fremddaten.** Ein leeres oder falsch gepflegtes
Regelwerk legt die Anspruchsprüfung lahm. Das ist gewollt (D4), verlagert aber Betriebsrisiko in
die Fachabteilung. In einem Produktivsystem gehörte hierher eine Validierung der Regelwerksdaten
beim Laden.

**Ein Port pro Parametertyp lässt das Interface wachsen.** Bei wirklich umfangreichen Regelwerken
kippt die Entscheidung aus D3 irgendwann. Der Umschlagpunkt liegt erfahrungsgemäß dort, wo
Parameter dynamisch, mandanten- oder zeitraumabhängig werden -- dann braucht es ohnehin ein
eigenes Regelwerks-Modell statt einzelner Getter.

**Die Testfälle des Dojos werden empfindlich gegen den Regelwerksstand.** Alle bestehenden
Kindanspruchs-Tests müssen die Altersgrenze künftig explizit setzen. Das ist Mehraufwand, macht
die Tests aber auch aussagekräftiger: Erstmals lässt sich prüfen, was bei einer abweichenden
Grenze passiert.
