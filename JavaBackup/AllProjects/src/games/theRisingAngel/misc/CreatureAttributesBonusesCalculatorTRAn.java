package games.theRisingAngel.misc;

import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import games.theRisingAngel.enums.AttributesTRAn;
import tools.WeightedSetOfRandomOutcomes;

// TODO : aggiornare la tabella qua sotto
/**
 * Bonuses are calculated this way (and should rely only on "basic attributes"
 * (luck may vary)
 * <ul>
 * <li>Every increments and reductions are integers. Fractions are computed
 * away.</li>
 * <li>EVERY probability and multipliers are considered as "per thousand" (i.e.:
 * "%/10"), meaning that those values must be divided by 1000 or considered in a
 * context where are added or subtracted to values are ranging in
 * [-1000;1000]</li>
 * <li>CriticalProbability: it's meant as a "n/10 %" (i.e.: it's not a "percent"
 * but "per thousand"). Luck helps providing each point half of that
 * "probability unit".</li>
 * <li>CriticalMultiplierPercentage: it should be divided by 10.0, at least (or
 * 100)</li> values "f") are to be meant: "add one point of the original value
 * for each 1/f points of this".</li>
 * <li>Strength:
 * <ul>
 * <li>+1/2 LifeMax</li>
 * <li>+0.25 PhysicalDamageReduction</li>
 * <li>+1.25 PhysicalDamageBonus</li>
 * <li>+0.25 CriticalMultiplierPercentage</li>
 * <li>+0.25 LifeRegen</li>
 * <li>+0.25 PhysicalProbabilityPerThousandHit
 * </ul>
 * </li>
 * <li>Constitution:
 * <ul>
 * <li>+5 LifeMax</li>
 * <li>+0.25 LifeRegen</li>
 * <li>+0.25 PhysicalDamageBonus</li>
 * <li>+0.75 PhysicalDamageReduction</li>
 * <li>+0.5 MagicalDamageReduction</li>
 * <li>+0.25 CriticalProbabilityPerThousandAvoid
 * </ul>
 * </li>
 * <li>Health:
 * <ul>
 * <li>+2 LifeMax</li>
 * <li>+0.75 LifeRegen</li>
 * <li>+ 0.125 ManaRegen</li>
 * <li>+0.03125 Luck</li>
 * </ul>
 * </li>
 * <li>Defense:
 * <ul>
 * <li>+1 LifeMax</li>
 * <li>+1 PhysicalDamageReduction</li>
 * <li>+1 MagicalDamageReduction</li>
 * <li>+0.5 CriticalProbabilityPerThousandAvoid</li>
 * </ul>
 * </li>
 * <li>Dexterity:
 * <ul>
 * <li>+1 PhysicalProbabilityPerThousandAvoid.
 * <li>+0.5 PhysicalProbabilityPerThousandHit</li>
 * <li>+0.25 PhysicalDamageReduction</li>
 * <li>+0.25 MagicalDamageReduction</li>
 * <li>+0.25 CriticalProbabilityPerThousand</li>
 * <li>+0.03125 Luck</li>
 * <li>+1 CriticalProbabilityPerThousandAvoid</li>
 * </ul>
 * </li>
 * <li>Precision:
 * <ul>
 * <li>+1 PhysicalProbabilityPerThousandHit</li>
 * <li>+0.5 PhysicalProbabilityPerThousandAvoid</li>
 * <li>+0.25 PhysicalDamageBonus</li>
 * <li>+0.125 MagicalDamageBonus</li>
 * <li>+1 CriticalProbabilityPerThousand</li>
 * <li>+0.5 CriticalMultiplierPercentage</li>
 * <li>+0.5 CriticalProbabilityPerThousandAvoid</li>
 * </ul>
 * </li>
 * <li>Intelligence:
 * <ul>
 * <li>+0.75 ManaMax</li>
 * <li>+1/16 ManaRegen</li>
 * <li>+1 MagicalDamageBonus</li>
 * <li>+0.25 MagicalDamageReduction</li>
 * <li>+0.25 CriticalProbabilityPerThousand</li>
 * <li>+0.25 CriticalMultiplierPercentage</li>
 * <li>+0.0625 Luck</li>
 * <li>+0.5 CriticalProbabilityPerThousandAvoid</li>
 * <li>+0.5 PhysicalProbabilityPerThousandHit</li>
 * </ul>
 * </li>
 * <li>Wisdom:
 * <ul>
 * <li>+1.5 ManaMax</li>
 * <li>+0.25 ManaRegen</li>
 * <li>+0.5 MagicalDamageBonus</li>
 * <li>+0.5 MagicalDamageReduction</li>
 * <li>+0.125 CriticalProbabilityPerThousand</li>
 * <li>+0.125 CriticalMultiplierPercentage</li>
 * <li>+0.125 LifeMax</li>
 * <li>+1/32 LifeRegen</li>
 * <li>+0.0625 Luck</li>
 * <li>+0.125 CriticalProbabilityPerThousandAvoid</li>
 * <li>+0.25 PhysicalProbabilityPerThousandHit</li>
 * </ul>
 * </li>
 * <li>Faith:
 * <ul>
 * <li>+4 ManaMax</li>
 * <li>+0.75 ManaRegen</li>
 * <li>+0.125 Luck
 * </ul>
 * </li>
 * <li>Velocity: "space-unit per time-unit", corresponds to the numbers of
 * sub-unit expressed in
 * {@link GModalityTRAnBaseWorld#SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN}</li>
 * <li>Luck: as for CriticalProbability, it's not meant as a percentage, but
 * "per 10 thousand", so it should divided by 100. Used to alter
 * {@link WeightedSetOfRandomOutcomes} in some way.<br>
 * It receives the following bonuses (fraction for each point):
 * <ul>
 * <li>Faith: 1/8</li>
 * <li>Intelligence and Wisdom: 1/16</li>
 * <li>Health and Dexterity: 1/32</li>
 * </ul>
 * </li>
 * </ul>
 */
// * <li></li>
public class CreatureAttributesBonusesCalculatorTRAn implements CreatureAttributesBonusesCalculator {

	protected CreatureAttributes creatureAttributesSet;

	@Override
	public CreatureAttributes getCreatureAttributesSet() {
		return creatureAttributesSet;
	}

	@Override
	public void setCreatureAttributesSet(CreatureAttributes creatureAttributesSet) {
		this.creatureAttributesSet = creatureAttributesSet;
	}

	@Override
	public void markCacheAsDirty() {
	}

	@Override
	public int getBonusFor(AttributeIdentifier identifier) {
		int v;
		AttributesTRAn a;
		CreatureAttributes c;
		if (identifier instanceof AttributesTRAn) {
			a = (AttributesTRAn) identifier;
		} else {
			a = AttributesTRAn.ALL_ATTRIBUTES[identifier.getIndex()];
		}
		c = creatureAttributesSet;
		// note: use upper case characters for open parenthesis, lower cases ones for
		// closings
		switch (a) {
			case Luck: {
				v = (+c.getValue(AttributesTRAn.Faith)// start 1/8
						+ (( // start 1/16
						+(( // start 1/32
						+c.getValue(AttributesTRAn.Dexterity) + c.getValue(AttributesTRAn.Health))//
								>> 1) // end 1/32
								+ c.getValue(AttributesTRAn.Wisdom) + c.getValue(AttributesTRAn.Intelligence)) //
								>> 1)) // end 1/16
						>> 3; // end 1/8
				break;
			}
			case LifeMax: {
				int cost, str, h;
				cost = c.getValue(AttributesTRAn.Constitution);
				str = c.getValue(AttributesTRAn.Strength);
				h = c.getValue(AttributesTRAn.Health);
				// V4
				/**
				 * 2*h + 5*cost + str/2 + wisdom/8 <br>
				 * By providing a total of 60 points, which is 15 points each attributes, it
				 * produces a bonus of 114.375
				 */
				v = (((c.getValue(AttributesTRAn.Wisdom) >> 2) + str) >> 1) //
						+ (cost + (((cost << 1) + h) << 1) //
						);
				// V3
				/** 4.5*h + 10*cost + 2*str + wisdom/4 */
				// v = (((c.getValue(AttributesTRAn.Wisdom) >> 1) + h) >> 1) //
				// + ((str + cost + (((cost << 1) + h) << 1)// str + h-2 cost*5
				// ) << 1); // *2
				// v2
				// v = +c.getValue(AttributesTRAn.Defense) //
				// + (( // *2
				// +h //
				// + (cost + (cost << 2)) // = *5
				// + str) //
				// << 1)
				// + //
				// (( //
				// +((c.getValue(AttributesTRAn.Wisdom) + str) >> 1) //
				// + h //
				// ) >> 1);
				break;
			}
			case LifeRegen: {
				int h, cons;
				// V4
				h = c.getValue(AttributesTRAn.Health);
				cons = c.getValue(AttributesTRAn.Constitution);
				/** h*0.375 (cioe' 3/8) + (cons/12) + (str/16) + (wisdom/32) */
				v = (//
				(((c.getValue(AttributesTRAn.Wisdom) >> 1)//
						+ c.getValue(AttributesTRAn.Strength))//
						>> 2) // fatta la parte str+wis , /4
						+ (cons / 3) + //
						(((h << 1) + h) >> 1) // h*3/2
				) >> 2 // another /4
				;
				// V3
				// h = c.getValue(AttributesTRAn.Health);
				// /** h*0.75 + (cons/4) + (str/16) + (wisdom/32) */
				// v = (//
				// (((c.getValue(AttributesTRAn.Wisdom) >> 1)//
				// + c.getValue(AttributesTRAn.Strength))//
				// >> 2) // fatta la parte str+wis , /4
				// + c.getValue(AttributesTRAn.Constitution) + //
				// (h << 1) + h // h*3
				// ) >> 2 // another /4
				// ;
				// V2
				// v = +((// 0.25
				// +(c.getValue(AttributesTRAn.Wisdom) >> 1) //
				// + c.getValue(AttributesTRAn.Strength)) >> 2)//
				// + c.getValue(AttributesTRAn.Constitution) //
				// + (c.getValue(AttributesTRAn.Health) << 1);
				break;
				// V1
				// +((c.getValue(AttributesTRAn.Health) +
				// (c.getValue(AttributesTRAn.Constitution) >> 1)
				// + (c.getValue(AttributesTRAn.Strength) / 3) // >> 2)
				// + ((c.getValue(AttributesTRAn.Wisdom) + c.getValue(AttributesTRAn.Faith)) >>
				// 3)) >> 3);
				// v >>= 1; // to highbreak;
			}
			case PhysicalDamageReduction: {
				int cons;
				// def + (cons*3 +str+dex)/4
				cons = c.getValue(AttributesTRAn.Constitution);
				v = +c.getValue(AttributesTRAn.Defense) //
						+ (( //
						+(cons + (cons << 1)) // *3
								+ c.getValue(AttributesTRAn.Strength) //
								+ c.getValue(AttributesTRAn.Dexterity) //
						) >> 2);
				// ( //
				// +c.getValue(AttributesTRAn.Defense) //
				// + (((c.getValue(AttributesTRAn.Strength) >> 1) +
				// c.getValue(AttributesTRAn.Constitution)
				// + (c.getValue(AttributesTRAn.Health) >> 2)) >> 1)
				// + (c.getValue(AttributesTRAn.Dexterity) / 5)//
				// ) >> 1;
				break;
			}
			case PhysicalDamageBonus: {
				int str, cost;
				str = c.getValue(AttributesTRAn.Strength);
				cost = c.getValue(AttributesTRAn.Constitution);
				/**
				 * 1.5*str + cost*0.625(5/8) + prec/4 + (wisdom+dext+int)/32 <br>
				 * By providing a total of 60 points, which is 10 points each attributes,
				 * produces a bonus of 23,4375
				 */
				v = str + ( // A
				(str + cost // B
						+ ( // C
						(c.getValue(AttributesTRAn.Precision) // D
								+ ((cost // E, F
										+ ((c.getValue(AttributesTRAn.Wisdom) + c.getValue(AttributesTRAn.Intelligence)
												+ c.getValue(AttributesTRAn.Dexterity)) // G, H, g
												>> 2) // h
								) >> 1) // f, e
						) >> 1) // d, c
				) >> 1); // b , a
				// ( //
				// +((c.getValue(AttributesTRAn.Strength) +
				// c.getValue(AttributesTRAn.Constitution)) >> 1)
				// + ((c.getValue(AttributesTRAn.Precision) +
				// c.getValue(AttributesTRAn.Intelligence)) / 5)
				// + (c.getValue(AttributesTRAn.Wisdom) >> 3)//
				// ) >> 1;
				break;
			}
			case ManaMax: {
				// V4
				/**
				 * faith*2 + wisdom + int/2<br>
				 * By providing a total of 60 points, which is 20 points each attributes,
				 * produces a bonus of 60
				 */
				v = (c.getValue(AttributesTRAn.Faith) << 1) + (// A
				( // B
				c.getValue(AttributesTRAn.Wisdom) //
						+ (c.getValue(AttributesTRAn.Intelligence) >> 1) // C, c
				) // b
						>> 1); // a
				// V3
				// int wis, intell;
				// wis = c.getValue(AttributesTRAn.Wisdom);
				// intell = c.getValue(AttributesTRAn.Intelligence);
				// /** faith*4 + wisdom*1.5 + int*0.75 */ // == faith<<2 + wis*6/4 + int*3/4
				// v = (wis << 1) + intell; // 2*wis + intell
				// v = (c.getValue(AttributesTRAn.Faith) << 2) + //
				// (((v << 1) + v)// == *3
				// >> 2) // == /4
				// ;
				// int faith;
				// faith = c.getValue(AttributesTRAn.Faith);
				// v = c.getValue(AttributesTRAn.Intelligence) //
				// + ((faith << 1) + (c.getValue(AttributesTRAn.Wisdom)) << 1) //
				// + (faith >> 2);
				break;
			}
			case ManaRegen: {
				// V3
				int intell;
				intell = c.getValue(AttributesTRAn.Intelligence);
				/** faith*0.25 + wis/8 + health/16 + int*3/32 */
				v = (//
				(// int + health + wis
				(( // int + health
				((intell + (intell << 1)) >> 1) // *1.5
						+ c.getValue(AttributesTRAn.Health) //
				) >> 1) //
						+ (c.getValue(AttributesTRAn.Wisdom) //
						) >> 1) + //
						c.getValue(AttributesTRAn.Faith) //
				) >> 2; // /4
				// v2
				// int f;
				// health/8 + wisdom/4 + faith/2
				// v = //
				// +(((c.getValue(AttributesTRAn.Wisdom) //
				// + (c.getValue(AttributesTRAn.Health) >> 1)//
				// ) >> 1) //
				// + c.getValue(AttributesTRAn.Faith) ) >> 1 //
				// ;
				break;
				// ;}case ManaRegen : {v= ((c.getValue(AttributesTRAn.Faith))
				// + (c.getValue(AttributesTRAn.Wisdom) >> 1)
				// + (c.getValue(AttributesTRAn.Intelligence) >> 2)
				// + (c.getValue(AttributesTRAn.Health) >> 3)) / 10;break;
			}
			case MagicalDamageBonus: {
				/**
				 * 1.25*int + wisdom/2 + faith/4 + (prec+dext)/32 <br>
				 * By providing a total of 60 points, which is 12 points each attributes,
				 * produces a bonus of 24,75
				 */
				v = c.getValue(AttributesTRAn.Intelligence) //
						+ (( //
						+(c.getValue(AttributesTRAn.Precision) >> 2) //
								+ c.getValue(AttributesTRAn.Wisdom)) >> 1);
				// /** V3: int + wisd/2 + prec/8 */
				// v = c.getValue(AttributesTRAn.Intelligence) //
				// + (( //
				// +(c.getValue(AttributesTRAn.Precision) >> 2) //
				// + c.getValue(AttributesTRAn.Wisdom)) >> 1);
				// ( //
				// (c.getValue(AttributesTRAn.Precision) / 5) +
				// c.getValue(AttributesTRAn.Intelligence)
				// + ((c.getValue(AttributesTRAn.Wisdom) + (c.getValue(AttributesTRAn.Faith) >>
				// 1)) >> 1)//
				// ) >> 1;
				break;
			}
			case MagicalDamageReduction: {
				// def + (cons+wis)/2 + (int+dex)/4
				v = +c.getValue(AttributesTRAn.Defense) //
						+ (( //
						+c.getValue(AttributesTRAn.Constitution) + c.getValue(AttributesTRAn.Wisdom) + ((//
						+c.getValue(AttributesTRAn.Intelligence) + c.getValue(AttributesTRAn.Dexterity)) >> 1) //
						) >> 1);
				// ( //
				// +(c.getValue(AttributesTRAn.Dexterity) / 5) +
				// c.getValue(AttributesTRAn.Intelligence)
				// + c.getValue(AttributesTRAn.Wisdom) //
				// + ((c.getValue(AttributesTRAn.Defense) >> 2) +
				// (c.getValue(AttributesTRAn.Faith)) >> 1)//
				// ) >> 1;
				break;
			}
			case CriticalMultiplierPercentageBonus: {
				v = +((//
				+(c.getValue(AttributesTRAn.Wisdom) >> 1) //
						+ c.getValue(AttributesTRAn.Strength) //
						+ c.getValue(AttributesTRAn.Precision) //
						+ c.getValue(AttributesTRAn.Intelligence) //
				) >> 2) //
				;
				// (c.getValue(AttributesTRAn.Strength) << 1) +
				// c.getValue(AttributesTRAn.Intelligence)
				// + c.getValue(AttributesTRAn.Wisdom) + c.getValue(AttributesTRAn.Luck);
				break;
			}
			case CriticalProbabilityPerThousandHit: {
				v = +c.getValue(AttributesTRAn.Precision) //
						+ ((// start 1/2
						+((// start 1/4
						+((c.getValue(AttributesTRAn.Wisdom)) >> 1) // 1/8
								+ c.getValue(AttributesTRAn.Dexterity) //
								+ c.getValue(AttributesTRAn.Intelligence) //
						) >> 1) // end 1/4
								+ c.getValue(AttributesTRAn.Luck)) >> 1); // end 1/2
				//
				// ((c.getValue(AttributesTRAn.Precision) << 1) //
				// + c.getValue(AttributesTRAn.Dexterity) //
				// + ((+c.getValue(AttributesTRAn.Intelligence) //
				// + c.getValue(AttributesTRAn.Wisdom)) >> 1)//
				// ) + c.getValue(AttributesTRAn.Luck);
				break;
			}
			case CriticalMultiplierPercentageReduction: {
				v = +c.getValue(AttributesTRAn.Defense) //
						+ ((//
						+c.getValue(AttributesTRAn.Constitution) //
								+ c.getValue(AttributesTRAn.Intelligence) //
								+ c.getValue(AttributesTRAn.Luck) //
						) >> 1);
				// ( //
				// +c.getValue(AttributesTRAn.Defense) + c.getValue(AttributesTRAn.Constitution)
				// //
				// + ((+(c.getValue(AttributesTRAn.Strength) >> 1) +
				// (c.getValue(AttributesTRAn.Dexterity) >> 2)) >> 1)//
				// ) >> 3;
				break;
			}
			case CriticalProbabilityPerThousandAvoid: {
				v = +c.getValue(AttributesTRAn.Dexterity) //
						+ ((// 1/2
						+c.getValue(AttributesTRAn.Defense) //
								+ c.getValue(AttributesTRAn.Intelligence) //
								+ c.getValue(AttributesTRAn.Luck) //
								+ c.getValue(AttributesTRAn.Precision) //
								+ ((// 1/4
								+c.getValue(AttributesTRAn.Constitution) //
										+ ((+c.getValue(AttributesTRAn.Wisdom)) >> 1) // 1/8
								) >> 1)) //
								>> 1);

				// (//
				// +c.getValue(AttributesTRAn.Constitution) //
				// + ((//
				// +c.getValue(AttributesTRAn.Dexterity)//
				// + ((c.getValue(AttributesTRAn.Defense) //
				// + ((c.getValue(AttributesTRAn.Precision) +
				// c.getValue(AttributesTRAn.Intelligence)) //
				// >> 1))//
				// >> 1))//
				// >> 1)//
				// );
				break;
			}
			case PhysicalProbabilityPerThousandHit: {
				v = +c.getValue(AttributesTRAn.Precision) //
						+ ((// 1/2
						+c.getValue(AttributesTRAn.Dexterity) //
								+ c.getValue(AttributesTRAn.Luck) //
								+ c.getValue(AttributesTRAn.Intelligence)//
								+ ((// 1/4
								+c.getValue(AttributesTRAn.Strength) //
										+ c.getValue(AttributesTRAn.Wisdom)) >> 1) // 1/8
						) >> 1);
				// +c.getValue(AttributesTRAn.Precision)
				// + c.getValue(AttributesTRAn.Luck)
				// + ((c.getValue(AttributesTRAn.Dexterity) +
				// (c.getValue(AttributesTRAn.Strength) >> 1)
				// + (c.getValue(AttributesTRAn.Intelligence) >> 2)) >> 1);
				break;
			}
			case PhysicalProbabilityPerThousandAvoid: {
				v = +c.getValue(AttributesTRAn.Dexterity) //
						+ ((// 1/2
						+c.getValue(AttributesTRAn.Precision) //
								+ c.getValue(AttributesTRAn.Luck) //
								+ ((// 1/4
								+c.getValue(AttributesTRAn.Strength) //
										+ c.getValue(AttributesTRAn.Wisdom) //
										+ c.getValue(AttributesTRAn.Intelligence) //
								) >> 1))//
								>> 1);
				// +c.getValue(AttributesTRAn.Dexterity) + c.getValue(AttributesTRAn.Luck)
				// + ((c.getValue(AttributesTRAn.Precision) +
				// (c.getValue(AttributesTRAn.Strength) >> 1)
				// + (c.getValue(AttributesTRAn.Intelligence) >> 2)) >> 1);
				break;
			}
			case MagicalProbabilityPerThousandHit: {
				v = +c.getValue(AttributesTRAn.Wisdom) //
						+ ((// 1/2
						+c.getValue(AttributesTRAn.Intelligence) //
								+ c.getValue(AttributesTRAn.Luck) //
								+ c.getValue(AttributesTRAn.Dexterity)//
								+ ((// 1/4
								+c.getValue(AttributesTRAn.Strength) //
										+ c.getValue(AttributesTRAn.Precision)) >> 1) // 1/8
						) >> 1);
				// +c.getValue(AttributesTRAn.Wisdom) + c.getValue(AttributesTRAn.Luck)
				// + ((c.getValue(AttributesTRAn.Intelligence) +
				// (c.getValue(AttributesTRAn.Faith) >> 1)
				// + (c.getValue(AttributesTRAn.Precision) >> 2)) >> 1);
				break;
			}
			case MagicalProbabilityPerThousandAvoid: {
				v = +c.getValue(AttributesTRAn.Wisdom) //
						+ ((// 1/2
						+c.getValue(AttributesTRAn.Intelligence) //
								+ c.getValue(AttributesTRAn.Luck) //
								+ ((// 1/4
								+c.getValue(AttributesTRAn.Strength) //
										+ c.getValue(AttributesTRAn.Dexterity) //
										+ c.getValue(AttributesTRAn.Precision) //
								) >> 1))//
								>> 1);
				// +c.getValue(AttributesTRAn.Luck) + c.getValue(AttributesTRAn.Intelligence)
				// + ((c.getValue(AttributesTRAn.Wisdom) + (c.getValue(AttributesTRAn.Faith) >>
				// 1)
				// + (c.getValue(AttributesTRAn.Dexterity) >> 3)) >> 1);
				break;
			}
			case VelocityAttackStrikePercentage: {
				v = +((c.getValue(AttributesTRAn.Strength) >> 1) + c.getValue(AttributesTRAn.Dexterity) >> 4);
				break;
			}
			case VelocitySpellCastPercentage: {
				v = +((c.getValue(AttributesTRAn.Intelligence) >> 1) + c.getValue(AttributesTRAn.Dexterity) >> 4);
				break;
			}
			case Velocity: {
				v = ((//
				(((c.getValue(AttributesTRAn.Constitution) + c.getValue(AttributesTRAn.Strength)) << 1) / 3)
						+ (c.getValue(AttributesTRAn.Dexterity) << 1)//
				) / 5) //
				;
				break;
			}
			case CostCastReductionPercentage: {
				final int MAX_REDUCTION = 50;
				int sum;
				sum = (c.getValue(AttributesTRAn.Intelligence) + c.getValue(AttributesTRAn.Wisdom)) >> 2;
				if (sum <= 0) {
					v = 0;
				} else if (sum >= MAX_REDUCTION) {
					v = MAX_REDUCTION;
				} else {
					v = MAX_REDUCTION - (MAX_REDUCTION / sum);
				}
				break;
			}
			case StaminaMax: {
				v = ( //
				((c.getValue(AttributesTRAn.Health) >> 1) + (c.getValue(AttributesTRAn.Strength) >> 1)) //
						+ c.getValue(AttributesTRAn.Constitution) //
				) >> 1;
				break;
			}
			case StaminaRegen: {
				v = ((//
				(c.getValue(AttributesTRAn.Health) >> 1) + c.getValue(AttributesTRAn.Strength)
				//
				) >> 2) //
						+ (c.getValue(AttributesTRAn.Constitution) / 6);
				break;

			}
			default:
				v = 0;
		}
		return v;
	}
}