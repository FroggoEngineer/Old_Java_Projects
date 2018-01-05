package kaströrelse;

import java.util.Scanner;

public class Main
{

    
    public static void main(String[] args) 
    {
        double xPos, yPos, Hu, t=0, g = 9.82, v;
        double[] Svar = new double[5];
        int i = 0, a;
        String Test, Tidigare;

        while(true)
        {
            Scanner scan = new Scanner(System.in);

            System.out.println("Utgångshastighet i kastet?: ");
            Hu = Double.parseDouble(scan.next());
            System.out.println("Vilken vinkel kastar du bollen i?: ");
            v = Double.parseDouble(scan.next());

            //Beräkning sker här
            while(true)
            {
                t += 0.01;
                yPos = (Hu * Math.sin(v*Math.PI/180) * t) - ((g*(t*t))/2);
                xPos = Hu * Math.cos(v*(Math.PI/180)) * t;

                if(yPos <= 0)
                    break;
            }
            System.out.println("Bollen flög " + xPos + "m");

            //Sparar svaret
            Svar[i] = xPos;
            i++;
            if(i > 4)
                i = 0;

            //Frågar om du vill kolla ett tidigare svar
            System.out.println("Vill du se ett tidigare svar?");
            if(scan.next().equals("ja"))
            {
                System.out.println("Ange tal mellan 1-5");
                a = Integer.parseInt(scan.next());
                System.out.println(Svar[a-1]);
            }

            //Frågar om du vill göra om simulationen med nya variabler
            System.out.println("Vill du testa igen?");
            Test = scan.next();
            if(Test.equals("nej"))
            {
                
                break;
                
            }
            else
                t = 0;

        }

        
    }

}
