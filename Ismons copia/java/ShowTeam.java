import java.io.*;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.*;  
import java.io.File;

public class ShowTeam
{		
	private char choose;
	private Scanner continua = new Scanner(System.in);
	private P_Sheet ismonInfo;

	private Scanner select = new Scanner(System.in);

	private Creature[] team;
	private int nNull = 0;

	private FileReader readermenu;
	private Properties propmenu;
	private String home;
	private String fs;

	private String horizontalBar = "+-----------------------+-----------------------+"; private int i = 0;
	private String tableDeliminator = "|                       |                       |";
	private String space = "";


	public String getHPBar(int currentHP, int maxHP)  				
	{													//Crea le barre degli HP. Ogni barra è formata da 10 tacche, e calcola quante 
		int i = 0; int j = 0; String hpbar = "";        //tacche "riempire" dal rapporto percentual di HP totali e HP massimi del pokémon

		hpbar = "";

		j = ((currentHP * 100) / maxHP) / 10; if (currentHP > 0 && j < 1){ j = 1; }

		for(i = 0; i < 10; i++)
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
		
		return hpbar;
	}
	
	public String getSpaceLength(String word)
	{
	    int spaceLength = 20-word.length();  //Dà lo spazio da creare dal nome del Pokémon alla fine della sua casella
		String space = new String(new char[spaceLength]).replace("\0", " ");
		
		return space;
	}
	
	public String getLevelLength(int level)		
	{		
		int spaceLength = 4-String.valueOf(level).length(); //Dà lo spazio da creare dal livello del Pokémon alla Barra HP
		String space = new String(new char[spaceLength]).replace("\0", " ");

		return space;
	}
	
	public String getHPLength(int currentHP, int maxHP)
	{													//Dà lo spazio da creare dagli HP del Pokémon alla fine della sua casella
		int spaceLength = 9-(String.valueOf(currentHP).length() + String.valueOf(maxHP).length());	
		String space = new String(new char[spaceLength]).replace("\0", " ");
		
		return space;
	}

	public void drawTeam()
	{
		String space; String hpbar;

		System.out.print("\033[H\033[2J");  
	    System.out.flush(); 
		i = 0;
		int j = 0;

		nNull = 0;
			
		do
		{
			System.out.println(horizontalBar);
				
			System.out.println(tableDeliminator);
				
			for(j = 0; j<2; j++)
			{
				if(team[i] != null)
				{	
					space = getSpaceLength(team[i].getNickname());
					
					System.out.print("|   " + team[i].getNickname() + space); 
				}
				else
				{
					nNull+=1;
					String s = "|"+new String(new char[this.tableDeliminator.split("\\|")[1].length()]).replace("\0", " ");
					System.out.print(s);
				}
				i+=1;
			}
				
			System.out.println("|"); 
			i-=2;
				
			for(j = 0; j<2; j++)
			{
				if(team[i] != null)
				{
					space = getLevelLength(team[i].getLevel());
					hpbar = getHPBar(team[i].getCurrentHP(), team[i].getStats()[0]);
					
					System.out.print("|  Lv. " + team[i].getLevel() + space + "<" + hpbar + "> ");
				}
				else
				{
					String s = "|"+new String(new char[this.tableDeliminator.split("\\|")[1].length()]).replace("\0", " ");
					System.out.print(s);
				}
				i+=1;
			}
				
			System.out.println("|"); i-=2;
				
			for(j = 0; j<2; j++)
			{
				if(team[i] != null)
				{
					space = getHPLength(team[i].getCurrentHP(), team[i].getStats()[0]);
					
					System.out.print("|             " + team[i].getCurrentHP() + "/" + team[i].getStats()[0] + space);
				}
				else
				{
					String s = "|"+new String(new char[this.tableDeliminator.split("\\|")[1].length()]).replace("\0", " ");
					System.out.print(s);
				}
				i+=1;
			}
				
			System.out.println("|");	
			System.out.println(tableDeliminator);
		}
		while(i < team.length);
		System.out.println(horizontalBar);

	}

	public ShowTeam(Creature[] team, boolean battle)
	{
		Scanner scegli = new Scanner(System.in);
		System.out.print("\033[H\033[2J");  
        System.out.flush(); 

		String space; String hpbar;

		this.team = team;
		
		drawTeam();

		System.out.print("\n                      > ");
		choose = continua.next().charAt(0);
	}

	public void switchIsmonOrder(int id)
	{
		drawTeam();
		System.out.println("");
		System.out.println("\n             Switch " + team[id].getNickname() + " with whom?");
		System.out.println("");
		System.out.print("                      > ");
		int switchWith = (int)(select.next().charAt(0) - '0') - 1;
		try
		{
			if(team[switchWith] != null && switchWith != id)
			{
				Creature y = new Creature(team[id]);
				team[id] = new Creature(team[switchWith]);
				team[switchWith] = new Creature(y);
			}
		}
		catch(ArrayIndexOutOfBoundsException aioobe)
		{
		}
	}

	public ShowTeam(Creature[] team)
	{
		char ismon;

		String space; String hpbar;

		this.team = team;

		do
		{	
			drawTeam();

			System.out.print("\n                      > ");
			ismon = select.next().charAt(0);

			if(Character.isDigit(ismon) && (int)(ismon - '0') > 0 && (int)(ismon - '0') <= 6 - nNull)
			{	
				System.out.print("\n                ");
				System.out.println("1-Info     2-Swap");
				System.out.println("");
				System.out.print("                      > ");
				int opzione = (int)(select.next().charAt(0) - '0');
				switch(opzione)
				{
					case 1:
						ismonInfo = new P_Sheet(team[(int)(ismon - '0') - 1]);
						break;

					case 2:
						switchIsmonOrder((int)(ismon - '0')-1);
						break;

					default:
						break;
				}
			}
		}
		while(Character.isDigit(ismon) && (int)(ismon - '0') > 0 && (int)(ismon - '0') <= 6 - nNull);
	}

	public int getScelta()
	{
		return (int)(this.choose - '0');
	}
}