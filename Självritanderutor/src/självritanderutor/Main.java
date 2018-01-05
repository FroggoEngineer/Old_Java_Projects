package självritanderutor;


import java.applet.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;



public class Main extends Applet implements Runnable, ActionListener
{


    boolean draw;

    Thread squareThread;
    Thread circleThread;

    //Square
    int[] a;
    int[] b;

    //Circle
    int[] c;
    int[] d;

    Dimension dim;

    Random generator;

    Checkbox drawbox;
    Checkbox circle;
    Checkbox square;

    CheckboxGroup circleGroup;
    Checkbox circleBlue;
    Checkbox circleRed;
    Checkbox circleGreen;

    CheckboxGroup squareGroup;
    Checkbox squareBlue;
    Checkbox squareRed;
    Checkbox squareGreen;

    Color circleColor;
    Color squareColor;



    public void init()
    {
        setSize(800, 700);
        dim = getSize();

        setLayout(null);

        generator = new Random();

        a = new int[50];
        b = new int[50];
        c = new int[50];
        d = new int[50];

        circle = new Checkbox("Circles", false);
        circle.setBounds(20,650,100,30);
        add(circle);

        circleGroup = new CheckboxGroup();
        circleBlue = new Checkbox("Blue", circleGroup, true);
        circleBlue.setBounds(120,650,100,30);
        circleRed = new Checkbox("Red", circleGroup, false);
        circleRed.setBounds(220,650,100,30);
        circleGreen = new Checkbox("Green", circleGroup, false);
        circleGreen.setBounds(320,650,100,30);
        add(circleBlue);
        add(circleRed);
        add(circleGreen);

        square = new Checkbox("Squares", false);
        square.setBounds(20,680,100,30);
        add(square);

        squareGroup = new CheckboxGroup();
        squareBlue = new Checkbox("Blue",squareGroup, false);
        squareBlue.setBounds(120,680,100,30);
        squareRed = new Checkbox("Red", squareGroup, true);
        squareRed.setBounds(220,680,100,30);
        squareGreen = new Checkbox("Green", squareGroup, false);
        squareGreen.setBounds(320,680,100,30);
        add(squareBlue);
        add(squareRed);
        add(squareGreen);

        drawbox = new Checkbox("Draw", false);
        drawbox.setBounds(100,600,100,30);
        add(drawbox);


        squareThread = new Thread(this);
        squareThread.start();

        circleThread = new Thread(this);
        circleThread.start();
        draw = true;
    }

    public void run()
    {
        while(draw)
        {
            if(square.getState())
            {
                for(int i = 0; i < 50; i++)
                {
                     a[i] = generator.nextInt(dim.width - 50);
                     b[i] = generator.nextInt(dim.height - 200);
                }

            }
            
            if(circle.getState())
            {
                for(int h = 0; h < 50; h++)
                {
                     c[h] = generator.nextInt(dim.width - 50);
                     d[h] = generator.nextInt(dim.height - 200);
                }
               
            }

            try
            {
                 circleThread.sleep(500);
            } catch(InterruptedException e) { System.out.println(e); }

            if(circleBlue.getState())
                circleColor = Color.blue;
            else if (circleRed.getState())
                circleColor = Color.red;
            else
                circleColor = Color.green;

            if(squareBlue.getState())
                squareColor = Color.blue;
            else if(squareRed.getState())
                squareColor = Color.red;
            else
                squareColor = Color.green;

            if(!square.getState() && !circle.getState())
                repaint();
            else if (drawbox.getState())
                repaint();


        }
    }

    public void destroy()
    {
        squareThread = null;
        circleThread = null;
    }

    public void paint(Graphics g)
    {
          if(square.getState())
          {
            for(int i = 0; i < 50; i++)
            {

                 g.setColor(squareColor);
                 g.drawRect(a[i],b[i],30,30);

             }
          }
          if(circle.getState())
          {
            for(int h = 0; h < 50; h++)
            {

                 g.setColor(circleColor);
                 g.drawArc(c[h], d[h], 30, 30, 0, 360);

             }
          }



    }

    public void actionPerformed(ActionEvent evt)
    {

    }


}
