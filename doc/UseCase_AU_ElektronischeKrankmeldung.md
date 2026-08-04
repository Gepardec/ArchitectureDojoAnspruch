Use Case Elektronische Krankmeldung
===================================

Dies ist ein Use Case aus dem Leistungswesen der Sozialversicherung im Bereich Arbeitsunfähigkeit (AU).

   * Use-Case-ID: UC-AU-01
   * Name: Elektronische Krankmeldung
   * Ziel: Ein Arzt kann einen Versicherten mit Hilfe seiner Arztsoftware krank melden.
   * Ebene: User Goal
   * Primärer Akteur: Arzt 

## Stakeholder und Interessen
* Benutzer: Möchte im Krankheitsfall keine bürokratischen Aufgaben erledigen müssen.
* Arzt: Möchte dem Benutzer ein gutes und einfaches Service bieten.
* Sozialversicherung: Möchte AU-Daten in elektronischer Form erhalten um weniger Arbeitsaufwand zu haben.

## Vorbedingungen
* Arzt hat Software, die eAUM-fähig ist.
* Technische Verbindung mittels Messaging zwischen Arzt und SV ist vorhanden.
* Der Arzt hat den Patienten untersucht und will ihn krankschreiben.

## Hauptszenario
1. Der Arzt gibt das Datum der Krankmeldung und die SVNR des Patienten in die Arztsoftware ein.
2. Das System sendet die Daten (AU-Beginn, SVNR) an die Sozialversicherung.
3. Die Sozialversicherung speichert die Daten in der Datenbank.

## Nebenszenario 1
3. Wenn die AU-Meldung ungültig ist:
    1. Das System speichert eine ensprechende Fehlermeldung in einer Error-Queue.
    2. Ein Sachbearbeiter bearbeitet in einem getrennen UseCase "Fehler bearbeiten" den Fehler.
