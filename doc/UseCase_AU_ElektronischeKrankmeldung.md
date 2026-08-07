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
   * Sozialversicherung: Möchte AU-Daten in elektronischer Form erhalten, um weniger Arbeitsaufwand zu haben.

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
      1. Das System speichert eine entsprechende Fehlermeldung in einer Error-Queue.
      2. Ein Sachbearbeiter bearbeitet in einem getrennten UseCase "Fehler bearbeiten" den Fehler.

## Iteration 2: Geschäftsregel AUM speichern
   * Alle AU-Meldungen sind gültig.

### Testfälle
   * Kurt geht am 6.8.2026 zum Arzt. Die AU-Meldung wird gespeichert.

## Iteration 2: Hintergrund
Arbeitsunfähigkeit (AU) ist in der SV ein Bereich des Leistungswesens. Technisch wird es von der
Applikation Leistung abgearbeitet.

## Iteration 4: AU-Fälle nur bei aktivem Anspruch speichern
Ergänzung der Regeln für Arbeitsunfähigkeit: Nur wenn eine Person einen aktiven Anspruch hat, ist
die AU-Meldung gültig und darf gespeichert werden.

### Testfälle
   * Eberhard geht zum Arzt. Die elektronische Krankmeldung wird nicht gespeichert.

## Umsetzung auf diesem Branch (feature/onion)
Der AU-Kontext ist ein vollständiges Hexagon: eigener Domänenring mit `AuMeldungService`, eigene
Ports (`AuMeldungPort`, `AnspruchPruefungPort`), eigene Adapter.

Die Anspruchsprüfung läuft über einen **eigenen Port des AU-Kontexts**, den
`au.infrastructure.AnspruchPruefungAdapter` auf den Anspruchskontext abbildet -- ein Anti-Corruption
Layer. Die Architekturregel `auKenntAnspruchNurAusDerInfrastruktur` sichert das ab.

Die Verarbeitung liefert ein `MeldungsErgebnis` (gespeichert / abgelehnt mit Grund) statt `void`.
Erst dadurch wäre Nebenszenario 1 anschließbar; die Error-Queue selbst ist nicht umgesetzt.