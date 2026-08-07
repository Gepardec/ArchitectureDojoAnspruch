## ADDED Requirements

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

## MODIFIED Requirements

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
