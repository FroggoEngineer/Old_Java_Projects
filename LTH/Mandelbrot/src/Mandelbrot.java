import se.lth.cs.ptdc.fractal.*;

/** Mandelbrot innehåller main-metoden till Mandelbrotsprogrammet */
public class Mandelbrot 
{

	/** Skapar GUI, generator och repeterar programmet till man trycker quit */
	public static void main(String[] args) 
	{
		MandelbrotGUI gui = new MandelbrotGUI();
		Generator mb = new Generator();
		boolean zoomTrue = false;
		
		while(true)
		{
			switch(gui.getCommand())
			{
				case MandelbrotGUI.RENDER: mb.render(gui); zoomTrue = true; break;
				case MandelbrotGUI.QUIT: System.exit(0); break;
				case MandelbrotGUI.RESET: gui.resetPlane(); gui.clearPlane(); zoomTrue = false; break;
				case MandelbrotGUI.ZOOM: if(zoomTrue)mb.render(gui); break;
			}
		}
	}

}
