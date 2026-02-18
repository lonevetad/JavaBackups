package game;

import game.statistiche.StatistichePersonaggio;
import game.statistiche.StatisticheStandard;

import java.util.LinkedHashMap;

/**
 * Banale classe che stocca la lista di tutte le tipologie di personaggi
 * esistenti.
 * */
public class CharactersList extends MyObject implements StatisticheStandard {

	public CharactersList() {
		// TODO Auto-generated constructor stub
	}

	static { // initializer
		sillyMethod_AntiBug_NullPointerException();
	}

	public static void sillyMethod_AntiBug_NullPointerException() {
		System.out.println("Character list initializer");
	}

	/*
	 * protected static final String[] charactersTypes = { "Engineer", "Wizard",
	 * "Druid", "Monk", "Paladin", "Elf", "Robber", "Barbarian", "Orc", "Human"
	 * };
	 */
	// TODO enum CharactersTypes
	public static enum CharactersTypes {
		Engineer(
				"L'ineguagliabile intelligenza, tecnica e razionalità sono le caratteristiche principali di ogni ingeniere."
						+ " Lunghi anni di studio gli hanno conferit una profonda conoscenza del mondo, comprendendo le regole ed i meccanismi del suo funzionamento."
						+ " Nulla sfugge alla sua mente scolpita nella matematica e tutto ha una spiegazione e una soluzione."
						+ " Non esiste strumento che non possa rutilizzare o realizzare."
						+ "\nPer far fronte alle sue debolezze fisiche, l'ingeniere si avvale di macchine che lo aiutano in ogni faccenda."
						+ " Unica pecca : non crede in nulla di metafisico, considerando ciò come un mero insieme di concetti astratti, fuorvianti e tutt'altro che razionali e sani."),
		//
		Wizard(
				"Il mago è una personaggio enigmatico : è dotato di grande intelligenza, la sua crescente cultura spazia in ogni campo, ma non si lascia dominare dalla razionalità  pura. Egli avverte le forze spirituali che permeano la realtà  e da essa trova la forza per concretizzare la sua ferrea volontà .\nLa magia è appunto la sua arma principale che sfrutta, assieme ad una variegata conoscenza del mondo naturale e alla sua vissuta saggezza, per svolgere le mansioni quotidiane, difendersi da ogni pericolo e danzare alle porte del mondo metafisico per cogliere i segreti inintelleggibili.\nDescrivendolo con una poesia scritta da me:\n\nSpiro arcano:"
						+ "\n\n"
						+ "-) Sempre fu e sarà cara la casa, ove da folle\n"
						+ "-) a illuminato la sudata strada diparte\n"
						+ "-) da terra all'orizzonte, qual guardo che si volse e include,\n"
						+ "-) ora, dopo sedute coi bianchi maestri, interminati\n"
						+ "-) spazi di visioni colmi, oltre gli umani\n"
						+ "-) limiti, e profondissimo sapere fa liete\n"
						+ "-) membra e mente, e nulla finto appare. In tal loco\n"
						+ "-) in pace la lettura cura, e il tempo lento\n"
						+ "-) scorre. Dei sussurri tra queste pagine quello\n"
						+ "-) di Minerva colgo, e tra infinite lettere in sì foce\n"
						+ "-) del sapere scoverto il pensiero eterno\n"
						+ "-) danza. Morte e vita in armonia lucente\n"
						+ "-) senso mostrano a chi osa sbirciar, con cor e testa.\n"
						+ "-) Nell'immenso mar di canoscenza un dio\n"
						+ "-) trassimi dall'annegar, e dolce mostra il potere d'amare.")
		//
		, Druid(
				"Questa strana figura vive a stretto contatto con la natura, ignorando ogni forma di tecnologia e aspetto di quella che riteniamo la civiltà avanzata. "
						+ "Egli potrà sembrare molto primitivo, ma è dotato di una buona intelligenza ed una profonda conoscienza della Natura, che gli permette di curarsi con facilità ed invocare piante ed animali in suo aiuto.")
		//
		, Elf(
				"Questa creatura magica dall'aspetto agile e slanciato è in stretta comunicazione con la Natura, in tutta somiglianza con il Druido. Come questi, fanno molto affidamento alle forze magiche."
						+ "\nDotati di una precisione impareggiabile con le armi a distanza, questi esseri sanno difendersi accuratamente nonostante disprezzino la lotta fisica. La loro gracilità è compensata da un'atleticità difficilmente pareggiabile.")
		//
		, Monk(
				"Il monaco è una persona dedita alla religione, in essa crede ciecamente, per essa ha offerto la sua intera vita e con essa trova la forza per affrontare le vicissitudini quotdiane."
						+ " Lui è un esperto di arti marziali, che gli conferisce una grande agilità, una grandissima resistenza al dolore e una accurata conoscienza dell'anatomia degli animali, in particolare quella umana."
						+ " La sua ferrea e totale devozione gli conferiscono un potere magico pressochè illimitato, ma ben poco gli importa della tecnologia e di nuovi punti di vista.")
		//
		, Paladin(
				"L'onore, la purezza e la legalità si sono incarnate nella figura del Paladino. Abili guerrieri, impiegano tutte le loro forze nella lotta contro ogni forma di eresia e di empietà."
						+ " Nella loro fede cieca trovano forza e motivazione per proseguire nella loro missione, però non sono in grado di pensare diversamente dal credo : "
						+ "non esiste altra verità oltre alla loro religione, e ciò li conduce a decisioni spesso prive di senno."
						+ "\n E' un peccato che le loro potenzialità mentali siano strettamente confinate, però sono degli ottimi guardiani, saldi e leali.")
		//
		, Robber(
				"Viscido. Scaltro, furbo e malevolo. Opportunista, subdolo, schivo e disonorevole. Tanti sono gli appellativi, tutti negativi, che additano al povero ladro, suo malgrado. "
						+ "Nessuno però accenna mai alle sue potenzialità : il disprezzo provato dagli altri si concretizza spesso in manifestazioni pubbliche di violenza nei suoi confronti, ma con gli anni ha imparato ad evitare i guai, fuggendo con grande agilità e astuzia."
						+ "\nQuesto personaggio non si lascia mai scappare un'opportunità per trarre vantaggio, soprattutto quando il nemico scopre uno de punti deboli. Infatti tende ad attaccare da lontano e quando il nemico è distratto."
						+ "\nInoltre, la deplorevole mansione, oltre a farlo campare, gli ha aguzzato notevolmente la vista : è in grado di scovare anche i tesori meglio nascosti."
						+ "\nLe sue statistiche di crescita sono mediamente più basse di tutti, ma ha un vantaggio impareggiabile, spesso fondamentale.")
		//
		, Barbarian(
				"Questa montagna di muscoli è un guerriero spietato, forgiato in innumerevoli battaglie nelle quali ha mietuto vittime di ogni stazza come se fosse grano. "
						+ "Non teme i rivali, i pericoli la morte e abbatte ogni ostacolo con la violenza pura."
						+ "\nLa sua forza è al limite del disumano, come la sua resistenza alla fatica e alla capacità di ignorare una freccia nella gamba. Tuttavia, le sue capacità celebrali sono primitive : non puoi di certo dilettarti con lui dialogando di filosofia. A meno che si tratti di uccidere.")
		//
		, Orc(
				"WHHAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHRRRRRRRGGGGG !\n"
						+ "Mai parole furono più sagge per un orco. Lui è la conferma che le capacitò intellettive sono inversamente proporzionali alla massa muscolare. Da notare, lui è una bestia alta quanta un umano e mezzo e pesante il triplo, di pura carne e potenza."
						+ "Questa enorme mole è lenta e poco agile, oltre che imprecisa. La bassa intelligenza di certo non lo rende un avversario temibile, ma l'indomabile forza si."
						+ " Questa macchina da guerra è infatti in grado di scaraventare via i suoi nemici come se fossero dei pupazzi e di assorbire ferite di ogni sorta.")
		//
		, Human(
				"Uno, nessuno, centomila. C'è, ce n'è un altro, ma che differenza fa? Non distinguo il primo dall'ultimo. Tutti uguali, tutti massificati, tutti prevedibili. Tutti grigi, privi di differenze di spicco, di particolarità, di tono."
						+ "\nQuesto è il frutto della società piena di finti sogni: una massa confusa di volti spenti e meccanici, privi di ogni moto proprio, di creatività, di qualità o difetti eccezionali.\n"
						+ "Tutti tranne lui: simile a tutto e a tutti, ma il fato gli ha riservato qualcosa ...")
		//
		, Bloodgus(
				"Quest'essere informe è un magus adoratore del dolore e del sangue. E' sadico masochista dotato di una grande vita, messa però tutta al servigio delle sue divinità :"
						+ " viene da loro premiato con grandi capacità offensive accettando, in cambio, perdere vita continuamente e subire molti più danni del normale."
						+ " Però, quando è lui la causa di dolore altrui, viene pervaso da un profondo appagamento.");

		private int index = -1;
		private String description = "";

		CharactersTypes(String des) {
			this();
			description = des;
		}

		CharactersTypes() {
			this(StaticFields.size++);
		}

		CharactersTypes(int i) {
			index = i;
			description = "";
		}

		public String getNameOfCharacter_s_Type() {
			return this.name();
		}

		public String getDescription() {
			return this.description;
		}

		public int getIndexOfCharacter() {
			return index;
		}

		public static String[] getListMembers_Name() {
			String[] ss = new String[StaticFields.size];
			CharactersTypes[] a = values();
			for (int i = 0; i < StaticFields.size; i++) {
				ss[i] = a[i].name();
			}
			return ss;
		}

		/**
		 * 32
		 */
		public static int getNumberOfMembers() {
			return StaticFields.size;
		}

		/**
		 * E' l'unicomodo per sapere la quantità di valori senza chiamare il
		 * metodo statico "values()" con il campo ".length", essendo un array.
		 *
		 * Infatti, richiamare il metodo "values()" è oneroso sia in termini di
		 * memoria che di tempo d'esecuzione perchè ogni volta crea un nuovo
		 * array e lo riempre con i campi dell'enumerazione, probabilmente al
		 * fine di preservare l'array originario. Eseguire tutta quella
		 * pappardella sarebbe alquanto controproducente, se si vuole sapere
		 * banalmente il numero di campi. Quindi, questa classe, avente il solo
		 * campo size, così sfruttata all'interno dell'enumerazione, è la
		 * soluzione più performante in assoluto.
		 * */
		private static final class StaticFields {
			private static int size = 0;
		}

	};// fine enum

	/*
	 * RADDOPPIARE ALMENO TUTTE LE CARATTERISTICHE, MANTENENDO IL SEGNO :
	 * BISOGNA TENERE CONTO DELLA RAPIDITà CON CUI SI CRESCE DI LIVELLO quindi,
	 * considerando che si guadagnano TRE punti a livello, si inizia con 50
	 * livelli bonus, quindi 150 punti da assegnare.
	 */

	protected static final StatistichePersonaggio[] charactersArray = new StatistichePersonaggio[CharactersTypes
			.getNumberOfMembers()];
	protected static final LinkedHashMap<String, StatistichePersonaggio> characters = createNewCharacterList();

	// TODO createNewCharacterList

	private static final LinkedHashMap<String, StatistichePersonaggio> createNewCharacterList() {
		LinkedHashMap<String, StatistichePersonaggio> ret = new LinkedHashMap<String, StatistichePersonaggio>(
				CharactersTypes.getNumberOfMembers());
		CharactersTypes ct;

		ct = CharactersTypes.Engineer;
		ret.put(ct.name(),
				(charactersArray[ct.getIndexOfCharacter()] = ((StatistichePersonaggio) (new StatistichePersonaggio())
						.setDescription(ct.getDescription())
						.setTypeOfCharacter(ct.name())
						.setDannoMagicoMin_Base(Math.PI)
						.setProbabilDannoCriticoMischia_Base(5)
						.setProbabilDannoCriticoDistanza_Base(5)
						.setProbabilDannoCriticoMagico_Base(
								Math.pow(Math.E, Math.PI))
						.setCoeffDannoCriticoMischia_Base(4.25)
						.setCoeffDannoCriticoDistanza_Base(8)
						.setCoeffDannoCriticoMagico_Base(15.625)
						.setCorazzaMischia_Base(-15)
						.setCorazzaDistanza_Base(-10)
						.setCorazzaMagica_Base(
								((Main.PHI * Main.PHI * Math.PI) / Math.E) * 10.0)
						// circa 9,6312088091179883655705578824391
						.setParataScudo_Base(8.0)
						.setAccellerazioneAttaccoFisico_Base(-12.5)
						.setAccellerazioneAttaccoMagico_Base(33.333333333)
						.setRigenerazioneVita_Base(RIGEN_VITA_STANDARD + 0.5)
						.setRigenerazioneMana_Base(RIGEN_MANA_STANDARD + 5)
						.setRigenerazioneScudo_Base(Math.PI + Math.E)
						.setFortunaMin_Base(3)
						// gli ingenieri sanno ottimizzare
						.setRiduzioneCostoMagie_Base(22)
						// .setManaRigen_NemicoColpito_Base(1)
						.setScudoRigen_NemicoColpito_Base(5)
						// .setVitaRigen_NemicoUcciso_Base(2)
						// .setManaRigen_NemicoUcciso_Base(2)
						.setRigenerazioneScudo_Base(15).setForza_Base(-14)
						.setCostituzione_Base(-7).setTemperanza_Base(-2)
						.setFede_Base(-42).setSaggezza_Base(60)
						.setIntelligenza_Base(120).setDestrezza_Base(8)
						.setPrecisione_Base(35).setAgilità_Base(-8)
						.setVita_Base((int) (VITA_STANDARD * 0.75))
						.setMana_Base(MANA_STANDARD)
						.setVelocità_Base(VELOCITà_STANDARD + 25)
						.setPercentQuantitàSoldiBonus_Base(10)

				)));

		// TODO TUTTO DA RIFARE COME SOPRAAAAA

		ct = CharactersTypes.Wizard;
		ret.put(ct.name(),
				(charactersArray[ct.getIndexOfCharacter()] = ((StatistichePersonaggio) (new StatistichePersonaggio())
						.setDescription(ct.getDescription())
						.setTypeOfCharacter(ct.name())
						.setDannoMagicoMax_Base(4)
						.setDannoMagicoMin_Base(2)
						.setProbabilDannoCriticoMagico_Base(Main.PHI * 10.0)
						.setCoeffDannoCriticoMischia_Base(-25)
						.setCoeffDannoCriticoDistanza_Base(-12.5)
						.setCoeffDannoCriticoMagico_Base(76.8)
						.setCorazzaMischia_Base(-20)
						.setCorazzaDistanza_Base(-15)
						.setCorazzaMagica_Base(15)
						.setParataScudo_Base(-10)
						.setAccellerazioneAttaccoFisico_Base(-75)
						.setAccellerazioneAttaccoMagico_Base(50)
						.setRigenerazioneVita_Base(RIGEN_VITA_STANDARD)
						.setRigenerazioneMana_Base(RIGEN_MANA_STANDARD + 7)
						.setFortunaMax_Base(4)
						.setFortunaMin_Base(2)
						.setRiduzioneCostoMagie_Base(25)
						// .setManaRigen_NemicoColpito_Base(2)
						// .setManaRigen_NemicoUcciso_Base(5)
						.setForza_Base(-25).setCostituzione_Base(-17)
						.setTemperanza_Base(-10).setFede_Base(60)
						.setSaggezza_Base(90).setIntelligenza_Base(70)
						.setAgilità_Base(-18).setDestrezza_Base(-13)
						.setPrecisione_Base(3).setVita_Base(VITA_STANDARD - 34)
						.setMana_Base((int) (MANA_STANDARD * 2.5))
						.setVelocità_Base(VELOCITà_STANDARD)
				//
				)));

		ct = CharactersTypes.Druid;
		ret.put(ct.name(),
				(charactersArray[ct.getIndexOfCharacter()] = ((StatistichePersonaggio) (new StatistichePersonaggio())
						.setDescription(ct.getDescription())
						.setTypeOfCharacter(ct.name())
						.setDannoDistanzaMax_Base(1)
						.setDannoMagicoMax_Base(2)
						.setDannoMagicoMin_Base(2)
						.setCoeffDannoCriticoMischia_Base(2)
						.setCoeffDannoCriticoDistanza_Base(14)
						.setCoeffDannoCriticoMagico_Base(14)
						.setProbabilDannoCriticoDistanza_Base(12.5)
						.setProbabilDannoCriticoMagico_Base(25)
						.setCorazzaMischia_Base(6)
						.setCorazzaDistanza_Base(4)
						.setCorazzaMagica_Base(11)
						.setParataScudo_Base(-7.5)
						.setAccellerazioneAttaccoFisico_Base(-5)
						.setAccellerazioneAttaccoMagico_Base(40)
						.setRigenerazioneVita_Base(RIGEN_VITA_STANDARD * 1.2)
						.setRigenerazioneMana_Base(
								RIGEN_MANA_STANDARD + (Math.PI - 1))
						.setFortunaMax_Base(1.5).setFortunaMin_Base(1.5)
						.setRiduzioneCostoMagie_Base(30)
						/*
						 * .setVitaRigen_NemicoColpito_Base(1)
						 * .setManaRigen_NemicoColpito_Base(1)
						 * .setVitaRigen_NemicoUcciso_Base(2)
						 * .setManaRigen_NemicoUcciso_Base(3)
						 */
						.setMana_Base((int) ((MANA_STANDARD / 3.0) * 4.0))
						.setVita_Base((int) (VITA_STANDARD * 0.8))
						.setVelocità_Base(VELOCITà_STANDARD + 100)
						.setForza_Base(-10).setCostituzione_Base(-4)
						.setTemperanza_Base(12).setFede_Base(100)
						.setSaggezza_Base(30).setIntelligenza_Base(35)
						.setAgilità_Base(7).setDestrezza_Base(-18)
						.setPrecisione_Base(-2)

				//
				)));

		// TODO

		ct = CharactersTypes.Elf;
		ret.put(ct.name(),
				(charactersArray[ct.getIndexOfCharacter()] = ((StatistichePersonaggio) (new StatistichePersonaggio())
						.setDescription(ct.getDescription())
						.setTypeOfCharacter(ct.name())
						.setDannoMischiaMax_Base(3)
						.setDannoDistanzaMax_Base(15)
						.setDannoDistanzaMin_Base(5)
						.setDannoMagicoMin_Base(1)
						.setProbabilDannoCriticoMischia_Base(20)
						.setProbabilDannoCriticoDistanza_Base(30)
						.setProbabilDannoCriticoMagico_Base(10)
						.setCoeffDannoCriticoMischia_Base(15)
						.setCoeffDannoCriticoDistanza_Base(50)
						.setCoeffDannoCriticoMagico_Base(17.5)
						.setCorazzaMagica_Base(6)
						// solo questa corazza : è gracilino
						.setAccellerazioneAttaccoFisico_Base(15)
						.setAccellerazioneAttaccoMagico_Base(15)
						.setRigenerazioneVita_Base(RIGEN_VITA_STANDARD + 0.5)
						.setRigenerazioneMana_Base(RIGEN_MANA_STANDARD + 1.5)
						.setFortunaMin_Base(1).setFortunaMax_Base(2)
						.setRiduzioneCostoMagie_Base(20).setForza_Base(-20)
						.setCostituzione_Base(-20).setTemperanza_Base(-15)
						.setPrecisione_Base(70).setAgilità_Base(50)
						.setDestrezza_Base(25).setFede_Base(25)
						.setIntelligenza_Base(20).setSaggezza_Base(25)
						.setVita_Base(VITA_STANDARD - 30)
						.setMana_Base(MANA_STANDARD)
						.setVelocità_Base(VELOCITà_STANDARD + 200.0)
						.setProbabilColpire_Base(10)
						.setProbabilSchivare_Base(15)
				//
				)));

		// alte corazze , temperanza, fede, danni critici, agilità, precis ...
		// alto TUTTO crescita , rigen mana, critici\ corazze, ... 0 saggezza,
		// -10
		// intellig
		ct = CharactersTypes.Monk;
		ret.put(ct.name(),
				(charactersArray[ct.getIndexOfCharacter()] = ((StatistichePersonaggio) (new StatistichePersonaggio())
						.setDescription(ct.getDescription())
						.setTypeOfCharacter(ct.name())
						.setDannoMischiaMax_Base(3)
						.setDannoDistanzaMax_Base(1)
						.setDannoMagicoMax_Base(1)
						.setDannoMischiaMin_Base(2)
						.setProbabilDannoCriticoMischia_Base(25)
						.setProbabilDannoCriticoMagico_Base(5)
						.setProbabilDannoCriticoDistanza_Base(5)
						.setCoeffDannoCriticoMischia_Base(25)
						.setCoeffDannoCriticoDistanza_Base(5)
						.setCoeffDannoCriticoDistanza_Base(10)
						.setCorazzaMischia_Base(15)
						.setCorazzaDistanza_Base(5)
						.setCorazzaMagica_Base(8)
						.setAccellerazioneAttaccoFisico_Base(15)
						.setRigenerazioneVita_Base(
								RIGEN_VITA_STANDARD + Main.PHI)
						.setRigenerazioneMana_Base(RIGEN_MANA_STANDARD + Math.E)
						.setRigenerazioneScudo_Base(-3).setFortunaMax_Base(1.5)
						.setRiduzioneCostoMagie_Base(12)
						.setVitaRigen_NemicoColpito_Base(2)
						.setManaRigen_NemicoColpito_Base(2)
						.setVitaRigen_NemicoUcciso_Base(8)
						.setManaRigen_NemicoUcciso_Base(5).setForza_Base(25)
						.setCostituzione_Base(10).setTemperanza_Base(25)
						.setFede_Base(75).setSaggezza_Base(-30)
						.setIntelligenza_Base(-40).setAgilità_Now(35)
						.setPrecisione_Base(20).setDestrezza_Base(15)
						.setVita_Base(VITA_STANDARD)
						.setMana_Base(MANA_STANDARD)
						.setVelocità_Base(VELOCITà_STANDARD + 125)
						.setProbabilSchivare_Base(15)
				//
				)));

		ct = CharactersTypes.Paladin;
		ret.put(ct.name(),
				(charactersArray[ct.getIndexOfCharacter()] = ((StatistichePersonaggio) (new StatistichePersonaggio())
						.setDescription(ct.getDescription())
						.setTypeOfCharacter(ct.name())
						.setDannoMischiaMax_Base(2)
						.setDannoDistanzaMax_Base(1)
						.setDannoMischiaMin_Base(2)
						.setProbabilDannoCriticoMischia_Now(10)
						.setProbabilDannoCriticoDistanza_Base(2.5)
						.setProbabilDannoCriticoMischia_Base(8)
						.setCoeffDannoCriticoMischia_Base(15)
						.setCoeffDannoCriticoDistanza_Base(6)
						.setCoeffDannoCriticoMagico_Base(9.5)
						// gli impuri non mi possono toccare
						.setCorazzaMischia_Base(15)
						.setCorazzaDistanza_Base(13)
						// non conosco la magia eretica, ma non la temo
						.setCorazzaMagica_Base(11)
						.setAccellerazioneAttaccoFisico_Base(18)
						.setAccellerazioneAttaccoMagico_Base(12)
						.setRiduzioneCostoMagie_Base(5)
						.setRigenerazioneVita_Base(RIGEN_VITA_STANDARD + 5)
						.setRigenerazioneMana_Base(RIGEN_MANA_STANDARD + 0.75)
						.setRigenerazioneScudo_Base(-3)
						// la fede è appagata
						.setFortunaMax_Base(1)
						.setFortunaMin_Base(0.5)
						// purifichiamo gli empi, gloria al #@?
						.setVitaRigen_NemicoColpito_Base(5)
						.setManaRigen_NemicoColpito_Base(3)
						.setVitaRigen_NemicoUcciso_Base(20)
						.setManaRigen_NemicoUcciso_Base(5)
						// fine purifichiamo gli empi, gloria al #@?
						.setForza_Base(35).setCostituzione_Base(27)
						.setTemperanza_Base(25).setFede_Base(65)
						.setIntelligenza_Base(-25).setSaggezza_Base(-55)
						.setAgilità_Base(23).setDestrezza_Base(30)
						.setPrecisione_Base(15)
						.setVita_Base(VITA_STANDARD + 35)
						.setMana_Base(MANA_STANDARD - 5)
						.setVelocità_Base(VELOCITà_STANDARD + 75)
						// la mano del dio lo guida
						.setProbabilColpire_Base(5)
				//
				)));

		ct = CharactersTypes.Robber;
		ret.put(ct.name(),
				(charactersArray[ct.getIndexOfCharacter()] = ((StatistichePersonaggio) (new StatistichePersonaggio())
						.setDescription(ct.getDescription())
						.setTypeOfCharacter(ct.name())
						.setDannoDistanzaMax_Base(3)
						.setDannoMagicoMax_Now(1)
						.setProbabilDannoCriticoMischia_Base(30)
						.setProbabilDannoCriticoDistanza_Base(30)
						.setProbabilDannoCriticoMagico_Base(30)
						.setCoeffDannoCriticoMischia_Base(30)
						.setCoeffDannoCriticoDistanza_Base(30)
						.setCoeffDannoCriticoMagico_Base(30)
						.setCorazzaMischia_Base(-12)
						.setCorazzaDistanza_Base(-12)
						.setCorazzaMagica_Base(-8)
						.setAccellerazioneAttaccoFisico_Base(10)
						.setAccellerazioneAttaccoMagico_Base(-10)
						.setRigenerazioneVita_Base(RIGEN_VITA_STANDARD)
						.setRigenerazioneMana_Base(RIGEN_MANA_STANDARD)
						.setRigenerazioneScudo_Base(2)
						.setFortunaMax_Base(10)
						.setFortunaMin_Base(5)
						.setRiduzioneCostoMagie_Base(-10)
						// sfrutta gli altri
						.setVitaRigen_NemicoColpito_Base(5)
						.setManaRigen_NemicoColpito_Base(2)
						.setVitaRigen_NemicoUcciso_Base(15)
						.setManaRigen_NemicoUcciso_Base(8)
						.setForza_Base(-10)
						.setCostituzione_Base(-25)
						// le busse gli hanno conferito grande resistenza al
						// dolore
						.setTemperanza_Base(25)
						.setFede_Base(-20)
						.setSaggezza_Base(23)
						.setIntelligenza_Base(17)
						.setAgilità_Base(36)
						.setDestrezza_Base(14)
						.setPrecisione_Base(30)
						//
						.setProbabilSchivare_Base(15)
						.setPercentQuantitàSoldiBonus_Base(15)
						.setPercentTrovareEquipment_Base(10)
						.setPercentTrovareSoldi_Base(20)
						.setPercentTrovareInMeno_Nulla_Base(-25)
						.setVita_Base(VITA_STANDARD - 20)
						.setMana_Base(MANA_STANDARD - 20)
						.setVelocità_Base(VELOCITà_STANDARD + 25)
						.setProbabilSchivare_Base(10)
						.setProbabilColpire_Base(12)
						.setProbabilDisarmare_Base(13)
				//
				)));

		ct = CharactersTypes.Barbarian;
		ret.put(ct.name(),
				(charactersArray[ct.getIndexOfCharacter()] = ((StatistichePersonaggio) (new StatistichePersonaggio())
						.setDescription(ct.getDescription())
						.setTypeOfCharacter(ct.name())
						.setDannoMischiaMax_Base(8).setDannoDistanzaMax_Base(2)
						.setDannoMischiaMin_Base(4).setDannoMagicoMin_Base(-2)
						.setProbabilDannoCriticoMischia_Base(15)
						.setProbabilDannoCriticoDistanza_Base(5)
						.setProbabilDannoCriticoMagico_Base(-10)
						.setCoeffDannoCriticoMischia_Base(10)
						.setCoeffDannoCriticoMagico_Base(-10)
						.setCorazzaMischia_Base(20).setCorazzaDistanza_Base(15)
						.setCorazzaMagica_Now(-5).setParataScudo_Base(15)
						.setAccellerazioneAttaccoFisico_Base(55)
						.setAccellerazioneAttaccoMagico_Base(-25)
						.setRigenerazioneVita_Base(RIGEN_VITA_STANDARD + 8)
						.setRigenerazioneMana_Base(RIGEN_MANA_STANDARD * 0.75)
						.setRigenerazioneScudo_Now(-2)
						.setRiduzioneCostoMagie_Base(-10)
						.setFortunaMax_Base(1.5).setFortunaMin_Base(-1)
						.setVitaRigen_NemicoColpito_Base(3)
						.setVitaRigen_NemicoUcciso_Base(8).setForza_Base(70)
						.setCostituzione_Base(35).setTemperanza_Base(45)
						.setFede_Base(-30).setSaggezza_Base(-20)
						.setIntelligenza_Base(-30).setAgilità_Base(25)
						.setDestrezza_Base(35).setPrecisione_Base(25)

						.setVita_Base(VITA_STANDARD + 150)
						.setMana_Base(MANA_STANDARD - 25)
						.setVelocità_Base(VELOCITà_STANDARD + 50)

						.setProbabilColpire_Base(15)
						.setProbabilSchivare_Base(8)
				//
				)));

		ct = CharactersTypes.Orc;
		ret.put(ct.name(),
				(charactersArray[ct.getIndexOfCharacter()] = ((StatistichePersonaggio) (new StatistichePersonaggio())
						.setDescription(ct.getDescription())
						.setTypeOfCharacter(ct.name())

						.setDannoMischiaMax_Base(11).setDannoMischiaMin_Base(5)
						.setDannoMagicoMin_Base(-4)
						.setProbabilDannoCriticoMischia_Base(13)
						.setProbabilDannoCriticoDistanza_Base(4)
						.setProbabilDannoCriticoMagico_Base(-12)
						.setCoeffDannoCriticoMischia_Base(10)
						.setCoeffDannoCriticoMagico_Base(-10)
						.setCorazzaMischia_Base(25).setCorazzaDistanza_Base(17)
						.setCorazzaMagica_Now(-15)
						.setRiduzioneCostoMagie_Base(-25).setFortunaMax_Base(2)
						.setFortunaMin_Base(-2)
						.setVitaRigen_NemicoColpito_Base(6)
						.setVitaRigen_NemicoUcciso_Base(10).setForza_Base(100)
						.setCostituzione_Base(70).setTemperanza_Base(65)
						.setFede_Base(-35).setSaggezza_Base(-45)
						.setIntelligenza_Base(-55).setAgilità_Base(25)
						.setDestrezza_Base(15)
						.setPrecisione_Base(10)

						.setAccellerazioneAttaccoFisico_Base(+25)
						.setAccellerazioneAttaccoMagico_Base(-50)
						.setRigenerazioneVita_Base(RIGEN_VITA_STANDARD + 13)
						.setRigenerazioneMana_Base(RIGEN_MANA_STANDARD * 0.5)
						// figurati te se capisce come funziona uno scudo ..
						.setRigenerazioneScudo_Now(-4)
						.setVita_Base(VITA_STANDARD + 200)
						.setMana_Base(MANA_STANDARD - 30)
						.setVelocità_Base(VELOCITà_STANDARD + 75)
				//
				)));

		ct = CharactersTypes.Human;
		ret.put(ct.name(),
				(charactersArray[ct.getIndexOfCharacter()] = ((StatistichePersonaggio) (new StatistichePersonaggio())
						.setDescription(ct.getDescription())
						.setTypeOfCharacter(ct.name())
						.setRigenerazioneVita_Base(RIGEN_VITA_STANDARD)
						.setRigenerazioneMana_Now(RIGEN_MANA_STANDARD)
						.setVita_Base((int) (VITA_STANDARD * 0.9))
						.setMana_Base(MANA_STANDARD - 16)
						.setVelocità_Base(VELOCITà_STANDARD + 40)
						.setFortunaMax_Base(10).setFortunaMin_Base(10)
						.setForza_Base(15).setCostituzione_Base(15)
						.setTemperanza_Base(15).setFede_Base(15)
						.setSaggezza_Base(20).setIntelligenza_Base(25)
						.setAgilità_Base(15).setDestrezza_Base(15)
						.setPrecisione_Base(15)
				//
				)));

		ct = CharactersTypes.Bloodgus;
		ret.put(ct.name(),
				(charactersArray[ct.getIndexOfCharacter()] = ((StatistichePersonaggio) (new StatistichePersonaggio()
						.setDescription(ct.getDescription())
						.setTypeOfCharacter(ct.name()))
						.setDannoMischiaMax_Base(7).setDannoMagicoMax_Base(3)
						.setDannoMischiaMin_Now(2).setCorazzaMischia_Base(-20)
						.setCorazzaDistanza_Base(-20)
						.setCorazzaMagica_Base(-20)
						.setProbabilDannoCriticoMischia_Base(15)
						.setCoeffDannoCriticoMischia_Base(15)
						.setProbabilDannoCriticoDistanza_Base(15)
						.setCoeffDannoCriticoDistanza_Base(15)
						.setProbabilDannoCriticoMagico_Base(15)
						.setCoeffDannoCriticoMagico_Base(15)
						.setAccellerazioneAttaccoFisico_Base(30)
						.setVita_Base(VITA_STANDARD + 150)
						.setFede_Base(MANA_STANDARD + 20)
						.setCostituzione_Base(-20).setTemperanza_Base(-35)
						.setRigenerazioneVita_Base(RIGEN_VITA_STANDARD - 5)
						.setVitaRigen_NemicoColpito_Base(15)
						.setVitaRigen_NemicoUcciso_Base(50)
						.setManaRigen_NemicoColpito_Base(2)
						.setManaRigen_NemicoUcciso_Base(8)
						.setVelocità_Base(VELOCITà_STANDARD + 85)
						.setForza_Base(75).setCostituzione_Base(-25)
						.setTemperanza_Base(-30).setFede_Base(45)
						.setSaggezza_Base(-15).setIntelligenza_Base(20)
						.setAgilità_Base(40).setDestrezza_Base(20)
						.setPrecisione_Base(20)

				// Bloodgus (magus of blood) : subisce molti danni,
				// rigenerazione molto negativa ma
				// vita e mana per colpo molto alti (per uccisione altissima).
				// Caratterizzato da magie che fanno perdere vita, ma fanno un
				// male atroce. E' tipo una sanguisuga-vampiro

				)));

		return ret;
	};

	// ordine da alta intelligenza, questa decresce in favore di fede e
	// saggezza, .. andando verso il corpo a corpo, fino a ridurre precisione
	// ecc a suo favore. poi, con l'orco, la questione si inverte e si verso gli
	// altri
	// N.B. : crea un nuovo oggetto per preservare i valori originali
	public static StatistichePersonaggio getStatistichePersonaggio_Engineer() {
		return new StatistichePersonaggio(
				characters.get(CharactersTypes.Engineer.name()));
	}

	public static StatistichePersonaggio getStatistichePersonaggio_Wizard() {
		return new StatistichePersonaggio(characters.get(CharactersTypes.Wizard
				.name()));
	}

	public static StatistichePersonaggio getStatistichePersonaggio_Druid() {
		return new StatistichePersonaggio(characters.get(CharactersTypes.Druid
				.name()));
	}

	public static StatistichePersonaggio getStatistichePersonaggio_Monk() {
		return new StatistichePersonaggio(characters.get(CharactersTypes.Monk
				.name()));
	}

	public static StatistichePersonaggio getStatistichePersonaggio_Paladin() {
		return new StatistichePersonaggio(
				characters.get(CharactersTypes.Paladin.name()));
	}

	public static StatistichePersonaggio getStatistichePersonaggio_Elf() {
		return new StatistichePersonaggio(characters.get(CharactersTypes.Elf
				.name()));
	}

	public static StatistichePersonaggio getStatistichePersonaggio_Robber /* ladrone */() {
		return new StatistichePersonaggio(characters.get(CharactersTypes.Robber
				.name()));
	}

	public static StatistichePersonaggio getStatistichePersonaggio_Barbarian() {
		return new StatistichePersonaggio(
				characters.get(CharactersTypes.Barbarian.name()));
	}

	public static StatistichePersonaggio getStatistichePersonaggio_Orch() {
		return new StatistichePersonaggio(characters.get(CharactersTypes.Orc
				.name()));
	}

	public static StatistichePersonaggio getStatistichePersonaggio_Human() {
		return new StatistichePersonaggio(characters.get(CharactersTypes.Human
				.name()));
	}

	public static StatistichePersonaggio getStatistichePersonaggio_Bloodgus() {
		return new StatistichePersonaggio(
				characters.get(CharactersTypes.Bloodgus.name()));
	}

	public static StatistichePersonaggio getNewStatistichePersonaggio_Choosen(
			String name) {
		return new StatistichePersonaggio(characters.get(name));
	}

	public static StatistichePersonaggio getNewStatistichePersonaggio_Choosen(
			int index) {
		return getNewStatistichePersonaggio_Choosen(index, true);
	}

	public static StatistichePersonaggio getNewStatistichePersonaggio_Choosen(
			int index, boolean mustCopy) {
		StatistichePersonaggio ret = null;
		if (index >= 0 && index < charactersArray.length) {
			if (mustCopy) {
				ret = new StatistichePersonaggio(charactersArray[index]);
			} else {
				ret = charactersArray[index];
			}
		}
		return ret;
	}

	public static int getCharactersTypesLength() {
		return CharactersTypes.getNumberOfMembers();
	}

	public static String getCharactersType(int index) {
		String ret = null;
		int n = CharactersTypes.getNumberOfMembers();
		CharactersTypes[] v = CharactersTypes.values();
		if (index >= 0 && index < n) {
			ret = v[index].name();
		}
		return ret;
	}

	public static int getIndexOfGivenCharacterName(String name) {
		int ret = -1, i = 0;
		int n = CharactersTypes.getNumberOfMembers();
		CharactersTypes[] v = CharactersTypes.values();
		while ((ret < 0) && (i < n)) {
			if (name.equals(v[i].name())) {
				ret = i;
			}
			i++;
		}
		return ret;
	}

}