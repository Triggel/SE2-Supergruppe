package de.uni_hamburg.informatik.swt.se2.mediathek.materialien;

import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;

/**
 * Mit Hilfe von Vormerkkarte werden beim Vormerken eines Mediums alle relevanten
 * Daten notiert.
 * 
 * Sie beantwortet die folgenden Fragen: Welches Medium wurde vorgemerkt? Wer
 * ist erster, zweiter und dritter Vormerker des Mediums?
 * 
 * Bei Änderungen wird jeweils eine neue Vormerkkarte erstellt.
 * Um die Verwaltung der Karten kümmert sich der VerleihService
 * 
 * @author Übungsgruppe
 * @version 20. Mai 2015
 */
public class Vormerkkarte
{
    //Eigentschaften der Vormerkkarte
    private Medium _medium;
    private Kunde _vormerker1;
    private Kunde _vormerker2;
    private Kunde _vormerker3;

    /**
     * Initialisert eine neue Vormerkkarte mit den gegebenen Daten.
     * 
     * @param medium Ein vorgemerktes Medium.
     * @param vormerker1 Der erste Vormerker des Mediums.
     * @param vormerker2 Der zweite Vormerker des Mediums.
     * @param vormerker3 Der dritte Vormerker des Mediums.
     * 
     * @require medium != null
     * 
     * @ensure #getMedium() == medium
     * @ensure #getVormerker1() == vormerker1
     * @ensure #getVormerker2() == vormerker2
     * @ensure #getVormerker3() == vormerker3
     */
    public Vormerkkarte(Medium medium, Kunde vormerker1, Kunde vormerker2,
            Kunde vormerker3)
    {
        assert medium != null : "Vorbedingung verletzt: medium != null";

        _medium = medium;
        _vormerker1 = vormerker1;
        _vormerker2 = vormerker2;
        _vormerker3 = vormerker3;
    }

    /**
     * Gibt das vorgemerkte Medium zurück.
     * 
     * @return Das vorgemerkte Medium
     * 
     * @ensure result != null
     */
    public Medium getMedium()
    {
        return _medium;
    }

    /**
     * Gibt den ersten Vormerker des Mediums zurück.
     * 
     * @return Der erste Vormerker
     * 
     * @ensure result != null
     */
    public Kunde getVormerker1()
    {
        return _vormerker1;
    }

    /**
     * Gibt den zweiten Vormerker des Mediums zurück.
     * 
     * @return Der zweite Vormerker
     * 
     * @ensure result != null
     */
    public Kunde getVormerker2()
    {
        return _vormerker2;
    }

    /**
     * Gibt den dritten Vormerker des Mediums zurück.
     * 
     * @return Der dritte Vormerker
     * 
     * @ensure result != null
     */
    public Kunde getVormerker3()
    {
        return _vormerker3;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_medium == null) ? 0 : _medium.hashCode());
        result = prime * result
                + ((_vormerker1 == null) ? 0 : _vormerker1.hashCode());
        result = prime * result
                + ((_vormerker2 == null) ? 0 : _vormerker2.hashCode());
        result = prime * result
                + ((_vormerker3 == null) ? 0 : _vormerker3.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof Vormerkkarte)
        {
            Vormerkkarte other = (Vormerkkarte) obj;

            if (other.getMedium()
                .equals(_medium) && other.getVormerker1()
                .equals(_vormerker1) && other.getVormerker2()
                .equals(_vormerker2) && other.getVormerker3()
                .equals(_vormerker3))
            {
                result = true;
            }
        }
        return result;
    }
}
