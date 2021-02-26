import java.io.*;
import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Game
{
	public static void main(String[] args)throws Exception
	{
		CreaturesList creature = new CreaturesList();
		Creature[] box = new Creature[200];
		Creature[] ismon = new Creature[6];  Creature[] ismonE = new Creature[6];
		Nature nature = new Nature();
		Characteristics cha = new Characteristics();
		int[] mosse;
		int[] stat = new int[6];
		int[] tp = new int[]{0, 0, 0, 0, 0, 0};
		int index = 1; int indexplus = 0;
		int level = 5;
		int sumtp = 0;
		int exp = 0;
		int scelta = 1;

		Scanner z = new Scanner(System.in);
		Scanner continua = new Scanner(System.in);
		boolean inputCheck = true;
		
		do
		{
			try
			{
				inputCheck = true;
				System.out.print("\033[H\033[2J");  
	       	    System.out.flush(); 

				System.out.println("\n\n Which Region's Starters you want to choose from?\n");
				System.out.print(" 1: Kanto  2: Johto  3: Hoenn  4: Sinnoh  5: Unova  6: Kalos  7: Alola\n\n" +
							     "                            ");
				scelta = z.nextInt();
			}
			catch(Exception iME)
			{	
				inputCheck = false;
				z.next();
			}
		}
		while(!inputCheck);

		switch(scelta)
		{
			case 1:
				indexplus = 0;
				break;

			case 2:
				indexplus = 9;
				break;

			case 3:
				indexplus = 18;
				break;

			case 4:
				indexplus = 27;
				break;

			case 5:
				indexplus = 36;
				break;

			case 6:
				indexplus = 45;
				break;

			case 7:
				indexplus = 54;
				break;

			default:
				break;
		}

		do
		{
			try
			{
				inputCheck = true;
				System.out.print("\033[H\033[2J");  
		        System.out.flush(); 

				System.out.print("\n   Choose Starter: \n\n  1: " + creature.getIsmonName(1+indexplus) + "      2: " + creature.getIsmonName(4+indexplus) + "      3: " + creature.getIsmonName(7+indexplus) + 
					             "\n\n                         ");
				scelta = z.nextInt();
			}
			catch(Exception iME)
			{
				inputCheck = false;
				z.next();
			}
		}
		while(!inputCheck);

		mosse = new int[4];
		switch (scelta)
		{
			case 1:
				index = 1 + indexplus;
				break;

			case 2:
				index = 4 + indexplus;
				break;

			case 3:
				index = 7 + indexplus;
				break;

			default:
				break;
		}

		ismon[0] = new Creature(index, (int)(Math.round(Math.random())), "Croccosecc", level, creature.getIsmonAbility(index), tp, nature.generateNature(), cha.generateCharacteristic(), creature.getIsmonStartMoves(index));
		//ismon[0].setNickname("Croccosecc"/*creature.getIsmonName(ismon[0].getIndex())*/);

		ismon[1] = new Creature(64, 1, null, level, creature.getIsmonAbility(64), tp, nature.generateNature(), cha.generateCharacteristic(), creature.getIsmonStartMoves(index));
		//ismon[1].setNickname("Principessa");


		System.out.println("\n You Choose " + ismon[0].getName() + "!\n");
		continua.nextLine();

		Mappa mappa = new Mappa(ismon);

		//ismon[0].setExp(ismon[0].getLevelUp());  Questo è come usi la caramella rara

	}
}