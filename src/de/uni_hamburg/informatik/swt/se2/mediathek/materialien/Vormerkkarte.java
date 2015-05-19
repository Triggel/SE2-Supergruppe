package de.uni_hamburg.informatik.swt.se2.mediathek.materialien;

import de.uni_hamburg.informatik.swt.se2.mediathek.fachwerte.Kundennummer;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;

public class Vormerkkarte
{
    private Medium _medium;
    private Kunde _vormerker1;
    private Kunde _vormerker2;
    private Kunde _vormerker3;
    private final Kunde _nullKunde;

    public Vormerkkarte(Kunde vormerker, Medium medium)
    {
        assert vormerker != null : "Vorbedingung verletzt: vormerker != null";
        assert medium != null : "Vorbedingung verletzt: medium != null";
        _medium = medium;
        _nullKunde = new Kunde(new Kundennummer(777777), "", "");
        _vormerker1 = _nullKunde;
        _vormerker2 = _nullKunde;
        _vormerker3 = _nullKunde;
    }
    
    public boolean vormerkenMoeglich (Medium medium)
    {
            return (_vormerker1.equals(_nullKunde)) && (_vormerker2.equals(_nullKunde)) && (_vormerker3.equals(_nullKunde));
    }
    
    public void setVormerker(Kunde vormerker, Medium medium)
    {
        if (_vormerker1.equals(_nullKunde))
        {
            _vormerker1 = vormerker;
        }
        else if (_vormerker2.equals(_nullKunde))
        {
            _vormerker2 = vormerker;
        }
        else
        {
            _vormerker3 = vormerker;
        }
    }
    
    public void entferneVormerker1(Kunde vormerker)
    {
        _vormerker1 = _vormerker2;
        _vormerker2 = _vormerker3;
        _vormerker3 = _nullKunde;
    }
    
    public void entferneVormerker(Kunde vormerker)
    {
        if (vormerker.equals(_vormerker3))
        {
            _vormerker3 = _nullKunde;
        }
        else if (vormerker.equals(_vormerker2))
        {
            _vormerker2 = _vormerker3;
            _vormerker3 = _nullKunde;
        }
        else
        {
            entferneVormerker1(vormerker);
        }
    }
    
    public Medium getMedium()
    {
        return _medium;
    }
    
    public Kunde getVormerker1()
    {
        return _vormerker1;
    }
    
    public Kunde getVormerker2()
    {
        return _vormerker2;
    }
    
    public Kunde getVormerker3()
    {
        return _vormerker3;
    }
    
    public Kunde getNullKunde()
    {
        return _nullKunde;
    }
}
