package no.oslomet.cs.algdat.Oblig2;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;


public class DobbeltLenketListe<T> implements Liste<T> {

    /**
     * Node class
     *
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() // Dette er konstruktøren vår for å opprette ny tom liste, så det er her vi "produserer" fra blueprints.
    {
        hode = null;
        hale = null;
        antall = 0;
        endringer = 0;
    }

    public DobbeltLenketListe(T[] a) // Er dette da konstruktøren for å endre lister? Vanskelig dette...
    {
        Objects.requireNonNull(a, "Tabellen a er null!");
        hode = null;
        hale = null;
        int i = 0;

        // Finner første verdi som ikke er null, og setter denne til head.
        while (hode == null && i < a.length)
        {
            if (a[i] != null)
            {
                hode = hale = new Node<>(a[i], null, null);
                antall++;
            }
            i++;
        }
        // Finner neste verdi som ikke er null, og setter denne til hale.
        while (hale == hode && i < a.length)
        {
            if (a[i] != null)
            {
                hale = new Node<>(a[i], hode, null);
                antall++;
            }
            i++;
        }
        while (i < a.length)
        {
            if (a[i] != null)
            {
                hale = hale.neste = new Node<>(a[i],hale,null); // er spent på om forrige rakk å bli til neste før den pekte, ellers peker den på seg selv...
                antall++;
            }
            i++;
        }


    }

    private Node<T> finnNode(int indeks)    // Tok utgangspunkt i koden med samme navn i kapittel 3.3 i kompendiet.
    {
        Node<T> p = hode;                                                   // Lager en referanse til index 0
        Node<T> tail= hale;                                                 // Lager en referanse til index -1

        if (indeks < antall/2) for (int i = 0; i < indeks; i++) p = p.neste;// Om sann leter vi etter indeks fra head:
        else for (int i = antall-1; i >= indeks; i--) p = tail.forrige;     // Alternativt begynner vi bakfra
        return p;
    }


        //for (T value : a) if (value != null) leggInn(value); // Får vente med denne til en annen anledning


    public Liste<T> subliste(int fra, int til) {
        throw new UnsupportedOperationException();
    }

    /**
     * @antall er en variabel vi oppdaterer i funksjonene:
     * @leggInn() øker antall
     * @fjern() senker antall
     * @nullstill() nullstiller antall
     * @return verdien til antall.
    */
    @Override
    public int antall() {

        return antall;
    }

    /**
     * @return true om antall er lik 0
     */
    @Override
    public boolean tom() {
        return (antall == 0) ? true : false;
    }

    /**
     *
     * @param verdi blir lagt inn bakerst i listen
     * @return true om alt er greit
     */
    @Override
    public boolean leggInn(T verdi) {

        // Ok, her kommer en munnfull:
        // Så vår nye hale "Future" er gamle hale "Oldtimer" sin ".neste" node. Vi har da ikke endret "Oldtimer" sin verdi.
        // "Future"-noden, som ikke har fått et eget navn,
        // får verdien fra funksjonen vår leggInn(verdi), samt en peker til venstre som refererer til "Oldtimer"-noden.
        // I retning høyre peker vår nye "Future" på null ->(defacto er nå "future" den nye halen) OR IS IT?

        if (!tom())
        {
            hale = hale.neste = new Node<>(verdi, hale, null);
        }
        else
        {
            hode = hale = new Node<>(verdi, null, null);
        }
        antall++;

        return true;

    }

    @Override
    public void leggInn(int indeks, T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean inneholder(T verdi) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param indeks
     * @return
     */
    @Override
    public T hent(int indeks)                   // Hent element som befinner seg ved indeks:
    {
        indeksKontroll(indeks, false);  // Sjekk at indeks er gyldig. Hold mus over indeksKontroll(), har prøvd å forklare.
        return finnNode(indeks).verdi;         // Returnerer aktuell nodeverdi
    }

    @Override
    public int indeksTil(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override                                       // Nå jobber jeg her
    public T oppdater(int indeks, T nyverdi)
    {
        T gammelverdi = hent(indeks);       // Lagrer unna eksisterende verdi først
        finnNode(indeks).verdi = nyverdi;   // finnNode returnerer aktuell node, så vi kan her oppdatere den direkte
        endringer++;                        // Dette teller som 1 endring
        return gammelverdi;
    }

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T fjern(int indeks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    public String omvendtString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException();
    }

    private class DobbeltLenketListeIterator implements Iterator<T> {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator() {
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }

} // class DobbeltLenketListe


