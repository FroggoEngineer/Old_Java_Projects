/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package teleportanimation;

import java.applet.*;
import java.awt.*;


public class Main extends Applet implements Runnable
{

    boolean running;
    int a,b,c;
    int update;
    Color farg;
    Graphics bufferGraphics;
    Image offscreen;
    Thread frameupdate;


    public void init()
    {
        setSize(200,200);
        running = true;
        update = 200;
        

        offscreen = createImage(200,200);
        bufferGraphics = offscreen.getGraphics();
        frameupdate = new Thread(this);
        frameupdate.start();

    }

    public void stop()
    {

    }

    public void run()
    {
        while(running)
        {
            if(update > 20)
                update -= 10;


            try
            {
                frameupdate.sleep(update);
            }catch(InterruptedException e){}

            repaint();

        }
    }


    public void paint(Graphics g)
    {
        bufferGraphics.clearRect(0,0, 400,400);
        for(int i = 0; i < 361; i++)
        {
            a = (int)(30*Math.random());
            b = (int)(30*Math.random());
            c = (int)(150*Math.random());
            farg = new Color(a, b, c);
            bufferGraphics.setColor(farg);
            bufferGraphics.fillArc(-100,-100,400,400,i,1);

        }
        g.drawImage(offscreen,0,0,this);
    }

    public void update(Graphics g)
    {
        paint(g);
    }


}
