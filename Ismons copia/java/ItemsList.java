import java.io.*;
import java.util.InputMismatchException;
import java.util.*;  
import java.io.File;

public class ItemsList
{
	String home;
	String fs;

	FileReader readeritems;
	Properties propitems;

	String items;

	public ItemsList()
	{
		home = System.getProperty("user.dir");
		home = home.substring(0, home.length()-5);
		fs = File.separator;

		try
		{
			readeritems = new FileReader(new File(home + fs + "data" + fs + "items.txt"));
		}
		catch(FileNotFoundException fnfe)
		{
		}

		propitems = new Properties();

		try
		{
			propitems.load(readeritems);
		}
		catch(IOException ioe)
		{
		}

		items = propitems.getProperty("items");
	}

	public String getName(int item)
	{
		return (items.split(" ;")[item]).split(" - ")[0];
	}

	public int getType(int item)
	{
		return Integer.parseInt((items.split(" ;")[item]).split(" - ")[1]);
	}

	public String getDescription(int item)
	{
		return (items.split(" ;")[item]).split(" - ")[2];
	}

	public String getEffect(int item)
	{
		return (items.split(" ;")[item]).split(" - ")[3];
	}

	public int getLength()
	{
		return (items.split(" ;")).length;
	}
}