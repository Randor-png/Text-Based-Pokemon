import java.io.*;
import java.util.Scanner;
import java.io.*;
import java.util.InputMismatchException;
import java.util.*;  
import java.io.File;

public class P_Sheet
{
	private Scanner sc = new Scanner(System.in);
	private Characteristics characteristics = new Characteristics();
	private Nature nature = new Nature();

	private FileReader readerSheet;
	private Properties propSheet;
	private String home;
	private String fs;

	private FileReader readerType;
	private Properties propType;

	private String name;
	private String nickname;

	private String[] type;

	private int sex;

	private int nat;
	private int chara;

	private int ability;

	private int level;
	private int exp;
	private int levelup;

	private int[] stat;
	private int currentHP;
	private int[] ev;

	private int status;

	private IsmonMosse[] moves;

	public String hpBar(int currentHP, int maxHP)
	{	
		int segment = 0; 
		String hpbar = "";

		int i = 0;

		segment = ((currentHP * 100) / maxHP) / 5; 

		if (currentHP > 0 && segment < 1)
		{ 
			segment = 1; 
		}

		hpbar = new String(new char[segment]).replace("\0", "|") + new String(new char[20-segment]).replace("\0", " ");

		return hpbar;
	}

	public void infoIsmon(String info, String sexArr, String stats)
	{
		Abilities abiList = null;

		try
		{
			abiList = new Abilities();
		}
		catch(Exception e)
		{
		}

		String hpbar = "";  

		//Name of the Pokémon Species
		System.out.println(" --------------------------\n " + info.split(",")[0].split("\\[a]")[0] + name + info.split(",")[0].split("\\[a]")[1]);

		//Nickname
		if(nickname != null)
		{
			System.out.println(" " + info.split(",")[1].split("\\[a]")[0] + nickname + info.split(",")[1].split("\\[a]")[1]);
		}

		String typetext = "";
		for(int i=0; i<type.length; i++)
		{
			typetext+=type[i] + (i == type.length-1 ? "" : " - ");
		}
		//Types
		System.out.println(" " + info.split(",")[2].split("\\[a]")[0] + typetext + info.split(",")[2].split("\\[a]")[1]);

		//Sex
		System.out.println(" " + info.split(",")[3].split("\\[a]")[0] + sexArr.split(", ")[sex] + info.split(",")[3].split("\\[a]")[1]);

		//Nature
		System.out.println(" " + info.split(",")[4].split("\\[a]")[0] + nature.getNatureName(nat) + info.split(",")[4].split("\\[a]")[1]);

		//Characteristic
		System.out.println(" \"" + characteristics.getCharacteristic(chara) + "\"");

		//Ability
		System.out.println(" " + info.split(",")[5].split("\\[a]")[0] + abiList.getName(ability) + info.split(",")[5].split("\\[a]")[1] + " \n");

		//Level
		System.out.println(" " + info.split(",")[6].split("\\[a]")[0] + level + info.split(",")[6].split("\\[a]")[1]);

		//Exp
		System.out.println(" " + info.split(",")[7].split("\\[a]")[0] + exp + info.split(",")[7].split("\\[a]")[1]);

		//Exp needed to level up
		System.out.println(" " + info.split(",")[8].split("\\[a]")[0] + levelup + info.split(",")[8].split("\\[a]")[1] + "\n --------------------------\n");

		hpbar = hpBar(currentHP, stat[0]);

		System.out.println(" " + stats.split(", ")[0] + ":  <" + hpbar + ">  " + ((status == 0) ? "" : status));
		System.out.println("               " + currentHP + "/" + stat[0]);
	}

	public void statsIsmon(int i, String stats)
	{	
		double[] nateffect = nature.getNatureEffect(nat);
		String[] natsymb = new String[nateffect.length];
		for(i = 0; i < nateffect.length; i++)
		{
			if(nateffect[i] > 1.0)
			{
				natsymb[i] = "(+)";
			}
			else if(nateffect[i] < 1.0)
			{
				natsymb[i] = "(-)";
			}
			else
			{
				natsymb[i] = "";
			}
		}

		System.out.print("\n      Stats   |    TP\n --------------------------\n");

		for(i = 0; i < stats.split(", ").length; i++)
		{
			String spazio = "";
			String statspazio = ":  "+new String(new char[3-stats.split(", ")[i].length()]).replace("\0", " ");

			String testo = " " + stats.split(", ")[i] + statspazio + stat[i] + natsymb[i];

			spazio = new String(new char[14-testo.length()]).replace("\0", " ");
			spazio+="|    ";

			System.out.println(testo + spazio + ev[i]);
		}
	}

	public void movesIsmon(int i, int j, String movesInfo)
	{	
		String fisico;

		System.out.println("\n\n Moves:\n");

		for(i=0; i<4; i++)
		{
			if(moves[i].getID() != 0)
			{
				j++;
			}
		}

		i = 0;

		do
		{
			if(moves[i].getPhysical())
			{ 
				fisico = "Physical"; 
			}
			else
			{ 
				fisico = "Special"; 
			} 

			if((moves[i].getDamage().charAt(0) == 'f' && Integer.parseInt(moves[i].getDamage().substring(1)) == 0) || (Integer.parseInt(moves[i].getDamage()) == 0))
			{
				fisico = "Status"; 
			}

							   //Move name
			System.out.println(" " + movesInfo.split(",")[0].split("\\[a]")[0] + moves[i].getName() + movesInfo.split(",")[0].split("\\[a]")[1] +
							   //Move description
							   "\n --------------------------\n " + movesInfo.split(",")[1].split("\\[a]")[0] + moves[i].getDescription() + movesInfo.split(",")[1].split("\\[a]")[1] + 
							   //Move type
							   "\n " + movesInfo.split(",")[2].split("\\[a]")[0] + propType.getProperty("type").split(";")[moves[i].getType()].split(" - ")[0] + movesInfo.split(",")[2].split("\\[a]")[1] + movesInfo.split(",")[3].split("\\[a]")[0] + fisico + movesInfo.split(",")[3].split("\\[a]")[1] +
							   //Move damage
							   "\n " + movesInfo.split(",")[4].split("\\[a]")[0] + ((moves[i].getDamage().charAt(0) == 'f') ? ((Integer.parseInt(moves[i].getDamage().substring(1)) > 0) ? moves[i].getDamage().substring(1) : "-") : ((Integer.parseInt(moves[i].getDamage()) > 0) ? moves[i].getDamage() : "-")) + movesInfo.split(",")[4].split("\\[a]")[1] +
							   //Move precision
							   "\n " + movesInfo.split(",")[5].split("\\[a]")[0] + ((moves[i].getPrecision().charAt(0) == 'a') ? "-" : moves[i].getPrecision()) + movesInfo.split(",")[5].split("\\[a]")[1] + 
							   //Move max and current PP
							   "\n " + movesInfo.split(",")[6].split("\\[a]")[0] + moves[i].getCurrentPP() + "/" + moves[i].getMaxPP() + movesInfo.split(",")[6].split("\\[a]")[1] +
							   "\n");
			i++;
		}
		while(i < j);
	}

	public P_Sheet(Creature ismon)
	{
		ImageLoad imgLoad = null;

		home = System.getProperty("user.dir");
        home = home.substring(0, home.length()-5);
        fs = File.separator;

        try
        {
			readerSheet = new FileReader(new File(home + fs + "data" + fs + "menuText" + fs + "pokemonInfo.txt"));
		}
		catch(FileNotFoundException fnfe)
        {
        }

        propSheet = new Properties();

        try
        {
            propSheet.load(readerSheet);
        }
        catch(IOException ioe)
        {
        }

        String info = propSheet.getProperty("sheetText");
        String stats = propSheet.getProperty("stats");
        String sexArr = propSheet.getProperty("sex");
        String movesInfo = propSheet.getProperty("moves");

        try
        {
	        readerType=new FileReader(new File(home+fs+"data"+fs+"types.txt"));  
			propType=new Properties();
			propType.load(readerType);
		}
		catch(Exception e)
		{
		}

		int i = 0;
		int j = 0;

		name = ismon.getName();
		nickname = ismon.nickname;

		type = new String[ismon.getType().length];
		for(i=0; i<type.length; i++)
		{
			type[i] = propType.getProperty("type").split(";")[ismon.getType()[i]].split(" - ")[0];
		}

		sex = ismon.getGender();

		nat = ismon.getNature();
		chara = ismon.getCharacteristic();

		ability = ismon.getAbility();

		level = ismon.getLevel();
		exp = ismon.getExp();
		levelup = ismon.getLevelUp()-exp;

		stat = ismon.getStats();
		currentHP = ismon.getCurrentHP();
		ev = ismon.getTP();

		status = ismon.getStatus();

		moves = ismon.getMosse();

		System.out.print("\033[H\033[2J");  
        System.out.flush(); 

        //Pokémon Image

        try
        {
        	imgLoad = new ImageLoad("ismonImg", name);
        }
        catch(Exception e)
        {
        	System.out.println(" " + e);
        }

      	// Pokémon General Info

        infoIsmon(info, sexArr, stats);

		//Pokémon Stats

        statsIsmon(i, stats);

		//Pokémon Moves

        movesIsmon(i, j, movesInfo);

		System.out.print("\n\nPress enter to continue. ");
		sc.nextLine();
		
		if(imgLoad != null)
		{
			imgLoad.close();
		}
	}

}