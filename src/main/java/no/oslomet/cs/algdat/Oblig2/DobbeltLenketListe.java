package no.oslomet.cs.algdat.Oblig2;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.*;


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
                hode.neste = hale;
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

    private Node<T> finnNode(int indeks) { // Oppgave 3a - Tok utgangspunkt i koden Programkode 3.3.3 a)
        Node<T> p = hode;           // Lager en referanse til index 0
        if (indeks < (antall/2)) { // indeks mindre enn antall/2 --> leting starter fra hode
            int i = 0;
            if (indeks == i) return p;
            while (i < indeks) {
                p = p.neste; // peker på neste node (fra hode) fram til vi når indeks
                i++; // inkrementerer i fram til vi når indeks
            }
        }
        else { // Indeks større enn antall/2 --> leting starter fra hale
            p = hale; // peker ny node på hale
            int i = antall-1; // indeks starter på 0, derfor antall-1
            if (indeks == i) return p;
            while (i > indeks) {
                p = p.forrige; // peker på forrige node (fra hale) fram til vi når indeks
                i--; // dekremerer i fram til vi når indeks
            }
        }
        return p;
    }
    private void fratilKontroll(int antall, int fra, int til) {      // Kopiert fra Programkode 1.2.3 a)
        if (fra < 0)                                  // fra er negativ
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");
        if (til > antall)                          // til er utenfor tabellen
            throw new IndexOutOfBoundsException
                    ("til(" + til + ") > antall(" + antall + ")");
        if (fra > til)                                // fra er større enn til
            throw new IllegalArgumentException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }



    public Liste<T> subliste(int fra, int til) {
        fratilKontroll(antall, fra, til);
        Liste<T> liste = new DobbeltLenketListe<>();
        for (int i = fra; i < til; i++) {
            liste.leggInn(hent(i,true));              // Bruker alternativ hent()- metode.
        }
        return liste;
    }

    /**
     * @antall er en variabel vi oppdaterer i funksjonene:
     * @tom() returnerer true hvis liste er tom og false hvis den ikke er det
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

    public boolean leggInn(T verdi) { // Oppgave 2b
        Objects.requireNonNull(verdi); // Null-verdi er ikke tillatt å legge til, kontrollerer derfor med requireNonNull-metode fra klassen Objects
        Node<T> nyNode = new Node<>(verdi, null, null); // Lager ny node, med ny verdi, som skal legges inn bakerst i listen (peker ingen steder enda)

        if (tom() & hale == null & hode == null) { // Tilfelle 1 - listen er på forhånd tom
            hode = nyNode; // Ettersom listen er tom vil nye noden og hode være samme
        }

        else { // Tilfelle 2 - listen er ikke tom så vi oppdaterer kun hale ettersom vi legger til ny node bakerst i liste
            hale.neste = nyNode; // gamle hale skal peke på ny hale
            nyNode.forrige = hale; // ny hale må peke tilbake på gammel hale
        }

        hale = nyNode; // I begge tilfeller vil hale være nye noden da vi skal legge den inn bakerst

        antall++; // Inkrementerer antall ettersom vi får en ekstra node i listen
        endringer++; // Inkrementerer endringer ettersom vi har utført en endring i listen

        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        if (verdi == null)
            throw new NullPointerException("Verdi er null.");
        if (indeks < 0 || indeks > antall) {
            if (indeks < 0)
                throw new IndexOutOfBoundsException("Indeks er mindre enn 0.");
            else
                throw new IndexOutOfBoundsException("Indeks er større enn antall.");
        }

        if (hode == null && hale == null){
            hode = hale = new Node<>(verdi, null, null);
        }
        else if(indeks == 0)
        {
            hode = new Node<>(verdi, null, hode);
            hode.neste.forrige = hode;
        }
        else if (indeks == antall)
        {
            hale = new Node<>(verdi, hale, null);
            hale.forrige.neste = hale;
        }
        else
        {
            Node<T> x = hode; // Testnode på indeks 0
            for(int i = 0; i<indeks; i++)
                x = x.neste;  // Flytter x til ønsket plassering av ny node
            x = new Node<>(verdi, x.forrige, x);
            x.forrige.neste = x;
            x.neste.forrige = x;
        }
        antall++;
        endringer++;
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
    public T hent(int indeks) {      // Oppgave 3a - Hent element som befinner seg ved indeks:
        indeksKontroll(indeks, false);  // Sjekk at indeks er gyldig. Hold mus over indeksKontroll(), har prøvd å forklare.
        return finnNode(indeks).verdi;         // Returnerer aktuell nodeverdi
    }

    public T hent(int indeks, boolean toggle)  // Lagde en versjon hvor vi kan sende inn variabelen til indeksKontroll sammen med leggInn
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
    public T oppdater(int indeks, T nyverdi) { // Oppgave 3a
        Objects.requireNonNull(nyverdi, "verdi = null!");
        T initialValue = hent(indeks);      // Henter eksisterende verdi på plass indeks først og lagrer verdi til initialValue generisk variable
        finnNode(indeks).verdi = nyverdi;   // finnNode returnerer aktuell node, så vi oppdaterer verdien til denne noden med nyverdi
        endringer++;                        // Dette teller som 1 endring
        return initialValue;
    }


    @Override
    public boolean fjern(T verdi) {
        if (verdi == null || antall == 0) // Sjekker for spesielle tilfeller hvor verdi ikke kan finnes eller listen er tom
            return false;
        Node<T> x = hode; // Testnode

        while (x != null) { // Å bruke while lånte jeg av onlinekompendiet da min egen metode bare ble tull.
            if (x.verdi.equals(verdi)) break;
            x = x.neste;
        }
        if (x == null) return false;

        if (antall == 1) { //Spesialtilfelle hvor det kun finnes ett element i listen.
            hode = hale = null;
            antall--;
            endringer++;
            return true;
        } else if (x == hode) { //Tilfelle hvor hodet skal fjernes.
            hode = hode.neste;
            hode.forrige = null;
            antall--;
            endringer++;
            return true;
        } else if (x == hale) { // Tilfelle hvor hale skal fjernes.
            hale = hale.forrige;
            hale.neste = null;
            antall--;
            endringer++;
            return true;
        } else { // Tilfeldig node som ikke er hode eller hale skal slettes.
            x.forrige.neste = x.neste;
            x.neste.forrige = x.forrige;
            antall--;
            endringer++;
            return true;
        }
    }

    @Override
    public T fjern(int indeks) {
        if(indeks >= antall || indeks < 0) // Sjekker for indeksfeil.
            throw new IndexOutOfBoundsException("Indeks må være større enn 0 eller mindre enn "+ antall +"!");
        if(antall == 1){ //Spesialtilfelle hvor listen kun inneholder en verdi.
            T verdi = hode.verdi;
            hode = hale = null;
            antall--;
            endringer++;
            return verdi;
        }
        else if(indeks == 0){ // Tilfelle hvor hodet skal fjernes.
            T verdi = hode.verdi;
            hode = hode.neste;
            hode.forrige = null;
            antall--;
            endringer++;
            return verdi;
        }
        else if(indeks == antall - 1){ //Tilfelle hvor halen skal fjernes.
            T verdi = hale.verdi;
            hale = hale.forrige;
            hale.neste = null;
            antall--;
            endringer++;
            return verdi;
        }
        else{ //Tilfelle hvor tilfeldig node som ikke er hode eller hale skal slettes.
            Node<T> x = hode; //Testnode
            for(int i = 0; i < indeks; i++) //Finner noden som skal slettes.
                x = x.neste;
            T verdi = x.verdi;
            x.forrige.neste = x.neste;
            x.neste.forrige = x.forrige;
            antall--;
            endringer++;
            return verdi;
        }

    }

    @Override
    public void nullstill()
    {
        Node<T> current = hode;
        while (current.neste != null)
        {
            Node<T> temp = current.neste;
            current.neste = null;
            current.forrige = null;
            current.verdi = null;
            current = temp;
        }
        hode = hale = null;
        endringer++;
        antall = 0;
    }
    // Alternativ versjon under som viste seg å være betydelig tregere:
    //public void nullstill()
    //{
    //    Node<T> current = hode;
    //    Node<T> temp;
//
    //    while (current.neste != null)
    //    {
    //        temp = current.neste;
    //        fjern(0);
    //        current = temp;
    //    }
    //    hode = hale = null;
    //    endringer++;
    //    antall--;
//
    //}




    @Override
    public String toString() { //Oppgave 2a
        StringBuilder sb = new StringBuilder(); // Vanlig concatination for generiske verdier. Trenger ikke sette capacity da stringBuilder justerer seg selv hvis vi går over
        sb.append('['); // Alle lister, uansett om den er tom, skal stare med klammeparantes ( [ )

        if (!tom()) {
            Node<T> tempNode = hode; // Lager ny midlertidig node class med generisk veri og setter den til hode noden i vår doblet lenket liste
            sb.append(tempNode.verdi); // Setter første verdi fra dobbelLenketListe inn i stringBuilder
            tempNode = tempNode.neste; // Går vidre til neste node i liste

            while (tempNode != null) { // Sjekker om det er enden av listen, hvis ikke fortsetter vi å legge til verdiene til nodene i sb
                sb.append(", " + tempNode.verdi); // Legger til node data i string sb
                tempNode = tempNode.neste; // Går vidre til neste node
            }
        }
        sb.append(']'); // Vi kom til enden av listen og legger da til klammeparantes for å lukke liste
        return sb.toString(); // Returnerer hele stringen
        //throw new UnsupportedOperationException();
    }

    public String omvendtString() { //Oppgave 2a
        StringBuilder sb = new StringBuilder(); // Vanlig concatination for generiske verdier. Trenger ikke sette capacity da stringBuilder justerer seg selv hvis vi går over
        sb.append('['); // Alle lister, uansett om den er tom, skal stare med klammeparantes ( [ )

        if (!tom()) {
            Node<T> tempNode = hale; // Lager ny midlertidig node class med generisk veri og setter den til hale noden i vår doblet lenket liste
            sb.append(tempNode.verdi); // Setter første verdi fra dobbelLenketListe inn i stringBuilder
            tempNode = tempNode.forrige; // Går bakover til forrige node i liste

            while (tempNode != null) { // Sjekker om det er starten av listen, hvis ikke fortsetter vi å legge til verdiene til nodene i sb
                sb.append(", " + tempNode.verdi); // Legger til node data i string sb
                tempNode = tempNode.forrige; // Går bakover til forrige node
            }
        }
        sb.append(']'); // Vi kom til starten av listen og legger da til klammeparantes for å lukke liste
        return sb.toString(); // Returnerer hele stringen
    }

    @Override
    public Iterator<T> iterator() { //Oppgave 8b
        return (Iterator<T>) new DobbeltLenketListeIterator(); // Usikker på om dette er riktig
        //throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) { // Oppgave 8d
        indeksKontroll(indeks, false); // Sjekker om indeks er loving, leggInn false da vi ikke skal legge inn noe i liste

        //throw new UnsupportedOperationException();
        return (Iterator<T>) new DobbeltLenketListeIterator(indeks);
    }

    private class DobbeltLenketListeIterator implements Iterator<T> {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator() {  // DENNE KONSTRUKTØREN ER FERDIGKODET I OPPGAVEN OG SKAL IKKE ENDRES
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks) { // Oppgave 8c
            denne = hode;     // setter først pekern denne til hode
            for (int i = 0; i < indeks; i++) {
                denne = denne.neste; // starter ved hode og peker til neste node fram til vi kommer til indeks
            }
            // Resten er som i konstruktøren som var ferdigkodet
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer

            //throw new UnsupportedOperationException();
        }

        @Override                   // DENNE ER FERDIGKODET I OPPGAVEN OG SKAL IKKE ENDRES
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() { // Oppgave 8a
            if (iteratorendringer != endringer) { //Sjekker først om iteratorendringer er lik endringer.
                throw new ConcurrentModificationException(); // Hvis ikke, kastes en ConcurrentModificationException.
            }
            if (!hasNext()) { // hvis det ikke er flere igjen i listen (dvs. hvis hasNext() ikke er sann/true)
                throw new NoSuchElementException(); // Så en NoSuchElementException
            }
            fjernOK = true; // Deretter settes fjernOK til sann/true
            T tempVerdi = denne.verdi; // Setter verdien til denne lik midlertidig verdi
            denne = denne.neste; // denne flyttes til den neste node
            return tempVerdi; // verdien til denne returneres
            //throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            if(fjernOK == false)
                throw new IllegalStateException("Kan ikke fjerne verdi.");
            if (iteratorendringer != endringer)
                throw new ConcurrentModificationException("Kan ikke fjerne verdi.");
            fjernOK = false; // Kan ikke fjerne samme verdi flere ganger.

            Node<T> x = hode; // Testnode

            if(antall == 1) // Finnes bare en node og denne skal slettes
                hode = hale = null;
           else if(denne == null) { // Siste skal fjernes
                x = hale;
                hale = hale.forrige;
                hale.neste = null;
            }
           else if(denne.forrige == hode){ // Første skal fjernes
                x = hode;
                hode = hode.neste;
                hode.forrige = null;
            }
            else{ // Random node som ikke er hode eller hale skal fjernes
                x = denne.forrige;
                x.neste.forrige = x.forrige;
                x.forrige.neste = x.neste;
            }
            antall--;
            endringer++;
            iteratorendringer++;


        }

    } // class DobbeltLenketListeIterator


    /**
     *
     * @param liste kan f.eks være DobbeltLenketListe<> eller EnkeltLenketListe<> etc.
     * @param c     kan f.eks være Comparator.naturalOrder()
     * @param <T>
     */
    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) { // prøver å anvende bubble sort - tatt insprasjon fra kode på nett
        if (liste.tom()) return; // Unntak for tom liste
        boolean byttetVerdi = true; // variable for å vite når vi har gått igjennom hele listen uten at bytte var nødvendig

        while (byttetVerdi) {
            byttetVerdi = false; // Setter til false tilfelle det ikke blir noe bytte --> går ut av while-løkke ved neste itrasjon
            for (int i = 0; i < liste.antall() - 1; i++) {
                // Anvender compare metoden fra comarator interface, usikker på akkurat hvordan metoden fungerer men etter litt testing virker det som at
                // den returnerer -1 hvis verdi på indeks+1 er mindre en indeks --> blir bytte på de to plassene (hadde satt pris på tilbakemelding hvis dette ikke stemmer)
                if ((c.compare(liste.hent(i+1), liste.hent(i))) < 0) {
                    byttetVerdi = true; // Settes høy hvis det ble utført bytte
                    T tempVerdi = liste.hent(i); // Setter midlertidig verdi til verdi på indeks i
                    liste.oppdater(i, liste.hent(i+1)); // Endrer verdi på indeks i til verdi på indeks i+1
                    liste.oppdater(i+1, tempVerdi); // Endrer verdi på indeks i+1 til verdi på indeks i
                }
            }
        }
    }
} // class DobbeltLenketListe

