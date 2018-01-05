import se.lth.cs.ptdc.window.SimpleWindow;

public class Turtle 
{
	private double  angleR;
	private int Xcur, Ycur, angleG;
	public boolean paint;
	protected SimpleWindow window;
	
	
	
	public Turtle(SimpleWindow w, int x, int y) 
	{
		w.moveTo(x,y);
		Xcur = x;
		Ycur = y;
		angleG = 90;
		angleR = Math.toRadians(angleG);
		window = w;
	}
	
	
	public void penDown() 
	{
		this.paint = true;
	}
	
	
	public void penUp() 
	{
		this.paint = false;
	}
	
	
	public void forward(int n) 
	{
		int x = (int) Math.round(Math.cos(angleR)*n);
		int y = (int)Math.round(Math.sin(angleR)*n);
		window.moveTo(Xcur, Ycur);
		
		if(paint)
		{
			window.lineTo(Xcur+x,Ycur-y);
			Xcur = window.getX();
			Ycur = window.getY();
		}
		else if(!paint)
		{
			window.moveTo(Xcur+x, Ycur-y);
			Xcur = window.getX();
			Ycur = window.getY();
		}
			
		
	}
	
	
	public void left(int beta) 
	{
		angleG = angleG + beta;
		angleR = Math.toRadians(angleG);
	}
	

	public void jumpTo(int newX, int newY) 
	{
		Xcur = newX;
		Ycur = newY;
	}
	

	public void turnNorth() 
	{
		angleG = 90;
	}
	
	
	public int getX() 
	{
		return Xcur;
	}
	
	
	public int getY() 
	{
		return Ycur;
	}
	
	
	public int getDirection() 
	{
		return angleG;
	}
}
