package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

import java.util.List;

import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Vormerkkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.ObservableService;

public interface VormerkService extends ObservableService
{
	void merkeVor(Kunde kunde, List<Medium> medien);
	
	void rueckeAuf(List<Medium> medien);
	
	boolean istVorgemerktFuer(Kunde kunde, Medium medium);
	
	boolean istVorgemerkt(Medium medium);

    boolean istVormerkenMoeglich(Kunde kunde, List<Medium> medien);

    Kunde getVormerker1Fuer(Medium medium);

    Kunde getVormerker2Fuer(Medium medium);
    
    Kunde getVormerker3Fuer(Medium medium);
    
    List<Vormerkkarte> getVormerkkarten();

    boolean istVerliehen(Medium medium);

    boolean sindAlleNichtVerliehen(List<Medium> medien);

    boolean sindAlleVerliehen(List<Medium> medien);

    boolean sindAlleVerliehenAn(Kunde kunde, List<Medium> medien);

    boolean istVerliehenAn(Kunde kunde, Medium medium);

    boolean kundeImBestand(Kunde kunde);

    boolean mediumImBestand(Medium medium);

    boolean medienImBestand(List<Medium> medien);

    List<Vormerkkarte> getVormerkkartenFuer(Kunde kunde);

    Vormerkkarte getVormerkkarteFuer(Medium medium);

    /**
     * @param kunde
     * @param medien
     * @return Returns true wenn Kunde erster Vormerker ist, oder erster Vormerker == null
     */
	boolean istKundeErsterVormerker(Kunde kunde, List<Medium> medien);

}
