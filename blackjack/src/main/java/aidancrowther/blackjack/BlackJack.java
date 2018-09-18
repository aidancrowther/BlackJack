/* COMP3004 - BlackJack
*  Aidan Crowther, 100980915
*/

package aidancrowther.blackjack;

//Import utilized java libraries
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class BlackJack{

    //Game control objects and variables
    protected static int inputMethod = 0;
    protected static String output = "";

    //User input scanner
    protected static Scanner scanner = new Scanner(System.in);

    protected static void init(){

        Boolean waiting = true;
        
        while(waiting){
            output += ("Please select control method:\n\nC) Command Line\nF) File input\nG) GUI");
            output();
            pad();
            String selection = scanner.nextLine().toLowerCase();

            switch(selection){
                case("c"):
                    inputMethod = 0;
                    waiting = false;
                break;

                case("f"):
                    inputMethod = 1;
                    waiting = false;
                break;

                case("g"):
                    inputMethod = 2;
                    waiting = false;
                break;

                default:
                    output += ("Invalid input, please try again");
                    output();
            }
        }
    }

    private static void output(){
        if(inputMethod != 2) System.out.println(output);
    }

    private static void pad(){
        System.out.print("\n\n>>> ");
    }

}