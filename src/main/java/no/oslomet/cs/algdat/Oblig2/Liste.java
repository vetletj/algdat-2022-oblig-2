package no.oslomet.cs.algdat.Oblig2;

import java.util.Iterator;

public interface Liste<T> extends Beholder<T> {
    public boolean leggInn(T verdi);           // Nytt element bakerst

    public void leggInn(int indeks, T verdi);  // Nytt element på plass indeks

    public boolean inneholder(T verdi);        // Er verdi i listen?

    public T hent(int indeks);                 // Hent element på plass indeks

    public int indeksTil(T verdi);             // Hvor ligger verdi?

    public T oppdater(int indeks, T verdi);    // Oppdater på plass indeks

    public boolean fjern(T verdi);             // Fjern objektet verdi

    public T fjern(int indeks);                // Fjern elementet på plass indeks

    public int antall();                       // Antallet i listen

    public boolean tom();                      // Er listen tom?

    public void nullstill();                   // Listen nullstilles (og tømmes)

    public Iterator<T> iterator();             // En iterator

    public default String melding(int indeks)  // Unntaksmelding
    {
        return "Indeks: " + indeks + ", Antall: " + antall();
    }

    /**
     * kortversjon:
     * Bruk denne for å sjekke at indeksen er "innafor".
     * Mat den med indeksen du vil sjekke og parameteren false.
     * Bruker du den med leggInn() er også siste indeks tillatt, og du bytter parameter til true.
     *
     * Beskrivelse hentet fra kapittel 3.2:
     * "Default-metoden indeksKontroll(int, boolean) kan brukes av alle subklasser.
     * Metoden leggInn(indeks,T) kan legge verdien bakerst, dvs. det er tillatt med indeks lik antall verdier i listen.
     *      Da brukes true som parameter i indeksKontroll().
     * Hvis det ikke er tillatt med indeks lik antall verdier, brukes false."
     *
     * @param indeks som skal sjekkes om "lovlig"
     * @param leggInn sett denne til false med mindre du bruker metoden leggInn()
     */
    public default void indeksKontroll(int indeks, boolean leggInn) {
        if (indeks < 0 ? true : (leggInn ? indeks > antall() : indeks >= antall())) {
            throw new IndexOutOfBoundsException(melding(indeks));
        }
    }
}  // Liste