import java.util.*;  
import java.io.*;  
import java.io.File;

public class CreaturesList
{
	private FileReader reader;
	private Properties p;

	//private FileReader readerType;
	//private Properties propType;

	private String ismon;

	private String name;
	private int index; 
	private int[] base;  
	private int ability1;
	private int ability2;
	private String type;
	private int leveluprate;
	private String evolve;
	private int move;
	private int[] startmove;
	private int exp;
	private boolean[] evostone; //Fuoco, Tuono, Idro, Sole, Luna, Brillo, Albore, Nero
	private String overworldAction;

/*

	public int getIsmonMoves(int i, int l)
	{
		move = 0;

		switch (i)
		{
			case 1: switch(l){ case 7: move = 14; break; case 9: move = 6; break; case 13: move = 15; break; case 14: move = 16; break; case 15: move = 17; break; case 19: move = 18; break; case 21: move = 19; break; case 25: move = 20; break; case 27: move = 21; break; case 31: move = 22; break; case 33: move = 24; break; case 37: move = 23; break; default: break; } break;
			case 2: switch(l){ case 7: move = 14; break; case 9: move = 6; break; case 13: move = 15; break; case 14: move = 16; break; case 15: move = 17; break; case 20: move = 18; break; case 23: move = 19; break; case 28: move = 20; break; case 31: move = 21; break; case 36: move = 22; break; case 39: move = 24; break; case 44: move = 25; break; default: break; } break;
			case 3: switch(l){ case 7: move = 14; break; case 9: move = 6; break; case 13: move = 15; break; case 14: move = 16; break; case 15: move = 17; break; case 20: move = 18; break; case 23: move = 19; break; case 28: move = 20; break; case 31: move = 21; break; case 32: move = 26; break; case 39: move = 22; break; case 45: move = 24; break; case 50: move = 27; break; case 53: move = 25; break; default: break;} break;
			case 4: switch(l){ case 7: move = 8; break; case 10: move = 28; break; case 16: move = 29; break; case 19: move = 30; break; case 25: move = 31; break; case 28: move = 32; break; case 34: move = 33; break; case 37: move = 34; break; case 43: move = 35; break; case 46: move = 36; break; default: break; } break;
			case 5: switch(l){ case 7: move = 8; break; case 10: move = 28; break; case 17: move = 29; break; case 21: move = 30; break; case 28: move = 31; break; case 32: move = 32; break; case 39: move = 33; break; case 43: move = 34; break; case 50: move = 35; break; case 54: move = 36; break; default: break; } break;
			case 6: switch(l){ case 7: move = 8; break; case 10: move = 28; break; case 17: move = 29; break; case 21: move = 30; break; case 28: move = 31; break; case 32: move = 32; break; case 36: move = 37; break; case 41: move = 33; break; case 47: move = 34; break; case 56: move = 35; break; case 62: move = 36; break; case 71: move = 38; break; case 77: move = 39; break; default: break; } break;
			case 7: switch(l){ case 7: move = 7; break; case 10: move = 11; break; case 13: move = 12; break; case 16: move = 40; break; case 19: move = 41; break; case 22: move = 42; break; case 25: move = 43; break; case 28: move = 44; break; case 31: move = 45; break; case 34: move = 46; break; case 37: move = 47; break; case 40: move = 48; break; default: break; } break;
			case 8: switch(l){ case 7: move = 7; break; case 10: move = 11; break; case 13: move = 12; break; case 16: move = 40; break; case 20: move = 41; break; case 24: move = 42; break; case 28: move = 43; break; case 32: move = 44; break; case 36: move = 45; break; case 40: move = 46; break; case 44: move = 47; break; case 48: move = 48; break; default: break; } break;
			case 9: switch(l){ case 7: move = 7; break; case 10: move = 11; break; case 13: move = 12; break; case 16: move = 40; break; case 20: move = 41; break; case 24: move = 42; break; case 28: move = 43; break; case 32: move = 44; break; case 36: move = 49; break; case 39: move = 45; break; case 46: move = 46; break; case 53: move = 47; break; case 60: move = 48; break; default: break;} break;

			default: break;
		}


		return move;
	}
*/

	public CreaturesList()throws Exception
	{
		String home = System.getProperty("user.dir");
		String fs = File.separator;
		home = home.substring(0, home.length()-5);

		File ismonfile = new File(home+fs+"data"+fs+"ismons"+fs+"creatures.txt");

		reader=new FileReader(ismonfile);
		p=new Properties();  
	    p.load(reader);

		/*readerType=new FileReader(new File(home+fs+"data"+fs+"types.txt"));  
		propType=new Properties();  
		propType.load(readerType);*/
	}

	public String getIsmonName(int i)
	{
		ismon = p.getProperty("pokemon").split("; ")[i];
		return ismon.split(", ")[0];
	}

	public int[] getIsmonBase(int i)
	{
		base = new int[6];
		ismon = p.getProperty("pokemon").split("; ")[i];
	    String totBase = ismon.split(", ")[1];

	    for(int j = 0; j<6; j++)
	    {
	    	base[j] = Integer.parseInt(totBase.split(" ")[j]);
	    }

	    return base;
	}

	public int getIsmonAbility(int i)
	{	
		ability1 = 0; ability2 = 0;
		ismon = p.getProperty("pokemon").split("; ")[i];

		ability1 = Integer.parseInt(ismon.split(", ")[2]);
		ability2 = Integer.parseInt(ismon.split(", ")[3]); 

		if(ability2 == 0)
		{	
			return ability1;
		}
		else if(ability1 == 0)
		{
			return ability2;
		}
		else if(Math.floor(Math.random()*100)+1 <= 50)
		{
			return ability1;
		}
		else
		{
			return ability2;
		}
	}

	public String getType(int i)
	{
		ismon = p.getProperty("pokemon").split("; ")[i];
		return ismon.split(", ")[4];
	}

	public int getLeveluprate(int i)
	{
		ismon = p.getProperty("pokemon").split("; ")[i];
		return Integer.parseInt(ismon.split(", ")[5]);
	}

	public String getEvolve(int i)
	{
		ismon = p.getProperty("pokemon").split("; ")[i];
		return ismon.split(", ")[6];
	}

	/*public boolean[] getEvoStone(int i)
	{
		evostone = new boolean[]{false, false, false, false, false, false, false, false};
		ismon = p.getProperty("pokemon").split("; ")[i];
		String evoStone = ismon.split(", ")[8];

	    switch(evoStone)
	    {
	    	case "fire":
	    		evostone[0] = true;
	    		break;

	    	case "thunder":
	    		evostone[1] = true;
	    		break;

	    	case "water":
	    		evostone[2] = true;
	    		break;

	    	case "sun":
	    		evostone[3] = true;
	    		break;

	    	case "moon":
	    		evostone[4] = true;
	    		break;

	    	case "shiny":
	    		evostone[5] = true;
	    		break;

	    	case "dawn":
	    		evostone[6] = true;
	    		break;

	    	case "dusk":
	    		evostone[7] = true;
	    		break;

	    	default:
	    		break;
	    }

		return evostone;
	}*/

	public int[] getIsmonStartMoves(int i)
	{
		startmove = new int[4];
		ismon = p.getProperty("pokemon").split("; ")[i];

		String totStartMoves = ismon.split(", ")[7];

	    for(int j = 0; j<4; j++)
	    {
	    	startmove[j] = Integer.parseInt(totStartMoves.split(" ")[j]);
	    }

		return startmove;
	}

	public int getExpDrop(int i)
	{
		ismon = p.getProperty("pokemon").split("; ")[i];

		return Integer.parseInt(ismon.split(", ")[8]);
	}

	public String getOverworldAction(int i)
	{
		ismon = p.getProperty("pokemon").split("; ")[i];

		return ismon.split(", ")[9];
	}

}