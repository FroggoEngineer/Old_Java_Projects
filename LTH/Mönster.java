import se.lth.cs.ptdc.window.*;
import java.awt.*;

public class Mönster 
{
	public static void main(String[] args)
	{
		SimpleWindow w = new SimpleWindow(400,400,"Mönster");
		int r,g,b,a=0;
		Color color;
		for(int i = 0; i < 46; i++)
		{
			r = (int) (255*Math.sin(Math.toRadians(i*4)));
			g = (int) (255*Math.sin(Math.toRadians(i*4)));
			b = (int) (255*Math.sin(Math.toRadians(i*4)));
			System.out.println(i);
			color = new Color(r,g,b);
			w.setLineColor(color);
			w.lineTo(400,a);
			a += 1;
			w.moveTo(0, a);
			if(i == 45)
				i = 0;
			if(a == 400)
				break;
		}
	}
}
