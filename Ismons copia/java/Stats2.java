import java.io.*;

public class Stats2
{
	private Nature natureffect = new Nature();

    private int[] stats;
    private double[] nature;

	public Stats2()
	{
	}

	public int[] getStats(int[] base, int[] pa, int livello, int nature)
	{
		this.stats = new int[base.length];
		this.nature = natureffect.getNatureEffect(nature);

		for(int i = 0; i < stats.length; i++)
		{
			//La formula è ((2 * Valore Base della Statistica + (TP assegnati alla Statistica / 4)) * livello della Creatura / 100 + (se Stat è HP '10', altrimenti '5') * Natura della Creatura
			this.stats[i] = (int)(Math.max(Math.floor(((2 * base[i] + 31 + pa[i] / 4) * livello / 100 + ((i == 0) ? 10 : 5)) * this.nature[i]), 1));
		}

        return this.stats;
	}


}
	 