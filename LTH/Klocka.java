import java.util.*;
import se.lth.cs.ptdc.window.*;


public class Klocka 
{


	public static void main(String[] args) 
	{
		SimpleWindow w = new SimpleWindow(300,300,"Klocka");
		Calendar cal = new GregorianCalendar();
		w.moveTo(w.getWidth()/2, w.getHeight()/2);
		int radius = w.getWidth()/2 - 20;
		double angleT, angleM, angleS;
		int tim, min, sek,x,y;
		Date newDate;
		
		while(true)
		{
			tim = cal.get(Calendar.HOUR);
			min = cal.get(Calendar.MINUTE);
			sek = cal.get(Calendar.SECOND);
			
			angleT = Math.toRadians(90-(tim*30));
			angleM = Math.toRadians(90-(min*6));
			angleS = Math.toRadians(90-(sek*6));
		
			x = (int)(150+Math.round(Math.cos(angleT)*radius));
			y = (int) (150+Math.round(Math.sin(angleT)*-radius));
			
			w.setLineColor(java.awt.Color.red);
			w.lineTo(x, y);
			w.moveTo(w.getWidth()/2, w.getHeight()/2);
			
			x = (int)(150+Math.round(Math.cos(angleM)*radius));
			y = (int) (150+Math.round(Math.sin(angleM)*-radius));
			
			w.setLineColor(java.awt.Color.blue);
			w.lineTo(x, y);
			w.moveTo(w.getWidth()/2, w.getHeight()/2);
			
			x = (int)(150+Math.round(Math.cos(angleS)*radius));
			y = (int) (150+Math.round(Math.sin(angleS)*-radius));
			
			w.setLineColor(java.awt.Color.green);
			w.lineTo(x, y);
			w.moveTo(w.getWidth()/2, w.getHeight()/2);
			
			SimpleWindow.delay(1000);
			w.clear();
			newDate = new Date();
			cal.setTime(newDate);
		}
		
		
	}

}
