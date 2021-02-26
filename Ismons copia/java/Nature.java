import java.io.*;
import java.util.InputMismatchException;
import java.util.*;  
import java.io.File;

public class Nature
{
	private FileReader readernature;
	private Properties propnature;
	private String home;
	private String fs;

	String natures;
	double bonus;

	public Nature()
	{
		home = System.getProperty("user.dir");
        home = home.substring(0, home.length()-5);
        fs = File.separator;

        try
        {
			readernature = new FileReader(new File(home + fs + "data" + fs + "ismons" + fs + "natures.txt"));
		}
		catch(FileNotFoundException fnfe)
        {
        }

        propnature = new Properties();

        try
        {
            propnature.load(readernature);
        }
        catch(IOException ioe)
        {
        }

		natures = propnature.getProperty("natures");
		bonus = Double.parseDouble(propnature.getProperty("natEff"));
	}

	public int generateNature()
	{	
		int natureID = 0;
		do
		{
			natureID = (int)(Math.random() * (natures.split(";").length));
		}
		while(natures.split(";")[natureID].split(", ").length == 3);
		return natureID;
	}

	public String getNatureName(int nID)
	{
		return natures.split(";")[nID].split(", ")[0];
	}

	public double[] getNatureEffect(int nID)
	{
		double[] statsNature = new double[]{1, 1, 1, 1, 1, 1};
		String effect = natures.split(";")[nID];

		for(int i = 0; i < effect.split(", ")[1].split(" ").length; i++)
		{
			String eff = effect.split(", ")[1];
			switch(eff.split(" ")[i].charAt(0))
			{
				case '+':
					statsNature[Integer.parseInt(eff.split(" ")[i].substring(1))] *= bonus;
					break;

				case '-':
					statsNature[Integer.parseInt(eff.split(" ")[i].substring(1))] *= (1/bonus);
					break;
			}
		}

		return statsNature;
	}
}