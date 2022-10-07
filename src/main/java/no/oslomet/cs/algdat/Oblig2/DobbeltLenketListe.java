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
        hode = hale = null;
        antall = endringer = 0;

    }

    public DobbeltLenketListe(T[] a) // Er dette da konstruktøren for å endre lister? Vanskelig dette...
    {
        Objects.requireNonNull(a, "Tabellen a er null!");
        hode = hale = null;
        int i = antall = endringer = 0;

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
                hale = hale.neste = new Node<>(a[i], hale, null); // er spent på om forrige rakk å bli til neste før den pekte, ellers peker den på seg selv...
                antall++;
            }
            i++;
        }


    }

    private Node<T> finnNode(int indeks)                // Tok utgangspunkt i koden Programkode 3.3.3 a)
    {
        Node<T> p = hode;           // Lager en referanse til index 0
        if (indeks < (antall/2))
        {

            int i = 0;
            if (indeks == i) return p;
            while (i < indeks)
            {
                p = p.neste;
                i++;
            }
        }

        else
        {
            p = hale;
            int i = antall-1;
            if (indeks == i) return p;
            while (i > indeks)
            {
                p = p.forrige;
                i--;
            }

        }     // Alternativt begynner vi bakfra
        return p;
    }
    private void fratilKontroll(int fra, int til)       // Utgangspunkt fra Programkode 1.2.3 a)
    {
        if (fra < 0 || (til >= antall || (fra > til))) {   // Her vinner jeg nok ingen konkurranse i lesbarhet...
            throw new IndexOutOfBoundsException("Fra "+ fra +" til " +til+ " fungerer ikke sammen med lengde: "+antall);
        }

    }

        //for (T value : a) if (value != null) leggInn(value); // Får vente med denne til en annen anledning



    public Liste<T> subliste(int fra, int til)  // Nå jobber jeg her
    {
        fratilKontroll(fra,til);
        Liste<T> liste = new DobbeltLenketListe<>();
        // HER MISTENKER JEG AT VI KAN FÅ BRUK FOR nullstill() siden antall kommer til å gå i spagat eller noe.

        for (int i = fra; i <= til; i++) leggInn(hent(i,true));              // Bruker alternativ hent()- metode.
        return liste;
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
        // Inspirert av Programkode 3.3.2 f)
        Objects.requireNonNull(verdi, "verdi = null!");

        // Ok, her kommer en munnfull:
        // Så vår nye hale "Future" er gamle hale "Oldtimer" sin ".neste" node. Vi har da ikke endret "Oldtimer" sin verdi.
        // "Future"-noden, som ikke har fått et eget navn,
        // får verdien fra funksjonen vår leggInn(verdi), samt en peker til venstre som refererer til "Oldtimer"-noden.
        // I retning høyre peker vår nye "Future" på null ->(defacto er nå "future" den nye halen) OR IS IT?


        if (!tom()) {
            hale = hale.neste = new Node<>(verdi, hale, null);
            antall++;
        }
        else {
            hode = hale = new Node<>(verdi, null, null);
            antall = 1;
        }



        return true;

    }

    @Override
    public void leggInn(int indeks, T verdi) {
        throw new UnsupportedOperationException();
    }
    /**
     * @inneholder Checks if value is found in a list.
     * @param verdi value to check if found.
     * @return True if found, False if not.
     */
    @Override
    public boolean inneholder(T verdi) {
        int x = indeksTil(verdi);  // Sjekker om verdi finnes i listen ved hjelp av indeksTil. Om den ikke finnes returneres -1
        if(x>=0)
            return true;
        else
            return false;
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

    public T hent(int indeks, boolean toggle)  // Lagde en versjon hvor vi kan sende inn variabelen til indeksKontroll sammen med hent
    {
        indeksKontroll(indeks, toggle);        // sjekker index-range hvor til er tillatt = antall.
        return finnNode(indeks).verdi;         // Returnerer aktuell nodeverdi
    }

    /**
     * @IndekTil A function to find index of said value in list.
     * @param verdi value to be found index of.
     * @return Index where value is placed in list, -1 if value not existing in list
     */
    @Override
    public int indeksTil(T verdi) {
        Node<T> x = hode; // Testvariabel
        for(int i = 0; i < antall; i++ )
        {
        if(x.verdi.equals(verdi)) // Sjekker om verdi finnes i node på indeks i
            return i;             // returnerer i om verdi finnes her
        else
        {
            x = x.neste;         // Går videre til neste node om verdi ikke funnet
        }
        }
        return -1;               // Returnerer -1 om verdi ikke finnes i listen
    }

    @Override
    public T oppdater(int indeks, T nyverdi)
    {
        Objects.requireNonNull(nyverdi, "verdi = null!");
        T initialValue = hent(indeks);      // Lagrer unna eksisterende verdi først
        finnNode(indeks).verdi = nyverdi;   // finnNode returnerer aktuell node, så vi kan her oppdatere den direkte?
        endringer++;                        // Dette teller som 1 endring
        return initialValue;
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


