Iterationen
===========
Diese Datei wird nur vom Sensei verwendet. Es werden darin die Iterationen der Aufgaben beschrieben.

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

# Iteration 2: Eigenanspruch bei aktiver Versicherungszeit

## Geschäftsregel
Ergänzung der Regeln für den Anspruch:
* Wenn eine Person eine aktive Versicherungszeit hat, dann hat sie Anspruch. Das ist z.B. durch eine unselbstständige
  Beschäftigung gegeben. Ein Anspruch aus dieser Regel heißt Eigenanspruch.

Versicherungszeiten werden im Versicherungswesen (Applikation MVB) verwaltet.
Das Service `at.gepardec.dojo.zeiten.ZeitenService` stellt die Versicherungszeiten bereit.
Eine Versicherungszeit ist aktiv, wenn das `bis`-Datum `null` ist.

## Testfälle
* Kurt hat Anspruch

# Iteration 3: UseCase Elektronische Arbeitsunfähigkeit

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
 
## Iteration 3: Geschäftsregel AUM speichern
   * Alle AU-Meldungen sind gültig 

## Iteration 3: Testfälle
   * Kurt geht am 6.8.2026 zum Arzt. Die AU-Meldung wird gespeichert.

## Iteration 3: Hintergrund
Krankmeldungen bzw. Arbeitsunfähiglkeit (AU) ist in der SV ein Bereich des Leistungswesen. 
Technisch wird es von der Applikation Leistung abgearbeitet.

# Iteration 4: AU-Fälle dürfen nur bei aktiven Anspruch gespeichert werden

## Geschäftsregel
Ergänzung der Regeln für Arbeitsunfähigkeit:
   * Nur wenn eine Person einen aktiven Anspruch hat, dann ist die AU-Meldung gültig und darf damit gespeichert werden.

## Testfälle
   * Eberhard geht zum Arzt. Die elektronische Krankmeldung wird nicht gespeichert.


# Iteration 5: Mitversicherung für Kinder

## Geschäftsregel
Ergänzung der Regeln für den Anspruch:
   * Kinder unter 18 Jahren haben Anspruch, wenn mindestens ein Elternteil einen Eigenanspruch hat. 
     D.h. Ein Elternteil hat eine aktive Versicherungszeit.

Das Geburtsdatum aus dem Service `at.gepardec.dojo.personen.PersonenService` (Applikation ZPV) erhalten.
Die Verwandschaftsbeziehung wird aus dem Service `at.gepardec.dojo.angehoerige.AngehoerigeService` erhalten.
Die Eigenschaft `at.gepardec.dojo.angehoerige.AngehoerigenBeziehung.angehoerigerTyp` muss 
`at.gepardec.dojo.angehoerige.AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL`sein.

## Testfälle
   * Angie hat Anspruch. Im UseCase _Anspruch Web Check_ kann sie ihren Anspruch prüfen.

# Iteration 6: Regelwerk 

## Architekturvorgabe
Konstanten wie Altersgrenze für Kinder (18 Jahre) sind Teil eines umfangreichen Regelwerks für die Anspruchprüfung. Dieses soll
durch die Fachabteilung geändert werden können.
   * Die Altersgrenze 18 soll aus einer Datenbank ausgelesen werden. 

# Iteration 7: Teststabilität

## Architekturvorgabe
Die Testfälle sollen in ein paar Jahren auch noch gehen. Dazu muss das aktuelle Datum (Tagesdatum)
für Tests vorgegeben werden können.
   * Für Tests soll das aktuelle Datum mit 5.8.2026 vorgegeben werden.



