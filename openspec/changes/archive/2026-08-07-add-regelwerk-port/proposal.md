## Why

Die Altersgrenze für die Mitversicherung von Kindern steht nach `add-onion-anspruch-baseline` als
Konstante `18` im Quelltext. Laut Architekturvorgabe (Iteration 6 der Aufgabenstellung) sind
solche Konstanten Teil eines umfangreichen Regelwerks für die Anspruchsprüfung, das **die
Fachabteilung ändern können muss** -- ohne Deployment. Die Altersgrenze soll deshalb aus einer
Datenbank gelesen werden.

Dieser Change ist bewusst von der Baseline getrennt. `doc/README.md` nennt als Zweck des Dojos,
nachvollziehbar zu machen, welche Pakete für welche Anforderungsänderung angefasst werden mussten.
Der Diff dieses Changes ist damit die Antwort auf die Frage, was ein konfigurierbares Regelwerk in
der Onion-Variante strukturell kostet.

## What Changes

- Neuer Port `RegelwerkPort` im Domänenring des Anspruchskontexts, über den Regelparameter
  abgefragt werden.
- `KindAnspruch` bezieht die Altersgrenze über diesen Port statt aus einer Konstanten. Die Regel
  holt sich den Wert **selbst** -- genau das Zugeständnis, für das die Onion-Variante gewählt
  wurde.
- Neuer Adapter in `infrastructure`, der die Regelparameter aus einem persistenten Speicher liest.
- Der Port ist von Beginn an für mehrere Parameter geschnitten, nicht nur für die Altersgrenze --
  die Vorgabe spricht von einem "umfangreichen Regelwerk".

Ausdrücklich **nicht** enthalten:

- Keine Oberfläche für die Fachabteilung zum Pflegen der Werte.
- Keine Caching- oder Invalidierungsstrategie.

## Capabilities

### New Capabilities

<!-- keine -->

### Modified Capabilities

- `anspruch-pruefung`: Die Altersgrenze der Mitversicherung ist nicht mehr fest im Code
  hinterlegt, sondern wird zum Prüfzeitpunkt aus dem Regelwerk gelesen.

## Impact

- **Neu:** `…leistung/anspruch/domain/port/RegelwerkPort.java`
- **Neu:** `…leistung/anspruch/infrastructure/RegelwerkAdapter.java`
- **Geändert:** `…leistung/anspruch/domain/rule/KindAnspruch.java` -- Konstante entfällt,
  Konstruktorparameter kommt hinzu
- **Geändert:** Composition Root -- eine zusätzliche Verdrahtung
- **Geändert:** Tests zu `KindAnspruch` -- der Port wird zum Test-Double, wodurch abweichende
  Altersgrenzen erstmals testbar werden
- **Nicht betroffen:** `application`, `au`-Kontext, Shared Kernel. Dass die Application-Schicht
  unberührt bleibt, ist das eigentliche Ergebnis dieses Changes und der Kern des Vergleichs mit
  Clean Architecture.
