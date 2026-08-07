Geschäftsregeln Bereich Anspruch
================================

Dieses Dokument beschreibt die Geschäftsregeln für den Bereich Anspruch aus dem Leistungswesen der Sozialversicherung. Die Regeln entsprechen nicht den reellen Geschäftsregeln, sonden sind für das Dojo vereinfacht und angepasst.

## Iteration 1
   * Der Benutzer hat keinen Anspruch.

## Iteration 3: Eigenanspruch
   * Wer zum Stichtag eine aktive Versicherungszeit hat, hat Anspruch (Eigenanspruch).
   * Eine Versicherungszeit ist aktiv, wenn ihr bis-Datum leer ist.

## Iteration 5: Mitversicherung für Kinder
   * Kinder unter 18 Jahren haben Anspruch, wenn mindestens ein Elternteil einen Eigenanspruch hat.
   * "Unter 18" heißt: Am Tag des 18. Geburtstags endet der Kindanspruch.
   * Beim Elternteil zählt ausschließlich der Eigenanspruch, nicht ein eigener Kindanspruch.

## Iteration 7: Stichtag
   * Die Anspruchsprüfung erfolgt zu einem Stichtag. Im Regelbetrieb ist das der heutige Tag.
   * Für Tests ist der Stichtag vorgebbar; verwendet wird der 5.8.2026.

## Iteration 6: Regelwerk
   * Die Altersgrenze der Mitversicherung ist kein fester Wert im Programm, sondern ein
     Regelparameter, den die Fachabteilung pflegt.
   * Fachlicher Vorgabewert: 18 Jahre.
   * Fehlt der Parameter, bricht die Anspruchsprüfung mit einem Fehler ab.
