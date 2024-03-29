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
    protected static Boolean playingTurn = true;

    //Local game objects
    protected static Deck deck;
    protected static ArrayList<Player> players = new ArrayList<>();
    protected static ArrayList<Player> dealers = new ArrayList<>();

    //Command sequence list for use in filegame
    protected static ArrayList<String> commandSequence;
    protected static int command = 0;
    protected static Set<String> seenCards = new HashSet<>();

    //User input scanner
    protected static Scanner scanner = new Scanner(System.in);

    public static void main(String args[]){

        //Intialize the game
        init();

        //Run game loop based on user input selection
        gameLoop();
        
    }

    //Main game loop
    protected static void gameLoop(){

        //Until the game is done being played, continue looping
        while(running){
            switch(inputMethod){
                case(0):
                    cmdGame();
                break;

                case(1):
                    fileGame(true);
                break;
            }

            //Determine whether or not to continue playing
            replay();
        }
    }

    protected static void init(){

        Boolean waiting = true;
        
        while(waiting){
            output("Please select control method:\n\nC) Command Line\nF) File input");
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

    //Give the player and dealer cards off the top of the deck
    protected static void dealHands(){

        players.get(0).giveCard(deck.pop());
        players.get(0).giveCard(deck.pop());
        dealers.get(0).giveCard(deck.pop());
        dealers.get(0).giveCard(deck.pop());
    }

    //Give the player and dealer cards in a given array
    protected static void dealHands(String[] s){

        players.get(0).giveCard(new Card(s[0].split("")[0], s[0].substring(1)));
        players.get(0).giveCard(new Card(s[1].split("")[0], s[1].substring(1)));
        dealers.get(0).giveCard(new Card(s[2].split("")[0], s[2].substring(1)));
        dealers.get(0).giveCard(new Card(s[3].split("")[0], s[3].substring(1)));
    }

    protected static void fileGame(Boolean play){

        //Setup variables for the file
        String fileContents = "";
        Boolean getFile = true;
        Boolean validFile = false;
        command = 0;

        //Request a filename
        System.out.print("Please choose a file: ");
        
        //Loop until we get a valid file
        while(!validFile){
            while(getFile){
                //Attempt to open the given file and read it to a string
                try{
                    fileContents = new String(Files.readAllBytes(Paths.get(scanner.nextLine())));
                }
                //Catch errors, and report invlid filename
                catch(IOException e){
                    output("File not found, please input a valid file: ");
                }

                getFile = fileContents.equals("");

            }

            //Generate an arraylist of commands using the file
            commandSequence = new ArrayList<String>(Arrays.asList(fileContents.split(" ")));

            //Verify the file input is valid
            validFile = true;
            for(String elem : commandSequence){
                validFile &= elem.split("")[0].matches("[SCHD]");
                if(!elem.substring(1).equals("")) validFile &= elem.substring(1).matches("10|[AJQK2-9]");
            }

            seenCards = new HashSet<>();

            for(String elem : commandSequence){
                if(!elem.substring(1).equals("")) if(!seenCards.add(elem)) validFile = false;
            }

            if(!validFile){
                output("File uses illegal characters/duplicates, please use a valid file: ");
                getFile = true;
            }
        }

        //Determine the starting hands
        String hands[] = {commandSequence.get(command++), commandSequence.get(command++), commandSequence.get(command++), commandSequence.get(command++)};

        //Setup the game with predefined hands
        setupGame(hands);

        //Run the game
        if(play) cmdGame();
        
    }

    //Command line game sequence
    private static void cmdGame(){

        Boolean BJSeen = false;

        //If we are not playing a file input game, run the general setup method
        if(inputMethod != 1) setupGame();

        //Initialize values for gameplay and check for opening blackjack
        playingTurn = !(players.get(0).hasBJ() || dealers.get(0).hasBJ()) && !BJSeen;
        int numPlayers = players.size();
        int numDealers = dealers.size();

        //If there were no opening blackjacks
        if(playingTurn){

            //SHow the hands from the players POV
            showHands(false);

            //Play through each of the players hands
            for(int i=0; i<numPlayers; i++){

                //Check if we received a blackjack after a split
                playingTurn = !(players.get(i).hasBJ()) && !BJSeen;
                if(!playingTurn) break;

                //If we have split previously, deal a new card to the new hand
                if(dealSplit){
                    if(inputMethod != 1) players.get(i).giveCard(deck.pop());
                    else players.get(i).giveCard(new Card(commandSequence.get(command).split("")[0], commandSequence.get(command++).substring(1)));
                    dealSplit = false;
                }

                //Continue running through the players hand until they are done
                while(playingTurn){

                    //Make sure to break out of the loop if we got dealt a blackjack on a split
                    playingTurn = !(players.get(i).hasBJ()) && !BJSeen;
                    if(!playingTurn) break;

                    //Run different methods based on user input method selected
                    //Overall, report the players current hand and score
                    //Then collect input and process it, whether from the console or the list of commands in the file
                    if(inputMethod != 1){
                        output("Player has " + players.get(i).toString() + "for "+ players.get(i).getHandValue() + ".\n\nH) Hit\nS) Stand\nD) Split");
                        pad();
                        playingTurn = processSelection(scanner.nextLine(), players.get(i)) && players.get(i).getHandValue() <= 21;
                    }
                    else{
                        output("Player has " + players.get(i).toString() + "for "+ players.get(i).getHandValue() + ".");
                        pad();
                        playingTurn = processSelection(commandSequence.get(command++).toLowerCase(), players.get(i)) && players.get(i).getHandValue() <= 21;
                    }
                }

                //Ensure there are no new blackjacks
                for(Player player : players) if(!BJSeen) BJSeen = player.hasBJ();

                //Upon finishing the hand, show the current state from the users perspective
                if(!BJSeen){
                    showHands(false);
                    output("\n\n");
                }

                //Ensure that we loop if the player has split
                numPlayers = players.size();
                playingTurn |= players.size()-i > 1;
            }
        }

        //Stop play if there have been any blackjacks
        playingTurn = true && !BJSeen;

        //Make sure no blackjacks occurred after a split
        for(Player player : players) playingTurn &= !player.hasBJ();

        //Ensure that the player hasn't busted all their hands
        Boolean playerBusted = true;
        for(Player player : players){
            playerBusted &= player.getHandValue() > 21;
        }

        //If they have, end the game there
        playingTurn = !playerBusted && playingTurn;

        //Begin the dealers turn
        if(playingTurn){

            //Run for each of the dealers hands
            for(int i=0; i<numDealers; i++){

                //Check if we received a blackjack after a split
                playingTurn &= !BJSeen;
                if(!playingTurn) break;

                //If we have split previously, deal a new card to the new hand
                if(dealSplit){
                    if(inputMethod != 1) dealers.get(i).giveCard(deck.pop());
                    else dealers.get(i).giveCard(new Card(commandSequence.get(command).split("")[0], commandSequence.get(command++).substring(1)));
                    dealSplit = false;
                }

                //Continue each hand to completion
                while(playingTurn){

                    //Declare the current visible portion of the dealers hand
                    output("Dealer has " + dealers.get(i).toString() + "showing.");
                    //Split if the dealer can split
                    if(dealers.get(i).canSplit()){
                        playingTurn = processSelection("d", dealers.get(i));
                    }
                    //Otherwise hit so long as the score threshold has not been exceeded
                    else if(dealers.get(i).getHandValue() < 17 || dealers.get(i).getSoft()){
                        playingTurn = processSelection("h", dealers.get(i)) && dealers.get(i).getHandValue() <= 21;
                    }
                    //Otherwise stand
                    else{
                        playingTurn = processSelection("s", dealers.get(i));
                    }

                    //Ensure that we update the hands should the dealer split
                    numDealers = dealers.size();
                }

                //Ensure there have been no new blackjacks
                if(!BJSeen) BJSeen = dealers.get(i).hasBJ();

                //Upon finishing the hand, show the current state from the users perspective
                if(!BJSeen){
                    showHands(false);
                    output("\n\n");
                }

                playingTurn |= dealers.size()-i > 1;
            }
        }

        //Show both players hands at the end
        showHands(true);

        //determine the winner
        getWinner();
        
    }

    protected static void setupGame(){

        playingTurn = true;
        dealSplit = false;

        players.clear();
        dealers.clear();
        players.add(new Player(false));
        dealers.add(new Player(true));

        deck = new Deck();
        deck.shuffle();

        dealHands();
    }

    protected static void setupGame(String[] s){

        playingTurn = true;
        dealSplit = false;

        players.clear();
        dealers.clear();
        players.add(new Player(false));
        dealers.add(new Player(true));

        dealHands(s);
    }

    //Determine who has won the current round
    protected static void getWinner(){

        //Define local variables to track win state
        Boolean playerBust = true;
        Boolean dealerBust = true;
        int playerMax = 0;
        int dealerMax = 0;

        //Iterate over all dealers and players to check all hands
        for(Player dealer : dealers){

            //Determine whether any blackjacks have occured, and if so, exit
            if(dealer.hasBJ()){
                output("Dealer has blackjack, dealer wins!");
                return;
            }

            for(Player player : players){

                if(player.hasBJ()){
                    output("Player has blackjack, player wins!");
                    return;
                }

                //
                if(dealer.getHandValue() > dealerMax && dealer.getHandValue() <= 21) dealerMax = dealer.getHandValue();
                if(player.getHandValue() > playerMax && player.getHandValue() <= 21) playerMax = player.getHandValue();

                playerBust &= player.getHandValue() > 21;
                dealerBust &= dealer.getHandValue() > 21;
            }
        }

        if(playerBust){
            output("Player busts, dealer wins!");
        }
        else if(dealerBust){
            output("Dealer busts, player wins!");
        }
        else if(dealerMax > playerMax){
            output("Score is " + playerMax+ " to " + dealerMax);
            output("Dealer wins!");
        }
        else if(dealerMax < playerMax){
            output("Score is " + playerMax + " to " + dealerMax);
            output("Player wins!");
        }
        else{
            output("Score is " + playerMax + " to " + dealerMax);
            output("Tie game!");
        }
        
    }

}