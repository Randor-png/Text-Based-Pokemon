public class Box
{
	String[] nameStat = new String[]{"HP","Attack","Defense","Special Attack","Special Defense","Speed"};

	private String name = "NN"; private int gender = 2; 
	private int index = 0;
	private String type = "TT";
	private String type2 = "TT";
	private String nickname = "NK";
	private int level = 1;
    private int[] base = new int[6];
    private int[] stats = new int[6];
    private int[] tp = new int[6];
    private String ability = "AA";
    private int nature = 0;
    private int leveluprate;
    private int exp = 0;
    private int levelup;
    private int evolve;
    private int[] mosse = new int[4];

    private CreaturesList creature = new CreaturesList();
    Stats2 a;



	public Box(String name, int gender, int index, String type, String type2, int level, int exp, int leveluprate, int evolve, int[] base, String ability, int[] tp, int nature, int[] mosse)
	{

		this.name = name;	this.gender = gender;
		this.index = index;
		this.level = level;
    	this.base = base;
    	this.ability = ability;
    	this.tp = tp;
    	this.nature = nature;
    	this.type = type;
    	this.type2 = type2;
    	this.leveluprate = leveluprate;
    	SetLevelUp lvup = new SetLevelUp(this.leveluprate, this.level);
    	SetLevelUp lvupafter = new SetLevelUp(this.leveluprate, this.level+1);
    	this.exp = (int)lvup.giveLevelUp();
    	this.levelup = (int)lvupafter.giveLevelUp() - this.exp;
    	this.evolve = evolve;
    	this.mosse = mosse;

		for (int i=0; i<6; i++)
        {
        	a = new Stats2(this.base[i], this.tp[i], this.level);
        	a.setNature(this.nature, i);
        	this.stats[i] = (int)a.getStat();
        }
	}

    public int[] getStats()
    {
    	return this.stats;
    }
	public void setStats()
	{

		for (int i=0; i<6; i++)
        {
        	a = new Stats2(this.base[i], this.tp[i], this.level);
        	a.setNature(this.nature, i);
        	this.stats[i] = (int)a.getStat();
        }
	}

    public String getName()
    {
    	return this.name;
    }

    public void setNickname(String nickname)
    {
    	if(nickname.equals(""))
		{
			this.nickname = creature.getIsmonName(this.index);
		}
		else
		{
    		this.nickname = nickname;
    	}
    }
 	public String getNickname()
 	{
 		return this.nickname;
 	}

  	public int getGender()
 	{
 		return this.gender;
 	}

    public void setEvolve()
    {
    	SetLevelUp lvup = new SetLevelUp(this.leveluprate, this.level);
    	SetLevelUp lvupafter = new SetLevelUp(this.leveluprate, this.level+1);

    	if(this.level >= this.evolve)
		{	
			if (this.index == 1 || this.index == 2 || this.index == 4 || this.index == 5 || this.index == 7 || this.index == 8 || this.index == 10 || this.index == 11 || this.index == 13 || this.index == 14 || this.index == 16 || this.index == 17)
			{
				if(this.nickname.equals(creature.getIsmonName(this.index)))
				{
					this.nickname = creature.getIsmonName(this.index+1);
				}
				this.name = creature.getIsmonName(this.index+1);
				this.type = creature.getType(this.index+1);
				this.type2 = creature.getType2(this.index+1);
				this.base = creature.getIsmonBase(this.index+1);
				this.levelup = this.levelup = (int)lvupafter.giveLevelUp() - this.exp;
				this.evolve = creature.getEvolve(this.index+1);
			}

			this.index+=1;
		}
    }

    public int getLevel()
    {
    	return this.level;
    }
	public void setExp(int exp)
	{
		SetLevelUp lvup;
		SetLevelUp lvupafter;
		lvup = new SetLevelUp(leveluprate, level);
		lvupafter = new SetLevelUp(leveluprate, level+1);

		if(this.level == 100)
		{
			this.exp = 0;
		}
		else
		{
			this.exp += exp;
		}
		if(this.exp >= (int)lvupafter.giveLevelUp() && this.level < 100)
		{
			while(this.exp >= (int)lvupafter.giveLevelUp() && this.level < 100)
			{ 

				Math.min(this.level += 1, 100);

				//Modifica le Stat in accordo con il Salire di livello
				setStats();

				//Crea un'istanza per le statistiche della Creatura prima del level up
				Creature ismonbefore = new Creature(this.name, this.gender, this.index, this.type, this.type2, this.level-1, this.exp, this.leveluprate, this.evolve, this.base, this.ability, this.tp, this.nature, this.mosse);
				int[] statbefore = new int[6];

				//Salva le stat vecchie nella variabile
				statbefore = ismonbefore.getStats();
       			System.out.println("\n  " + this.nickname + " raised to Level " + this.level + "!");

       			//Mostra di quanto sono aumentate le stat al salire di livello (es. 5 + 1)
				setEvolve();

				//Aumenta l'esperienza necessaria per salire di livello
				if(this.level == 100)
				{
					this.exp = 0;
				}
				else
				{
    				lvupafter = new SetLevelUp(this.leveluprate, this.level+1);
    				this.levelup = (int)lvupafter.giveLevelUp() - this.exp;
				}


				for(int i=0; i<6; i++)
				{
					System.out.print("\n  " + nameStat[i] + ": " + this.stats[i] + "   (" + statbefore[i] + " + " + (this.stats[i]-statbefore[i]) + ")  (TP: " + this.tp[i] + ")");
				}
				System.out.println("\n");
				try
				{
					Thread.sleep(1000);
				}
				catch(InterruptedException interrupted)
				{
				}
			}
		}
		else
		{
			lvupafter = new SetLevelUp(leveluprate, level+1);
			if(this.exp < (int)lvupafter.giveLevelUp())
			{
			   	 lvup = new SetLevelUp(leveluprate, level);
			   	 int lvup1 = (int)lvup.giveLevelUp();
    			this.levelup -= exp;
			}
		}
	}
	public int getExp()
	{
		return this.exp;
	}

	public int getLevelUp()
	{
		return this.levelup;
	}

	public int[] getBase()
	{
		return this.base;
	}

	public int[] getTP()
	{
		return this.tp;
	}
	public void setTP(int[] tp)
	{
		this.tp = tp;
		setStats();
	}

	public String getAbility()
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
}