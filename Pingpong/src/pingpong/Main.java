//A Reuters production
//made by Daniel Forsman and Kevin Soderberg
//Ping-pong v1.0


package pingpong;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends Applet implements Runnable, KeyListener
{
    //Double-Buffering
    Image offscreen;
    Graphics bufferGraphics;

    //Score
    int[] score;

    //Player Movement
    boolean p1up, p1down, p2up, p2down;
    boolean running;
    Thread Frameupdate;

    //Player Bricks
    int Bwidth, Bheight;    //Height and width of the bricks
    int xpos1,xpos2,ypos1,ypos2;    //X and Y positions for the bricks

    //Ball
    int BallWidth, BallHeight;  //Width and Height of the ball
    int xposBall,yposBall;  //X and Y position for the ball
    int Bxspeed, Byspeed; //speed in X and Y axis


    public void init()
    {
        //Playground
        setSize(600,400);
        setBackground(Color.black);

        //Score
        score = new int[2];
        for(int i = 0; i < 2; i++)
            score[i] = 0;

        //Player Bricks
        Bwidth = 20;
        Bheight = 80;
        xpos1 = 20; ypos1 = 160; xpos2 = 560; ypos2 = 160;
        xposBall = 295; yposBall = 195;

        //Double buffering
        offscreen = createImage(600,400);
        bufferGraphics = offscreen.getGraphics();

        //Keys
        addKeyListener(this);

        //Threads and Run
        running = true;
        Frameupdate = new Thread(this);
        Frameupdate.start();

        Bxspeed = 7;
        Byspeed = 5;

    }

    public void run()
    {
        while(running)
        {
            if(p2up)
            {
                if(ypos2 >= 0)
                    ypos2 = ypos2 - 7;
            }
                
            
            if(p2down)
            {
                if(ypos2 + 80 <= 400)
                    ypos2 = ypos2 + 7;
            }


            if(p1up)
            {
                if(ypos1 >= 0)
                    ypos1 = ypos1 -7;
            }


            if(p1down)
            {
                if(ypos1 + 80 <= 400)
                    ypos1 = ypos1 + 7;
            }

            ball();

            score();

            try
            {
                Frameupdate.sleep(25);
            }catch(InterruptedException e){System.out.println(e);}

            repaint();
        }
    }

    public void paint(Graphics g)
    {
        bufferGraphics.clearRect(0,0,600,400);
        bufferGraphics.setColor(Color.white);
        bufferGraphics.fillRect(xpos1, ypos1, Bwidth, Bheight);
        bufferGraphics.fillRect(xpos2, ypos2, Bwidth, Bheight);
        bufferGraphics.setColor(Color.red);
        bufferGraphics.fillRect(295,0, 10, 400);
        bufferGraphics.setColor(Color.blue);
        bufferGraphics.fillArc(xposBall, yposBall, 10, 10, 0, 360);
        bufferGraphics.setColor(Color.red);
        bufferGraphics.drawString(""+score[0], 280, 15);
        bufferGraphics.drawString(""+score[1], 315, 15);

        g.drawImage(offscreen,0,0,this);
    }

    public void update(Graphics g)
    {
        paint(g);
    }



    public void keyPressed(KeyEvent e)
    {
        int KeyPress;
        KeyPress = e.getKeyCode();
        

        if(KeyPress == KeyEvent.VK_UP)
            p2up = true;
        else if(KeyPress == KeyEvent.VK_DOWN)
            p2down = true;
        
        if(KeyPress == KeyEvent.VK_W)
            p1up = true;
        else if(KeyPress == KeyEvent.VK_S)
            p1down = true;


    }

    public void keyReleased(KeyEvent e)
    {
        int KeyRelease;
        KeyRelease = e.getKeyCode();

        if(KeyRelease == KeyEvent.VK_UP)
            p2up = false;
        else if(KeyRelease == KeyEvent.VK_DOWN)
            p2down = false;

        if(KeyRelease == KeyEvent.VK_W)
            p1up = false;
        else if(KeyRelease == KeyEvent.VK_S)
            p1down = false;
    }

    public void keyTyped(KeyEvent e)
    {

    }



    public void stop()
    {

    }

    public void destroy()
    {
       Frameupdate = null;
    }

    public void ball()
    {
            if(xposBall <= xpos1 + Bwidth && yposBall-5 >= ypos1 &&
                    yposBall + 5 <= ypos1+Bheight || xposBall+10 >= xpos2 &&
                    yposBall-5 >= ypos2 && yposBall+5 <= ypos2 + Bheight)
                Bxspeed *= -1;

            if(yposBall <= 0 || yposBall+10 >= 400)
                Byspeed *= -1;





            if(xposBall + 10 < xpos1 || xposBall > xpos2 + Bwidth)
            {
                xposBall = 295;
                yposBall = 195;
            }

            yposBall += Byspeed;
            xposBall += Bxspeed;
    }

    public void score()
    {
        if(xposBall + 10 < xpos1)
            score[1]++;
        if(xposBall > xpos2 + Bwidth)
            score[0]++;
    }
    
}


/*  Change is needed to this code
 *             if(yposBall-10 >= ypos1 && ypos1 - 10 <= ypos1+5 &&
                    xposBall-5<=xpos1 + Bwidth ||
                    yposBall-10 >= ypos2 && yposBall-10<=ypos2+7 &&
                    xposBall-5 >=xpos2 ||
                    yposBall <=ypos1 + Bheight && yposBall >= ypos1-7+Bheight &&
                    xposBall -5<=xpos1+Bwidth ||
                    yposBall <= ypos2+Bheight && yposBall >= ypos2-7+Bheight &&
                    xposBall -5>= xpos2)
                Byspeed *= -1;
 */

