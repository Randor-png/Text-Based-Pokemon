import java.io.*;
import java.util.InputMismatchException;
import java.util.*;  
import java.io.File;

public class Characteristics
{
	private FileReader readerchara;
	private Properties propchara;
	private String home;
	private String fs;

	private String characteristics;

	public Characteristics()
	{
		home = System.getProperty("user.dir");
        home = home.substring(0, home.length()-5);
        fs = File.separator;

        try
        {
			readerchara = new FileReader(new File(home + fs + "data" + fs + "menuText" + fs + "characteristics.txt"));
		}
		catch(FileNotFoundException fnfe)
        {
        }

        propchara = new Properties();

        try
        {
            propchara.load(readerchara);
        }
        catch(IOException ioe)
        {
        }

		characteristics = propchara.getProperty("characteristics");
	}

	public String getCharacteristic(int chara)
	{
		return characteristics.split(" ,")[chara];
	}

	public int generateCharacteristic()
	{
		return (int)(Math.random() * (characteristics.split(" ,").length));
	}
}