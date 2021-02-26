import java.io.*;
import java.util.*;  
import java.io.File;

public class MovesList
{
	private FileReader reader;
	private Properties p;

	private String mossa;

	public MovesList() 
	{
		try
		{
			String home = System.getProperty("user.dir");
			String fs = File.separator;
			
			home = home.substring(0, home.length()-5);
			File movesfile = new File(home+fs+"data"+fs+"ismons"+fs+"moves.txt");
			reader=new FileReader(movesfile);  
			p=new Properties();  
		    p.load(reader); 
		}
		catch(Exception ioe)
		{
		}
	}

	public String getName(int mossaid)
	{
		mossa = p.getProperty("move").split("; ")[mossaid];
		return mossa.split(" - ")[0];
	}

	public String getDescription(int mossaid)
	{
		mossa = p.getProperty("move").split("; ")[mossaid];
		return mossa.split(" - ")[1];
	}

	public boolean getPhysical(int mossaid)
	{
		mossa = p.getProperty("move").split("; ")[mossaid];
		if(mossa.split(" - ")[2].equals("physical"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public int getType(int mossaid)
	{
		mossa = p.getProperty("move").split("; ")[mossaid];
		return Integer.parseInt(mossa.split(" - ")[3]);
	}

	public String getDamage(int mossaid)
	{
		mossa = p.getProperty("move").split("; ")[mossaid];
		return mossa.split(" - ")[4];
	}

	public String getPrecision(int mossaid)
	{
		mossa = p.getProperty("move").split("; ")[mossaid];
		return mossa.split(" - ")[5];
	}

	public int getPP(int mossaid)
	{
		mossa = p.getProperty("move").split("; ")[mossaid];
		return Integer.parseInt(mossa.split(" - ")[6]);
	}

	public int getPriority(int mossaid)
	{
		mossa = p.getProperty("move").split("; ")[mossaid];
		return Integer.parseInt(mossa.split(" - ")[7]);
	}

	public String getStatMod(int mossaid)
	{
		mossa = p.getProperty("move").split("; ")[mossaid];
		return mossa.split(" - ")[8];
	}
	
	public String getSpecialEffect(int mossaid)
	{	
		mossa = p.getProperty("move").split("; ")[mossaid];
		return mossa.split(" - ")[9];
	}

	public String getAfflictedStatus(int mossaid)
	{
		mossa = p.getProperty("move").split("; ")[mossaid];
		return mossa.split(" - ")[10];
	}

}