import java.util.*;  
import java.io.*;  
import java.io.File;

public class TeamList
{
	String home;
	String fs;

	File teamsfile;
	FileReader readerteams;
	Properties propteams;

	String teams;

	public TeamList()
	{
		home = System.getProperty("user.dir");
		home = home.substring(0, home.length()-5);
		fs = File.separator;

		teamsfile = new File(home + fs + "data" + fs + "teams.txt");

		try
		{
			readerteams = new FileReader(teamsfile);
		}
		catch(FileNotFoundException fnfe)
		{
		}

		propteams = new Properties();

		try
		{
			propteams.load(readerteams);
		}
		catch(IOException ioe)
		{
		}

		teams = propteams.getProperty("teams");
	}

	public String getTeam(int i)
	{
		return teams.split(" I")[i];
	}
}