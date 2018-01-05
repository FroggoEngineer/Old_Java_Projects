import se.lth.cs.ptdc.window.SimpleWindow;

public class Blomma 
{
	public static void main(String[] args) 
	{
		SimpleWindow w = new SimpleWindow(400, 400, "Circle");
		int x0 = w.getWidth() / 2;
		int y0 = w.getHeight() / 2;
		int radius = (int) (Math.cos(12*Math.toRadians(0))*200);
		w.moveTo(x0 + radius, y0);
		
		for (int i = 0; i <= 360*10; ++i) 
		{
			double angle = Math.toRadians(i);
			radius = (int) (Math.cos(12*angle)*200);
			int x = x0 + (int) Math.round(radius * Math.cos(angle));
			int y = y0 + (int) Math.round(radius * Math.sin(angle) *-1);
			w.lineTo(x, y); 
			
			
			
			SimpleWindow.delay(10);
		}
	}
}
