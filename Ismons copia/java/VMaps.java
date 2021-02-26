import java.io.*;

public class VMaps
{
	private char[][] map;
	private String colori;
	private String eventi;
	private String runeventi;
	private String stepeventi;
	private char steptile;

	public VMaps(char[][] map, String colori, String eventi, String runeventi, String stepeventi, char steptile)
	{
		this.map = map;
		this.colori = colori;
		this.eventi = eventi;
		this.runeventi = runeventi;
		this.stepeventi = stepeventi;
		this.steptile = steptile;
	}

	public char[][] getMap()
	{
		return this.map;
	}

	public String getColori()
	{
		return this.colori;
	}

	public String getEventi()
	{
		return this.eventi;
	}

	public String getRunEventi()
	{
		return this.runeventi;
	}

	public String getStepEventi()
	{
		return this.stepeventi;
	}

	public char getStepTile()
	{
		return this.steptile;
	}
}