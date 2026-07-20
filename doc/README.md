Anleitung Architektur-Dojo Anspruch
===================================

Dieses Dokument enthält eine Beschreibung über das Vorgehen im Dojo und Informationen zum Hintergrund.

## Hintergrund
Das Dojo dient zur Erprobung unterschiedlicher Architektur bzw. Designs, wie z.B. unterschiedlicher Ansätze von 
Hexagonaler Architektur. Es soll daher in erster Linie auf Paketstrukturen und deren Abhängigkeiten geachtet werden
und nicht so sehr auf Details der Implementierung wie ExceptionHandling oder Edge-Cases.

Als fachliches Beispiel dienen UseCases aus dem Bereich der österreichischen Sozialversicherung, die alltägliche Anwendungsfälle
darstellen. Die Regeln sind allerdings gegenüber der tatsächlichen Implementierung stark vereinfacht bzw. abgeändert.

## Rollen im Dojo

   * Sensei (Product Owner): führt durch die Fachlichkeit. Kennt die gesamte Aufgabe
   * Implementierer Gruppen: Eine oder mehrere Gruppen die im abwechselnden Pair-Programming die vom Sensei gestellten Aufgaben 
       implementieren. Jede Gruppe arbeitet an einer Lösungsversion.
   * Beobachter: Beobachtet die Implementierer und kann technische Fragen stellen oder Hilfestellung geben. 
       Kennt die gesamte Aufgabe.

## Vorgehen

1. Jede Gruppe checkt den Basiscode aus und kompiliert ihn. 
2. Der Sensei stellt den Rahmen der Fachlichkeit und die Testfälle 
[testcases.md](testcases.md) vor. 
3. Dann werden in mehreren Iterationen Usecases und Geschäftsregeln eingeführt bzw. erweitert.
Die Änderungen werden in die jeweiligen Dateien [UseCase_AnspruchWebCheck.md](UseCase_AnspruchWebCheck.md), 
[GeschäftsRegeln_Anspruch.md](GeschäftsRegeln_Anspruch.md),... eingefügt. 
4. Dann werden die Anforderungen implementiert. 
5. Am Ende jeder Iteration werden alle angepassten Dateien committed. 

Dieses Vorgehen dient dazu, dass später nachvollzogen werden kann,
welche Dateien bzw. Klassen oder Pakete für welche Änderungen der Anforderungen angepasst werden mussten.

## Template Projekt
Das Projekt ist als Maven Multi-Module Projekt angelegt. In `dojo-leistung` soll die Lösung implementiert werden.

In `dojo-external-domain` sind Hilfsklassen bzw. Implementierungen für externe Services die für die Testfälle
passende Daten liefern. In diesem Modul soll nichts geändert werden.


