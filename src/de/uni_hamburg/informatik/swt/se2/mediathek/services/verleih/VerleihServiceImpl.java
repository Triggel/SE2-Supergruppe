package de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_hamburg.informatik.swt.se2.mediathek.fachwerte.Datum;
import de.uni_hamburg.informatik.swt.se2.mediathek.fachwerte.Kundennummer;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Verleihkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Vormerkkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.AbstractObservableService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.medienbestand.MedienbestandService;

/**
 * Diese Klasse implementiert das Interface VerleihService. Siehe dortiger
 * Kommentar.
 * 
 * @author SE2-Team
 * @version SoSe 2015
 */
public class VerleihServiceImpl extends AbstractObservableService implements
        VerleihService
{
    /**
     * Diese Map speichert für jedes eingefügte Medium die dazugehörige
     * Verleihkarte. Ein Zugriff auf die Verleihkarte ist dadurch leicht über
     * die Angabe des Mediums möglich. Beispiel: _verleihkarten.get(medium)
     */
    private Map<Medium, Verleihkarte> _verleihkarten;

    /**
     * Diese Map speichert für jedes eingefügte Medium die dazugehörige
     * Vormerkkarte. Ein Zugriff auf die Vormerkkarte ist dadurch leicht über
     * die Angabe des Mediums möglich. Beispiel: _vortmerkkarten.get(medium)
     */
    private Map<Medium, Vormerkkarte> _vormerkkarten;

    /**
     * Der Medienbestand.
     */
    private MedienbestandService _medienbestand;

    /**
     * Der Kundenstamm.
     */
    private KundenstammService _kundenstamm;

    /**
     * Der Protokollierer für die Verleihvorgänge.
     */
    private VerleihProtokollierer _protokollierer;

    /**
     * Ein "NullKunde", der für "niemand", "N/A" o.ä. steht und dazu dient,
     * ein leeres Feld in der Tabelle zu schaffen.
     */
    private final Kunde _nullKunde;

    /**
     * Konstruktor. Erzeugt einen neuen VerleihServiceImpl.
     * 
     * @param kundenstamm Der KundenstammService.
     * @param medienbestand Der MedienbestandService.
     * @param initialBestand Der initiale Bestand.
     * 
     * @require kundenstamm != null
     * @require medienbestand != null
     * @require initialBestand != null
     */
    public VerleihServiceImpl(KundenstammService kundenstamm,
            MedienbestandService medienbestand,
            List<Verleihkarte> initialBestand)
    {
        assert kundenstamm != null : "Vorbedingung verletzt: kundenstamm  != null";
        assert medienbestand != null : "Vorbedingung verletzt: medienbestand  != null";
        assert initialBestand != null : "Vorbedingung verletzt: initialBestand  != null";
        _nullKunde = new Kunde(new Kundennummer(999999), "", "");
        _verleihkarten = erzeugeVerleihkartenBestand(initialBestand);
        _vormerkkarten = erzeugeVormerkkartenBestand(medienbestand.getMedien());
        _kundenstamm = kundenstamm;
        _medienbestand = medienbestand;
        _protokollierer = new VerleihProtokollierer();
    }

    /**
     * Erzeugt eine neue HashMap aus dem Initialbestand.
     */
    private HashMap<Medium, Verleihkarte> erzeugeVerleihkartenBestand(
            List<Verleihkarte> initialBestand)
    {
        HashMap<Medium, Verleihkarte> result = new HashMap<Medium, Verleihkarte>();
        for (Verleihkarte verleihkarte : initialBestand)
        {
            result.put(verleihkarte.getMedium(), verleihkarte);
        }
        return result;
    }

    /**
     * Erzeugt eine neue HashMap zur Speicherung eines Mediums in Verbindung mit deren Vormerkkarte.
     */
    private HashMap<Medium, Vormerkkarte> erzeugeVormerkkartenBestand(
            List<Medium> medienbestand)
    {
        HashMap<Medium, Vormerkkarte> result = new HashMap<Medium, Vormerkkarte>();
        for (Medium medium : medienbestand)
        {
            result.put(medium, new Vormerkkarte(medium, _nullKunde, _nullKunde,
                    _nullKunde));
        }
        return result;
    }

    @Override
    public List<Verleihkarte> getVerleihkarten()
    {
        return new ArrayList<Verleihkarte>(_verleihkarten.values());
    }

    @Override
    public boolean istVerliehen(Medium medium)
    {
        assert mediumImBestand(medium) : "Vorbedingung verletzt: mediumExistiert(medium)";
        return _verleihkarten.get(medium) != null;
    }

    @Override
    public boolean istVerleihenMoeglich(Kunde kunde, List<Medium> medien)
    {
        assert kundeImBestand(kunde) : "Vorbedingung verletzt: kundeImBestand(kunde)";
        assert medienImBestand(medien) : "Vorbedingung verletzt: medienImBestand(medien)";

        return sindAlleNichtVerliehen(medien)
                && (kundeIstErsterVormerkerAlle(medien, kunde) || keineVormerkungAlle(medien));
    }

    @Override
    public void nimmZurueck(List<Medium> medien, Datum rueckgabeDatum)
            throws ProtokollierException
    {
        assert sindAlleVerliehen(medien) : "Vorbedingung verletzt: sindAlleVerliehen(medien)";
        assert rueckgabeDatum != null : "Vorbedingung verletzt: rueckgabeDatum != null";

        for (Medium medium : medien)
        {
            Verleihkarte verleihkarte = _verleihkarten.get(medium);
            _verleihkarten.remove(medium);
            _protokollierer.protokolliere(
                    VerleihProtokollierer.EREIGNIS_RUECKGABE, verleihkarte);
        }

        informiereUeberAenderung();
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
    public boolean istVerliehenAn(Kunde kunde, Medium medium)
    {
        assert kundeImBestand(kunde) : "Vorbedingung verletzt: kundeImBestand(kunde)";
        assert mediumImBestand(medium) : "Vorbedingung verletzt: mediumImBestand(medium)";

        return istVerliehen(medium) && getEntleiherFuer(medium).equals(kunde);
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
    public void verleiheAn(Kunde kunde, List<Medium> medien, Datum ausleihDatum)
            throws ProtokollierException
    {
        assert kundeImBestand(kunde) : "Vorbedingung verletzt: kundeImBestand(kunde)";
        assert sindAlleNichtVerliehen(medien) : "Vorbedingung verletzt: sindAlleNichtVerliehen(medien) ";
        assert ausleihDatum != null : "Vorbedingung verletzt: ausleihDatum != null";
        assert istVerleihenMoeglich(kunde, medien) : "Vorbedingung verletzt:  istVerleihenMoeglich(kunde, medien)";

        for (Medium medium : medien)
        {
            Verleihkarte verleihkarte = new Verleihkarte(kunde, medium,
                    ausleihDatum);

            _verleihkarten.put(medium, verleihkarte);

            if (kunde.equals(_vormerkkarten.get(medium)
                .getVormerker1()))
            {
                entferneVormerker(medium, kunde);
            }

            _protokollierer.protokolliere(
                    VerleihProtokollierer.EREIGNIS_AUSLEIHE, verleihkarte);
        }
        // XXX Was passiert wenn das Protokollieren mitten in der Schleife
        // schief geht? informiereUeberAenderung in einen finally Block?
        informiereUeberAenderung();
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
        // assert !medien.isEmpty() : "Vorbedingung verletzt: !medien.isEmpty()";

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
    public List<Medium> getAusgelieheneMedienFuer(Kunde kunde)
    {
        assert kundeImBestand(kunde) : "Vorbedingung verletzt: kundeImBestand(kunde)";
        List<Medium> result = new ArrayList<Medium>();
        for (Verleihkarte verleihkarte : _verleihkarten.values())
        {
            if (verleihkarte.getEntleiher()
                .equals(kunde))
            {
                result.add(verleihkarte.getMedium());
            }
        }
        return result;
    }

    @Override
    public Kunde getEntleiherFuer(Medium medium)
    {
        assert istVerliehen(medium) : "Vorbedingung verletzt: istVerliehen(medium)";
        Verleihkarte verleihkarte = _verleihkarten.get(medium);
        return verleihkarte.getEntleiher();
    }

    @Override
    public Verleihkarte getVerleihkarteFuer(Medium medium)
    {
        assert istVerliehen(medium) : "Vorbedingung verletzt: istVerliehen(medium)";
        return _verleihkarten.get(medium);
    }

    @Override
    public List<Verleihkarte> getVerleihkartenFuer(Kunde kunde)
    {
        assert kundeImBestand(kunde) : "Vorbedingung verletzt: kundeImBestand(kunde)";
        List<Verleihkarte> result = new ArrayList<Verleihkarte>();
        for (Verleihkarte verleihkarte : _verleihkarten.values())
        {
            if (verleihkarte.getEntleiher()
                .equals(kunde))
            {
                result.add(verleihkarte);
            }
        }
        return result;
    }

    @Override
    public void merkeMedienVor(List<Medium> medien, Kunde kunde)
    {
        assert medien != null : "Vorbedingung verletzt: kunde  != null";
        assert kunde != null : "Vorbedingung verletzt: kunde  != null";
        assert vormerkenMoeglichAlleFuer(medien, kunde) : "Vorbedingung "
                + "verletzt: !vormerkenMoeglichAlleFuer()";

        for (Medium medium : medien)
        {
            Vormerkkarte vormerkkarteAlt = _vormerkkarten.get(medium);
            if (vormerkkarteAlt.getVormerker1() == _nullKunde)
            {
                _vormerkkarten.put(medium, new Vormerkkarte(medium, kunde,
                        _nullKunde, _nullKunde));
            }
            else
            {
                if (vormerkkarteAlt.getVormerker2() == _nullKunde)
                {
                    _vormerkkarten.put(medium, new Vormerkkarte(medium,
                            vormerkkarteAlt.getVormerker1(), kunde, _nullKunde));
                }
                else
                {
                    _vormerkkarten.put(
                            medium,
                            new Vormerkkarte(medium,
                                    vormerkkarteAlt.getVormerker1(),
                                    vormerkkarteAlt.getVormerker2(), kunde));
                }

            }

        }
        informiereUeberAenderung();
    }

    @Override
    public void storniereVormerkung(List<Medium> medien, Kunde kunde)
    {
        assert medien != null : "Vorbedingung verletzt: kunde  != null";
        assert kunde != null : "Vorbedingung verletzt: kunde  != null";
        //TODO für Zusatzaufgabe: Funktion in die Oberfläche einbinden.
        for (Medium medium : medien)
        {
            entferneVormerker(medium, kunde);
        }
        informiereUeberAenderung();
    }

    @Override
    public Vormerkkarte getVormerkkarteFuer(Medium medium)
    {
        assert medium != null : "Vorbedingung verletzt: kunde  != null";

        return _vormerkkarten.get(medium);
    }

    @Override
    public void entferneVormerker(Medium medium, Kunde kunde)
    {
        assert medium != null : "Vorbedingung verletzt: kunde  != null";
        assert kunde != null : "Vorbedingung verletzt: kunde  != null";

        Vormerkkarte vormerkkarteAlt = _vormerkkarten.get(medium);
        if (vormerkkarteAlt.getVormerker3() == kunde)
        {
            _vormerkkarten.put(medium,
                    new Vormerkkarte(medium, vormerkkarteAlt.getVormerker1(),
                            vormerkkarteAlt.getVormerker2(), _nullKunde));
        }
        else
        {
            if (vormerkkarteAlt.getVormerker2() == kunde)
            {
                _vormerkkarten.put(
                        medium,
                        new Vormerkkarte(medium,
                                vormerkkarteAlt.getVormerker1(),
                                vormerkkarteAlt.getVormerker3(), _nullKunde));
            }
            else
            {
                _vormerkkarten.put(
                        medium,
                        new Vormerkkarte(medium,
                                vormerkkarteAlt.getVormerker2(),
                                vormerkkarteAlt.getVormerker3(), _nullKunde));
            }

        }
    }

    @Override
    public boolean vormerkenMoeglich(Medium medium)
    {
        assert medium != null : "Vorbedingung verletzt: kunde  != null";

        boolean result = _vormerkkarten.get(medium)
            .getVormerker3()
            .equals(_nullKunde);
        return result;
    }

    @Override
    public boolean vormerkenMoeglichAlle(List<Medium> medien)
    {
        assert medien != null : "Vorbedingung verletzt: kunde  != null";

        boolean result = true;
        for (Medium medium : medien)
        {
            if (!vormerkenMoeglich(medium))
            {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean vormerkenMoeglichFuer(Medium medium, Kunde kunde)
    {
        assert medium != null : "Vorbedingung verletzt: kunde  != null";
        assert kunde != null : "Vorbedingung verletzt: kunde  != null";

        boolean result = vormerkenMoeglich(medium)
                && !istVerliehenAn(kunde, medium)
                && !(_vormerkkarten.get(medium)
                    .getVormerker1()
                    .equals(kunde) || _vormerkkarten.get(medium)
                    .getVormerker2()
                    .equals(kunde) || _vormerkkarten.get(medium)
                    .getVormerker3()
                    .equals(kunde));
        return result;
    }

    @Override
    public boolean vormerkenMoeglichAlleFuer(List<Medium> medien, Kunde kunde)
    {
        assert medien != null : "Vorbedingung verletzt: kunde  != null";
        assert kunde != null : "Vorbedingung verletzt: kunde  != null";

        boolean result = true;
        for (Medium medium : medien)
        {
            if (!vormerkenMoeglichFuer(medium, kunde))
            {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean kundeIstErsterVormerker(Medium medium, Kunde kunde)
    {
        assert medium != null : "Vorbedingung verletzt: kunde  != null";
        assert kunde != null : "Vorbedingung verletzt: kunde  != null";

        boolean result = _vormerkkarten.get(medium)
            .getVormerker1()
            .equals(kunde);
        return result;
    }

    @Override
    public boolean kundeIstErsterVormerkerAlle(List<Medium> medien, Kunde kunde)
    {
        assert medien != null : "Vorbedingung verletzt: kunde  != null";
        assert kunde != null : "Vorbedingung verletzt: kunde  != null";

        boolean result = true;
        for (Medium medium : medien)
        {
            if (!kundeIstErsterVormerker(medium, kunde))
            {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean keineVormerkung(Medium medium)
    {
        assert medium != null : "Vorbedingung verletzt: kunde  != null";

        boolean result = _vormerkkarten.get(medium)
            .getVormerker1()
            .equals(_nullKunde);
        return result;
    }

    @Override
    public boolean keineVormerkungAlle(List<Medium> medien)
    {
        assert medien != null : "Vorbedingung verletzt: kunde  != null";

        boolean result = true;
        for (Medium medium : medien)
        {
            if (!keineVormerkung(medium))
            {
                result = false;
            }
        }
        return result;
    }

    @Override
    public Kunde getNullKunde()
    {
        return _nullKunde;
    }
}
