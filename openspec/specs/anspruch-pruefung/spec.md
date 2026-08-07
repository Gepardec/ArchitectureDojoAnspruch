# anspruch-pruefung Specification

## Purpose
TBD - created by archiving change add-onion-anspruch-baseline. Update Purpose after archive.
## Requirements
### Requirement: Anspruchsauskunft zu einem Stichtag

Das System SHALL zu einer gegebenen Versicherungsnummer und einem Stichtag feststellen, ob ein
Leistungsanspruch besteht, und das Ergebnis als Ja/Nein-Aussage zurückgeben.

Der Stichtag MUST der Fachlogik von außen übergeben werden. Die Domäne MUST NOT selbst auf die
Systemuhr zugreifen.

#### Scenario: Anspruch besteht

- **WHEN** für eine Versicherungsnummer zu einem Stichtag mindestens eine Anspruchsregel erfüllt ist
- **THEN** liefert das System "Anspruch vorhanden"

#### Scenario: Kein Anspruch

- **WHEN** für eine Versicherungsnummer zu einem Stichtag keine Anspruchsregel erfüllt ist
- **THEN** liefert das System "Kein Anspruch"

#### Scenario: Unbekannte Versicherungsnummer

- **WHEN** zu einer syntaktisch gültigen Versicherungsnummer keine Person bekannt ist
- **THEN** liefert das System "Kein Anspruch" und bricht NOT mit einem Fehler ab

### Requirement: Gültigkeit der Versicherungsnummer

Das System SHALL eine Versicherungsnummer bei der Erzeugung des Wertobjekts syntaktisch und über
die Prüfsumme validieren und ungültige Nummern zurückweisen.

#### Scenario: Gültige Versicherungsnummer

- **WHEN** eine zehnstellige Nummer mit korrekter Prüfziffer übergeben wird
- **THEN** entsteht ein Wertobjekt `Svnr`

#### Scenario: Ungültige Prüfziffer

- **WHEN** eine Nummer mit falscher Prüfziffer übergeben wird
- **THEN** wird die Erzeugung mit einer `IllegalArgumentException` abgelehnt

### Requirement: Eigenanspruch bei aktiver Versicherungszeit

Eine Person SHALL Anspruch haben, wenn sie zum Stichtag eine aktive Versicherungszeit besitzt.
Eine Versicherungszeit ist aktiv, wenn ihr Bis-Datum leer ist. Ein Anspruch aus dieser Regel heißt
Eigenanspruch.

#### Scenario: Aktive Beschäftigung

- **WHEN** Kurt (SVNR aus `TestData.SVNR_KURT`) geprüft wird, dessen Versicherungszeit seit
  01.09.2022 kein Bis-Datum hat
- **THEN** besteht Eigenanspruch

#### Scenario: Nur abgelaufene Versicherungszeiten

- **WHEN** eine Person ausschließlich Versicherungszeiten mit gesetztem Bis-Datum hat
- **THEN** besteht kein Eigenanspruch

#### Scenario: Keine Versicherungszeiten

- **WHEN** Eberhard (SVNR aus `TestData.SVNR_EBERHARD`) geprüft wird, der nie beschäftigt war
- **THEN** besteht kein Eigenanspruch

### Requirement: Mitversicherung für Kinder unterhalb der Altersgrenze

Eine Person unterhalb der im Regelwerk hinterlegten Altersgrenze SHALL Anspruch haben, wenn
mindestens ein Elternteil einen Eigenanspruch besitzt. Als Elternteil gilt eine
Angehörigenbeziehung vom Typ `AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL`.

Die Altersgrenze MUST zum Prüfzeitpunkt über den Regelwerk-Port ermittelt werden und MUST NOT als
Konstante im Programmcode stehen. Der fachliche Vorgabewert beträgt 18 Jahre.

Beim Elternteil MUST ausschließlich der Eigenanspruch geprüft werden, NOT der volle Anspruch --
ein Kindanspruch des Elternteils begründet keinen Anspruch des Kindes.

#### Scenario: Kind eines eigenanspruchsberechtigten Elternteils

- **WHEN** Angie (SVNR aus `TestData.SVNR_ANGIE`, geboren 24.07.2010) zum Stichtag 05.08.2026
  geprüft wird, die Altersgrenze im Regelwerk 18 beträgt und ihr Vater Kurt Eigenanspruch hat
- **THEN** besteht Anspruch

#### Scenario: Volljähriges Kind

- **WHEN** Eberhard (geboren 01.04.2002) zum Stichtag 05.08.2026 bei einer Altersgrenze von 18
  geprüft wird, obwohl sein Vater Kurt Eigenanspruch hat
- **THEN** besteht kein Anspruch

#### Scenario: Genau an der Altersgrenze

- **WHEN** eine Person am Stichtag exakt das im Regelwerk hinterlegte Grenzalter erreicht
- **THEN** besteht kein Kindanspruch mehr, da die Regel "unter" der Grenze fordert

#### Scenario: Kein Elternteil mit Eigenanspruch

- **WHEN** ein Kind unterhalb der Altersgrenze geprüft wird, dessen Eltern alle keinen
  Eigenanspruch haben
- **THEN** besteht kein Anspruch

#### Scenario: Fachabteilung hebt die Altersgrenze an

- **WHEN** die Altersgrenze im Regelwerk auf 27 gesetzt wird und Eberhard (24 Jahre am Stichtag
  05.08.2026) geprüft wird, dessen Vater Kurt Eigenanspruch hat
- **THEN** besteht Anspruch, ohne dass Programmcode geändert wurde

### Requirement: Erweiterbarkeit des Regelkatalogs

Anspruchsregeln SHALL als eigenständige, einzeln prüfbare Einheiten modelliert sein. Das
Hinzufügen einer weiteren Anspruchsart MUST ohne Änderung bestehender Regelklassen möglich sein.

Jede Regel SHALL sich die von ihr benötigten Daten selbst über Ports beschaffen, damit die
aufrufende Schicht den Datenbedarf einzelner Regeln NOT kennen muss.

#### Scenario: Regel einzeln prüfbar

- **WHEN** eine einzelne Anspruchsregel getestet wird
- **THEN** ist sie ohne die übrigen Regeln instanziierbar und liefert für eine Versicherungsnummer
  und einen Stichtag ein Ja/Nein-Ergebnis

#### Scenario: Anspruch besteht, sobald eine Regel greift

- **WHEN** mehrere Anspruchsregeln ausgewertet werden und mindestens eine erfüllt ist
- **THEN** besteht Anspruch, unabhängig vom Ergebnis der übrigen Regeln

### Requirement: Selbstbedienungsauskunft Anspruch Web Check

Das System SHALL einem angemeldeten Benutzer über einen Einstiegspunkt der Anwendung erlauben,
zu einer eingegebenen Versicherungsnummer die Anspruchsauskunft abzurufen (UC-ANSP-01).

#### Scenario: Auskunft bei bestehendem Anspruch

- **WHEN** ein Benutzer die Versicherungsnummer einer anspruchsberechtigten Person eingibt
- **THEN** zeigt das System "Anspruch vorhanden"

#### Scenario: Auskunft ohne Anspruch

- **WHEN** ein Benutzer die Versicherungsnummer einer nicht anspruchsberechtigten Person eingibt
- **THEN** zeigt das System "Kein Anspruch"

### Requirement: Architekturkonformität Onion

Der Quelltext SHALL die Dependency Rule der Onion-Architektur einhalten: Klassen im Paket
`…leistung.anspruch.domain` MUST NOT von Klassen außerhalb dieses Pakets abhängen, ausgenommen
`java.*` und das Shared-Kernel-Paket `…leistung.shared.domain`.

Klassen im Domänenring MUST NOT `LocalDate.now()` oder eine andere Systemuhr aufrufen.

Diese Regeln SHALL als ausführbarer Test im Build verankert sein.

#### Scenario: Verstoß gegen die Dependency Rule

- **WHEN** eine Domänenklasse eine Klasse aus `application` oder `infrastructure` importiert
- **THEN** schlägt der Architekturtest im Build fehl

#### Scenario: Zugriff auf die Systemuhr in der Domäne

- **WHEN** eine Klasse im Domänenring `LocalDate.now()` aufruft
- **THEN** schlägt der Architekturtest im Build fehl

### Requirement: Stichtag ist von außen vorgebbar

Das System SHALL den Stichtag der Anspruchsprüfung über einen Port beziehen, dessen
Implementierung austauschbar ist.

Im Regelbetrieb SHALL der Stichtag dem aktuellen Tagesdatum entsprechen. Für Tests MUST ein
festes Datum vorgebbar sein, ohne dass Produktionscode geändert wird.

Ein Aufruf der Systemuhr MUST ausschließlich im dafür vorgesehenen Adapter stattfinden. Kein
anderes Paket SHALL `LocalDate.now()` oder eine gleichwertige Quelle aufrufen.

#### Scenario: Regelbetrieb

- **WHEN** die Anspruchsprüfung ohne besondere Konfiguration ausgeführt wird
- **THEN** verwendet sie das aktuelle Tagesdatum als Stichtag

#### Scenario: Test mit festem Stichtag

- **WHEN** die Anwendung mit einem festen Stichtag 05.08.2026 verdrahtet wird
- **THEN** liefert jede Anspruchsprüfung dasselbe Ergebnis, unabhängig vom tatsächlichen
  Kalenderdatum der Testausführung

#### Scenario: Kein Umschalten zur Laufzeit über den Produktivvertrag

- **WHEN** der Vertrag des Zeit-Ports betrachtet wird
- **THEN** enthält er ausschließlich eine lesende Operation und NOT Methoden zum Setzen oder
  Zurücksetzen des Datums

#### Scenario: Systemuhr nur im Adapter

- **WHEN** eine Klasse außerhalb des Systemuhr-Adapters `LocalDate.now()` aufruft
- **THEN** schlägt der Architekturtest im Build fehl

### Requirement: Zeitstabile Testfälle des Dojos

Die fachlichen Testfälle SHALL mit einem festen Stichtag ausgeführt werden, damit ihr Ergebnis
über die Jahre unverändert bleibt.

#### Scenario: Angie am festen Stichtag

- **WHEN** Angie (geboren 24.07.2010) zum festen Stichtag 05.08.2026 geprüft wird und ihr Vater
  Kurt Eigenanspruch hat
- **THEN** besteht Anspruch -- unabhängig davon, in welchem Jahr der Test läuft

#### Scenario: Angie nach Erreichen der Altersgrenze

- **WHEN** dieselbe Prüfung mit dem Stichtag 05.08.2029 ausgeführt wird
- **THEN** besteht kein Anspruch mehr, da Angie die Altersgrenze überschritten hat

#### Scenario: Eberhard am festen Stichtag

- **WHEN** Eberhard (geboren 01.04.2002) zum festen Stichtag 05.08.2026 geprüft wird
- **THEN** besteht kein Anspruch

### Requirement: Regelparameter aus dem Regelwerk

Das System SHALL Parameter der Anspruchsprüfung aus einem persistenten Regelwerk lesen, das ohne
Änderung des Programmcodes gepflegt werden kann.

Der Zugriff MUST über einen Port des Domänenrings erfolgen, sodass die Speichertechnologie
austauschbar bleibt und die Regeln ohne Datenbank testbar sind.

Fehlt ein angefragter Parameter im Regelwerk, MUST die Prüfung mit einem eindeutigen Fehler
abbrechen. Ein stillschweigender Rückfall auf einen im Code hinterlegten Wert SHALL NOT
stattfinden, da er den Zweck der Auslagerung unterläuft.

#### Scenario: Parameter wird gelesen

- **WHEN** eine Anspruchsregel einen Regelparameter benötigt
- **THEN** liest sie ihn zum Prüfzeitpunkt über den Regelwerk-Port

#### Scenario: Geänderter Parameter wirkt ohne Deployment

- **WHEN** der Wert eines Regelparameters im Regelwerk geändert wird
- **THEN** verwendet die nächste Anspruchsprüfung den neuen Wert, ohne dass der Programmcode
  geändert oder neu ausgeliefert wird

#### Scenario: Fehlender Parameter

- **WHEN** ein angefragter Regelparameter im Regelwerk nicht hinterlegt ist
- **THEN** bricht die Prüfung mit einem Fehler ab, der den fehlenden Parameter benennt

#### Scenario: Regeln ohne Datenbank testbar

- **WHEN** eine Anspruchsregel getestet wird
- **THEN** genügt eine Test-Implementierung des Regelwerk-Ports; eine Datenbank ist NOT nötig

