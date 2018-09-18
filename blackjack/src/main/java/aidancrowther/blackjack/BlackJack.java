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
    protected static Boolean running = true;

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

        //Switch on user input
        switch(selection.toLowerCase()){

            //If standing, return false
            case("s"):
                output("Stand");
                return false;

            //If hitting, give the player a new card; either from the deck or file input
            case("h"):
                output("Hit");
                if(inputMethod != 1) p.giveCard(deck.pop());
                else p.giveCard(new Card(commandSequence.get(command).split("")[0], commandSequence.get(command++).substring(1)));
                return true;

            //If splitting...
            case("d"):

                //Verify that the player can split
                if(p.canSplit()){
                    output("Split");
                    Player newPlayer = new Player(false, false);

                    //Create a dealer if the player is a dealer
                    if(p.isDealer()) newPlayer = new Player(true, false);

                    //Give the new player one of the first players cards
                    newPlayer.giveCard(p.takeCard());
                        //Determine card source based on input method
                        if(inputMethod != 1){
                            p.giveCard(deck.pop());
                            dealSplit = true;
                        }
                        else{
                            p.giveCard(new Card(commandSequence.get(command).split("")[0], commandSequence.get(command++).substring(1)));
                            dealSplit = true;
                        }

                    //Add the player to the correct list
                    if(p.isDealer()) dealers.add(newPlayer);
                    else players.add(newPlayer);

                    //Don't loop if the player has a blackjack
                    if(p.hasBJ()) return false;

                    //showHands(false);
                }
                //Otherwise report that splitting is not allowed
                else{
                    output("Cannot split");
                }

                //Return true to continue the hand
                return true;

            //Identify invalid command selection
            default:
                output("Invalid selection, please try again");
                return true;

        }
    }

    //Request if the user would like to play again
    protected static void replay(){

        Boolean waiting = true;

        while(waiting){
            System.out.println("Play again?\nY/N");
            pad();
            String selection = scanner.nextLine().toLowerCase();

            switch(selection){
                case("y"):
                    waiting = false;
                break;

                case("n"):
                    running = false;
                    waiting = false;
                break;

                default:
                    output("Invalid input, please try again");
            }
        }
    }

    //Display the players hands
    protected static void showHands(Boolean end){

        //Play the players hands
        output("Player:");
        for(int i=0; i<players.size(); i++){
            output("Hand " + (i+1) + ": " + players.get(i).toString());
        }

        //If we are done playing, display the entirety of the dealers hands
        if(!end){
            output("\nDealer:");
            for(int i=0; i<dealers.size(); i++){
                output("Hand " + (i+1) + ": " + dealers.get(i).toString() + "showing");
            }
        }
        //Otherwise print all of the dealers cards except for the first one
        else{
            output("\nDealer:");
            for(int i=0; i<dealers.size(); i++){
                output("Hand " + (i+1) + ": " + dealers.get(i).toString(true));
            }
        }
    }

}