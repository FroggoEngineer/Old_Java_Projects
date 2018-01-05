//Author: Kevin Söderberg
//Sprites and songs belongs to their rightful owners
//This game is a non-commercial project


package rpgspel;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class Main extends Applet implements Runnable, KeyListener
{
    //Intro & Ending
    boolean ending, intro;

    //Double-Buffering
    Image offscreen;
    Graphics bufferGraphics;
    
    //Battle
    boolean battle, Benter, cmd, party, atkopt, safezone, victory, retreat;
    Color box1, box2, box3;
    int anibox, colorbox;
    double battlestart;
    Image arrow;
    Image[] atkpos;
    int[] arrowpos, yatkpos;
    int monsteramount;
    
    //Battle animation
    int[] MonsterAni, Animation, ResetAni;
    boolean AniStart;

    //Party
    int partymin, partymax;

    //Menu
    boolean ingamemenu;
    boolean esckey;
    String[] menunames;

    //Attacks
    String[] macronames, atknames;
    boolean[] macro;
    
    //Names
    String[] charname;
    int[] atkname;

    //Area
    int location;
    boolean run, newarea;
    
    //Sound
    AudioClip bgmusic, lastmusic;
    boolean sound;
    

    //Frameupdate
    boolean running;
    Thread frameupdate;

    //Character movement sprites will be taken from these
    Image[] char1, char2, char3, char4, char5;
    int ImgAni;


    //Menu backgrounds and cities will be loaded from the background[]
    Image[] background;
    int bgimage, lastbg; //Decides which background that will be loaded

    //Walk system
    boolean Wup , Wdown, Wleft, Wright;
    int[] xchar, ychar;
    int charheight, charwidth;
    boolean checkup, checkdown, checkleft, checkright;
    boolean move; //Too move the party in a correct way


    //Checkobjects
    int[] xobject, yobject, objectwidth, objectheight;
    int objectamount;

    //Url to the folder with the images and sounds
    URL base;

    //**************************************************
    //Character settings
    int[] health, maxhealth, mana, maxmana, exp;
    Image[] charPM;
    int partyamount;
    //**************************************************


    MediaTracker mt;



    public void init()
    {
        setSize(800,600);

        

        //Double buffering
        offscreen = createImage(800,600);
        bufferGraphics = offscreen.getGraphics();

        //Keys
        addKeyListener(this);
        
        
        ImgAni = 3;
        location = 1;
        battle = false;
        cmd = false;
        sound = true;
        newarea = true;

        resources();

        
        

        



    }

    public void run()
    {
        
           while(run)
           {
            while(intro)
            {

            }
            while(ending)
            {
                
            }

            while(running)
            {
                 newinput();

                 input();

                 area();

                 if(newarea)
                    insertobjects();

                 checkobjects();

                 if(!ingamemenu)
                    walk();

                 battlecheck();

            

            try
            {
                frameupdate.sleep(150);
            }catch(InterruptedException e){System.out.println(e);}

            repaint();
            }
        
       

        while(battle)
        {
            
            if(Wup)
                anibox--;
            if(Wdown)
                anibox++;

            

            if(cmd)
                cmdbox();

            if(party)
                partybox();

            if(atkopt)
                atkbox();

            repaint();
            try
            {
                frameupdate.sleep(200);
            }catch(InterruptedException e){System.out.println(e);}

            
        }

        while(intro)
        {
          intro();
        }

        while(ending)
        {
            ending();
        }



    }
            
        
       

    }

    public void paint(Graphics g)
    {
        
        bufferGraphics.clearRect(0,0,800,600);

        if(!battle)
        {
            bufferGraphics.drawImage(background[bgimage], 0, 0, this);
            if(move)
            {
                bufferGraphics.drawImage(char1[ImgAni], xchar[0], ychar[0], this);
                bufferGraphics.drawImage(char2[ImgAni], xchar[1], ychar[1], this);
            }
            else
            {
                bufferGraphics.drawImage(char2[ImgAni], xchar[1], ychar[1], this);
                bufferGraphics.drawImage(char1[ImgAni], xchar[0], ychar[0], this);
            }
            if(ingamemenu)
            {
                ingamemenu();
            }
                
        
           
        }

        else if(battle)
        {
            bufferGraphics.drawImage(background[bgimage], 0, 0, this);


            UI();

        }
        //resets the color to black
        bufferGraphics.setColor(Color.black);

        //Displays the offscreen image
        g.drawImage(offscreen,0,0,this);
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void stop()
    {

    }

    public void destroy()
    {

    }

    public void keyPressed(KeyEvent e)
    {
        int KeyPress;
        KeyPress = e.getKeyCode();


        if(KeyPress == KeyEvent.VK_UP)
            Wup = true;
        else if(KeyPress == KeyEvent.VK_DOWN)
            Wdown = true;
        else if(KeyPress == KeyEvent.VK_LEFT)
            Wleft = true;
        else if(KeyPress == KeyEvent.VK_RIGHT)
            Wright = true;
        if(KeyPress == KeyEvent.VK_ENTER)
            Benter = true;
        if(KeyPress == KeyEvent.VK_ESCAPE)
            esckey = true;



    }

    public void keyReleased(KeyEvent e)
    {
        int KeyRelease;
        KeyRelease = e.getKeyCode();

        if(KeyRelease == KeyEvent.VK_UP)
            Wup = false;
        else if(KeyRelease == KeyEvent.VK_DOWN)
            Wdown = false;
        else if(KeyRelease == KeyEvent.VK_LEFT)
            Wleft = false;
        else if(KeyRelease == KeyEvent.VK_RIGHT)
            Wright = false;

        if(KeyRelease == KeyEvent.VK_ENTER)
            Benter = false;

        if(KeyRelease == KeyEvent.VK_ESCAPE)
            esckey = false;
    }

    public void keyTyped(KeyEvent e)
    {

    }









    //Images,Sounds and Variables used in the game are loaded in this function
    public void resources()
    {
        char1 = new Image[12];
        char2 = new Image[12];
        char3 = new Image[12];
        char4 = new Image[12];
        char5 = new Image[12];

        //Names
        atkname = new int[5];
        atkname[0] = 3;
        atkname[1] = 1;
        atkname[2] = 0;
        atkname[3] = 2;
        atkname[4] = 4;
        charname = new String[5];
        charname[0] = "Renton";
        charname[1] = "Freya";
        charname[2] = "Nagato";
        charname[3] = "Eureka";
        charname[4] = "Thor";

        //Menu names
        menunames = new String[5];
        menunames[0] = "Items";
        menunames[1] = "Equip";
        menunames[2] = "Spells";
        menunames[3] = "Status";
        menunames[4] = "Macro";

        xchar = new int[5];
        ychar = new int[5];

        xobject = new int[30];
        yobject = new int[30];
        objectwidth = new int[30];
        objectheight = new int[30];

        background = new Image[6];


        base = getDocumentBase();

        mt = new MediaTracker(this);

        //Sound
        bgmusic = getAudioClip(base,"ps4motaviatown3.mid");
        sound = true;

        //Frameupdate
        running = true;
        run = true;
        frameupdate = new Thread(this);
        frameupdate.start();

        charwidth = 15;
        charheight = 30;

        xchar[0] = 300;
        ychar[0] = 400;

        xchar[1] = 300;
        ychar[1] = 400;

        //Battle system
        box1 = Color.white;
        box2 = Color.white;
        box3 = Color.white;
        anibox = 1;
        colorbox = 1;
        //Partybox
        arrowpos = new int[5];
        arrow = getImage(base, "arrow.gif");
        mt.addImage(arrow, 68);

        for(int i = 0; i < 5; i++)
        {
            arrowpos[i] = 55+(i*25)-12;
        }

        for(int i = 0; i < 12; i++)
        {
            char1[i] = getImage(base, i + ".gif");
            char2[i] = getImage(base, (i+12) + ".gif");
            char3[i] = getImage(base, (i+24) + ".gif");
            char4[i] = getImage(base, (i+36) + ".gif");
            char5[i] = getImage(base, (i+48) + ".gif");

            mt.addImage(char1[i], i + 1);
            mt.addImage(char2[i], i + 13);
            mt.addImage(char3[i], i + 25);
            mt.addImage(char4[i], i + 37);
            mt.addImage(char5[i], i + 49);
        }

        //**************************************************
        health = new int[5];
        maxhealth = new int[5];
        mana = new int[5];
        maxmana = new int[5];
        charPM = new Image[5];
        exp = new int[5];
        partyamount = 2;

        for(int i = 0 ; i < 5 ; i++)
        {
            maxhealth[i] = 30;
            health[i] = 30;
            maxmana[i] = 50;
            mana[i] = 50;
            exp[i] = 0;
        }
        
        for(int i = 0; i < 5; i++)
        {
            charPM[i] = getImage(base, (i+60) + ".gif");

            mt.addImage(charPM[i], i + 60);
        }
        //**************************************************

        for(int i = 0; i < 4;i++)
        {
            background[i] = getImage(base, "bg" + (i+1) + ".gif");
            mt.addImage(background[i], i + 64);
        }
        
        
            background[4] = getImage(base,"City1.png");
            mt.addImage(background[4], 69);
            background[5] = getImage(base,"Location2.gif");
            mt.addImage(background[5], 70);
        

        yatkpos = new int[5];
        yatkpos[0] = 387;
        yatkpos[1] = 382;
        yatkpos[2] = 389;
        yatkpos[3] = 411;
        yatkpos[4] = 382;

        atkpos = new Image[5];
        for(int i = 0; i < 5; i++)
            atkpos[i] = getImage(base, (i+65) + ".gif");

        try
        {
          mt.waitForAll();  
        }catch(Exception e){}
        
    }

    public void intro()
    {

    }

    public void area()
    {
        if(location == 1)
        {
            if(sound)
            {
                bgmusic.stop();
                bgmusic = getAudioClip(base, "ps4motaviatown3.mid");
                bgmusic.loop();
                sound = false;
            }
            bgimage = 4;
            safezone = true;

            if(ychar[0] > 540)
            {
                location = 2;
                sound = true;
                xchar[0] = 25;
                ychar[0] = 60;
                newarea = true;
            }
                
        }

        if(location == 2)
        {
            if(sound)
            {
                bgmusic.stop();
                bgmusic = getAudioClip(base, "ps4fieldmotavia3.mid");
                bgmusic.loop();
                sound = false;
                
            }
            safezone = false;
            bgimage = 5;

            if(ychar[0] < 40 && xchar[0] < 40)
            {
                location = 1;
                sound = true;
                xchar[0] = 400;
                ychar[0] = 520;
                newarea = true;
            }

            if(ychar[0] > 140 && ychar[0] < 214 && xchar[0] - 10 > 165 && xchar[0] < 223)
            {
                location = 3;
                sound = true;
                newarea = true;

            }


                
        }

        if(location == 3)
        {
            if(sound)
            {
                bgmusic.stop();
                bgmusic = getAudioClip(base, "ps4dungeonarrangeI5.mid");
                bgmusic.loop();
                sound = false;
                
            }



        }

    }

    //Partially finished
    public void walk()
    {
        
        if(Wup && checkup || Wdown && checkdown || 
                Wleft && checkleft || Wright && checkright)
            for(int i = 4; i > 0; i--)
            {
                if(Wup && checkup || Wdown && checkdown)
                    xchar[i] = xchar[i-1];
                else if(Wleft && checkleft)
                    xchar[i] = xchar[i-1] + charwidth;
                else if(Wright && checkright)
                    xchar[i] = xchar[i-1] - charwidth;

                if(Wup && checkup)
                    ychar[i] = ychar[i-1] + 6;
                else if(Wdown && checkdown)
                    ychar[i] = ychar[i-1] - 6;
                else if(Wleft && checkleft || Wright && checkright)
                    ychar[i] = ychar[i-1];
            }
        
        if(Wup && checkup)
        {
            ychar[0] -= 5;
            ImgAni++;
            if(ImgAni > 5 || ImgAni < 3)
                ImgAni = 3;
            move = true;
            battlestart = 50 * Math.random();
        }

        else if(Wdown && checkdown)
        {
            ychar[0] += 5;
            ImgAni++;
            if(ImgAni > 2)
                ImgAni = 0;
            move = false;
            battlestart = 50 * Math.random();
        }

        else if(Wleft && checkleft)
        {
            xchar[0] -= 5;
            ImgAni++;
            if(ImgAni > 11 || ImgAni < 9)
                ImgAni = 9;
            move = false;
            battlestart = 50 * Math.random();
        }

        else if(Wright && checkright)
        {
            xchar[0] += 5;
            ImgAni++;
            if(ImgAni > 8 || ImgAni < 6)
                ImgAni = 6;
            move = false;
            battlestart = 50 * Math.random();
        }
        
        
            


        


    }


    //Finished
    public void checkobjects()
    {
        for(int i = 0; i < objectamount; i++)
        {
            if(xobject[i] + objectwidth[i] < xchar[0] &&
                    xobject[i] + objectwidth[i] > xchar[0] - 6 &&
                    yobject[i] < ychar[0] + charheight &&
                    yobject[i] + objectheight[i] - 25 > ychar[0])
                checkleft = false;

            if(xobject[i] > xchar[0] + charwidth &&
                    xobject[i] - 6 < xchar[0] + charwidth &&
                    yobject[i] < ychar[0] + charheight &&
                    yobject[i] + objectheight[i] - 25 > ychar[0])
                checkright = false;
            
            
            if(xobject[i] < xchar[0] + charwidth  + 3 &&
                    xobject[i] + objectwidth[i] + 3 > xchar[0] &&
                    yobject[i] + objectheight[i] < ychar[0] + charheight &&
                    yobject[i] + objectheight[i]  > ychar[0] + charheight - 10)
                checkup = false;

            if(xobject[i] < xchar[0] + charwidth + 3 &&
                    xobject[i] + objectwidth[i] + 3 > xchar[0] &&
                    yobject[i] > ychar[0] + charheight - 3 &&
                    yobject[i] < ychar[0] + charheight + 3)
                checkdown = false;

            



        }
    }

    public void insertobjects()
    {
       if(location == 1)
       {
           objectamount = 22;
           //Width/Height ************************
           //Market
           objectwidth[0] = 191;
           objectheight[0] = 64;
           //*****

           //Houses with 2 rooms + Inn 1-3
           for(int i = 1; i < 4; i++)
           {
               objectwidth[i] = 128;
               objectheight[i] = 96;
           }
           //*****

           //Houses with single room + Weapon store
           for(int i = 4; i < 6; i++)
           {
               objectwidth[i] = 64;
               objectheight[i] = 96;
           }
           //*****

           //Benches, crates, wells, statue, 6-12
           for(int i = 6; i < 13; i++)
               objectwidth[i] = 32;
           //*****

           for(int i = 13; i < 17; i++)
           {
               objectwidth[i] = 28;
               objectheight[i] = 31;
           }
           //*****
           

           //Vase 17
           objectwidth[17] = 14;
           objectheight[17] = 16;
           xobject[17] = 323;
           yobject[17] = 326;
           //*****

           //Fences 18-19
           for(int i = 18; i < 20; i++)
                objectheight[i] = 13;
           objectwidth[18] = 336;
           objectwidth[19] = 392;
           //***************************************
           
           //X-Ypos **************************************
           
           //Market
           xobject[0] = 180;
           yobject[0] = 105;
           //*****

           //Inn
           xobject[1] = 149;
           yobject[1] = 246;
           //*****

           //House
           xobject[2] = 446;
           yobject[2] = 74;
           //*****

           //House 2
           xobject[3] = 553;
           yobject[3] = 231;
           //*****

           //Weapon Store
           xobject[4] = 69;
           yobject[4] = 408;
           //*****

           //Single house
           xobject[5] = 611;
           yobject[5] = 393;
           //*****
           
           //Benches 6-7
           for(int i = 6; i < 8; i++)
               objectheight[i] = 16;
           xobject[6] = 287;
           yobject[6] = 326;
           xobject[7] = 569;
           yobject[7] = 473;
           //*****
           
           //Wells 8-9
           for(int i = 8; i < 10; i++)
               objectheight[i] = 32;
           xobject[8] = 117;
           yobject[8] = 310;
           xobject[9] = 521;
           yobject[9] = 295;
           //*****
           
           //Crates 10-11
           objectheight[10] = 41;
           xobject[10] = 136;
           yobject[10] = 463;
           objectheight[11] = 27;
           xobject[11] = 168;
           yobject[11] = 469;
           //*****

           //Statue
           xobject[12] = 400;
           yobject[12] = 265;
           objectheight[12] = 63;
           //*****

           //Gravestones 13-16
           for(int i = 0; i < 2; i++)
           {
               xobject[i+13] = 704 + (i*34);
               xobject[i+15] = 704 + (i*34);
               yobject[i+13] = 444;
               yobject[i+15] = 478;
           }
           //*****

           //Fences
           xobject[18] = 0;
           for(int i = 18; i < 20; i++)
                yobject[i] = 517;
           xobject[19] = 408;
           //*****

       }

       if(location == 2)
       {
           for(int i = 0; i < 30; i++)
           {
               xobject[i] = 0;
               yobject[i] = 0;
               objectwidth[i] = 0;
               objectheight[i] = 0;
           }
       }

       newarea = false;
        
    }

    public void animation()
    {
        
    }

    //Finished
    public void newinput()
    {
        checkleft = true;
        checkright = true;
        checkup = true;
        checkdown = true;


    }

    //Partially finished
    public void battlecheck()
    {
        if(!safezone && battlestart >= 49)
        {
            lastbg = bgimage;
            lastmusic = bgmusic;
            running = false;
            cmd = true;
            battle = true;
            party = false;
            atkopt = false;
            anibox = 1;
            partycheck();
            
            if(location == 2)
                bgimage = 0;
            bgmusic.stop();
            bgmusic = getAudioClip(base, "ps4meetthemheadon2.mid");
            bgmusic.loop();
            
            
        }
        
    }

    //Partially finished
    public void BattleEnd()
    {
        if(victory)
        {

        }

        battlestart = 1;
        battle = false;
        running = true;
        cmd = false;
        bgimage = lastbg;
        bgmusic.stop();
        bgmusic = lastmusic;
        bgmusic.loop();
        anibox = 1;
        
        

    }

    public void UI()
    {
            //Party Boxes
            bufferGraphics.setColor(Color.blue);
            bufferGraphics.fillRect(0,475,800,125);
            bufferGraphics.setColor(Color.black);
            for(int i = 1; i < 5; i++)
                bufferGraphics.fillRect((i*160) - 2, 475, 4, 125);

            bufferGraphics.fillRect(0, 475, 4 , 125);
            bufferGraphics.fillRect(796, 475, 4, 125);
            bufferGraphics.fillRect(0, 473, 800, 4);
            bufferGraphics.fillRect(0,600-4, 800, 4);
            bufferGraphics.setColor(Color.blue);
            bufferGraphics.fillRect(25,25, 125, 150);
            bufferGraphics.setColor(Color.black);
            bufferGraphics.fillRect(23,23, 129, 4);
            bufferGraphics.fillRect(23,173, 129, 4);
            bufferGraphics.fillRect(23,23, 4, 154);
            bufferGraphics.fillRect(148,23, 4, 154);



            //Images for attack positions
            for(int i = partymin; i < partymax; i++)
                bufferGraphics.drawImage(atkpos[i], (i*160) + 40, yatkpos[i],this);

            //Health, Mana and Names
            
            for(int i = partymin; i < partymax; i++)
            {
                bufferGraphics.setColor(Color.white);
                bufferGraphics.drawString("Name: " + charname[atkname[i]],
                        6 + (i*160), 490);
                bufferGraphics.setColor(Color.red);
                bufferGraphics.drawString("Hp: " + health[atkname[i]] + " / " +
                                            maxhealth[atkname[i]],6 + (i*160),
                                            520);
                bufferGraphics.setColor(Color.MAGENTA);
                bufferGraphics.drawString("Mp: " + mana[atkname[i]] + " / " +
                                            maxmana[atkname[i]],6 + (i*160),
                                            550);
            }
                

            if(cmd)
            {
                //Command box
                bufferGraphics.setColor(box1);
                bufferGraphics.fillRect(35, 40, 15,15);
            
                bufferGraphics.setColor(box2);
                bufferGraphics.fillRect(35, 40 + (1 *(15+35)), 15,15);

                bufferGraphics.setColor(box3);
                bufferGraphics.fillRect(35, 40 + (2 *(15+35)), 15,15);
            
                bufferGraphics.setColor(Color.gray);
            
                for(int i = 0; i < 3; i++)
                {
                    bufferGraphics.fillRect(35, 40 + (i *(15+35)), 3,  15);
            
                    bufferGraphics.fillRect(35, 40 + (i *(15+35)), 15,  3);
            
                    bufferGraphics.fillRect(50, 40 + (i *(15+35)), 3,  18);
            
                    bufferGraphics.fillRect(35, 40 + (i *(15+35)) + 15, 15,  3);
                }

                bufferGraphics.setColor(Color.white);
                bufferGraphics.drawString("Command", 60, 55);
                bufferGraphics.drawString("Macro", 60,40 + (1 *(15+35)) + 15);
                bufferGraphics.drawString("Retreat", 60, 40 + (2 *(15+35)) + 15);
            }
            if(party)
            {
                for(int i = 0; i < partyamount; i++)
                {
                    bufferGraphics.setColor(Color.white);
                    bufferGraphics.drawString(charname[i], 35, 55 + (i *(25)));
                    
                }
                
                bufferGraphics.drawImage(arrow, 80, arrowpos[anibox - 1], this);
            }
            
            

    }

    public void cmdbox()
    {
        if(anibox <= 0)
                anibox = 3;
            if(anibox >= 4)
                anibox = 1;

            if(anibox == 1)
            {
                box1 = Color.red; 
                if(Benter)
                {
                    party = true;
                    cmd = false;
                    anibox = 1;
                }
                    
            }  
            else if(anibox != 1)
                box1 = Color.white;

            if(anibox == 2)
            {
                box2 = Color.red;
            }
                
            else if(anibox != 2)
                box2 = Color.white;


            if(anibox == 3)
            {
                box3 = Color.red;
                if(Benter == true)
                {
                    BattleEnd();
                }
                    
            }
                
            else if(anibox != 3)
                box3 = Color.white;
    }

    public void partybox()
    {
        if(anibox == 0)
                anibox = 5;
            if(anibox == 6)
                anibox = 1;
        if(esckey)
        {
            cmd = true;
            party = false;
            anibox = 1;
        }
    }

    public void atkbox()
    {
        
    }
    
    public void macrobox()
    {
        
    }

    public void battle()
    {

    }
    
    public void Animation()
    {
        for(int i = 0; i < monsteramount; i++)
        {
          if(AniStart)
            ResetAni[i] = Animation[i];  
        }

        for(int i = 0; i < monsteramount; i++)
        {
            Animation[i]++;

            if(Animation[i] > MonsterAni[i])
                Animation[i] = ResetAni[i];
        }
        
            
    }

    //Finished
    public void partycheck()
    {
            if(partyamount == 1)
            {
                partymin = 2;
                partymax = 3;
            }
            if(partyamount == 2)
            {
                partymin = 1;
                partymax = 3;
            }
            if(partyamount == 3)
            {
                partymin = 1;
                partymax = 4;
            }
            if(partyamount == 4)
            {
                partymin = 0;
                partymax = 4;
            }
            if(partyamount == 5)
            {
                partymin = 0;
                partymax = 5;
            }


    }

    public void input()
    {

        if(Benter)
        {
            if(ingamemenu)
            {
                ingamemenu = false;
                anibox = 1;
            }    
            else
                ingamemenu = true;
        }
        


    }

    public void ending()
    {

    }

    public void ingamemenu()
    {
        if(Wup)
            anibox--;
        if(Wdown)
            anibox++;

        if(anibox < 0)
            anibox = 4;
        if(anibox > 4)
            anibox = 0;

        bufferGraphics.setColor(Color.blue);
        bufferGraphics.fillRect(30,50,100,200);

        for(int i = 0; i < 2; i++)
        {
            bufferGraphics.setColor(Color.black);
            bufferGraphics.fillRect(28,48 + (i*200),104,4);
            bufferGraphics.fillRect(28 + (i*100), 48, 4, 204);
        }

        for(int i = 0; i < 5; i++)
        {
            bufferGraphics.setColor(Color.white);
            bufferGraphics.drawString(menunames[i], 40, 70 + (i*40));
        }
        bufferGraphics.drawImage(arrow, 85, 58 + (anibox * 40), this);

        if(Benter)
        {
            if(anibox == 4)
            {
                
            }
        }

    }

}
