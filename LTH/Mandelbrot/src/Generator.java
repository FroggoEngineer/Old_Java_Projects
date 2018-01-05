import se.lth.cs.ptdc.fractal.*;
import java.awt.*;
import java.util.Random;

/** Klassen Generator tar hand om komplexa talplanet, genererar mandelbrotsföljden 
 * och ritar upp denna */ 
public class Generator 
{
	private Color[] color;
	
	/** Skapar Generator och slumpar färg vektorn. */
	public Generator()
	{
		color = new Color[256];
		
		Random rand = new Random();
		for(int i = 0; i < 256; i++)
		{
			int r = rand.nextInt(256);
			int g = rand.nextInt(256);
			int b = rand.nextInt(256);
			color[i] = new Color(r,g,b);
		}
	}
	
	/** Skapar mesh matrisen */
	private Complex[][] mesh(double minRe, double maxRe,
							 double minIm, double maxIm,
							 int width, int height)
	{
		Complex[][] mesh = new Complex[height][width];
		
		for(int i = 0; i < height; i++)
		{
			for(int k = 0; k < width; k++)
			{
				mesh[i][k] = new Complex(minRe + (((maxRe-minRe)/width)*k), maxIm - (((maxIm-minIm)/height) * i));
			}
		}
		return mesh;
	}
	
	/** Beräknar mandelbrotföljden, kontrollerar upplösningen, skapar complex & picture matriserna.
	 *  Skickar sedan in picture matrisen till MandelbrotGUI.putData(). */
	public void render(MandelbrotGUI mb)
	{
		mb.disableInput();
		Complex[][] complex = mesh(mb.getMinimumReal(), mb.getMaximumReal(),
								   mb.getMinimumImag(), mb.getMaximumImag(),
								   mb.getWidth(), mb.getHeight());
		int res = 0;
		int iter = 0;
		
		if(mb.getExtraText().length() == 0)
			iter = 600;
		else
			iter = Integer.parseInt(mb.getExtraText());
		
		switch(mb.getResolution())
		{
		case MandelbrotGUI.RESOLUTION_VERY_HIGH: res = 1; break;
		case MandelbrotGUI.RESOLUTION_HIGH: res = 3; break;
		case MandelbrotGUI.RESOLUTION_MEDIUM: res = 5; break;
		case MandelbrotGUI.RESOLUTION_LOW: res = 7; break;
		case MandelbrotGUI.RESOLUTION_VERY_LOW: res = 9; break;
		}
		
		Color[][] picture = new Color[complex.length/res][complex[0].length/res];
		
		
		
		for(int i = res/2; i < complex.length-res; i += res)
		{
			for(int k = res/2; k < complex[0].length-res; k += res)
			{
				Complex temp = complex[i][k];
				Complex sum = new Complex(0,0);
				for(int a = 0; a < iter; a++)
				{
					
					if(a < 1)
						sum.add(complex[i][k]);
					else
					{
						sum.mul(sum);	
						sum.add(temp);
					}
					
					if(sum.getAbs2() > 4 && mb.getMode() == MandelbrotGUI.MODE_COLOR)
					{
						picture[(i-(res/2))/res][(k-(res/2))/res] = color[5*(int)sum.getAbs2()];
						break;
					}
					else if(sum.getAbs2() > 4 && mb.getMode() == MandelbrotGUI.MODE_BW)
					{
						picture[(i-(res/2))/res][(k-(res/2))/res] = Color.WHITE;
						break;
					}
					else if(a == iter-1)
					{
						picture[(i-(res/2))/res][(k-(res/2))/res] = Color.BLACK;
					}
				}
			}
		}
		mb.putData(picture, res, res);
		mb.enableInput();
	}
}