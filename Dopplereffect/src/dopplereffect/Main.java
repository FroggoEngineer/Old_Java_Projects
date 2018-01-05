package dopplereffect;

import java.applet.*;
import java.awt.*;
import java.net.*;

public class Main extends Applet implements Runnable
{
    //Images that will be used
    Image background, car, person;

    //Get the applet base
    URL base;

    //Use the images/sounds
    MediaTracker mt;

    //Double-Buffering
    Image offscreen;
    Graphics bufferGraphics;
    
    //Waves
    int[] Xwave, Ywave, wavewidth, waveheight;
    int waveamount, waveupdate;
    

    //Positions
    int xposcar, gubbexpos;
    boolean gubbewave;


    //Threads & Booleans
    boolean running;
    Thread Frameupdate;

    public void init()
    {
        setSize(565,200);
        mt = new MediaTracker(this);

        try
        {
            base = getDocumentBase();
        }catch(Exception e){System.out.println(e);}

        //Double buffering
        offscreen = createImage(565,200);
        bufferGraphics = offscreen.getGraphics();
        
        waveamount = 0;
        waveupdate = 0;
        
        Xwave = new int[100];
        Ywave = new int[100];
        wavewidth = new int[100];
        waveheight = new int[100];
        gubbexpos = 280;
        
        for(int i = 0; i < 50; i++)
        {
            Xwave[i] = 0;
            Ywave[i] = 0;
            wavewidth[i] = 0;
            waveheight[i] = 0;
        }

        background = getImage(base,"Bakgrund.gif");
        car = getImage(base,"car.gif");
        person = getImage(base,"gubbe.gif");

        mt.addImage(background,1);
        mt.addImage(car,2);
        mt.addImage(person, 3);

        

        try
        {
            mt.waitForAll();
        }catch(InterruptedException e){System.out.println(e);}
        
        Frameupdate = new Thread(this);
        Frameupdate.start();

        running = true;

    }

    public void run()
    {
        while(running)
        {
            gubbewave = false;
            xposcar = xposcar + 1;
            
            waveupdate++;
            
            
            if(waveamount == 60)
            {
                waveamount = 0;
                xposcar = 0;
                
                for(int i = 0; i < 100; i++)
                {
                    Xwave[i] = 0;
                    Ywave[i] = 0;
                    wavewidth[i] = 0;
                    waveheight[i] = 0;
                }
            }
                
            
            if(waveupdate % 10 == 0)
            {
                waveamount++;
                Xwave[waveamount - 1] = xposcar + 50;
                Ywave[waveamount - 1] = 162;
            }
                
            
            for(int i = 0; i < waveamount; i++)
            {
                Xwave[i] = Xwave[i] - 4;
                Ywave[i] = Ywave[i] - 4;
                wavewidth[i] = wavewidth[i] + 8;
                waveheight[i] = waveheight[i] + 8;

                if(Xwave[i] > gubbexpos && Xwave[i] < gubbexpos + 20 ||
                        Xwave[i] + wavewidth[i] > gubbexpos &&
                        Xwave[i] + wavewidth[i] < gubbexpos + 20)
                {
                    gubbewave = true;
                }
                

            }

            if(gubbewave)
                person = getImage(base, "gubbe2.gif");
            else
                person = getImage(base, "gubbe.gif");


            /*try
            {
                Frameupdate.sleep(1);
            }catch(InterruptedException e){}*/

            repaint();
            
        }
    }

    public void stop()
    {

    }

    public void paint(Graphics g)
    {
        bufferGraphics.clearRect(0,0,565,200);
        bufferGraphics.drawImage(background, 0, 0, this);
        bufferGraphics.drawImage(person, 280,162,this);
        bufferGraphics.drawImage(car, xposcar, 162, this);

        for(int i = 0; i < waveamount; i++)
            bufferGraphics.drawArc(Xwave[i], Ywave[i], wavewidth[i], waveheight[i], 0, 360);

        g.drawImage(offscreen,0,0,this);
        
    }

    public void update(Graphics g)
    {
        paint(g);
    }

}
