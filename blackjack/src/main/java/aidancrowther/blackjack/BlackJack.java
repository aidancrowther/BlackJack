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
    protected static String outputHistory = "";
    protected static Boolean dealSplit = false;

    //Local game objects
    protected static Deck deck;
    protected static ArrayList<Player> players = new ArrayList<>();
    protected static ArrayList<Player> dealers = new ArrayList<>();

    //Command sequence list for use in filegame
    protected static ArrayList<String> commandSequence;
    protected static int command = 0;

    //User input scanner
    protected static Scanner scanner = new Scanner(System.in);

    protected static void init(){

        Boolean waiting = true;
        
        while(waiting){
            output("Please select control method:\n\nC) Command Line\nF) File input\nG) GUI");
            pad();

            switch(scanner.nextLine().toLowerCase()){
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
                    output("Invalid input, please try again");
            }
        }
    }

    private static void output(String s){
        if(inputMethod != 2){
            System.out.println(s);
            outputHistory += s;
        }
    }

    private static void pad(){
        System.out.print("\n\n>>> ");
    }

    protected static Boolean processSelection(String selection, Player p){

        switch(selection.toLowerCase()){
            case("s"):
                output("Stand");
                return false;

            case("h"):
                output("Hit");
                if(inputMethod != 1) p.giveCard(deck.pop());
                else p.giveCard(new Card(commandSequence.get(command).split("")[0], commandSequence.get(command++).substring(1)));
                return true;

            default:
                output("Invalid selection, please try again");
                return true;

        }
    }

}