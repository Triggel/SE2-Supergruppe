package de.uni_hamburg.informatik.swt.se2.mediathek.materialien;

import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;

public class Vormerkkarte
{
	private Kunde _vormerker1 = null;
	private Kunde _vormerker2 = null;
	private Kunde _vormerker3 = null;
    private final Medium _medium;

    public Vormerkkarte(Kunde vormerker, Medium medium)
    {
    	addVormerker(vormerker);
        _medium = medium;
    }

    public void addVormerker(Kunde vormerker)
    {
    	if(_vormerker1 == null) {
    		_vormerker1 = vormerker;
    	}
    	else if (_vormerker2 == null)
    	{
    		_vormerker2 = vormerker;
    	}
    	else if (_vormerker3 == null)
    	{
    		_vormerker3 = vormerker;
    	}
    	else
    	{
    		// TODO Till schönerer Fehler?
    		System.out.println("Vormerken nicht möglich");
    	}
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

    public String getFormatiertenString()
    {
        return _medium.getFormatiertenString() + " vorgemerkt von\n"
                + _vormerker1.getFormatiertenString() + ", "
                + _vormerker1.getFormatiertenString() + ", "
                + _vormerker1.getFormatiertenString();
    }

    public Medium getMedium()
    {
        return _medium;
    }

	public void rueckeAuf() {
		_vormerker1 = _vormerker2;
		_vormerker2 = _vormerker3;
	}

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((_vormerker1 == null) ? 0 : _vormerker1.hashCode());
        result = prime * result
                + ((_vormerker2 == null) ? 0 : _vormerker2.hashCode());
        result = prime * result
                + ((_vormerker3 == null) ? 0 : _vormerker3.hashCode());
        result = prime * result + ((_medium == null) ? 0 : _medium.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof Vormerkkarte)
        {
            Vormerkkarte other = (Vormerkkarte) obj;

            if (other.getVormerker1().equals(_vormerker1)
                    && other.getVormerker2().equals(_vormerker2)
                    && other.getVormerker3().equals(_vormerker3)
                    && other.getMedium().equals(_medium))

                result = true;
        }
        return result;
    }

    @Override
    public String toString()
    {
        return getFormatiertenString();
    }
}
