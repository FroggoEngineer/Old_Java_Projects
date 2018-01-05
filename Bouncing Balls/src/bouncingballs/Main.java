
package bouncingballs;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends Applet implements Runnable, ActionListener
{
    //Velocities
    double[] Vx, Vy;
    double xspeed, g, t;

    //Coordinates
    int[] xpos,ypos;

    //Amount of balls
    int amount;

    //Threads
    boolean running, draw;
    Thread update;

    //Applet size
    int width, height;

    //Buttons
    Button Ballinc, Balldec, start;
    Checkbox Gravity, Nocollide;

    //Position of the balls (Inside,true/outside,false)
    boolean[] Inside;

    //Collision
    double temp;

    //Double-Buffering
    Graphics bufferGraphics;
    Image offscreen;

    public void init()
    {
        UI();
        xspeed = 5;
        balls();

        threads();
        running = true;
        draw = false;
        amount = 0;
        g = 9.82;
        offscreen = createImage(501,501);
        bufferGraphics = offscreen.getGraphics();
        
    }

    public void run()
    {
        while(running)
        {
            while(draw)
            {
                if(amount > 0)
                {
                    collision();
                    positioncheck();
                    t = t + 0.1;
                    position();
                    
                    try
                    {
                        update.sleep(100);
                    }catch(InterruptedException e){System.out.println(e);}

                    repaint();

                }
            }
            
        }
    }

    public void actionPerformed(ActionEvent evt)
    {
        if(evt.getSource() == Ballinc)
        {
            if(amount < 10)
                amount++;
        }
        if(evt.getSource() == Balldec)
        {
            if(amount >= 0)
                amount--;
        }
        if(evt.getSource() == start)
        {
            if(!draw)
                draw = true;
            else
                draw = false;
        }

    }


    public void paint(Graphics g)
    {
        bufferGraphics.clearRect(0,0,501,501);
        //Ball area
        bufferGraphics.setColor(Color.red);
        bufferGraphics.drawLine(0,0,500,0);
        bufferGraphics.drawLine(0,0,0,400);
        bufferGraphics.drawLine(0,400,500,400);
        bufferGraphics.drawLine(500,400,500,0);
        //End of Ball area

        //User Interface area
        bufferGraphics.setColor(Color.black);
        bufferGraphics.drawLine(0,400,0,500);
        bufferGraphics.drawLine(0, 500, 500, 500);
        bufferGraphics.drawLine(500,500, 500,400);
        bufferGraphics.drawLine(0, 401, 500, 401);
        bufferGraphics.drawString("Amount of balls: " + amount + " of 10",150,475);
        //End of User Interface area

        for(int i = 0; i < amount; i++)
        {
            bufferGraphics.fillArc(xpos[i],ypos[i],10,10,0,360);
        }
        g.drawImage(offscreen,0,0,this);
    }
    public void update(Graphics g)
    {
        paint(g);
    }

    public void stop()
    {
        running = false;
    }
    


    public void destroy()
    {
        update = null;
    }



    public void UI()
    {
        width = 500;
        height = 500;
        setSize(width,height);
        amount = 0;

        setLayout(null);
        //Buttons
        Ballinc = new Button("Increase");
        Ballinc.setBounds(20,410,100,25);
        add(Ballinc);
        Ballinc.addActionListener(this);

        Balldec = new Button("Decrease");
        Balldec.setBounds(20,460,100,25);
        add(Balldec);
        Balldec.addActionListener(this);

        start = new Button("Start/Stop");
        start.setBounds(150,410,100,25);
        add(start);
        start.addActionListener(this);

        Gravity = new Checkbox("Gravity", false);
        Gravity.setBounds(280,410,70,25);
        add(Gravity);

        Nocollide = new Checkbox("No collide", false);
        Nocollide.setBounds(380,410,100,25);
        add(Nocollide);
        //End Buttons
    }



    public void balls()
    {
        xpos = new int[10];
        ypos = new int[10];
        Vx = new double[10];
        Vy = new double[10];
        Inside = new boolean[10];

        for(int i = 0; i < 10; i++)
        {
            xpos[i] = 1;
            ypos[i] = 1;
            Vx[i] = xspeed;
            Vy[i] = 3;
            Inside[i] = true;
        }

    }



    public void threads()
    {
        update = new Thread(this);
        update.start();
        
    }


    public void position()
    {
        for(int i = 0; i < amount; i++)
        {
            if(Gravity.getState())
                Vy[i] = Vy[i] + (g * 0.1);
            else
                Vy[i] = Vy[i];
            xpos[i] = xpos[i] + (int)Vx[i];
            ypos[i] = ypos[i] + (int)Vy[i];
        }
    }


    public void collision()
    {
        for(int a = 0; a < amount; a++)
        {
            //Collision among balls
            if(!Nocollide.getState())
            {
                for(int i = 0; i < amount; i++)
                {
                    //Solution doesn't work as intended, change is needed
                    if(xpos[a] + 10 == xpos[i])
                    {
                        temp = Vx[a];
                        Vx[a] = Vx[i];
                        Vx[i] = temp;
                    }
                }
            }
            //End of Collision among balls
            
            
            
            
            //Collision with walls
            if(ypos[a] + 10 >= 400 && Inside[a] == true)
                Vy[a] = Vy[a] * -1;
            

            if(ypos[a] <= 0 && Inside[a] == true)
                Vy[a] = Vy[a] * -1;

            if(xpos[a] + 10 >= 500 && Inside[a] == true)
                Vx[a] = Vx[a] * -1;

            if(xpos[a] <= 0 && Inside[a] == true)
                Vx[a] = Vx[a] * -1;
            //End of Collision with walls
        }

    }
    
    
    public void positioncheck()
    {
        for(int a = 0; a < amount; a++)
        {
           if(ypos[a] + 10 < 400 && ypos[a] > 0 && xpos[a] > 0 && xpos[a] + 10 < 500)
               Inside[a] = true;
           else
               Inside[a] = false;
        }
        
    }
}
