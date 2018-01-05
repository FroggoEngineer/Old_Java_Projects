import se.lth.cs.ptdc.window.SimpleWindow;
import java.awt.Color;

public class Färger 
{
	public static void main(String[] args)
	{
		SimpleWindow w = new SimpleWindow(256,256, "Färger");
		int r,g;
		
		
			for(g = 0; g < 256; g++)
			{
				for(r=0;r < 256; r++)
				{
					Color color = new Color(r,g,0);
					w.setLineColor(color);
					w.moveTo(g,r);
					w.lineTo(g,r);
				}
			}
		
	}
}
