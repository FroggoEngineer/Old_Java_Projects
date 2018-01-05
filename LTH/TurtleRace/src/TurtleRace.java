import se.lth.cs.ptdc.window.*;
import java.util.Scanner;

public class TurtleRace 
{
	
	
	public static void main(String[] args) 
	{
		int turtleamount=0;
		
		Scanner scan = new Scanner(System.in);
		SimpleWindow w = new SimpleWindow(800,600,"TurtleRace");
		
		System.out.print("Hur många fillurer vill du ha?: (max 50st)");
		
		turtleamount = scan.nextInt();
		if(turtleamount > 50)
			turtleamount = 50;
		else if(turtleamount < 2)
			turtleamount = 2;
		
		RaceTrack track = new RaceTrack(500,100);
		
		
		Turtle[] turtle = new Turtle[turtleamount];
		
		
		for(int i = 0; i < turtle.length; i++)
		{
			turtle[i] = new Turtle(w,150+(i*10), track.getStart());
			turtle[i].penDown();
		}
		
		
		
		RacingEvent racingevent = new RacingEvent(track,turtle);
		
		track.draw(w);
		
		while(racingevent.loop())
		{
			racingevent.race();
			SimpleWindow.delay(10);
		}
		
		
	}

}
