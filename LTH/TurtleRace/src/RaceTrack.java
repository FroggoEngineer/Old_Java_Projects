import se.lth.cs.ptdc.window.*;
import java.awt.*;

public class RaceTrack 
{
	private int yStart, yFinish;
	
	public RaceTrack(int yStart, int yFinish)
	{
		this.yStart = yStart;
		this.yFinish = yFinish;
	}
	
	void draw(SimpleWindow w)
	{
		w.setLineColor(Color.red);
		w.moveTo(100, yFinish);
		w.lineTo(700, yFinish);
		
		w.setLineColor(Color.blue);
		w.moveTo(100, yStart);
		w.lineTo(700, yStart);
		
		w.setLineColor(Color.green);
	}
	
	int getFinish()
	{
		return yFinish;
	}
	
	int getStart()
	{
		return yStart;
	}
}
