import java.io.*;

public class Item
{
	private int itemID;
	private String itemName;
	private int itemType;
	private String itemDesc;
	private String itemEffect;
	private int amount = 0;

	private ItemsList il = new ItemsList();

	public Item(int itemID, int itemAmount)
	{
		this.itemID = itemID;
		this.itemName = il.getName(itemID);
		this.itemType = il.getType(itemID);
		this.itemDesc = il.getDescription(itemID);
		this.itemEffect = il.getEffect(itemID);
		this.amount = itemAmount;
	}

	public int getID()
	{
		return this.itemID;
	}

	public String getName()
	{
		return this.itemName;
	}

	public int getType()
	{
		return this.itemType;
	}

	public String getDesc()
	{
		return this.itemDesc;
	}

	public String getEffect()
	{
		return this.itemEffect;
	}

	public int getAmount()
	{
		return this.amount;
	}

	public void addAmount(int amount)
	{
		this.amount += amount;
	}
}