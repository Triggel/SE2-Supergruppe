package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Vormerkkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.AbstractObservableService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.medienbestand.MedienbestandService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih.VerleihService;

/**
 * Diese Klasse implementiert das Interface VormerkService. Siehe dortiger
 * Kommentar.
 * 
 * @author SE2-Team
 * @version SoSe 2015
 */
public class VormerkServiceImpl extends AbstractObservableService implements
        VormerkService
{
    private Map<Medium, Vormerkkarte> _vormerkkarten;

    private MedienbestandService _medienbestand;

    private KundenstammService _kundenstamm;
    
    private VerleihService _verleihService;

    public VormerkServiceImpl(KundenstammService kundenstamm,
            MedienbestandService medienbestand,
            List<Vormerkkarte> initialBestand, VerleihService verleihService)
    {
    	_vormerkkarten = erzeugeVormerkkartenBestand(initialBestand);
        _kundenstamm = kundenstamm;
        _medienbestand = medienbestand;
        _verleihService = verleihService;
    }

    private HashMap<Medium, Vormerkkarte> erzeugeVormerkkartenBestand(
            List<Vormerkkarte> initialBestand)
    {
        HashMap<Medium, Vormerkkarte> result = new HashMap<Medium, Vormerkkarte>();
        for (Vormerkkarte vormerkkarte : initialBestand)
        {
            result.put(vormerkkarte.getMedium(), vormerkkarte);
        }
        return result;
    }

    @Override
    public List<Vormerkkarte> getVormerkkarten()
    {
        return new ArrayList<Vormerkkarte>(_vormerkkarten.values());
    }

    @Override
    public boolean istVorgemerkt(Medium medium)
    {
        assert mediumImBestand(medium) : "Vorbedingung verletzt: mediumExistiert(medium)";
        return _vormerkkarten.get(medium) != null;
    }

    @Override
    public boolean istVormerkenMoeglich(Kunde kunde, List<Medium> medien)
    {
        assert kundeImBestand(kunde) : "Vorbedingung verletzt: kundeImBestand(kunde)";
        assert medienImBestand(medien) : "Vorbedingung verletzt: medienImBestand(medien)";
        
        for (Medium medium : medien)
        {
        	if(istVerliehenAn(kunde, medium)
        		|| kunde.equals(getVormerker1Fuer(medium))
        		|| kunde.equals(getVormerker2Fuer(medium))
        		|| kunde.equals(getVormerker3Fuer(medium))
        		|| getVormerker3Fuer(medium) != null) {
        		return false;
        	}
        }
        return true;
        //return sindAlleNichtVerliehen(medien);
    }

    @Override
    public boolean sindAlleNichtVerliehen(List<Medium> medien)
    {
        assert medienImBestand(medien) : "Vorbedingung verletzt: medienImBestand(medien)";
        boolean result = true;
        for (Medium medium : medien)
        {
            if (istVerliehen(medium))
            {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean sindAlleVerliehenAn(Kunde kunde, List<Medium> medien)
    {
        assert kundeImBestand(kunde) : "Vorbedingung verletzt: kundeImBestand(kunde)";
        assert medienImBestand(medien) : "Vorbedingung verletzt: medienImBestand(medien)";

        boolean result = true;
        for (Medium medium : medien)
        {
            if (!istVerliehenAn(kunde, medium))
            {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean istVorgemerktFuer(Kunde kunde, Medium medium)
    {
        assert kundeImBestand(kunde) : "Vorbedingung verletzt: kundeImBestand(kunde)";
        assert mediumImBestand(medium) : "Vorbedingung verletzt: mediumImBestand(medium)";

        if(istVerliehen(medium) &&
        		(getVormerker1Fuer(medium).equals(kunde) ||
				 getVormerker2Fuer(medium).equals(kunde) ||
				 getVormerker3Fuer(medium).equals(kunde))) {
        	return true;
        }
        else
        {
        	return false;
        }
    }

    @Override
    public boolean sindAlleVerliehen(List<Medium> medien)
    {
        assert medienImBestand(medien) : "Vorbedingung verletzt: medienImBestand(medien)";

        boolean result = true;
        for (Medium medium : medien)
        {
            if (!istVerliehen(medium))
            {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean kundeImBestand(Kunde kunde)
    {
        return _kundenstamm.enthaeltKunden(kunde);
    }

    @Override
    public boolean mediumImBestand(Medium medium)
    {
        return _medienbestand.enthaeltMedium(medium);
    }

    @Override
    public boolean medienImBestand(List<Medium> medien)
    {
        assert medien != null : "Vorbedingung verletzt: medien != null";
        assert !medien.isEmpty() : "Vorbedingung verletzt: !medien.isEmpty()";

        boolean result = true;
        for (Medium medium : medien)
        {
            if (!mediumImBestand(medium))
            {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public Kunde getVormerker1Fuer(Medium medium)
    {
        Vormerkkarte vormerkkarte = _vormerkkarten.get(medium);
        return vormerkkarte.getVormerker1();
    }

    @Override
    public Kunde getVormerker2Fuer(Medium medium)
    {
        Vormerkkarte vormerkkarte = _vormerkkarten.get(medium);
        return vormerkkarte.getVormerker2();
    }

    @Override
    public Kunde getVormerker3Fuer(Medium medium)
    {
        Vormerkkarte vormerkkarte = _vormerkkarten.get(medium);
        return vormerkkarte.getVormerker3();
    }

    @Override
    public Vormerkkarte getVormerkkarteFuer(Medium medium)
    {
        return _vormerkkarten.get(medium);
    }

    @Override
    public List<Vormerkkarte> getVormerkkartenFuer(Kunde kunde)
    {
        assert kundeImBestand(kunde) : "Vorbedingung verletzt: kundeImBestand(kunde)";
        List<Vormerkkarte> result = new ArrayList<Vormerkkarte>();
        for (Vormerkkarte vormerkkarte : _vormerkkarten.values())
        {
            if (vormerkkarte.getVormerker1().equals(kunde)
        		|| vormerkkarte.getVormerker2().equals(kunde)
        		|| vormerkkarte.getVormerker3().equals(kunde))
            {
                result.add(vormerkkarte);
            }
        }
        return result;
    }

	@Override
	public void merkeVor(Kunde kunde, List<Medium> medien) {
        for (Medium medium : medien)
        {
        	Vormerkkarte vormerkkarte = getVormerkkarteFuer(medium);
        	if(vormerkkarte == null)
        	{
        		vormerkkarte = new Vormerkkarte(kunde, medium);
                _vormerkkarten.put(medium, vormerkkarte);
        	}
        	else
        	{
        		vormerkkarte.addVormerker(kunde);
        	}
        }
        // XXX Was passiert wenn das Protokollieren mitten in der Schleife
        // schief geht? informiereUeberAenderung in einen finally Block?
        informiereUeberAenderung();
	}

	@Override
	public void rueckeAuf(List<Medium> medien) {
		for (Medium medium : medien)
        {
        	Vormerkkarte vormerkkarte = getVormerkkarteFuer(medium);
        	if(vormerkkarte != null)
        	{
        		vormerkkarte.rueckeAuf();
                informiereUeberAenderung();
        	}
        }
	}

	@Override
	public boolean istVerliehen(Medium medium) {
		if(_verleihService.getVerleihkarteFuer(medium) == null)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean istVerliehenAn(Kunde kunde, Medium medium) {
		if(_verleihService.getVerleihkarteFuer(medium) != null)
		{
			return kunde.equals(_verleihService.getVerleihkarteFuer(medium).getEntleiher());
		}
		return false;
	}

	@Override
	public boolean istKundeErsterVormerker(Kunde kunde, List<Medium> medien) {
		for(Medium medium : medien)
		{
			if(!kunde.equals(getVormerker1Fuer(medium)))
			{
				if(getVormerker1Fuer(medium) == null) {
					return true;
				}
				return false;
			}
		}
		return true;
	}

}
