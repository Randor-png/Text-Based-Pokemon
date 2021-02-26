import java.io.*;
import java.util.Scanner;

public class Bag
{
	private Scanner continua = new Scanner(System.in);
	private ItemsList il = new ItemsList();
	private Item[] items;

	public Bag()
	{
		items = new Item[il.getLength()];
	}

	//Shows the contents of the Bag
	public void showBag()
	{
		System.out.print("\033[H\033[2J");
		System.out.flush();

		for(int i = 0; i < items.length; i++)
		{
			if(items[i] != null)
			{
				System.out.println(items[i].getName() + " x" + items[i].getAmount());
			}
		}

		continua.nextLine();
	}

	//Adds an Item to the Bag. If the player already has that item, it just increases its amount.
	public void addItem(int item, int amount)
	{	
		for(int i = 0; i < items.length; i++)
		{
			if(items[i] == null)
			{
				items[i] = new Item(item, amount);
				break;
			}
			else
			{
				if(items[i].getID() == item)
				{
					items[i].addAmount(amount);

					if(items[i].getAmount() == 0)
					{
						items[i] = null;
					}
					break;
				}
			}
		}
	}

	//Checks if the Player has an item in the bag, and returns the amount they have of that item
	public int checkItem(int item)
	{
		for(int i = 0; i < items.length; i++)
		{
			if(items[i] != null && items[i].getID() == item)
			{
				return items[i].getAmount();
			}
		}

		return 0;
	}
}