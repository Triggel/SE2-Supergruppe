package de.uni_hamburg.informatik.swt.se2.mediathek.materialien;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni_hamburg.informatik.swt.se2.mediathek.fachwerte.Kundennummer;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.CD;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;

public class VormerkkarteTest
{

    private Vormerkkarte _karte;
    private Medium _medium;
    private Kunde _vormerker1;
    private Kunde _vormerker2;
    private Kunde _vormerker3;

    public VormerkkarteTest()
    {
        _vormerker1 = new Kunde(new Kundennummer(123456), "Vormerker", "Eins");
        _vormerker2 = new Kunde(new Kundennummer(123457), "Vormerker", "Zwei");
        _vormerker3 = new Kunde(new Kundennummer(123458), "Vormerker", "Drei");

        _medium = new CD("bar", "baz", "foo", 123);
        _karte = new Vormerkkarte(_medium, _vormerker1, _vormerker2, _vormerker3);
    }

    @Test
    public void testeKonstruktor() throws Exception
    {
        assertEquals(_medium, _karte.getMedium());
        assertEquals(_vormerker1, _karte.getVormerker1());
        assertEquals(_vormerker2, _karte.getVormerker2());
        assertEquals(_vormerker3, _karte.getVormerker3());
    }
    
    @Test
    public void testEquals()
    {
        Vormerkkarte karte1 = new Vormerkkarte(_medium, _vormerker1, _vormerker2, _vormerker3);

        assertTrue(_karte.equals(karte1));
        assertEquals(_karte.hashCode(), karte1.hashCode());

        Kunde testVormerker1 = new Kunde(new Kundennummer(654321), "Klaus", "Müller");
        Kunde testVormerker2 = new Kunde(new Kundennummer(654322), "Hans", "Müller");
        Kunde testVormerker3 = new Kunde(new Kundennummer(654323), "Peter", "Müller");
        CD medium2 = new CD("hallo", "welt", "foo", 321);
        Vormerkkarte karte2 = new Vormerkkarte(medium2, testVormerker1, testVormerker2, testVormerker3);

        assertFalse(_karte.equals(karte2));
        assertNotSame(_karte.hashCode(), karte2.hashCode());
    }
}
