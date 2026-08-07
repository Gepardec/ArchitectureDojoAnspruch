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

Eine Person unterhalb der Altersgrenze SHALL Anspruch haben, wenn mindestens ein Elternteil einen
Eigenanspruch besitzt. Die Altersgrenze beträgt 18 Jahre. Als Elternteil gilt eine
Angehörigenbeziehung vom Typ `AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL`.

Beim Elternteil MUST ausschließlich der Eigenanspruch geprüft werden, NOT der volle Anspruch --
ein Kindanspruch des Elternteils begründet keinen Anspruch des Kindes.

#### Scenario: Kind eines eigenanspruchsberechtigten Elternteils

- **WHEN** Angie (SVNR aus `TestData.SVNR_ANGIE`, geboren 24.07.2010) zum Stichtag 05.08.2026
  geprüft wird und ihr Vater Kurt Eigenanspruch hat
- **THEN** besteht Anspruch

#### Scenario: Volljähriges Kind

- **WHEN** Eberhard (geboren 01.04.2002) zum Stichtag 05.08.2026 geprüft wird, obwohl sein Vater
  Kurt Eigenanspruch hat
- **THEN** besteht kein Anspruch

#### Scenario: Genau 18 Jahre alt

- **WHEN** eine Person am Stichtag exakt ihren 18. Geburtstag hat
- **THEN** besteht kein Kindanspruch mehr, da die Regel "unter 18" fordert

#### Scenario: Kein Elternteil mit Eigenanspruch

- **WHEN** ein Kind unter 18 geprüft wird, dessen Eltern alle keinen Eigenanspruch haben
- **THEN** besteht kein Anspruch

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

