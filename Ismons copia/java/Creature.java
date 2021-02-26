import java.io.*;
import java.util.Scanner;
import java.util.*;  
import java.io.File;

public class Creature 
{

	private String name = "NN"; private int gender = 2; 
	private int index = 0;
	private int[] type;
	String nickname = null;
	private int level = 1;
    private int[] base;
    private int[] stats;
    private int currentHP = 0;
    private int[] tp;
    private int ability = 0;
    private int nature = 0;
    private int characteristic = 0;
    private int leveluprate;
    private int exp = 0;
    private int levelup = 0;
    private String evolve = null;
    private String overworldAction = "";

    private Scanner continua = new Scanner(System.in);

    private int status = 0;

    private IsmonMosse[] moves = new IsmonMosse[4];

    private CreaturesList creature;
    	
    private Stats2 calcStats = new Stats2();

    private FileReader readerstats;
    private Properties propstats;

    //Questo costruttore lo usiamo per i pokémon di un altro allenatore
    public Creature(String eIsmon)
    {
    	int i;
    	int m = 0;

    	try
    	{
    		creature = new CreaturesList();
   		}
   		catch(Exception c)
   		{
   		}

   		this.index = Integer.parseInt(eIsmon.split(" , ")[0]);
   		this.name = creature.getIsmonName(this.index);
		this.gender = Integer.parseInt(eIsmon.split(" , ")[1]);	
		this.level = Integer.parseInt(eIsmon.split(" , ")[2]);
    	this.base = creature.getIsmonBase(this.index);
    	this.stats = new int[base.length];
    	this.nature = Integer.parseInt(eIsmon.split(" , ")[3]);
    	this.characteristic = Integer.parseInt(eIsmon.split(" , ")[4]);
    	this.ability = Integer.parseInt(eIsmon.split(" , ")[5]);

    	this.tp = new int[base.length];
    	for(i=0; i<this.tp.length; i++)
    	{
    		this.tp[i] = Integer.parseInt((eIsmon.split(" , ")[6]).split(" ")[i]);
    	}
    	
    	this.type = new int[creature.getType(this.index).split(" ").length];
    	for(i=0; i<this.type.length; i++)
    	{
    		this.type[i] = Integer.parseInt(creature.getType(this.index).split(" ")[i]);
    	}

    	this.leveluprate = creature.getLeveluprate(this.index);
    	SetLevelUp lvup = new SetLevelUp(this.leveluprate, this.level);
    	SetLevelUp lvupafter = new SetLevelUp(this.leveluprate, this.level+1);
    	this.exp = (int)lvup.giveLevelUp();
    	this.levelup = (int)lvupafter.giveLevelUp();

    	this.evolve = creature.getEvolve(this.index);

    	for(i=0; i<4; i++)
    	{ 
    		int moveID = Integer.parseInt((eIsmon.split(" , ")[7]).split(" ")[i]);
    		this.moves[i] = new IsmonMosse(moveID);
    	}

		setStats();

        currentHP = stats[0];
    }

    //Questo costruttore lo usiamo per pokémon catturati
    public Creature(Creature ismoncopy) 
    {
    	int i;
    	int m = 0;

		try
    	{
    		creature = new CreaturesList();
   		}
   		catch(Exception c)
   		{
   		}

    	this.index = ismoncopy.getIndex();

    	this.name = creature.getIsmonName(this.index);
    	this.nickname = ismoncopy.nickname;
    	this.gender = ismoncopy.getGender();
    	this.level = ismoncopy.getLevel();
    	this.base = creature.getIsmonBase(this.index);
    	this.stats = new int[base.length];
    	this.ability = ismoncopy.getAbility();
    	this.nature = ismoncopy.getNature();
    	this.tp = new int[base.length];
    	for(i = 0; i < this.tp.length; i++)
    	{
    		setTP(i, ismoncopy.getTP()[i]);
    	}
    	this.characteristic = ismoncopy.getCharacteristic();

    	this.type = new int[creature.getType(this.index).split(" ").length];
    	for(i=0; i<this.type.length; i++)
    	{
    		this.type[i] = Integer.parseInt(creature.getType(this.index).split(" ")[i]);
    	}

    	this.leveluprate = creature.getLeveluprate(this.index);
    	SetLevelUp lvup = new SetLevelUp(this.leveluprate, this.level);
    	SetLevelUp lvupafter = new SetLevelUp(this.leveluprate, this.level+1);
    	this.exp = ismoncopy.getExp();
    	this.levelup = (int)lvupafter.giveLevelUp();

    	this.evolve = creature.getEvolve(this.index);

    	try
    	{
    		this.overworldAction = creature.getOverworldAction(this.index);
    	}
    	catch(Exception e)
    	{
    	}

    	for(i = 0; i < 4; i++)
    	{
    		this.moves[i] = ismoncopy.getMosse()[i];
    	}

		setStats();

		this.currentHP = ismoncopy.getCurrentHP();
    }

    //Questo costruttore lo usiamo per pokémon ricevuti
	public Creature(int index, int gender, String nickname, int level, int ability, int[] tp, int nature, int characteristic, int[] mosse)
	{
		int i;
		int m = 0;

		try
    	{
    		creature = new CreaturesList();
   		}
   		catch(Exception c)
   		{
   		}

		this.index = index;

		this.name = creature.getIsmonName(this.index);	
		this.gender = gender;
		setNickname(nickname);
		this.level = level;
    	this.base = creature.getIsmonBase(this.index);
    	this.stats = new int[base.length];
    	this.ability = ability;
    	this.tp = new int[base.length];
    	setNature(nature);

    	for(i = 0; i < this.tp.length; i++)
    	{
    		setTP(i, tp[i]);
    	}

    	this.characteristic = characteristic;

    	this.type = new int[creature.getType(this.index).split(" ").length];
    	for(i=0; i<this.type.length; i++)
    	{
    		this.type[i] = Integer.parseInt(creature.getType(this.index).split(" ")[i]);
    	}

    	this.leveluprate = creature.getLeveluprate(this.index);
    	SetLevelUp lvup = new SetLevelUp(this.leveluprate, this.level);
    	SetLevelUp lvupafter = new SetLevelUp(this.leveluprate, this.level+1);
    	this.exp = (int)lvup.giveLevelUp();
    	this.levelup = (int)lvupafter.giveLevelUp();

    	this.evolve = creature.getEvolve(this.index);

    	try
    	{
    		this.overworldAction = creature.getOverworldAction(this.index);
    	}
    	catch(Exception e)
    	{
    	}

    	for(i=0; i<4; i++)
    	{
    		this.moves[i] = new IsmonMosse(mosse[i]); 
    	}

		setStats();

        currentHP = stats[0];
	}

    public int[] getStats()
    {
    	return this.stats;
    }
	public void setStats()
	{
        this.stats = calcStats.getStats(this.base, this.tp, this.level, this.nature);
	}

	public int getCurrentHP()
	{
		return this.currentHP;
	}
	public void setCurrentHP(int health)
	{
		if(this.currentHP + health > this.stats[0])
		{ 
			this.currentHP = this.stats[0]; 
		}
		else
		{ 
			this.currentHP += health; 
		}
	}


    public String getName()
    {
    	return this.name;
    }

    public void setNickname(String nickname)
    {
    	if(nickname == null || nickname.equals(""))
		{
			this.nickname = null;
		}
		else
		{
    		this.nickname = nickname;
    	}
    }
 	public String getNickname()
 	{
 		return (this.nickname == null ? this.name : this.nickname);
 	}

 	public int getGender()
 	{
 		return this.gender;
 	}

    public void setEvolve()
    {	
    	if(!this.evolve.equals("none"))
    	{	
    		int evolveTo = 0; //L'indice del pokémon in cui si evolve
	    	boolean evolve = false; //Controlla se è possibile per il pokémon evolversi
	    	int add1 = 0; int add2 = 0;

		    //Controlla quante evoluzioni alternative ha il pokémon. Eevee, per esempio, ne ha 8.
	    	for(int i = 0; i < this.evolve.split(" || ").length; i++)
	    	{	
	    		try 
	    		{ 
	    			evolveTo = Integer.parseInt(this.evolve.split(" || ")[i].split(" - ")[1]); 
	    		}
	    		catch(ArrayIndexOutOfBoundsException aiobe) 
	    		{ 
	    			evolveTo = Integer.parseInt(this.evolve.split(" - ")[1]); 
	    		}

	    		//Controlla quante condizioni sono necessarie affinché si evolvi. Per esempio, Stenee deve conoscere
	    		//la mossa pestone E essere almeno al livello 21.
	    		for(int j = 0; j < this.evolve.split(" && ").length; j++)
	    		{
	    			//Questa stringa tiene la condizione per evolversi di cui il programma controlla se 
	    			//è stata soddisfatta
	    			String method = "";
	    			try 
	    			{ 
	    				method = this.evolve.split(" && ")[j].split("\\(")[0]; 
	    			}
		    		catch(ArrayIndexOutOfBoundsException aiobe) 
		    		{ 
		    			method = this.evolve.split("\\(")[0]; 
		    		}
	    			
	    			switch(method)
	    			{
	    				//A un certo livello il pokémon si evolve
	    				case "LEVEL":
	    					if(this.level >= Integer.parseInt(this.evolve.split("\\(")[1].split("\\)")[0]))
	    					{
	    						evolve = true;
	    					}
	    					else
	    					{
	    						evolve = false;
	    					}
	    					break;

	    				//Il pokémon si evolve facendogli toccare una Pietra specifica
	    				case "STONE":
	    					evolve = false;
	    					break;
	    			}
	    		}

	    		if(evolve)
	    		{
	    			break;
	    		}
	    	}

	    	//Se tutte le condizioni sono soddisfatte, il pokémon può evolversi.
	    	if(evolve)
	    	{
		    	int previousIndex = this.index;
		    	String prename = this.getNickname();
		    	//L'index del pokémon viene cambiato con quello della sua evoluzione
				this.index = evolveTo;
				
				//I parametri del pokémon (Nome Specie, Tipo, Statistiche etc.) sono ricalcolati usando il nuovo index
				this.name = creature.getIsmonName(this.index);

				this.type = new int[creature.getType(this.index).split(" ").length-1];
    			for(int i=0; i<this.type.length; i++)
    			{
    				this.type[i] = Integer.parseInt(creature.getType(this.index).split(" ")[i]);
    			}		
				this.base = creature.getIsmonBase(this.index);
				setStats();

				this.leveluprate = creature.getLeveluprate(this.index);
				SetLevelUp lvup = new SetLevelUp(this.leveluprate, this.level);
		    	SetLevelUp lvupafter = new SetLevelUp(this.leveluprate, this.level+1);
				this.levelup = (int)lvupafter.giveLevelUp();
				this.evolve = creature.getEvolve(this.index);
				this.overworldAction = creature.getOverworldAction(this.index);

				System.out.print("\n  " + prename + " evolved into " + this.name + "!");
				continua.nextLine();
	    	}
    	}
    }

    public int getLevel()
    {
    	return this.level;
    }
	public void addExp(int exp)
	{
		if(this.level < 100)
		{
			this.exp += exp;
		}
	}
	public void checkLevelUp()
	{
		String home = System.getProperty("user.dir");
        home = home.substring(0, home.length()-5);
        String fs = File.separator;
        try
        {
            readerstats = new FileReader(new File(home + fs + "data" + fs + "menuText" + fs + "pokemonInfo.txt"));
        }
        catch(FileNotFoundException fnfe)
        {
        }
        propstats = new Properties();
        try
        {
            propstats.load(readerstats);
        }
        catch(IOException ioe)
        {
        }

		String[] nameStat = propstats.getProperty("stats").split(", ");
		int[] movesID = new int[]{moves[0].getID(), moves[1].getID(), moves[2].getID(), moves[3].getID()};
		Creature ismonbefore = new Creature(this.index, this.gender, this.nickname, this.level, this.ability, this.tp, this.nature, this.characteristic, movesID);	

		SetLevelUp lvup;
		SetLevelUp lvupafter;
		lvup = new SetLevelUp(leveluprate, level);
		lvupafter = new SetLevelUp(leveluprate, level+1);

		if(this.exp >= this.levelup && this.level < 100)
		{
			while(this.exp >= this.levelup && this.level < 100)
			{ 
				System.out.print("\033[H\033[2J");  
        		System.out.flush(); 

				this.level = Math.min(this.level + 1, 100);

				//Modifica le Stat in accordo con il Salire di livello
				setStats();

				//Crea un'istanza per le statistiche della Creatura prima del level up
				try
				{
					ismonbefore = new Creature(this.index, this.gender, this.nickname, this.level-1, this.ability, this.tp, this.nature, this.characteristic, movesID);
				}
				catch(Exception im)
				{
				}
				
				int[] statbefore = new int[6];

				//Salva le stat vecchie nella variabile "statbefore"
				statbefore = ismonbefore.getStats();
       			String message = ("\n  " + getNickname() + " raised to Level " + this.level + "!");

				//Aumenta l'esperienza necessaria per salire di livello
				if(this.level == 100)
				{
					this.exp = 0;
				}
				else
				{
    				lvupafter = new SetLevelUp(this.leveluprate, this.level+1);
				}

				this.levelup = (int)lvupafter.giveLevelUp();

				System.out.println(message + "\n--------------------------");

				//Qua mostra l'incremento di ogni statistica al salire di livello
				for(int i=0; i<this.stats.length; i++)
				{
					String spazio = ": "+new String(new char[3-nameStat[i].length()]).replace("\0", " ");
					System.out.print("\n  " + nameStat[i] + spazio + statbefore[i] + " + " + (this.stats[i]-statbefore[i]));
				}

				this.currentHP = this.currentHP + (this.stats[0] - statbefore[0]);

				System.out.println("\n"); 	
				continua.nextLine();

				System.out.print("\033[H\033[2J");  
       			System.out.flush(); 

				System.out.println(message + "\n--------------------------");

				//Qui mostra i nuovi valori delle statistiche al salire di livello
				for(int i=0; i<6; i++)
				{
					String spazio = ": "+new String(new char[3-nameStat[i].length()]).replace("\0", " ");
					System.out.print("\n  " + nameStat[i] + spazio + this.stats[i]);
				}

				System.out.println("\n"); 
				continua.nextLine();
			}

			setEvolve();
		}
		else
		{
			lvupafter = new SetLevelUp(leveluprate, level+2);
			if(this.exp < (int)lvupafter.giveLevelUp())
			{
			   	lvup = new SetLevelUp(leveluprate, level);
			}
		}
	}

	public int getExp()
	{
		return this.exp;
	}

	public int getLevelUpBefore()
	{
		SetLevelUp lvup = new SetLevelUp(leveluprate, level);
		return (int)lvup.giveLevelUp();
	}
	public int getLevelUp()
	{
		return this.levelup;
	}
	public int getLevelUpRate()
	{
		return this.leveluprate;
	}

	public int[] getBase()
	{
		return this.base;
	}

	public int[] getTP()
	{
		return this.tp;
	}

	//Prima di incrementare i TP del Pokémon, è necessario controllare
	//se esso non abbia già raggiunto il limite massimo di TP che potesse
	//guadagnare
	public void setTP(int stat, int tp) 
	{
		this.tp[stat] = Math.max(Math.min(this.tp[stat] + tp, 750), 0);
		setStats();
	}

	public int getAbility()
	{
		return this.ability;
	}

	public int getNature()
	{
		return this.nature;
	}
	public void setNature(int nature)
	{
		this.nature = nature;
		setStats();
	}

	public int getCharacteristic()
	{
		return this.characteristic;
	}

	public int[] getType()
	{
		return this.type;
	}

	public IsmonMosse[] getMosse()
	{
		return this.moves;
	}

	public int getStatus()
	{
		return this.status;
	}
	public boolean setStatus(int status)
	{	
		if(this.status == 0)
		{
			this.status = status;
		}
		else
		{
			return false;
		}
		return true;
	}

	public int getIndex()
	{
		return this.index;
	}

	public String getOverworldAction()
	{
		return this.overworldAction;
	}
}