

package mathapplication;

import java.util.Scanner;
import java.applet.*;
import java.awt.*;

public class Main extends Applet
{
    
    static Scanner in = new Scanner(System.in);
    
    
    
    
    static void Sphere()
    {
        System.out.println("Surface area(1) or Volume(2)?");
        double choice = in.nextDouble();
        System.out.println("Value on the radius?");
        double radius = in.nextDouble();
        
        if(choice == 1)
        {
            System.out.println("4*π*r^2");
            System.out.println(4*Math.PI*(radius*radius));
        }
        else if (choice == 2)
        {
            System.out.println("(4*π*r^3)/3");
            System.out.println((4*Math.PI*(radius*radius*radius))/3);
        }
    }
    
    static void Cone()
    {
        System.out.println("Surface area(1) or volume(2)?");
        double choice = in.nextDouble();
        System.out.println("Value on the radius: ");
        double radius = in.nextDouble();
        
        if(choice == 1)
        {
            System.out.println("Value on the hypotenuse: ");
            double hypo = in.nextDouble();
            
            System.out.println("π*radius*hypotenuse + π*radius^2");
            System.out.println(Math.PI*radius*hypo + Math.PI*(radius*radius));
        }
        if(choice == 2)
        {
            System.out.println("Value on the height: ");
            double height = in.nextDouble();
            
            System.out.println("(π * radius^2 * height)/3");
            System.out.println((Math.PI*(radius*radius)*height)/3);
            
        }
    }
    
    static void Rectprism()
    {
        System.out.println("Surface area(1) or Volume(2)?");
        double choice = in.nextDouble();
        System.out.println("Value on Height: ");
        double height = in.nextDouble();
        System.out.println("Length: ");
        double length = in.nextDouble();
        System.out.println("Width: ");
        double width = in.nextDouble();
        
        if(choice == 1)
        {
            System.out.println("2*Length*Width + 2*Length*Height + 2*Width*Height");
            System.out.println(2*length*width + 2*length*height + 2*width*height);
        }
        if(choice == 2)
        {
            System.out.println("Length*Width*Height");
            System.out.println(length*width*height);
        }
    }
    
    static void Triprism()
    {
        System.out.println("Surface area(1) or Volume(2)");
        double choice = in.nextDouble();
        System.out.println("Value on the Base: ");
        double base = in.nextDouble();
        System.out.println("Height: ");
        double height = in.nextDouble();
        System.out.println("Length: ");
        double length = in.nextDouble();
        
        if(choice == 1)
        {
            System.out.println("Hypotenuse: ");
            double hypo = in.nextDouble();
            System.out.println("Base*Height + 2*Length*Hypotenuse + Length * Base");
            System.out.println(base*height + 2*length*hypo + length*base);
        }
        if(choice == 2)
        {
            System.out.println("(Base*Height*Length)/2");
            System.out.println((base*height*length)/2);
        }
    }
    
    static void Pyramid()
    {
       System.out.println("Surface area(1) or Volume(2)?");
       double choice = in.nextDouble();

       System.out.println("Base: ");
       double base = in.nextDouble();
       
       if(choice == 1)
       {
            System.out.println("Perimeter: ");
            double perimeter = in.nextDouble();
            System.out.println("Side Length: ");
            double side = in.nextDouble();
            System.out.println((perimeter*side)/2 + (base*base));

       }
       if(choice == 2)
       {
            System.out.println("Height: ");
            double height = in.nextDouble();
            System.out.println("(Base^2*Height)/3");
            System.out.println(((base*base)*height)/3);
            
       }
       
       
    }
    
    static void Sector()
    {
        System.out.println("Value on the radius: ");
        double radius = in.nextDouble();
        System.out.println("Amount of degrees: ");
        double degrees = in.nextDouble();
        System.out.println((degrees/360) * Math.PI * (radius*radius));

    }
    
    static void sge()
    {
       System.out.println("----------------------"); 
       System.out.println("     Second grade     ");
       System.out.println("      Equations       ");
       System.out.println("----------------------");
       System.out.println("Enter the three different values: x^2+-x+-a");
       double a = in.nextDouble();
       double b = in.nextDouble();
       double c = in.nextDouble();
       
       System.out.println(a + "x^2 " + b + " x " + c );
       
       if(a > 1)
       {
           b = b/a;
           c = c/a;
           a = a/a;
           System.out.println("");
           System.out.println(a + "x^2 " + b + " x " + c );
       }
       b = b/2;
       System.out.println("");
       System.out.println("x = " + b + " +-√ " + b +"^2 +" + (c * -1));
       System.out.println("");
       if(Math.sqrt((b*b)+c) <= 0)
       {
           System.out.println("Nonreal answer");
       }
       else
       {
       System.out.println("X1 = " + (b + Math.sqrt((b*b)+c)) );
       System.out.println("X1 = " + (b - Math.sqrt((b*b)+c)) );
       }

    }
    
    static void geo()
    {
       System.out.println("----------------------"); 
       System.out.println("      Geometric       ");
       System.out.println("     Calculations     ");
       System.out.println("----------------------");
       System.out.println("Sphere (1)");
       System.out.println("Cone (2)");
       System.out.println("Rectangular prism (3)");
       System.out.println("Triangular prism (4)");
       System.out.println("Pyramid (5)");
       System.out.println("Circle sector (6)");
       double choice = in.nextDouble();
       
       if(choice == 1)
           Sphere();
       if(choice == 2)
           Cone();
       if(choice == 3)
           Rectprism();
       if(choice == 4)
           Triprism();
       if(choice == 5)
           Pyramid();
       if(choice == 6)
           Sector();
       
    }
    
    static void tri()
    {
        
    }
            
    
    static void Physics()
    {
        
    }

    static void Math()
    {
        System.out.println("----------------------");
        System.out.println("     Mathematical     ");
        System.out.println("     Calculations     ");
        System.out.println("   Errors may occur!  ");
        System.out.println("----------------------");
        System.out.println("Second Grade Equations (1)");
        System.out.println("Geometry (2)");
        System.out.println("Trigonometry (3)");
        System.out.println("Back (4)");
        double choice = in.nextDouble();
        
        if(choice == 1)
            sge();
        else if(choice == 2)
            geo();
        else if(choice == 3)
            tri();
        
    }
    
    public static void main(String[] args)
    {
        boolean running = true;
        while(running)
        {
            System.out.println("----------------------");
            System.out.println("    Math & Physics    ");
            System.out.println("      Application     ");
            System.out.println("----------------------");
            System.out.println("Physics (1)");
            System.out.println("Math (2)");
            System.out.println("Exit (3)");
            System.out.println("Choice: ");
            double choice = in.nextDouble();
           
            if(choice == 1)
                Physics();
            else if (choice == 2)
                Math();
            else if(choice == 3)
                running = false;
            
        }
    }

    public void init()
    {

    }

    public void stop()
    {

    }

    public void paint(Graphics g)
    {

    }
}
