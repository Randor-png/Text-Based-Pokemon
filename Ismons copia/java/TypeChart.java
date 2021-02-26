import java.io.*;
import java.util.*;  
import java.io.File;

public class TypeChart
{	
	private FileReader readerType;
	private Properties propType;
	private String home;
	private String fs;

	private String types;
	private String effectiveness;
	private String effectiveMod;

	private double nE;
	private double sE;

	private double effect = 1.0;

	//Vengono caricati dal file "types.txt" contro quali tipi un tipo é non efficace, poco efficace e superefficace 
	public TypeChart() 
	{	
		try
		{
			home = System.getProperty("user.dir");
			fs = File.separator;
			home = home.substring(0, home.length()-5);

			readerType=new FileReader(new File(home+fs+"data"+fs+"types.txt"));  
			propType=new Properties();
		    propType.load(readerType);
		}
		catch(Exception ioe)
		{
		}

		effectiveness = propType.getProperty("type");
		effectiveMod = propType.getProperty("typeEffectiveMod").split(";")[1];

		nE = Double.parseDouble(effectiveMod.split(" , ")[0]);
		sE = Double.parseDouble(effectiveMod.split(" , ")[1]);
	}

    //Qui viene calcolato il moltiplicatore al danno dato dai tipi della mossa attaccante e del bersaglio
	public double getEffect(int mType, int[] tType)
	{
		int index1 = 0; int index2 = 0; int index3 = 0;

		String noteffective = null;
		String notveryeffective = null;
		String supereffective = null;

		noteffective = effectiveness.split(";")[mType].split(" - ")[1];
		notveryeffective = effectiveness.split(";")[mType].split(" - ")[2];
		supereffective = effectiveness.split(";")[mType].split(" - ")[3];

		int i = 0; //Si userà come indice per i tipi del bersaglio della mossa
		int j = 0; //Si userà come indice per i tipi deboli e resistenti alla mossa

		effect = 1.0;	//Di base il moltiplicatore equivale a 1 (Quindi Danno *= 1)

		for(i = 0; i < tType.length; i++)
		{
			if(!noteffective.equals("none"))
			{
				for(j = 0; j < noteffective.split(", ").length; j++)
				{
					int t = Integer.parseInt(noteffective.split(", ")[j]);
					if(t == tType[i])
					{
						return nE;
					}
				}
			}
			if(!notveryeffective.equals("none"))
			{
				for(j = 0; j < notveryeffective.split(", ").length; j++)
				{
					int t = Integer.parseInt(notveryeffective.split(", ")[j]);
					if(t == tType[i])
					{
						effect *= (1/sE);
						break;
					}
				}
			}
			if(!supereffective.equals("none"))
			{
				for(j = 0; j < supereffective.split(", ").length; j++)
				{
					int t = Integer.parseInt(supereffective.split(", ")[j]);
					if(t == tType[i])
					{
						effect *= sE;
						break;
					}
				}
			}
		}

		return effect; //Infine, viene passato il moltiplicatore da aggiungere alla formula del danno
	}

	public double getNE()
	{
		return this.nE;
	}
	public double getSE()
	{
		return this.sE;
	}

}