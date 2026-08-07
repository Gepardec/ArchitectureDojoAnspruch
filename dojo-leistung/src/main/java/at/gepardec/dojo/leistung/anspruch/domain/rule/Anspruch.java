package at.gepardec.dojo.leistung.anspruch.domain.rule;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;

/**
 * Eine einzelne Anspruchsregel.
 * <p>
 * Jede Anspruchsart der Geschäftsregeln ist eine eigene Implementierung. Eine weitere Art
 * hinzuzufügen bedeutet daher, eine Klasse zu ergänzen -- nicht, eine bestehende zu ändern.
 * <p>
 * Der Stichtag wird übergeben und NICHT aus der Systemuhr gelesen. Nur so ist die Fachlogik zu
 * einem beliebigen Datum prüfbar.
 */
public interface Anspruch {

    boolean anspruch(Svnr svnr, LocalDate stichtag);
}
