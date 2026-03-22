package tests.tGame.tBalancing;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;

/**
 * Some {@link AbilityGeneric} may be activated (and stay active until a
 * condition is met, like manual de-activation) at the cost of reserving some
 * {@link RechargeableResourceType}. <br>
 * Usually, that reservation is expressed in terms of percentage. In that case,
 * a specific ability may reduce that percentage by some amount
 * ({@link ReservationReductionPercentage}) at the cost of some non-relative
 * amount. That cost may vary on the allocated level of this ability (as for the
 * already mentioned {@link ReservationReductionPercentage}), on that reduction
 * ({@link ReservationReductionCostFactor}), a fixed amount
 * ({@link ReservationReductionCostFixed} and so on.<br>
 */
public class T_ResourceReservationReduction {

	public static final int RESERVATIONS_PERCENTAGE[], RESERVATIONS_TO_TEST, MAX_LEVEL = 20;
	static {
		RESERVATIONS_PERCENTAGE = new int[] { 15, 25, 35, 40, 50, 75 };
		RESERVATIONS_TO_TEST = RESERVATIONS_PERCENTAGE.length;
	}

	public T_ResourceReservationReduction() {}

	//

	public static void doAndPrintTest(ReservationReductionPercentage c, ReservationReductionCostFactor f_c,
			ReservationReductionCostFixed m_c) {

		int res, minimum, levelAtMinimum, lowerBound;

		System.out.println("\n\n\n\n[][][][][][][][][][]\n\nSTART");

		for (int resIndex = 0; resIndex < RESERVATIONS_TO_TEST; resIndex++) {
			minimum = Integer.MAX_VALUE;
			levelAtMinimum = -1;

			res = RESERVATIONS_PERCENTAGE[resIndex];
			System.out.println("\n\n\n ---------------\nTesting reservation: " + res);

			for (int level = 0; level <= MAX_LEVEL; level++) {
				lowerBound = minMana(res, c, f_c, m_c, level);
				if (lowerBound < minimum) {
					levelAtMinimum = level;
					minimum = lowerBound;
				}
				System.out.println("\n starting with level: " + level);
				System.out.print("\t");
				System.out.println(lowerBound);
			}
			System.out.println("\n++++++++++   --->   at level " + levelAtMinimum + ", minimum found: " + minimum);
		}

		System.out.println("\n\n END");
	}

	//

	public static void main(String[] args) {
		ReservationReductionPercentage c;
		ReservationReductionCostFactor f_c;
		ReservationReductionCostFixed m_c;

		c = (Integer level) -> level * 5;
		m_c = () -> 112;

		f_c = (Integer level, Integer reservationAmount) -> level * 20; // or just 4000
		doAndPrintTest(c, f_c, m_c);
		/**
		 * percentuale di riserva ... livello ideale e mana minimo
		 * <ul>
		 * <li>15 .. l: 11, m: 2824</li>
		 * <li>25 .. l: 11, m: 1694</li>
		 * <li>35 .. l: 11, m: 1210</li>
		 * <li>40 .. l: 11, m: 1059</li>
		 * <li>50 .. l: 11, m: 847</li>
		 * <li>75 .. l: 11, m: 564</li>
		 * </ul>
		 */

		f_c = (Integer level, Integer reservationAmount) -> 200;
		doAndPrintTest(c, f_c, m_c);

		/**
		 *
		 * <ul>
		 * <li>15 .. l: 20, m: 2080</li>
		 * <li>25 .. l: 11, m: 1248</li>
		 * <li>35 .. l: 11, m: 891</li>
		 * <li>40 .. l: 11, m: 780</li>
		 * <li>50 .. l: 11, m: 624</li>
		 * <li>75 .. l: 11, m: 416</li>
		 * </ul>
		 */
	}

	/***
	 * 2022-04-02
	 * <p>
	 *
	 * Per la meccanica di "riservare mana/vita/etc come costo di una abilita'",
	 * progettare un'altra abilita' che riduca l'ammontare di risorsa riservata.<br>
	 * Tale abilita' applica delle riduzioni alla percentuale di "riservamento" del
	 * mana, piu' dei costi circa fissi come prezzo, che dipendono dal livello della
	 * abilita' allocata. Conviene quando si ha abbastanza mana. La formula e': <br>
	 * <br>
	 * m = mana max<br>
	 * r = fattore di "riservamento" del mana \in [0, 1] ( 25% -> 0.25 )<br>
	 * c = fattore di conversione/riduzione<br>
	 * f_c = costo fisso, da riservare, per la conversione<br>
	 * m_c = costo fisso, da riservare, per la conversione MA dipendente dal fattore
	 * di conversione<br>
	 * <p>
	 * Quindi il mana rimanente e': <<br>
	 * m*(1-r) <= m*(1 - r*(1-c)) - c*f_c - m_c<br>
	 *
	 * m*[ 1-r - (1 - r*(1-c)) ] <= -c*f_c + m_c <br>
	 *
	 * m*[ -r + r*(1-c) ] <= ..<br>
	 * m*r*(-c) <= ...<br>
	 * m <= (-c*f_c - m_c) / ( -r*c )<br>
	 * m <= (c*f_c + m_c) / ( r*c )<br>
	 * <br>
	 * <p>
	 * ipotizando che "c" dipenda dal livello allocato "l \in N | 0 <= l <= 19"
	 * come<br>
	 * c = c_(l) := l * 0.05<br>
	 * f_c = l*20 ... (in alternativa, semplicemente 4000)<br>
	 * m_c = 112<br>
	 * <br>
	 * <br>
	 * diventa:<br>
	 * m <= ( l*0.05 * l*20 + 112) / (r*(2-l*0.05))<br>
	 * m <= ( l^2 + 112 ) / (r*2 - r*l*0.05)<br>
	 * <p>
	 * example: <br>
	 *
	 */

	public static int minMana(int reservePercentage, ReservationReductionPercentage costFunction__c,
			ReservationReductionCostFactor fixedCostDependent__fc, ReservationReductionCostFixed fixedCostSupplier,
			int level) {
		int denominator;
		Integer c, f_c, m_c;
		c = costFunction__c.apply(level);
		f_c = fixedCostDependent__fc.apply(level, c);
		m_c = fixedCostSupplier.get();
		/**
		 * (c*f_c + m_c) / ( r*c )<br>
		 * ... r and c are percentages<br>
		 * ([c/100]*f_c + m_c) / ( [r/100]*(2-[c/100]) )<br>
		 * ([c/100]*f_c + m_c) / ( [r/100]*([200/100 - c/100]) )<br>
		 * ([c/100]*f_c + m_c) / ( [r/100]*([200 - c]/100) )<br>
		 * ([c/100]*f_c + m_c) / ( (r*[200 - c])/10000 )<br>
		 * [10000*([c/100]*f_c + m_c)] / (r*c)<br>
		 * ( c*100*f_c + [10000*m_c] ) / (r*[200 - c])<br>
		 */
		denominator = reservePercentage * c;
		if (denominator == 0) { return Integer.MAX_VALUE; }
		return (100 * c * f_c + 10000 * m_c) / denominator;
	}

	//

	/**
	 * Given a level (possibly not influent at all), it provides the percentage of
	 * resource reserved reduction.
	 */
	public static interface ReservationReductionPercentage extends Function<Integer, Integer> {
	}

	/**
	 * Given, in order, a level and the value returned by a
	 * {@link ReservationReductionPercentage}, it provides the cost percentage of
	 * resource reserved reduction.
	 */
	public static interface ReservationReductionCostFactor extends BiFunction<Integer, Integer, Integer> {
	}

	/**
	 * Just a fixed amount.
	 */
	public static interface ReservationReductionCostFixed extends Supplier<Integer> {
	}
}