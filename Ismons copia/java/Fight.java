import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.*;
import java.util.Random;
import java.util.*;  
import java.io.File;

final class StatMod
{
	private int[] aMod;
	private int[] bMod;

	public StatMod(int[] aMod, int[] bMod)
	{
		this.aMod = aMod;
		this.bMod = bMod;
	}

	public int[] getAMod()
	{
		return this.aMod;
	}
	public int[] getBMod()
	{
		return this.bMod;
	}
}

public class Fight
{
	private Scanner z = new Scanner(System.in);  private Scanner continua = new Scanner(System.in); private Scanner sc = new Scanner(System.in);

	private TypeChart chart = new TypeChart(); //La tabella contenete le debolezze e resistenze dei vari tipi
	private Creature[] ismon = new Creature[6]; //La squadra del giocatore
	private Creature[] ismonE = new Creature[6]; //La squadra del Computer

	private Creature playerMon; private int[] stats; //Il pokémon mandato in campo dal giocatore
    private int[] statsM = new int[]{0, 0, 0, 0, 0, 0}; //I modificatori alle Statistiche del pokémon mandato in campo dal giocatore		
    private int[] accEva = new int[]{100, 100}; //Precisione ed Evasione del Pokémon mandato in campo dal giocatore
    private int priority = 0; //Indica la priorità nel turno di azione del pokémon del giocatore

    private int[] ismonEsceltamosse;

    private boolean turn = true; //Indica chi stia attacando tra giocatore e computer
    private boolean swap = false; //Indica se il giocatore ha switchato il suo pokémon

    private Creature enemyMon; private int[] statsE; //Il pokémon mandato in campo dal computer
    private int[] statsME = new int[]{0, 0, 0, 0, 0, 0}; //I modificatori alle Statistiche del pokémon mandato in campo dal computer
	private int[] accEvaE = new int[]{100, 100}; //Precisione ed Evasione del Pokémon mandato in campo dal computer
	private int priorityE = 0; //Indica la priorità nel turno di azione del pokémon del computer

    private String weather = "TT";	//Il tempo atmosferico presente in lotta. "TT" significa che non c'è alcun tempo atmosferico al momento

    private int sentIsmon = 0; //L'indice del Pokémon mandato in campo dal giocatore 
    private int sentIsmonE = 0; //L'indice del Pokémon mandato in campo dal computer
    private boolean[][] sentCreatures = new boolean[6][6]; //Controlla quali pokémon sono stati mandati in campo e quali di questi pokémon
														   //hanno incontrato un pokémon avversario

    private double a = 1.0;    //Valore che indica il pokémon che stiamo affrontando. "1.0" è Selvatico, "1.5" è di un Allenatore

    private double typemod;

    private boolean inputCheck = true;
    private boolean crit = false;

    private String abilityEffect;

    private String home;
    private String fs;
    private FileReader readerStatus;
    private Properties propStatus;

    private String states = "";
    private String applyText = "";


	public Fight(Creature[] ismon, Creature[] ismonE)
	{	
		home = System.getProperty("user.dir");
        home = home.substring(0, home.length()-5);
        fs = File.separator;
        try
        {
            readerStatus = new FileReader(new File(home + fs + "data" + fs + "states.txt"));
        }
        catch(FileNotFoundException fnfe)
        {
        }
        propStatus = new Properties();
        try
        {
            propStatus.load(readerStatus);
        }
        catch(IOException ioe)
        {
        }

        states = propStatus.getProperty("main");
		applyText = propStatus.getProperty("applyText");


		int azione = 1;
		boolean win = false, lose = false; 

		a = 1.5;

        int playerConditions = 0;
        int opponentConditions = 0;

		this.ismon = ismon; 
		this.ismonE = ismonE; 

		int k = 0;
		for(int i = 0; i<6; i++) //Qua si ottiene quanti pokémon sono presenti nel team
		{ 
			if(ismon[i] != null)
			{ 
				if(ismon[i].getStatus() != 1)
				{  
					playerMon = ismon[i];
					sentIsmon = i;
					break;
				}
			} 
		}

		enemyMon = ismonE[0];

		this.stats = playerMon.getStats();
		this.statsE = enemyMon.getStats();

		sentCreatures[sentIsmon][sentIsmonE] = true;

		do
		{
			int playerPP = 0; int opponentPP = 0; int i = 0; int j = 0; int j1 = 0;

			for(i = 0; i<4; i++){ playerPP += playerMon.getMosse()[i].getCurrentPP(); }  //controlla i PP totali 
			for(i = 0; i<4; i++){ opponentPP += enemyMon.getMosse()[i].getCurrentPP(); } //controlla i PP totali del nemico

			playerConditions = getTeamConditions(this.ismon); //Ottiene le condizioni del team
			opponentConditions = getTeamConditions(this.ismonE); //Ottiene le condizioni del team nemico

			System.out.print("\033[H\033[2J");  
      		System.out.flush(); 

			showHPBar();

			if (playerConditions == 0) //se non hai alcun pokémon in forze, perdi
			{	
				System.out.println(" You lost... "); continua.nextLine(); lose = true;
			}
			else if(opponentConditions == 0) //se l'avversario non ha alcun pokémon in forze, vinci
			{ 
					System.out.println(" You won! "); continua.nextLine(); win = true;
			}
			else
			{
				if(playerMon.getStatus() == 1)
				{
					switchMember(ismon, true);
				}

				do
				{
					inputCheck = true;
					System.out.print(" 1: Fight               2: Switch\n 3: Bag                 4: Run\n\n               >");

					try
					{
						azione = sc.nextInt();
					}
					catch(Exception iME)
					{
						inputCheck = false;
						sc.next();

						System.out.print("\033[H\033[2J");  
	      				System.out.flush(); 

						showHPBar();
					}
				}
				while(!inputCheck);

				switch(azione)
				{
					//Fight
					case 1: 
						chooseMove(playerPP, opponentPP); 
						break;

					//Switch
					case 2:
						swap = true;
						chooseMove(playerPP, opponentPP);
						swap = false;
						break;

					default:
						break; 
				}			
			}
		}
		while(!win && !lose);
	}

	//Qua si vede quanti pokémon di un team sono in grado di combattere
	public int getTeamConditions(Creature[] team)
	{
		int i; 
		int j = 0; 

		int conditions;

		for(i = 0; i<6; i++) //Qua si ottiene quanti pokémon sono presenti nel team
		{ 
			if(team[i] != null)
			{ 
				j+=1; 
			} 
		}

		conditions = j; //'conditions' assume il valore del numero di pokémon in team

		for(i = 0; i<j; i++) 
		{ 	
			if(team[i].getStatus() == 1) //Ogni qualvolta un pokémon del team ha lo status "K.O.", 'conditions' scende di 1
			{										//Se arriva a 0, vuole dire che il team non ha più alcun pokémon in forze
				conditions -= 1; 
			}
		}

		return conditions;
	}

	//Qui si calcola l'exp guadagnata da un pokémon quando sconfitto
	public void exp(int ismonIndex, int ismonLv)
	{
		//La formula dell'exp prende in considerazione anche la differenza di livelli tra il tuo pokémon e quello
		//sconfitto: otterrai più esperienza se sei di livello più basso dell'avversario, e ne riceverai di meno
		//se sei di livello più alto
		int i = 0; int j = 0;

		int item = 1;  //Il pokémon potrebbe avere uno strumento che aumenta l'exp ricevuta

		double exp = 0;

		CreaturesList expList;
		int ismonExp = 0;

		int sharedExp = 0;
		int[] receivers;

		//Controlla quali pokémon hanno incontrato il pokémon appena sconfitto
		for(i = 0; i < 6; i++)
		{
			//Se il pokémon è entrato in campo almeno una volta contro il pokémon appena sconfitto senza andare K.O.,
			//anche lui riceverà l'exp droppata
			if(sentCreatures[i][sentIsmonE])
			{
				sharedExp+=1;
			}
		}

		receivers = new int[sharedExp];

		//Mette il numero nella squadra dei pokémon che hanno incontrato il pokémon sconfitto nell'array "receivers"
		for(i = 0; i < 6; i++)
		{
			if(sentCreatures[i][sentIsmonE])
			{
				receivers[j] = i;
				j+=1;
			}
		}

		//Prende il valore base dell'exp ceduta dal pokémon sconfitto. Ogni pokémon ha un suo valore di exp
		try
		{
			expList = new CreaturesList();
			ismonExp = expList.getExpDrop(ismonIndex);
		}
		catch(Exception cL)
		{
		}

		for(i = 0; i < receivers.length; i++)
		{
			if(ismon[receivers[i]].getLevel() < 100)
			{ 
				exp = (double)((ismonExp * ismonLv * item) / 7) * ( Math.pow((double)2 * ismonLv + 10, 2.5) / Math.pow((double)ismonLv + (double)ismon[receivers[i]].getLevel() + 10, 2.5));

				ismon[receivers[i]].addExp((int)exp);
				showHPBar();
				System.out.println(" " + ismon[receivers[i]].getNickname() + " gains " + (int)exp + " Exp. Points!");
				continua.nextLine();

				ismon[receivers[i]].checkLevelUp();
			}
		}
	}


	public void showHPBar()  				
	{													//Crea le barre degli HP mostrate in lotta. Ogni barra è formata da 20 tacche, e calcola quante 
		int j = 0; String hpbar = "";        //tacche "riempire" dal rapporto percentual di HP attuali e HP massimi del pokémon

		System.out.print("\033[H\033[2J");  
        System.out.flush();

        String stateE = ((enemyMon.getStatus() != 0) ? states.split(";")[enemyMon.getStatus()].split(" - ")[0] : "");
        String state = ((playerMon.getStatus() != 0) ? states.split(";")[playerMon.getStatus()].split(" - ")[0] : "");

		j = ((enemyMon.getCurrentHP() * 100) / enemyMon.getStats()[0]) / 5; // x : 100 = HP : maxHP

		if (enemyMon.getCurrentHP() > 0 && j < 1) //Finchè il Pokémon avrà più di 0 HP, verrà sempre mostrata almeno una tacca
		{ 
			j = 1; 
		}

	    // Riempe la barra con le tacche 
	    hpbar = new String(new char[j]).replace("\0", "|") + new String(new char[20-j]).replace("\0", " ");

		System.out.println("                   " + enemyMon.getNickname() + "  HP:  <" + hpbar + ">  " + stateE + "\n\n\n\n\n");

		hpbar = "";

		j = ((playerMon.getCurrentHP() * 100) / playerMon.getStats()[0]) / 5; // x : 100 = HP : maxHP

		if (playerMon.getCurrentHP() > 0 && j < 1) //Finchè il Pokémon avrà più di 0 HP, verrà sempre mostrata almeno una tacca
		{ 
			j = 1;
	    }

	    // Riempe la barra con le tacche 
	    hpbar = new String(new char[j]).replace("\0", "|") + new String(new char[20-j]).replace("\0", " ");

		System.out.println(" " + playerMon.getNickname() + "  HP:  <" + hpbar + ">  " + state); 
		System.out.println("                        " + playerMon.getCurrentHP() + "/" + playerMon.getStats()[0]);

		hpbar = "";

		int exp = playerMon.getExp()-playerMon.getLevelUpBefore();
		int rexp = playerMon.getLevelUp()-playerMon.getLevelUpBefore();
		j = ((exp * 100) / rexp) / 5;
		j = Math.min(j, 20);
		if(exp > 0 && j == 0)
		{
			j = 1;
		}

		// Riempe la barra con le tacche 
	    hpbar = new String(new char[j]).replace("\0", "*") + new String(new char[20-j]).replace("\0", " ");

		System.out.println("   Exp:  <" + hpbar + ">  " + "\n\n"); 
	}


	public int[] recalculateStats(int[] stat, int[] statsmod)  
	{															//Ricalcola le stat a seconda dei buff e debuff ricevuti 			

		int[] statsrec = new int[]{stat[0], stat[1], stat[2], stat[3], stat[4], stat[5]};

		for(int i = 0; i < stat.length; i++)
		{

			switch (statsmod[i])    //Le statistice ottengono un moltiplicatore se ricevono un buff o un debuff
			{
				case -6: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 0.25)); break; case -5: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 0.28)); break; case -4: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 0.33)); break; 
				case -3: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 0.40)); break; case -2: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 0.50)); break; case -1: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 0.66)); break;
				case 0: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 1.0)); break; case 1: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 1.5)); break; case 2: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 2.0)); break;
				case 3: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 2.5)); break; case 4: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 3.0)); break; case 5: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 3.5)); break;
				case 6: statsrec[i] = Math.max(1, (int)Math.floor(statsrec[i] *= 4)); break; default: break;
			}

		}
		int[] sendstats = new int[]{statsrec[0], statsrec[1], statsrec[2], statsrec[3], statsrec[4], statsrec[5]};
		return sendstats;
	}


	public double giveWeather(int moveid)
	{	
		double bonusWeather = 1.0;

		String moveStrong = "mS";
		String moveWeak = "mW";

		switch(this.weather)
		{
			case "Rain":
				moveStrong = "Water";
				moveWeak = "Fire";
				break;

			case "Sunny":
				moveStrong = "Fire";
				moveWeak = "Water";
				break;

			default:
				break;
		}

		if(moveStrong.equals(moveid))
		{
			bonusWeather = 1.5;
		}
		else if(moveWeak.equals(moveid))
		{
			bonusWeather = 0.5;
		}

		return bonusWeather;
	}

	public int giveDamage(Creature attacker, int[] statsAt, IsmonMosse move, Creature defender, int[] statsDe) throws Exception
	{
		Abilities abiList = new Abilities();

		double damage = 0;
		int bDamage = 0;

		//Prende i tipi del pokémon attaccato
		int[] dtype = defender.getType();

		//calcola il modificatore del danno basato sul tipo della mossa e quello
		//del bersaglio
		typemod = chart.getEffect(move.getType(), dtype);		
							    
		if (typemod == 0.0) 
		{
		} 
		else
		{ 
			//riceve il danno base inflitto dalla mossa
			String base_damage = move.getDamage();
			
			//se il danno base comincia con 'f', vuol dire che quella mossa farà sempre un danno
			//pari al suo valore base, ignorando le statistiche proprie o del bersaglio

			if(base_damage.charAt(0) == 'f')
			{
				damage = (double)Integer.parseInt(base_damage.substring(1));
				return (int)damage;
			}
			else
			{
				bDamage = Integer.parseInt(base_damage);
			}
			
			double weather = giveWeather(move.getID()); //I vari tempi atmosferici hanno effetti diversi - 'Rain' potenzia gli attacchi con mosse di Tipo Acqua ed indebolisce 
								  				  		//quelli con mosse di tipo Fuoco - 

			double random = (Math.random() * 0.15) + 0.85; //random è un modificatore casuale al danno dell'attacco che varia da 0.85 a 1.0


			//STAB è un bonus dato agli attacchi se il tipo  
			//della mossa utilizzata è uguale a quello 
			//dell'utilizzatore
			double stab = 1.0; 
       		int atype[] = attacker.getType();    
       		for(int i=0; i<atype.length; i++)
       		{
       			if(atype[i] == move.getType())
				{
					if(move.getType() != 0)
					{
						stab += 0.50;
						break;
					}
				}  
       		}                 

			double statusM = 1.0;	//Uno Status può influenzare il danno di un attacco. 
			if (attacker.getStatus() == 2 && move.getPhysical()) 
			{ 
				statusM = 0.5; 
			}    


			double critical = 1.0;  //Alcune mosse hanno la possibilità di infliggere casualmente un danno più elevato del normale.
								    //Questi sono chiamati Colpi Critici.
			crit = false;
			String spec = move.getSpecialEffect();
			if((spec.split(" ")[0]).equals("crit"))
			{ 
				int critChance = Integer.parseInt(spec.split(" ")[1]);
				if(Math.floor(Math.random()*100)+1 <= critChance)
				{
					critical += 0.5; 
					crit = true;
				}
			} 

			double other = 1.0;	//Anche le Abilità possono influenzare il danno di un attacco

			abilityEffect = abiList.getEffect(attacker.getAbility());
			if(abilityEffect.split(" ")[0].equals("boost"))
			{
				if(move.getType() == Integer.parseInt(abilityEffect.split(" ")[1]) && attacker.getCurrentHP() <= (statsAt[0] / Integer.parseInt(abilityEffect.split(" ")[2])))
				{
					other += 0.5;
				}
			}
			if(abilityEffect.split(" ")[0].equals("RATE"))
			{
				switch(abilityEffect.split(" ")[1].charAt(0))
				{
					case 'a':
						statsAt[Integer.parseInt(abilityEffect.split(" ")[2])] *= (Integer.parseInt(abilityEffect.split(" ")[3])/100);
						break;

					case 'b':
						statsDe[Integer.parseInt(abilityEffect.split(" ")[2])] = (Integer.parseInt(abilityEffect.split(" ")[3])/100);
						break;

					default:
						break;
				}
			}

			double modifier = weather * critical * random * statusM * stab * typemod * other; 

			System.out.println(" ");

			if(move.getPhysical())
			{   
				damage = Math.max((((((2*attacker.getLevel())/5) + 2) * bDamage * (statsAt[1]/statsDe[2]))) / 50 * modifier, 1); 

				attacker.setTP(1, 1); 
				defender.setTP(2, 1); 
			}  
		    else
		    { 
		    	damage = Math.max((((((2*attacker.getLevel())/5) + 2) * bDamage * (statsAt[3]/statsDe[4]))) / 50 * modifier, 1); 

		    	attacker.setTP(3, 1); 
		        defender.setTP(4, 1); 
		    }
		}

		return (int)Math.round(damage);
	}


	public StatMod sMod(IsmonMosse move, int[] aStatMod, String nicknameA, int statusA, int[] dStatMod, String nicknameD, int statusD)
	{
		String mod = move.getStatMod();	//Qui ottiene quali statistiche vengono alterate dalla mossa e in che quantità

		String[][] message = new String[][]{ {"'s Attack ","'s Defense ","'s S.Attack ","'s S.Defense ","'s Speed "},
										   {"rose!", "harshly rose!", "drastically rose!", "fell!", "harshly fell!", "drastically fell!"} };	
										   //Array contenente testo espositivo per il giocatore, 
										   //contiene le statistiche potenzialmente alterabili e in che quantità

		int indexStatMessage = 0;	//L'indice per indicare in String[][] message quale statistica è affetta dalla mossa
		int indexModifierMessage = 0;	//L'indice per indicare in String[][] message in che modo la statistica viene modificata

		//Se la mossa non ha dati riguardo a un cambio di statistiche, l'intero processo viene saltato
		if(!mod.equals(""))
		{
				String target = mod.split(" ")[0];	//target indica chi viene afflito dalla modifica alle statistiche
				int chance = Integer.parseInt(mod.split(" ")[1]);	//la probabilità che la modifica alle statistiche avvenga

				//Se il tiro del d100 da un risultato minore o pari a chance, vuol dire che la modifica può essere applicata
				if(Math.floor(Math.random()*100)+1 <= chance)
				{
					String[] counter = mod.split(" ");	//counter conta il numero di statistiche influenzate dalla mossa

					//Il processo viene ripetuto per ogni statistica alterata
					for(int nmods = 0; nmods < (counter.length-2); nmods+=2)
					{
						int indexStat = Integer.parseInt(mod.split(" ")[nmods+2]);	//indexStat contiene l'indice della stat influenzata
						int modifierStat = Integer.parseInt(mod.split(" ")[nmods+3]);	//modifierStat contiene di quanto la stat è modificata

						indexStatMessage = indexStat-1;		//qui sono messi in sicrono i messaggi con l'indice della stat

						//a seconda di quanto è alterata la stat, viene cambiato l'indice del messaggio mostrato
						switch(modifierStat)
						{
							case -3:
								indexModifierMessage = 5;
								break;
								
							case -2:
								indexModifierMessage = 4;
								break;

							case -1:
								indexModifierMessage = 3;
								break;

							case 1:
								indexModifierMessage = 0;
								break;

							case 2:
								indexModifierMessage = 1;
								break;
								
							case 3:
								indexModifierMessage = 2;
								break;
						}


						//"a" è l'utilizzatore: qui si controlla se è l'utente ad essere influenzato
						if(target.equals("a") && statusA != 1)
						{	
							showHPBar();
							//Se il modificatore della Stat è pari a 6, non può essere aumentato di più
							if(aStatMod[indexStat] == 6)
							{
								System.out.println(" " + ((!turn) ? "The foe " : "") + nicknameA + message[0][indexStatMessage] + "couldn't be any higher!");
							}
							else
							{
								//Se il modificatore della Stat è pari a -6, non può essere diminuito di più
								if(aStatMod[indexStat] == -6)
								{
									System.out.println(" " + ((!turn) ? "The foe " : "") + nicknameA + message[0][indexStatMessage] + "couldn't be any lower!");
								}
								else
								{
									aStatMod[indexStat] = Math.max(Math.min(aStatMod[indexStat] + modifierStat, 6), -6);
									System.out.println(" " + ((!turn) ? "The foe " : "") + nicknameA + message[0][indexStatMessage] + message[1][indexModifierMessage]);
								}
							}
						}
						else if(target.equals("b") && statusD != 1)
						{
							showHPBar();
							if(dStatMod[indexStat] == 6)
							{
								System.out.println(" " + ((turn) ? "The foe " : "") + nicknameD + message[0][indexStatMessage] + "couldn't be any higher!");
							}
							else
							{
								if(dStatMod[indexStat] == -6)
								{
									System.out.println(" " + ((turn) ? "The foe " : "") + nicknameD + message[0][indexStatMessage] + "couldn't be any lower!");		
								}
								else
								{
									dStatMod[indexStat] = Math.max(Math.min(dStatMod[indexStat] + modifierStat, 6), -6);
									System.out.println(" " + ((turn) ? "The foe " : "") + nicknameD + message[0][indexStatMessage] + message[1][indexModifierMessage]);
								}
							}
						}
						continua.nextLine();
					}
				}
		}

		return new StatMod(aStatMod,dStatMod);
	}
	
	//Qui è dove si vede se la mossa ha un qualche effetto speciale
	public void specialEffect(IsmonMosse move, int damage, Creature a, Creature d)
	{
		String spec = move.getSpecialEffect(); //Qui ottiene gli effetti speciali della mossa
		
		if(!spec.equals(""))
		{
			boolean pausa = false;;

			String effect = spec.split(" ")[0];	//effect è l'effetto speciale della mossa
			String effectValue = spec.split(" ")[1]; //effectValue è l'efficacia dell'effetto speciale
			
			int effectResult = 0;
			switch(effect)
			{
				case "absorb":
					if(a.getCurrentHP() < a.getStats()[0])
					{
						effectResult = Math.max(1, damage/Integer.parseInt(effectValue));
						a.setCurrentHP(effectResult);
						
						showHPBar();
						System.out.println(" " + ((!turn) ? "The foe " : "") + a.getNickname() + " absorbs " + effectResult + " HP!");
						pausa = true;
					}
					break;
					
				case "recoil":
					if(effectValue.equals("struggle"))
					{
						effectResult = a.getStats()[0]/4;
						if(a.getCurrentHP() - effectResult < 0)
						{
							effectResult += a.getCurrentHP() - effectResult;
						}
						pausa = true;
					}
					else
					{
						effectResult = damage/Integer.parseInt(effectValue);
						if(a.getCurrentHP() - effectResult < 0)
						{
							effectResult += a.getCurrentHP() - effectResult;
						}
						pausa = true;
					}

					a.setCurrentHP(Math.min(1, -effectResult));
					
					showHPBar();
					System.out.println(" " + ((!turn) ? "The foe " : "") + a.getNickname() + " receives " + effectResult + " of recoil damage!");
					break;
			}

			if(pausa)
			{
				continua.nextLine();
			}
		}
	}

	public int addStatus(IsmonMosse move, Creature a, Creature d)
	{
		String status = move.getAfflictedStatus();

		if(!status.equals(" "))
		{
			String statusTarget = status.split(" ")[0];
			int[][] applicableStates = new int[status.split(",").length][2];
			for(int i = 0; i < applicableStates.length; i++)
			{
				applicableStates[i][0] = Integer.parseInt(status.split(" ")[1]);
				applicableStates[i][1] = Integer.parseInt(status.split(" ")[2]);
			}

			Creature ismonTarget = a;

			if(statusTarget.equals("a") && a.getCurrentHP() > 0)
			{
				ismonTarget = null;
				ismonTarget = a;
			}
			else if(statusTarget.equals("b") && d.getCurrentHP() > 0)
			{
				ismonTarget = null;
				ismonTarget = d;
			}
			else
			{
				return 1;
			}

			for(int[] s : applicableStates)
			{
				if(Math.floor(Math.random()*100)+1 <= s[0])
				{
					String aText = applyText.split(";")[s[1]];

					int[] targetTypes = ismonTarget.getType();

					boolean immune = false;

					for(int j = 0; j < targetTypes.length; j++)
					{
						if(targetTypes[j] == Integer.parseInt(states.split(";")[s[1]].split(" - ")[5].split(",")[j]))
						{
							immune = true;
							break;
						}				
					}

					if(immune)
					{
						showHPBar();
						System.out.println(" " + ((turn) ? "The foe " : "") + ismonTarget.getNickname() + " " + applyText.split(";")[applyText.split(";").length-1]);
						continua.nextLine();
					}
					else if(ismonTarget.setStatus(s[1]))
					{
							showHPBar();
							System.out.println(" " + ((turn) ? "The foe " : "") + ismonTarget.getNickname() + " " + aText.split(" - ")[0]);
							continua.nextLine();
					}
					else
					{
							showHPBar();
							System.out.println(" " + ((turn) ? "The foe " : "") + ismonTarget.getNickname() + " " + applyText.split(";")[0]);
							continua.nextLine();
					}		
				}
			}
		}

		return 0;
	}

	//Si determinano gli effetti dei vari status
	public void getStatusEffect(Creature ismon, boolean turn)
	{
		switch(ismon.getStatus())
		{
			case 2: case 7:
				ismon.setCurrentHP(-(ismon.getStats()[0] / 8));
				showHPBar();
				System.out.println(" " + ((!turn) ? "The foe " : "") + ismon.getNickname() + " was hurt by " + ((ismon.getStatus() == 2) ? "burns!" : "") + ((ismon.getStatus() == 7) ? "poison!" : ""));
				System.out.println("  Receives " + (ismon.getStats()[0] / 8) + " damage!");
				continua.nextLine();
				break;
		}
	}

	//Vengono creati dei pokémon "fantocci" per calcolare gli effetti delle mosse sui vari bersagli
	public int moveEffect(IsmonMosse move, Creature attacker, int[] statsAt, int[] attackerMod, int[] attackerAccEva, Creature defender, int[] statsDe, int[] defenderMod, int[] defenderAccEva)
	{
		int damage = 0;

		int precision; 

		if(move.getID() == -1)
		{
			return 1;
		}

		showHPBar();
		System.out.println(" " + ((!turn) ? "The foe " : "") + attacker.getNickname() + " uses " + move.getName() + "!"); continua.nextLine();

		//la chance che l'attacco colpisca il bersaglio. Se manca, danni ed effetti dell'attacco non vengono calcolati.
		precision = ((move.getPrecision().charAt(0) == 'a') ? Integer.parseInt(move.getPrecision().substring(1)) : Integer.parseInt(move.getPrecision()) * (attackerAccEva[0] / defenderAccEva[1])); 																			

		//Se il d100 da un valore minore o pari a 'precision', o se la mossa è fatta per colpire a prescindere, si può continuare col processo
		if(Math.floor(Math.random()*100)+1 <= precision || move.getPrecision().charAt(0) == 'a')
		{
			if((move.getDamage().charAt(0) == 'f' && Integer.parseInt(move.getDamage().substring(1)) > 0) || (Integer.parseInt(move.getDamage()) > 0))
			{ 	
				try
				{
					damage = giveDamage(attacker, statsAt, move, defender, statsDe);
				}
				catch(Exception e)
				{
				}

				if(defender.getCurrentHP() - damage < 0)
				{
					damage += defender.getCurrentHP() - damage;
				}

				defender.setCurrentHP(-damage); //riduce gli HP del bersaglio per il valore del danno
				showHPBar();

				double typeText = Math.floor(typemod * 100);
				String effective = " ";

				if (damage > 0)
				{
					if(typeText < 100)
					{
						effective = " It's not really effective...\n ";
					}
					else if(typeText > 100)
					{
						effective = " It's super effective!\n "; 
					}

					System.out.println(effective +
									   ((crit) ? "A critical hit!\n " : "") +
									   ((turn) ? "The foe " : "") + defender.getNickname() + " receives " + damage + " damage!"); 
				} 
				else
				{
					double nE = chart.getNE() * 100;
					if(typeText == nE)
					{
						effective = " It doesn't have any effect...\n ";
						System.out.println(effective);
					}
				}

				continua.nextLine();
			}
			
			
			//Possibili effetti speciali
			specialEffect(move, damage, attacker, defender);

			//Possibili status aggiunti
			addStatus(move, attacker, defender);
			
			if(defender.getCurrentHP() <= 0 && attacker.getCurrentHP() > 0) //se gli HP del bersaglio raggiungono 0, gli effetti secondari della mossa sono ignorati se hanno come
			{								 							    //bersaglio sempre lui
				defender.setStatus(1);									    
				showHPBar(); 
				if(turn)
				{
					System.out.print(" The foe");
				}
				else
				{
					sentCreatures[sentIsmon][sentIsmonE] = false;
				}

				System.out.println(" " + defender.getNickname() + " is exhausted!"); 
				continua.nextLine();

				defenderMod = new int[]{0, 0, 0, 0, 0, 0}; 
				defenderAccEva[0] = 100; 
				defenderAccEva[1] = 100;

				if(turn) //Se è il turno del giocatore, vuole dire che un pokémon dell'avversario è andato K.O.
				{		 //Ricevi quindi EXP

					if(attacker.getLevel() < 100)
					{ 
						showHPBar(); 

						exp(defender.getIndex(), defender.getLevel());
					} 
				}
			}
			if(attacker.getCurrentHP() <= 0 && defender.getCurrentHP() > 0) //L'attaccante potrebbe andare K.O. per effetto di danno da contraccolpo, o l'abilità del bersaglio
			{								 
				attacker.setStatus(1);									    
				showHPBar(); 
				if(!turn)
				{
					System.out.print(" The foe");
				}
				else
				{
					sentCreatures[sentIsmon][sentIsmonE] = false;
				}

				System.out.println(" " + attacker.getNickname() + " is exhausted!"); 
				continua.nextLine();

				attackerMod = new int[]{0, 0, 0, 0, 0, 0}; 
				attackerAccEva[0] = 100; 
				attackerAccEva[1] = 100;

				if(!turn) //Se non è il turno del giocatore, vuole dire che un pokémon dell'avversario è andato K.O.
				{		 //Ricevi quindi EXP

					exp(attacker.getIndex(), attacker.getLevel());
				}
			}
			
			//Possibili modifiche a statistiche
			StatMod mod = sMod(move, attackerMod, attacker.getNickname(), attacker.getStatus(), defenderMod, defender.getNickname(), defender.getStatus());
			attackerMod = mod.getAMod();
			defenderMod = mod.getBMod();

		}
		else
		{
			showHPBar();
			if(!turn)
			{
				System.out.print(" The foe");
			}
			System.out.println(" " + attacker.getNickname() + "'s attack missed!\n"); 		
			continua.nextLine();
		}

		//Le stat dell'attaccante e difensore sono ricalcolate per eventuali buff o debuff ricevuti
		statsAt = recalculateStats(attacker.getStats(), attackerMod); 
		statsDe = recalculateStats(defender.getStats(), defenderMod); 

		System.out.print("\n");

		//I cambi alle statistiche, agli HP e altro sono passati agli effettivi pokémon in campo
		if(turn)
		{
			this.stats = statsAt; 

			this.statsE = statsDe; 

			this.statsM = attackerMod; 
			this.statsME = defenderMod;

			this.accEva = attackerAccEva;
			this.accEvaE = defenderAccEva; 
		}
		else
		{
			this.stats = statsDe;
			this.statsE = statsAt; 

			this.statsM = defenderMod; 
			this.statsME = attackerMod; 

			this.accEva = defenderAccEva;
			this.accEvaE = attackerAccEva;
		}

		return 0;
	}


	//Qui si decide l'ordine in cui i due avversari fanno la loro mossa
	public void turnOrder(IsmonMosse mossaScelta, IsmonMosse mE) 		
	{		
		if(priority > priorityE)
		{
			turn = true; 

			moveEffect(mossaScelta, playerMon, this.stats, statsM, accEva, enemyMon, this.statsE, statsME, accEvaE);

			if(enemyMon.getCurrentHP() <= 0)	//se l'ultimo pokémon che agisce va K.O. durante il turno del primo, non potrà effettuare un attacco
			{
			}
		    else
		    { 
		    	turn = false; 

		    	moveEffect(mE, enemyMon, statsE, statsME, accEvaE, playerMon, stats, statsM, accEva); 
		    }

		    getStatusEffect(playerMon, true);
		    getStatusEffect(enemyMon, false);
		}		
		else
		{
			if(priorityE > priority)
			{
				turn = false; 

				moveEffect(mE, enemyMon, statsE, statsME, accEvaE, playerMon, stats, statsM, accEva); 

				if (playerMon.getCurrentHP() <= 0) //se l'ultimo pokémon che agisce va K.O. durante il turno del primo, non potrà effettuare un attacco
				{
			    } 
			    else
			    {
			    	if(!swap)
			    	{
				    	turn = true; 

				    	moveEffect(mossaScelta, playerMon, stats, statsM, accEva, enemyMon, statsE, statsME, accEvaE); 
			    	}
			    }

			    getStatusEffect(enemyMon, false);
			    getStatusEffect(playerMon, true);
			}
			else
			{
				if (stats[5] > statsE[5]) //Si paragona la Stat Speed dei due pokémon: quello con Speed più alta agisce
				{ 						  //per primo.
					turn = true; 

					playerMon.setTP(5, 1); 
					moveEffect(mossaScelta, playerMon, stats, statsM, accEva, enemyMon, statsE, statsME, accEvaE); 

					if(enemyMon.getCurrentHP() <= 0)	//se l'ultimo pokémon che agisce va K.O. durante il turno del primo, non potrà effettuare un attacco
					{
					}
				    else
				    { 
				    	turn = false; 

				    	moveEffect(mE, enemyMon, statsE, statsME, accEvaE, playerMon, stats, statsM, accEva); 
				    }

				    getStatusEffect(playerMon, true);
				    getStatusEffect(enemyMon, false);
				}
				else 
				{ 
					if (statsE[5] > stats[5])
					{ 
						turn = false; 

						moveEffect(mE, enemyMon, statsE, statsME, accEvaE, playerMon, stats, statsM, accEva); 

						if (playerMon.getCurrentHP() <= 0) //se l'ultimo pokémon che agisce va K.O. durante il turno del primo, non potrà effettuare un attacco
						{
					    } 
					    else
					    { 
					    	turn = true; 

					    	moveEffect(mossaScelta, playerMon, stats, statsM, accEva, enemyMon, statsE, statsME, accEvaE); 
					    }

					    getStatusEffect(enemyMon, false);
					    getStatusEffect(playerMon, true);
					} 
					else 
					{ 
						int randomTurn = (int)(Math.round(Math.random())); 

						if(randomTurn == 0)
						{ 
							turn = true; 

							playerMon.setTP(5, 1); 
							moveEffect(mossaScelta, playerMon, stats, statsM, accEva, enemyMon, statsE, statsME, accEvaE); 

							if(enemyMon.getCurrentHP() <= 0) //se l'ultimo pokémon che agisce va K.O. durante il turno del primo, non potrà effettuare un attacco
							{
						    } 
						    else
						    { 
						    	turn = false; 

						    	moveEffect(mE, enemyMon, statsE, statsME, accEvaE, playerMon, stats, statsM, accEva); 
						    }

						    getStatusEffect(playerMon, true);
				    		getStatusEffect(enemyMon, false);
						}
						else
						{ 
							turn = false; 

							moveEffect(mE, enemyMon, statsE, statsME, accEvaE, playerMon, stats, statsM, accEva); 

							if (playerMon.getCurrentHP() <= 0) //se l'ultimo pokémon che agisce va K.O. durante il turno del primo, non potrà effettuare un attacco
							{
						    } 
						    else
						    { 
						    	turn = true;

						        moveEffect(mossaScelta, playerMon, stats, statsM, accEva, enemyMon, statsE, statsME, accEvaE); 
						    }

							getStatusEffect(enemyMon, false);
						} 
					} 
				}
			}
		}						
	}

	public IsmonMosse aiChooseMove(int opponentPP) 
	{	
		IsmonMosse choice = new IsmonMosse(14); int i = 0; int j = 0;
		IsmonMosse[] mosseE = enemyMon.getMosse();

		if(opponentPP == 0)
		{
			return choice;
		}

		ismonEsceltamosse = new int[mosseE.length];
		for(i = 0; i<ismonEsceltamosse.length; i++)
		{
			ismonEsceltamosse[i] = 0;
		}

		i = 0;

		int moreDamage = 0;
		for(i = 0; i<ismonEsceltamosse.length; i++)
		{
			if(mosseE[i].getCurrentPP() > 0)
			{
				int danno = Integer.parseInt(mosseE[i].getDamage());
				int higherDamage = Integer.parseInt(mosseE[moreDamage].getDamage());

				if(danno >= higherDamage)
				{
					if(danno > higherDamage)
					{
						ismonEsceltamosse[moreDamage] -= 1;
						moreDamage = i;
					}

					ismonEsceltamosse[i] += 1;
				}

				if(danno > 0)
				{
					Double typemod = chart.getEffect(mosseE[i].getType(), playerMon.getType());
					if(typemod > 1.0)
					{
						ismonEsceltamosse[i] += 2;
					}
					else if(typemod < 1.0)
					{
						if(playerMon.getCurrentHP() * 100 / playerMon.getStats()[0] > 15)
						{
							ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]-2, 0);
						}
					}
					else if(typemod == 0.0)
					{
						ismonEsceltamosse[i] = -1;
					}

					int atype[] = enemyMon.getType();    
		       		for(int tipo : atype)
		       		{
		       			if(tipo == mosseE[i].getType())
						{
							if(mosseE[i].getType() != 0)
							{
								ismonEsceltamosse[i] +=1;
								break;
							}
						}  
		       		}

		       		if(mosseE[i].getPhysical())
		       		{
		       			if(this.statsE[1] > this.statsE[3])
		       			{
		       				ismonEsceltamosse[i] +=1;
		       			}
		       			if(statsM[2] < 0 || statsM[2] > 0)
		       			{
		       				ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]-statsM[2], 0);
		       			}
		       			if(statsME[1] > 0 || statsME[1] < 0)
		       			{
		       				ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]+statsME[1], 0);
		       			}
		       		}
		       		else
		       		{
		       			if(this.statsE[3] > this.statsE[1])
		       			{
		       				ismonEsceltamosse[i] +=1;
		       			}
		       			if(statsM[4] < 0 || statsM[4] > 0)
		       			{
		       				ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]-statsM[4], 0);
		       			}
		       			if(statsME[3] > 0 || statsME[3] < 0)
		       			{
		       				ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]+statsME[3], 0);
		       			}
		       		}
				}

				String staMod = mosseE[i].getStatMod();

				//Si occupa delle mosse che aumentano/abbassano statistiche
				if(!staMod.equals(""))
				{
					String target = staMod.split(" ")[0];

					for(int nmods = 0; nmods < (staMod.split(" ").length-2); nmods+=2)
					{
						int staAffected = Integer.parseInt(staMod.split(" ")[nmods+2]), modified = Integer.parseInt(staMod.split(" ")[nmods+3]);

						if(this.stats[1] >= this.statsE[2])
						{
							if(target.equals("b"))
							{	
								if(staAffected == 1 && statsM[1] > -6)
								{
									ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]-modified, 0);
								}
							}
							else if(target.equals("a"))
							{
								if(staAffected == 2 && statsME[2] < 6)
								{
									ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]+modified, 0);
								}
							}
						}
						if(this.stats[2] >= this.statsE[1])
						{
							if(target.equals("b"))
							{	
								if(staAffected == 2 && statsM[2] > -6)
								{
									ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]-modified, 0);
								}
							}
							else if(target.equals("a"))
							{
								if(staAffected == 1 && statsME[1] < 6)
								{
									ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]+modified, 0);
								}
							}
						}
						if(this.stats[3] >= this.statsE[4])
						{
							if(target.equals("b"))
							{	
								if(staAffected == 3 && statsM[3] > -6)
								{
									ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]-modified, 0);
								}
							}
							else if(target.equals("a"))
							{
								if(staAffected == 4 && statsME[4] < 6)
								{
									ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]+modified, 0);
								}
							}
						}
						if(this.stats[4] >= this.statsE[3])
						{
							if(target.equals("b"))
							{	
								if(staAffected == 4 && statsM[4] > -6)
								{
									ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]-modified, 0);
								}
							}
							else if(target.equals("a"))
							{
								if(staAffected == 3 && statsME[3] < 6)
								{
									ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]+modified, 0);
								}
							}
						}
						if(this.stats[5] >= this.statsE[5])
						{
							if(staAffected == 5)
							{
								if(target.equals("b") && statsM[5] > -6)
								{	
									ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]-modified, 0);
								}
								else if(target.equals("a") && statsME[5] < 6)
								{
									ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]+modified, 0);
								}
							}
						}
					}
				}

				//Si occupa delle mosse con Priorità
				if(this.stats[5] >= this.statsE[5])
				{
					if(mosseE[i].getPriority() > 0)
					{
						ismonEsceltamosse[i] += 2;
						if(playerMon.getCurrentHP() * 100 / this.stats[0] <= 15)
						{
							ismonEsceltamosse[i] += 1;
						}
					}
				}

				if(mosseE[i].getCurrentPP() * 100 / mosseE[i].getMaxPP() <= 20)
				{
					ismonEsceltamosse[i] = Math.max(ismonEsceltamosse[i]-1, 0);
				}
			}
			else
			{
				ismonEsceltamosse[i] = -1;
			}
		}

		for(int m : ismonEsceltamosse)
		{
			System.out.print(m + "  ");
		}
		System.out.println("");

		int biggerPri = -1;
		int smallerPri = ismonEsceltamosse[0];
		for(int p : ismonEsceltamosse)
		{
			if(p > biggerPri)
			{
				biggerPri = p;
			}
			else if(p >= 0 && p < smallerPri)
			{
				smallerPri = p;
			}
		}

		Random r = new Random();
		int roll = r.nextInt(biggerPri-(smallerPri-1)) + smallerPri;

		System.out.print(roll);
		
		int[] lastMove = null;
		for(i = 0; i < ismonEsceltamosse.length; i++)
		{
			if(roll <= ismonEsceltamosse[i])
			{
				if(lastMove == null)
				{
					choice = mosseE[i];
					lastMove = new int[]{ismonEsceltamosse[i], i};
				}
				else if(lastMove[0] >= ismonEsceltamosse[i])
				{	
					int rand = 0;
				    if(lastMove[0] == ismonEsceltamosse[i])
				    {
				        switch((int)Math.round(Math.random()))
				        {
				        	case 0:
				        		choice = mosseE[i];
								lastMove = new int[]{ismonEsceltamosse[i], i};
								break;

				        	case 1:
				        		choice = mosseE[lastMove[1]];
				        		break;
				        }
				    }
				    else
				    {
				    	choice = mosseE[i];
						lastMove = new int[]{ismonEsceltamosse[i], i};
				    }
				}
			}
		}

		return choice;
	}


	public int chooseMove(int playerPP, int opponentPP) 
	{
		int defaultMove = 14;

		int m = 0; IsmonMosse mE = new IsmonMosse(defaultMove);

		int show = 0;
		int i = 0;
		int j = 0;

		IsmonMosse[] mosse = new IsmonMosse[4];
		for(i = 0; i < 4; i++)
		{
			mosse[i] = playerMon.getMosse()[i];
		}
		
		IsmonMosse[] mosseE = new IsmonMosse[4];
		for(i = 0; i < 4; i++)
		{
			mosseE[i] = enemyMon.getMosse()[i];
		}

		IsmonMosse mossaScelta = new IsmonMosse(defaultMove);

		if(swap)
		{
			mE = aiChooseMove(opponentPP);
			mossaScelta = new IsmonMosse(0);
			if(!switchMember(ismon, false))
			{
				return 0;
			}
			priority = -99;
		}
		else
		{
			i = 0;

			do
			{
				if(mosse[i].getID() != 0)
				{
					j++;
				}
				i++;
			}
			while(i < 4); i = 0;

			do
			{
				showHPBar();

				if(playerPP == 0)
			    {
				    System.out.println(playerMon.getNickname() + " doesn't have any PP left!");
				    continua.nextLine(); 
				    break;
			    }	
				else
				{

					do
					{
						inputCheck = true;

						System.out.print("   ");
						do
						{
							show = i+1;

							System.out.print(show + ": " + mosse[i].getName() + "[" + mosse[i].getCurrentPP() + "/" + mosse[i].getMaxPP() + "]          ");
							if (i == 1){ System.out.print("\n   ");}
							i++;
						}
						while(i < j); i = 0;
				
						System.out.print("\n\n              >");  

						try
						{
							m = z.nextInt(); 
						}
						catch(Exception iME)
						{
							inputCheck = false;
							z.next();

							System.out.print("\033[H\033[2J");  
	      					System.out.flush(); 
	      					showHPBar();
						}
					}
					while(!inputCheck);

					if(m > j || m < 1) 
					{ 
						return 0; 
					}

					m-=1;

					if (mosse[m].getCurrentPP() > 0)
					{ 
						mosse[m].setCurrentPP(-1); 
						mossaScelta = mosse[m]; 
					}
					else 
					{ 
						showHPBar(); 
						System.out.print("\n You don't have PP to use " + mosse[m].getName() + "!\n\n"); 
						continua.nextLine(); 
					}
				}
			}
			while(mosse[m].getCurrentPP() <= 0);
			priority = mossaScelta.getPriority();

			mE = aiChooseMove(opponentPP); 
			priorityE = mE.getPriority(); 
		}

		turnOrder(mossaScelta, mE);

		return 1;
	}

	public boolean switchMember(Creature[] team, boolean ko)
	{
		boolean error = false;

		ShowTeam sw = null;
		int i = 0;
		int nismon = 0;

		do
		{
			if(ismon[i] != null)
			{
				nismon+=1;
			}
			i++;
		}
		while(i<6);

		if(ko)
		{
			do
			{
				inputCheck = true;
				try
				{
					sw = new ShowTeam(team, true);
				}
				catch(Exception e)
				{
					inputCheck = false;
					sc.next();
				}
			}
			while(!inputCheck || sw.getScelta() > nismon || sentIsmon == sw.getScelta()-1 || (ismon[sw.getScelta()-1].getStatus()) == 1);

			showHPBar();

			System.out.println("Return, " + playerMon.getNickname() + "!");
			continua.nextLine();

			playerMon = ismon[sw.getScelta()-1];
			sentIsmon = sw.getScelta()-1;
			sentCreatures[sentIsmon][sentIsmonE] = true;
			statsM = new int[]{0, 0, 0, 0, 0, 0}; 
	    	accEva = new int[]{100, 100};
	    	this.stats = recalculateStats(playerMon.getStats(), statsM);

			showHPBar();

			System.out.println("It's your turn, " + playerMon.getNickname() + "!");
			continua.nextLine();

			return true;
		}
		else
		{
			do
			{
				inputCheck = true;
				try
				{
					sw = new ShowTeam(team, true);
				}
				catch(Exception e)
				{
					inputCheck = false;
					sc.next();
				}
			}
			while(!inputCheck || sw.getScelta() > nismon || (ismon[sw.getScelta()-1].getStatus()) == 1);

			if(sentIsmon == sw.getScelta()-1)
			{
				return false;
			}
			else
			{
				showHPBar();

				System.out.println("Return, " + playerMon.getNickname() + "!");
				continua.nextLine();

				playerMon = ismon[sw.getScelta()-1];
				sentIsmon = sw.getScelta()-1;
				sentCreatures[sentIsmon][sentIsmonE] = true;
				statsM = new int[]{0, 0, 0, 0, 0, 0}; 
		    	accEva = new int[]{100, 100};
		    	this.stats = recalculateStats(playerMon.getStats(), statsM);

				showHPBar();

				System.out.println("It's your turn, " + playerMon.getNickname() + "!");
				continua.nextLine();

				return true;
			}
		}
	}

}