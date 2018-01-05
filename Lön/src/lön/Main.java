
package lön;

import java.util.Scanner;

public class Main {

    static final double TAX = 0.32;//fast skattvärde.

    public static void main (String[] args){

        Scanner in = new Scanner(System.in);

          System.out.println("Enter your monthly salery : ");
          double salery = in.nextDouble();


          double skillnad   = TAX * salery;
          double total      = salery - skillnad;
          double day        = salery / 160;
          double day2       = total / 160;
          double annualsalery = salery * 12;
          double taxfree    = total * 12;

          System.out.println("------------------------------------------");
          System.out.println("monthly salary before taxes  : " + salery);
          System.out.println("salery per day               : " + day);
          System.out.println("------------------------------------------");
          System.out.println("monthly salary after taxes   : " + total);
          System.out.println("taxfree salery per day       : " + day2);
          System.out.println("------------------------------------------");
          System.out.println("total year income before tax : " + annualsalery);
          System.out.println("total year income after tax  : " + taxfree);
          System.out.println("------------------------------------------");
    }


}
