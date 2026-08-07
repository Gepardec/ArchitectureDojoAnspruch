Use Case Anspruch Web Check
===========================

Dies ist ein Use Case aus dem Leistungswesen der Sozialversicherung im Bereich Anspruch (ANSP).

* Use-Case-ID: UC-ANSP-01
* Name: Anspruch Web Check
* Ziel: Ein Kunde kann selbst über Internet überprüfen, ober er/sie Anspruch auf Versicherungsleistungen hat.
* Ebene: User Goal
* Primärer Akteur: Kunde

## Stakeholder und Interessen
   * Benutzer: Möchte wissen, ob er Leistungen beziehen kann, z.B. kostenlos zum Arzt gehen kann.
   * Sozialversicherung: Möchte Auskunft geben, ohne Arbeitszeit von Mitarbeitern zu benötigen.

## Vorbedingungen
   * Der Benutzer besitzt ein aktives Benutzerkonto.
   * Der Benutzer ist am System angemeldet.

## Hauptszenario
   1. Das System präsentiert die Übersichtsseite.
   2. Der Benutzer wählt den Link "Anspruch prüfen".
   3. Das System präsentiert die "Anspruch prüfen" Maske.
   4. Der Benutzer gibt seine Sozialversicherungsnummer im Formular ein und drückt den Button "Prüfen".
   5. Das System zeigt "Anspruch vorhanden" oder "Kein Anspruch" an, je nachdem ob der Benutzer Anspruch hat.

## Iteration 1: Geschäftsregel Anspruch
   * Der Benutzer hat keinen Anspruch.

## Iteration 1: Testfälle
   * Eberhard hat keinen Anspruch.

## Iteration 1: Hintergrund
Anspruchsprüfung ist in der SV ein Bereich des Leistungswesens. Technisch wird es von der
Applikation Leistung abgearbeitet.

## Iteration 3: Eigenanspruch bei aktiver Versicherungszeit
Ergänzung der Regeln für den Anspruch: Wenn eine Person eine aktive Versicherungszeit hat, dann
hat sie Anspruch. Das ist z.B. durch eine unselbstständige Beschäftigung gegeben. Ein Anspruch aus
dieser Regel heißt Eigenanspruch.

Versicherungszeiten werden im Versicherungswesen (Applikation MVB) verwaltet. Das Service
`at.gepardec.dojo.zeiten.ZeitenService` stellt sie bereit. Eine Versicherungszeit ist aktiv, wenn
das bis-Datum `null` ist.

### Testfälle
   * Kurt hat Anspruch.

## Iteration 5: Mitversicherung für Kinder
Ergänzung der Regeln für den Anspruch: Kinder unter 18 Jahren haben Anspruch, wenn mindestens ein
Elternteil einen Eigenanspruch hat.

Das Geburtsdatum kommt aus `at.gepardec.dojo.personen.PersonenService` (Applikation ZPV), die
Verwandtschaftsbeziehung aus `at.gepardec.dojo.angehoerige.AngehoerigeService`. Die Eigenschaft
`AngehoerigenBeziehung.angehoerigerTyp` muss `ANG_TYP_ELTERNTEIL` sein.

### Testfälle
   * Angie hat Anspruch. Im UseCase Anspruch Web Check kann sie ihren Anspruch prüfen.

## Umsetzung auf diesem Branch (feature/onion)
Zielvariante ist **Onion Architecture (Palermo)**: Die Ports liegen im Domänenring
(`anspruch.domain.port`), die Anspruchsregeln in `anspruch.domain.rule` nutzen sie direkt.
Jede Anspruchsart ist eine eigene `Anspruch`-Implementierung -- eine weitere Art hinzuzufügen
ändert keine bestehende Klasse.

Der Stichtag wird der Domäne übergeben und nicht aus der Systemuhr gelesen. Die Architekturregeln
stehen als ausführbarer Test in `ArchitectureRulesTest`.

## Iteration 7: Teststabilität
Architekturvorgabe: Das aktuelle Datum muss für Tests vorgegeben werden können; Testwert ist der
5.8.2026.

### Umsetzung auf feature/onion
Neuer Port `anspruch.domain.port.ZeitPort` mit ausschließlich lesender Operation, dazu
`SystemZeitAdapter` (Regelbetrieb) und `FixerZeitAdapter` (Tests). Kein Umschalten über globalen
Zustand. Der Domänenring blieb unverändert, weil er den Stichtag von Anfang an als Parameter
bekommt.

## Iteration 6: Regelwerk
Architekturvorgabe: Konstanten wie die Altersgrenze für Kinder sind Teil eines umfangreichen
Regelwerks, das die Fachabteilung ändern können muss. Die Altersgrenze wird aus einer Datenbank
gelesen.

### Umsetzung auf feature/onion
Neuer Port `anspruch.domain.port.RegelwerkPort`; `KindAnspruch` liest die Grenze selbst.
Fehlt ein Parameter, bricht die Prüfung ab -- kein stiller Rückfall auf einen Wert im Code.
Berührt wurden ausschließlich `anspruch.domain` und `anspruch.infrastructure`; die
Application-Schicht und der AU-Kontext blieben unverändert.
