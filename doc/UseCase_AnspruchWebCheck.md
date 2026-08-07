Use Case Anspruch Web Check
===========================

Dies ist ein Use Case aus dem Leistungswesen der Sozialversicherung im Bereich Anspruch (ANSP).

* Use-Case-ID: UC-ANSP-01
* Name: Anspruch Web Check
* Ziel: Ein Kunde kann selbst über Internet überprüfen, ober er/sie Anspruch auf Versicherungsleistungen hat.
* Ebene: User Goal
* Primärer Akteur: Kunde

# Iteration 1: UseCase Anspruch Web Check

## Stakeholder und Interessen
 * Benutzer: Möchte wissen, ob er Leistungen beziehen kann, z.B. kostenlos zum Arzt gehen kann.
   * Sozialversicherung: Möchte Auskunft geben, ohne Arbeitszeit von Mitarbeitern zu benötigen.

## Vorbedingungen
   * Der Benutzer besitzt ein aktives Benutzerkonto.
   * Der Benutzer ist am System angemeldet.

## Hauptszenario
   1. Das System präsentiert die Übersichtsseite.
   2. Der Benutzer wählt den Link "Anspruch prüfen"
   3. Das System präsentiert die "Anspruch prüfen" Maske.
   4. Der Benutzer gibt seine Sozialversicherungsnummern im Formular ein und drückt den Button "Prüfen".
   6. Das System zeigt "Anspruch vorhanden" oder "Kein Anspruch" an, je nachdem ob der Benutzer Anspruch hat.

## Iteration 1: Geschäftsregel Anspruch

   * Der Benutzer hat keinen Anspruch.

## Iteration 1: Testfälle

   * Eberhard hat keinen Anspruch

## Iteration 1: Hintergrund
Anspruchsprüfung ist in der SV Bereich des Leistungswesen. Technisch wird es von der Applikation Leistung abgearbeitet.


## Iteration 2: Eigenanspruch bei aktiver Versicherungszeit

## Geschäftsregel
Ergänzung der Regeln für den Anspruch:
Wenn eine Person eine aktive Versicherungszeit hat, dann hat sie Anspruch. Das ist z.B. durch eine unselbstständige
Beschäftigung gegeben. Ein Anspruch aus dieser Regel heißt Eigenanspruch.
Versicherungszeiten werden im Versicherungswesen (Applikation MVB) verwaltet.
Das Service at.gepardec.dojo.zeiten.ZeitenService stellt die Versicherungszeiten bereit.
Eine Versicherungszeit ist aktiv, wenn das bis-Datum null ist.

## Testfälle
Kurt hat Anspruch