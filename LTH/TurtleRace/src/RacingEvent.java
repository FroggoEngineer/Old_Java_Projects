
public class RacingEvent 
{
	Turtle[] turtle;
	RaceTrack track;
	private boolean racefin;
	
	public RacingEvent(RaceTrack track, Turtle[] turtle)
	{
		this.turtle = turtle;
		this.track = track;
	}
	
	void race()
	{
		for(int i = 0; i < turtle.length; i++)
		{
			if(turtle[i].getY() <= track.getFinish())
			{
				racefin = true;
			}
			turtle[i].forward((int)(Math.random()*3));
		}
		
	}
	
	boolean loop()
	{
		if(!racefin)
			return true;
		else
			return false;
	}
}
