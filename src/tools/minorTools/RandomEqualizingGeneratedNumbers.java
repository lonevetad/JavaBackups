package tools.minorTools;

import java.util.Arrays;
import java.util.Random;

public class RandomEqualizingGeneratedNumbers {

	public RandomEqualizingGeneratedNumbers() {
		this(16);
	}

	/** This class generate a random set of number included in this set : [0, n). So, "n" is NOT included. */
	public RandomEqualizingGeneratedNumbers(int n) {
		if (n < 2) throw new IllegalArgumentException("The number of numbers must be greater than 1.");
		a = new int[len = n];
		len_1 = n - 1;
		rand = new Random();
		resetArrayToUniformity();
	}

	int len, len_1;
	int[] a;
	/* long */int sum;
	final Random rand;

	/*
	 * Idea : l'array contiene il numero di "presenze", ossia ogni cella contiene quante volte il numero equivalente all'indice
	 * della stessa e' presente nell'insieme. Se l'array contenesse valori diversi, allora e' come se
	 */
	public int next() {
		int index, i, r;

		/**
		 * <br>
		 * Ricordando che l' "n" del costruttore, ossia "len", e' il numero massimo ed escluso che potrebbe uscire
		 * randomicamente ...<br>
		 * Idea : l'array contiene il numero di "presenze" dell'indice di quella cella, ossia ogni cella contiene quante volte
		 * il numero equivalente all'indice della stessa e' presente nell'insieme di valori possibili che questa istanza puo'
		 * generare. <br>
		 * Quindi, dei valori diversi in due celle indicano che i loro indici hanno probabilita' differente di uscire.<br>
		 * Essendo l'insieme un raggruppamento di elementi non ordinato per natura, i possibili valori ( che si ricorda essere
		 * inclusi in [0,n) ) possono essere ordinati in senso crescente.<br>
		 * Essendo la distribuzione di "rand" uniforme,<br>
		 * FUCK NON MI SO ESPRIMERE LEGGI IL CODICE
		 */
		r = rand.nextInt(sum);
		// System.out.print("\t\t\t r: " + r);

		/**
		 * Una volta estratto l'indice (incluso in [0,len) ), bisogna rimuoverlo dall'insieme cosi' da favorire la fuoriuscita
		 * di tutti gli altri numeri. Dato che la rimozione e' un'operazione distruttiva, si opta per una soluzione affine : si
		 * riduce il numero di presenze di tale indice di "len-1" e si incrementa il numero di presenze di tutti gli altri
		 * indici di 1. "sum" rimane invariato, infatti <code>sum = sum + (len-1)*(-1) [l'indice rimosso] + 1*(len-1) [gli altri
		 * indici], ossia sum = sum - len + 1 + len -1</code>. CVD<br>
		 * Impostando i valori iniziali ad un numero specificato in {@link #resetArrayToUniformity()}, che in questo caso e'
		 * "len-1", un dato indice puo' fuoriuscire consecutivamente mediamente (oppure "solo" ???) <code>1 + ( VALORE_INIZIALE/
		 * (len-1) ) </code> volte, e per ora equivale a 2.
		 * <p>
		 * Al fine di ottimizzare, l'incremento viene eseguito durante lo scorrimnto dell'array. Una volta trovato l'indice, si
		 * riduce il contenuto di quella cella e infine si incrementano i numeri rimanenti
		 */
		// search the index containing "r"
		i = -1;
		/*
		 * si va avanti fino a "len-1" anziche' "len" perche' se r eccede, allora l'indice e' sicuramente "len-1", quindi mi
		 * evito un ciclo e dei controlli inutili
		 */
		while (++i < len_1 && r > a[i]) {
			r -= a[i]++;
		}
		// System.out.println("\t\t\t r after: " + r);
		a[index = i] -= len_1;
		while (++i < len) {
			a[i]++;
		}
		// System.out.println("\t\t\t index: " + index);
		return index;
	}

	protected void resetArrayToUniformity() {
		int i;
		i = -1;
		while (++i < len) {
			a[i] = len_1;
		}
		sum = // Math.abs
		(len * len);
		if (sum < 0) {
			sum = Integer.MAX_VALUE;
		}
	}

	public static void main(String[] args) {
		int i, ripetizioni, n;
		i = -1;
		n = 12;
		ripetizioni = 8;
		while (++i < ripetizioni) {
			doTest(10, n);
			n *= 10;
		}

		System.out.println("FINISH");
	}

	protected static void doTest(int n, int ripetizioni) {
		int i;
		int[] a;
		RandomEqualizingGeneratedNumbers r;

		System.out.println("Starting test with n: " + n + ", and ripetizioni: " + ripetizioni);
		r = new RandomEqualizingGeneratedNumbers(n);
		a = new int[n];
		i = -1;
		while (++i < ripetizioni) {
			a[r.next()]++;
		}
		System.out.println("RandomEqualizingGeneratedNumbers 's array:\n\t" + Arrays.toString(r.a));
		System.out.println("result 's array:\n\t" + Arrays.toString(a));
		System.out.println("\n\n");
	}
}