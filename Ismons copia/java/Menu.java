import java.io.*;
import java.util.Scanner;

public class Menu
{
	private Scanner menuchoose = new Scanner(System.in);
	private String bagmenu = "+-------------------+-------------------+\n" +
	                 		 "|                   |                   |\n" +
	                 		 "|      Pokémon      |      Pokédex      |\n" +
	                 		 "|                   |                   |\n" +
	                 		 "|-------------------+-------------------|\n" +
	                 		 "|                   |                   |\n" +
	                 		 "|        Bag        |       Save        |\n" +
	                 		 "|                   |                   |\n" +
	                 		 "|-------------------+-------------------|\n" +
	                 		 "|                   |                   |\n" +
	                 		 "|     Pokégear      |     Settings      |\n" +
	                 		 "|                   |                   |\n" +
	                 		 "+-------------------+-------------------+\n\n\n";

	public Menu(Creature[] ismon, Bag bag)
	{
		System.out.print("\033[H\033[2J");
		System.out.flush();

		System.out.print(bagmenu + "                  > ");
		switch(menuchoose.next().charAt(0))
		{
			case '1':
				ShowTeam st = new ShowTeam(ismon);
				break;

			case '2':
				//Here goes the Pokédex
				break;

			case '3':
				bag.showBag();
				break;

			case '4':
				break;

			case '5':
				break;

			case '6':
				break;
		}
	}
}