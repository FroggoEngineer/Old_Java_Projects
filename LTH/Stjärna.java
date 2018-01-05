import se.lth.cs.ptdc.window.*;
import java.awt.*;
import java.util.*;

public class Stjärna 
{
	
	public static void main(String[] args)
	{
		SimpleWindow w = new SimpleWindow(400,400,"Stjärna");
		int x,y,r,g,b,length;
		Color col;
		Random rand = new Random();
		for(int i = 0; i < 360; i++)
		{
			w.moveTo(200, 200);
			r = rand.nextInt(256);
			g = rand.nextInt(256);
			b = rand.nextInt(256);
			col = new Color(r,g,b);
			length = rand.nextInt(190);
			x = (int)(200+Math.round(Math.cos(Math.toRadians(i))*length));
			y = (int) (200+Math.round(Math.sin(Math.toRadians(i))*-length));
			w.setLineColor(col);
			w.lineTo(x,y);
		}
	}
	
}
