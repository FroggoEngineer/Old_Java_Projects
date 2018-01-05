import se.lth.cs.ptdc.window.*;


public class Funktionskurva 
{
	public static void main(String[] args)
	{
		SimpleWindow w = new SimpleWindow(720,400,"Funktionskurva");
		
		int y;
		
		w.moveTo(0, 200);
		for(int i = 0; i <= 720; i++)
		{
			y = 200 + (int)(200*Math.sin(Math.toRadians(i)*-1));
			w.lineTo(i,y);
		}
		
	}
}
