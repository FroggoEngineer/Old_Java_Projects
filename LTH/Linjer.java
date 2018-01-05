import se.lth.cs.ptdc.window.SimpleWindow;



public class Linjer 
{
	public static void main(String[] args)
	{
		SimpleWindow w = new SimpleWindow(600,600,"Linjer");
		
		while(true)
		{
			w.waitForMouseClick();
			w.lineTo(w.getMouseX(), w.getMouseY());
		}
		
	}
}
