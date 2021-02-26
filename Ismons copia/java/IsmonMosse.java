import java.io.*;

public class IsmonMosse
{
	private MovesList mosseInfo = new MovesList();

	private int mossaID;
	private String name;
	private String description;
	private boolean physical;
	private int type;
	private String damage;
	private String precision;
	private int maxPP;
	private int currentPP;
	private int priority;
	private String statMod;
	private String specialEffect;
	private String afflictedStatus;

	public IsmonMosse(int mossaID) 
	{
		this.mossaID = mossaID;

		if(mossaID == 0)
		{
			this.name = null;
			this.description = null;
			this.physical = false;
			this.type = 0;
			this.damage = "0";
			this.precision = "100";
			this.maxPP = 0;
			this.currentPP = 0;
			this.priority = 0;
			this.statMod = null;
			this.specialEffect = null;
			this.afflictedStatus = null;
		}
		else
		{
			this.name = mosseInfo.getName(mossaID);
			this.description = mosseInfo.getDescription(mossaID);
			this.physical = mosseInfo.getPhysical(mossaID);
			this.type = mosseInfo.getType(mossaID);
			this.damage = mosseInfo.getDamage(mossaID);
			this.precision = mosseInfo.getPrecision(mossaID);
			this.maxPP = mosseInfo.getPP(mossaID);
			this.currentPP = maxPP;
			this.priority = mosseInfo.getPriority(mossaID);
			this.statMod = mosseInfo.getStatMod(mossaID);
			this.specialEffect = mosseInfo.getSpecialEffect(mossaID);
			this.afflictedStatus = mosseInfo.getAfflictedStatus(mossaID);
		}
	}

	public int getID()
	{
		return mossaID;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public boolean getPhysical()
	{
		return physical;
	}

	public int getType()
	{
		return type;
	}

	public String getDamage()
	{
		return damage;
	}

	public String getPrecision()
	{
		return precision;
	}

	public int getMaxPP()
	{
		return maxPP;
	}

	public int getCurrentPP()
	{
		return currentPP;
	}

	public void setCurrentPP(int mod)
	{
		currentPP += mod;
	}

	public int getPriority()
	{
		return priority;
	}

	public String getStatMod()
	{
		return statMod;
	}
	
	public String getSpecialEffect()
	{	
		return specialEffect;
	}

	public String getAfflictedStatus()
	{
		return afflictedStatus;
	}

}