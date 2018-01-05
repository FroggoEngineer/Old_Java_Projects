import se.lth.cs.ptdc.window.*;
import java.util.Scanner;

public class BMI 
{
	public static void main(String[] args)
	{
		SimpleWindow w = new SimpleWindow(600, 800, "BMI");
		Scanner scan = new Scanner(System.in);
		double maxHeight = 2.00;
		double maxWeight = 120.0;
		double bmi;
		int x=0,y=800,pointX=0,pointY=0;
		System.out.print("Längd: ");
		double length = scan.nextDouble();
		System.out.println("Vikt: ");
		double weight = scan.nextDouble();
		
		pointX = (int)((length-1.4)*1000);
		pointY = (int)((weight-40)*10);
		
		w.moveTo(x,y);
		for(double minHeight = 1.4; minHeight <= maxHeight; minHeight += 0.001)
		{
			for(double minWeight = 40.0; minWeight <= maxWeight; minWeight+=0.1)
			{
				
				bmi = (minWeight)/(Math.pow(minHeight,2));
				if(bmi > 25)
					w.setLineColor(java.awt.Color.red);
				else if(bmi <= 25 && bmi > 18.5)
					w.setLineColor(java.awt.Color.green);
				else if(bmi <= 18.5)
					w.setLineColor(java.awt.Color.yellow);
				
				
				
				
				w.lineTo(x, y);
				y--;
			}
			x++;
			y = 800;
			w.moveTo(x, 800);
		}
		w.setLineColor(java.awt.Color.black);
		w.setLineWidth(2);
		w.moveTo(pointX, pointY);
		w.lineTo(pointX-4, pointY);
		w.lineTo(pointX+8, pointY);
		w.moveTo(pointX+2, pointY-6);
		w.lineTo(pointX+2, pointY+8);
	}
}
