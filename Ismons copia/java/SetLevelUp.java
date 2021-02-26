	public class SetLevelUp
	{
		private double levelup = 1;
		private double level = 1;

		public SetLevelUp(int leveluprate, int lv)
		{
			level = (double)lv;

			switch (leveluprate)
			{
				case 1:

					if(level <= 50)
					{
						levelup = (Math.pow(level, 3.0) * (100-level)) / 50;
					}
					if(level >= 50 && level <= 68)
					{
						levelup = (Math.pow(level, 3.0) * (150-level)) / 100;
					}
					if(level >= 68 && level <= 98)
					{
						levelup = (Math.pow(level, 3.0) * ((1911-10*level)/3)) / 500;
					}
					if(level >= 98 && level <= 100)
					{
						levelup = (Math.pow(level, 3.0) * (160-level)) / 100;
					}
					break;

				case 2:

					levelup = (4 * Math.pow(level, 3.0)) / 5; 
					break;

				case 3:
			
					levelup = Math.pow(level, 3.0); 
					break;

				case 4:

					levelup = 1.2 * Math.pow(level, 3.0) - 15*Math.pow(level,2.0) + 100*level - 140; 
					break;

				case 5:

					levelup = (5 * Math.pow(level, 3.0)) / 4; 
					break;

				case 6:

					if(level <= 15)
					{
	    				levelup = Math.pow(level, 3) * (((level+1)/3 + 24) / 50);
					}
					if(level >= 15 && level <= 36)
					{
						levelup = Math.pow(level, 3) * ((level+14) / 50);
					}
					if(level >= 36 && level <= 100)
					{
						levelup = Math.pow(level, 3) * ((level/2 + 32) / 50);
					}
					break;

				default: 
					break;
			}
		}

		public double giveLevelUp()
		{
			return this.levelup;
		}

	}