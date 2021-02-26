import java.util.*;  
import java.io.*;  
import java.io.File;

public class Abilities
{
	private FileReader reader;
	private Properties p;

	private String ability;

	public Abilities()throws Exception
	{
		String home = System.getProperty("user.dir");
		String fs = File.separator;

		home = home.substring(0, home.length()-5);

		File abilitiesfile = new File(home+fs+"data"+fs+"ismons"+fs+"abilities.txt");
		
		reader=new FileReader(abilitiesfile);  
		p=new Properties();  
	    p.load(reader); 
	}

	public String getName(int i)
	{
		ability = p.getProperty("ability").split("; ")[i];
		return ability.split(" - ")[0];
	}

	public String getDescription(int i)
	{
		ability = p.getProperty("ability").split("; ")[i];
		return ability.split(" - ")[1];
	}

	public String getEffect(int i)
	{
		ability = p.getProperty("ability").split("; ")[i];
		return ability.split(" - ")[2];	
	}

}