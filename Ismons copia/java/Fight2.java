public class Fight2
{
	private TypeChart chart = new TypeChart();

	private int battleRule = 0; //0: single, 1: double, 2: rotation
	private int opponent = 0; //0: Wild Mon, 1: Trainer
	private int ai = 0;

	private boolean turn = true; //checks if it's the player turn to attack

	private boolean[][] sentCreatures = new boolean[6][6]; //Controlla quali pokémon sono stati mandati in campo e quali di questi pokémon
														   //hanno incontrato un pokémon avversario

	private Creature[] playerTeam = new Creature[6];
	private Creature[] enemyTeam = new Creature[6];

	private double typeMod;

	private Creature[] playerMon;
	private int playerSent = 0; //keeps the place in the team of the pokémon on the field
	private int[] playerMods = new int[]{0, 0, 0, 0, 0, 0, 0, 0}; //buff or debuffs gained by the mon while in battle
	private int pPriority = 0;


	private Creature[] enemyMon; 
	private int enemySent = 0; //keeps the place in the team of the pokémon on the field
	private int[] enemyMods = new int[]{0, 0, 0, 0, 0, 0, 0, 0}; //buff or debuffs gained by the mon while in battle
	private int ePriority = 0;

	public Fight2(Creature[] ismon, Creature[] ismonE, int smartIA, int a)
	{	
		int action = 1;
		opponent = a;
        boolean battle = true; 

        int playerConditions = 0;  int enemyConditions = 0; 

		playerTeam = ismon; enemyTeam = ismonE; 
		playerMon = ismon[0]; enemyMon = ismonE[0];
		ai = smartIA;

		sentCreatures[sentIsmon][sentIsmonE] = true;

		do
		{
			int playerPP = 0; int enemyPP = 0; int i = 0; int j = 0;

			for(i = 0; i<4; i++){ playerPP += playerMon.getMosse()[i].getCurrentPP(); }  //controlla i PP totali 
			for(i = 0; i<4; i++){ enemyPP += enemyMon.getMosse()[i].getCurrentPP(); } //controlla i PP totali del nemico

			playerConditions = getTeamConditions(playerTeam); //Ottiene le condizioni del team
			enemyConditions = getTeamConditions(enemyTeam); //Ottiene le condizione del team nemico

			System.out.print("\033[H\033[2J");  
      		System.out.flush(); 

			showHPBar();

			if (playerConditions == 0) //se non hai alcun pokémon in forze, perdi
			{	
				System.out.println(" You lost... "); continua.nextLine(); battle = false;
			}
			else 
			{ 
				if(opponentConditions == 0) //se l'avversario non ha alcun pokémon in forze, vinci
				{ 
					System.out.println(" You won! "); continua.nextLine(); battle = false;
				}
				else
				{
					if(playerIsmon.getStatus().equals("K.O."))
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
						case 1: 
							chooseMove(playerPP, opponentPP, false); 
							break;

						case 2:
							if(switchMember(ismon, false))
							{
								chooseMove(playerPP, opponentPP, true); 
							} 
							break;

						default:
							break; 
					}			
				}
			}
		}
		while(battle);
	}

	public void showHPBar()  				
	{													//Crea le barre degli HP mostrate in lotta. Ogni barra è formata da 20 tacche, e calcola quante 
		int i = 0; int j = 0; String hpbar = "";        //tacche "riempire" dal rapporto percentual di HP attuali e HP massimi del pokémon

		System.out.print("\033[H\033[2J");  
        System.out.flush(); 

		j = ((enemyMon.getCurrentHP() * 100) / enemyMon.getStats()[0]) / 5; // x : 100 = HP : maxHP

		if (enemyMon.getCurrentHP() > 0 && j < 1) //Finchè il Pokémon avrà più di 0 HP, verrà sempre mostrata almeno una tacca
		{ 
			j = 1; 
		}

		for(i = 0; i < 20; i++) // Riempe la barra con le tacche 
		{ 
			if (i < j)
			{ 
				hpbar += "|"; 
			} 
			else
			{ 
				hpbar += " "; 
			}
		}

		i = 0; j = 0;

		System.out.println("                   " + enemyMon.getNickname() + "  HP:  <" + hpbar + ">  " + opponentIsmon.getStatus() + "\n\n\n\n\n");

		hpbar = "";

		j = ((playerMon.getCurrentHP() * 100) / playerMon.getStats()[0]) / 5; // x : 100 = HP : maxHP

		if (playerMon.getCurrentHP() > 0 && j < 1) //Finchè il Pokémon avrà più di 0 HP, verrà sempre mostrata almeno una tacca
		{ 
			j = 1;
	    }

		for(i = 0; i < 20; i++) // Riempe la barra con le tacche 
		{ 
			if (i < j)
			{ 
				hpbar += "|"; 
			} 
			else
			{ 
				hpbar += " "; 
			}
		}

		i = 0; j = 0;

		System.out.println(" " + playerMon.getNickname() + "  HP:  <" + hpbar + ">  " + playerMon.getStatus()); 
		System.out.println("                        " + playerMon.getCurrentHP() + "/" + playerMon.getStats()[0] + "\n\n");

	}
}