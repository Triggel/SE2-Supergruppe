package de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih;

import java.util.List;

import de.uni_hamburg.informatik.swt.se2.mediathek.fachwerte.Datum;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Verleihkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Vormerkkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.ObservableService;

/**
 * Der VerleihService erlaubt es, Medien auszuleihen und zurückzugeben.
 * 
 * Für jedes ausgeliehene Medium wird eine neue Verleihkarte angelegt. Wird das
 * Medium wieder zurückgegeben, so wird diese Verleihkarte wieder gelöscht.
 * 
 * VerleihService ist ein ObservableService, als solcher bietet er die
 * Möglichkeit über Verleihvorgänge zu informieren. Beobachter müssen das
 * Interface ServiceObserver implementieren.
 * 
 * @author SE2-Team
 * @version SoSe 2015
 */
public interface VerleihService extends ObservableService
{
    /**
     * Verleiht Medien an einen Kunden. Dabei wird für jedes Medium eine neue
     * Verleihkarte angelegt.
     * 
     * @param kunde Ein Kunde, an den ein Medium verliehen werden soll
     * @param medien Die Medien, die verliehen werden sollen
     * @param ausleihDatum Der erste Ausleihtag
     * 
     * @throws ProtokollierException Wenn beim Protokollieren des
     *             Verleihvorgangs ein Fehler auftritt.
     * 
     * @require kundeImBestand(kunde)
     * @require sindAlleNichtVerliehen(medien)
     * @require ausleihDatum != null
     * 
     * @ensure sindAlleVerliehenAn(kunde, medien)
     */
    void verleiheAn(Kunde kunde, List<Medium> medien, Datum ausleihDatum)
            throws ProtokollierException;

    /**
     * Prüft ob die ausgewählten Medium für den Kunde ausleihbar sind
     * 
     * @param kunde Der Kunde für den geprüft werden soll
     * @param medien Die medien
     * 
     * 
     * @return true, wenn das Entleihen für diesen Kunden möglich ist, sonst
     *         false
     * 
     * @require kundeImBestand(kunde)
     * @require medienImBestand(medien)
     */
    boolean istVerleihenMoeglich(Kunde kunde, List<Medium> medien);

    /**
     * Liefert den Entleiher des angegebenen Mediums.
     * 
     * @param medium Das Medium.
     * 
     * @return Den Entleiher des Mediums.
     * 
     * @require istVerliehen(medium)
     * 
     * @ensure result != null
     */
    Kunde getEntleiherFuer(Medium medium);

    /**
     * Liefert alle Medien, die von dem gegebenen Kunden ausgeliehen sind.
     * 
     * @param kunde Der Kunde.
     * @return Alle Medien, die von dem gegebenen Kunden ausgeliehen sind.
     *         Liefert eine leere Liste, wenn der Kunde aktuell nichts
     *         ausgeliehen hat.
     * 
     * @require kundeImBestand(kunde)
     * 
     * @ensure result != null
     */
    List<Medium> getAusgelieheneMedienFuer(Kunde kunde);

    /**
     * @return Eine Listenkopie aller Verleihkarten. Für jedes ausgeliehene
     *         Medium existiert eine Verleihkarte. Ist kein Medium verliehen,
     *         wird eine leere Liste zurückgegeben.
     * 
     * @ensure result != null
     */
    List<Verleihkarte> getVerleihkarten();

    /**
     * Nimmt zuvor ausgeliehene Medien zurück. Die entsprechenden Verleihkarten
     * werden gelöscht.
     * 
     * 
     * @param medien Die Medien.
     * @param rueckgabeDatum Das Rückgabedatum.
     * 
     * @require sindAlleVerliehen(medien)
     * @require rueckgabeDatum != null
     * 
     * @ensure sindAlleNichtVerliehen(medien)
     */
    void nimmZurueck(List<Medium> medien, Datum rueckgabeDatum)
            throws ProtokollierException;

    /**
     * Prüft ob das angegebene Medium verliehen ist.
     * 
     * @param medium Ein Medium, für das geprüft werden soll ob es verliehen
     *            ist.
     * @return true, wenn das gegebene medium verliehen ist, sonst false.
     * 
     * @require mediumImBestand(medium)
     */
    boolean istVerliehen(Medium medium);

    /**
     * Prüft ob alle angegebenen Medien nicht verliehen sind.
     * 
     * @param medien Eine Liste von Medien.
     * @return true, wenn alle gegebenen Medien nicht verliehen sind, sonst
     *         false.
     * 
     * @require medienImBestand(medien)
     */
    boolean sindAlleNichtVerliehen(List<Medium> medien);

    /**
     * Prüft ob alle angegebenen Medien verliehen sind.
     * 
     * @param medien Eine Liste von Medien.
     * 
     * @return true, wenn alle gegebenen Medien verliehen sind, sonst false.
     * 
     * @require medienImBestand(medien)
     */
    boolean sindAlleVerliehen(List<Medium> medien);

    /**
     * Prüft, ob alle angegebenen Medien an einen bestimmten Kunden verliehen
     * sind.
     * 
     * @param kunde Ein Kunde
     * @param medien Eine Liste von Medien
     * @return true, wenn alle Medien an den Kunden verliehen sind, sonst false.
     * 
     * @require kundeImBestand(kunde)
     * @require medienImBestand(medien)
     */
    boolean sindAlleVerliehenAn(Kunde kunde, List<Medium> medien);

    /**
     * Prüft, ob ein Medium an einen bestimmten Kunden verliehen ist.
     * 
     * @param kunde Ein Kunde
     * @param medium Ein Medium
     * @return true, wenn das Medium an den Kunden verliehen ist, sonst false.
     * 
     * @require kundeImBestand(kunde)
     * @require mediumImBestand(medium)
     */
    boolean istVerliehenAn(Kunde kunde, Medium medium);

    /**
     * Prüft ob der angebene Kunde existiert. Ein Kunde existiert, wenn er im
     * Kundenstamm enthalten ist.
     * 
     * @param kunde Ein Kunde.
     * @return true wenn der Kunde existiert, sonst false.
     * 
     * @require kunde != null
     */
    boolean kundeImBestand(Kunde kunde);

    /**
     * Prüft ob das angebene Medium existiert. Ein Medium existiert, wenn es im
     * Medienbestand enthalten ist.
     * 
     * @param medium Ein Medium.
     * @return true wenn das Medium existiert, sonst false.
     * 
     * @require medium != null
     */
    boolean mediumImBestand(Medium medium);

    /**
     * Prüft ob die angebenen Medien existierien. Ein Medium existiert, wenn es
     * im Medienbestand enthalten ist.
     * 
     * @param medien Eine Liste von Medien.
     * @return true wenn die Medien existieren, sonst false.
     * 
     * @require medien != null
     * @require !medien.isEmpty()
     */
    boolean medienImBestand(List<Medium> medien);

    /**
     * Gibt alle Verleihkarten für den angegebenen Kunden zurück.
     * 
     * @param kunde Ein Kunde.
     * @return Alle Verleihkarten des angebenen Kunden. Eine leere Liste, wenn
     *         der Kunde nichts entliehen hat.
     * 
     * @require kundeImBestand(kunde)
     * 
     * @ensure result != null
     */
    List<Verleihkarte> getVerleihkartenFuer(Kunde kunde);

    /**
     * Gibt die Verleihkarte für das angegebene Medium zurück, oder null wenn
     * das Medium nicht verliehen ist.
     * 
     * @param medium Ein Medium.
     * @return Die Verleihkarte für das angegebene Medium.
     * 
     * @require istVerliehen(medium)
     * 
     * @ensure (result != null)
     */
    Verleihkarte getVerleihkarteFuer(Medium medium);

    /**
     * Merkt Medien für einen Kunden vor.
     * 
     * @param medien Die Liste der vorzumerkenden Medien.
     * @param kunde Der Kunde, der die Medien vormerken will.
     * 
     * @require medien != null
     * @require kunde != null
     */
    void merkeMedienVor(List<Medium> medien, Kunde kunde);

    /**
     * Storniert eine getätigte Vormerkung von Medien für einen Kunden.
     * 
     * @param medien Eine Liste der Medien, deren Vormerkung storniert werden soll.
     * @param kunde Der Kunde, desse Vormerkung storniert werden soll.
     * 
     * @require medien != null
     * @require kunde != null
     */
    void storniereVormerkung(List<Medium> medien, Kunde kunde);

    /**
     * Entfernt eine Vormerkung eines Kunden für ein Medium.
     * 
     * @param medium Das Medium, für das die Vormerkung entfernt werden soll.
     * @param kunde Der Kunde, dessen Vormerkung entfernt werden soll.
     * 
     * @require medium != null
     * @require kunde != null
     */
    void entferneVormerker(Medium medium, Kunde kunde);

    /**
     * Gibt die in der HashMap gespeicherte Vormerkkarte für ein Medium zurück.
     * 
     * @param medium Das Medium, für das die Vormerkkarte zurückgegeben wird.
     * 
     * @return Die Vormerkkarte für das Medium.
     * 
     * @require medium != null
     * 
     * @ensure result != null
     */
    Vormerkkarte getVormerkkarteFuer(Medium medium);

    /**
     * Prüft, ob das Vormerken eines Mediums möglich ist.
     * 
     * @param medium Ein Medium, für das überprüft wird.
     * 
     * @return True, wenn Vormerken möglich, false sonst.
     * 
     * @require medium != null
     */
    boolean vormerkenMoeglich(Medium medium);

    /**
     * Prüft, ob alle Medien einer Liste von Medien vorgemerkt werden können.
     * 
     * @param medien Eine Liste von Medien, für die überprüft wird.
     * 
     * @return True, wenn Vormerken aller Medien möglich, false sonst.
     * 
     * @require medien != null
     */
    boolean vormerkenMoeglichAlle(List<Medium> medien);

    /**
     * Prüft, ob ein Medium für einen bestimmten Kunden vorgemerkt werden kann.
     * 
     * @param medium Ein Medium, für das überprüft wird.
     * @param kunde Der Kunde, für den überprüft wird.
     * 
     * @return True, wenn das Vormerken des Mediums für den Kunden möglich ist, false sonst.
     * 
     * @require medium != null
     * @require kunde != null
     */
    boolean vormerkenMoeglichFuer(Medium medium, Kunde kunde);

    /**
     * Prüft, ob eine Liste von Medien für einen bestimmten Kunden vorgemerkt werden kann.
     * 
     * @param medien Eine Liste von Medien, für die überprüft wird.
     * @param kunde Der Kunde, für den überprüft wird.
     * 
     * @return True, wenn das Vormerken der Medien für den Kunden möglich ist, false sonst.
     * 
     * @require medien != null
     * @require kunde != null
     */
    boolean vormerkenMoeglichAlleFuer(List<Medium> medien, Kunde kunde);

    /**
     * Prüft, ob ein Kunde der erste Vormerker eines Mediums ist.
     * 
     * @param medium Ein Medium, für das überprüft wird.
     * @param kunde Der Kunde, für den überprüft wird.
     * 
     * @return True, wenn der Kunde erster Vormerker des Mediums ist, false sonst.
     * 
     * @require medium != null
     * @require kunde != null
     */
    boolean kundeIstErsterVormerker(Medium medium, Kunde kunde);

    /**
     * Prüft, ob für ein Medium Vormerkungen existieren.
     * 
     * @param medium Ein Medium, für das überprüft wird.
     * 
     * @return True, wenn mind. eine Vormerkung für das Medium besteht, false sonst.
     * 
     * @require medium != null
     */
    boolean keineVormerkung(Medium medium);
}
