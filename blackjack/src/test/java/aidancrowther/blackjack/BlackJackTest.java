package aidancrowther.blackjack;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.*;

/**
 * Unit test for BlackJack Deck
 */
public class BlackJackTest{

    @Test
    //Test that the program accepts file input mode
    public void CheckFileInput(){

        BlackJack.scanner = new Scanner("f");
        BlackJack.init();

        assertTrue(BlackJack.inputMethod == 1);
    }

    @Test
    //Test that the program accepts command input mode
    public void CheckCommandInput(){

        BlackJack.scanner = new Scanner("c");
        BlackJack.init();

        assertTrue(BlackJack.inputMethod == 0);
    }

    @Test
    //Catch invalid play mode selection
    public void CheckInvalidCatch(){

        BlackJack.scanner = new Scanner("j\nc");
        BlackJack.init();

        assertTrue(BlackJack.outputHistory.contains("Invalid input, please try again"));
    }

    @Test
    //Verify that player can hit
    public void CheckPlayerHit(){

        Player player = new Player(false);

        BlackJack.deck = new Deck();

        assertTrue(player.getHandSize() == 0);

        //Should take in an input and player
        //Outputs True/False depending on whether or not to continue
        //Should modify the players hand if we hit/split
        assertTrue(BlackJack.processSelection("H", player));
        assertTrue(player.getHandSize() == 1);
    }

    @Test
    //Verify that player can hit
    public void CheckPlayerStand(){

        Player player = new Player(false);

        BlackJack.deck = new Deck();

        //Should take in an input and player
        //Outputs True/False depending on whether or not to continue
        //Should modify the players hand if we hit/split
        assertFalse(BlackJack.processSelection("S", player));
    }

    @Test
    //Catch invalid input for user selection
    public void CheckPlayerInputInvalid(){

        Player player = new Player(false);

        BlackJack.deck = new Deck();
        BlackJack.outputHistory = "";
        
        //Verify that an invalid input responds correctly
        //Invalid inputs should return true
        assertTrue(BlackJack.processSelection("J", player));
        assertTrue(BlackJack.outputHistory.contains("Invalid selection, please try again"));
    }

    @Test
    //Verify that splitting is rejected if a player does not have a vlid hand for splitting
    public void CheckBlockInvalidSplit(){

        Player player = new Player(false);
        
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "J"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.deck = new Deck();
        BlackJack.inputMethod = 0;
        BlackJack.outputHistory = "";

        //Verify that a game will not split illegaly
        assertTrue(BlackJack.processSelection("D", player));
        assertTrue(BlackJack.players.size() == 1);
        assertFalse(BlackJack.dealSplit);
        assertTrue(player.getHandSize() == 2);
        assertTrue(player.getHandValue() == 20);
        assertTrue(BlackJack.outputHistory.contains("Cannot split"));
    }

    @Test
    //Verify that splitting is allowed, and verify hands are correct
    public void CheckAllowValidSplit(){
        
        BlackJack.players = new ArrayList<>();

        BlackJack.players.add(new Player(false));
        BlackJack.players.get(0).giveCard(new Card("H", "K"));
        BlackJack.players.get(0).giveCard(new Card("C", "K"));

        BlackJack.inputMethod = 0;
        BlackJack.deck = new Deck();
        BlackJack.outputHistory = "";

        //Test that splitting functions correctly under normal play
        assertTrue(BlackJack.processSelection("D", BlackJack.players.get(0)));
        assertTrue(BlackJack.players.size() == 2);
        assertTrue(BlackJack.dealSplit);
        assertTrue(BlackJack.players.get(0).getHandSize() == 2);
        assertTrue(BlackJack.players.get(1).getHandSize() == 1);
        assertFalse(BlackJack.players.get(0).takeCard().toString().equals("CK"));
        assertTrue(BlackJack.outputHistory.contains("Split"));
    }

    @Test
    //Verify that splitting is allowed, and can detect a blackjack
    public void CheckBJSplit(){

        BlackJack.players = new ArrayList<>();

        BlackJack.players.add(new Player(false));
        BlackJack.players.get(0).giveCard(new Card("H", "K"));
        BlackJack.players.get(0).giveCard(new Card("C", "K"));

        BlackJack.inputMethod = 0;
        BlackJack.deck = new Deck();
        BlackJack.deck.push(new Card("H", "A"));
        BlackJack.outputHistory = "";

        //Test that A split into a blackjack is detected
        assertFalse(BlackJack.processSelection("D", BlackJack.players.get(0)));
        assertTrue(BlackJack.players.size() == 2);
        assertTrue(BlackJack.dealSplit);
        assertTrue(BlackJack.players.get(0).getHandSize() == 2);
        assertTrue(BlackJack.players.get(0).hasBJ());
        assertTrue(BlackJack.players.get(1).getHandSize() == 1);
        assertTrue(BlackJack.outputHistory.contains("Split"));
    }

    @Test
    //Verify that the user can be asked to replay
    //Test with yes
    public void CheckYesReplay(){

        BlackJack.outputHistory = "";
        BlackJack.running = true;
        BlackJack.scanner = new Scanner("y\n");
        BlackJack.replay();

        assertTrue(BlackJack.running);
        assertFalse(BlackJack.outputHistory.contains("Invalid input, please try again"));
    }

    @Test
    //Verify that the user can be asked to replay
    //Test with no
    public void CheckNoReplay(){

        BlackJack.outputHistory = "";
        BlackJack.running = true;
        BlackJack.scanner = new Scanner("n");
        BlackJack.replay();

        assertFalse(BlackJack.running);
        assertFalse(BlackJack.outputHistory.contains("Invalid input, please try again"));
    }

    @Test
    //Verify that the user can be asked to replay
    //Test with invalid
    public void CheckInvalidReplay(){

        BlackJack.outputHistory = "";
        BlackJack.running = true;
        BlackJack.scanner = new Scanner("j\ny");
        BlackJack.replay();

        assertTrue(BlackJack.running);
        assertTrue(BlackJack.outputHistory.contains("Invalid input, please try again"));
    }

    @Test
    //Verify that hands display correctly
    public void CheckDisplayPlaying(){

        BlackJack.players = new ArrayList<>();
        BlackJack.dealers = new ArrayList<>();

        BlackJack.players.add(new Player(false));
        BlackJack.dealers.add(new Player(true));
        BlackJack.outputHistory = "";
        BlackJack.deck.push(new Card("H", "K"));
        BlackJack.deck.push(new Card("H", "J"));
        BlackJack.deck.push(new Card("H", "Q"));
        BlackJack.deck.push(new Card("H", "A"));

        BlackJack.players.get(0).giveCard(BlackJack.deck.pop());
        BlackJack.players.get(0).giveCard(BlackJack.deck.pop());
        BlackJack.dealers.get(0).giveCard(BlackJack.deck.pop());
        BlackJack.dealers.get(0).giveCard(BlackJack.deck.pop());

        BlackJack.showHands(false);

        assertTrue(BlackJack.outputHistory.contains("HA HQ"));
        assertFalse(BlackJack.outputHistory.contains("HJ"));
        assertTrue(BlackJack.outputHistory.contains("HK showing"));
    }

    @Test
     //Verify that hands display correctly at the end of play
     public void CheckDisplayEnding(){

        BlackJack.players = new ArrayList<>();
        BlackJack.dealers = new ArrayList<>();

        BlackJack.players.add(new Player(false));
        BlackJack.dealers.add(new Player(true));
        BlackJack.outputHistory = "";
        BlackJack.deck.push(new Card("H", "K"));
        BlackJack.deck.push(new Card("H", "J"));
        BlackJack.deck.push(new Card("H", "Q"));
        BlackJack.deck.push(new Card("H", "A"));

        BlackJack.players.get(0).giveCard(BlackJack.deck.pop());
        BlackJack.players.get(0).giveCard(BlackJack.deck.pop());
        BlackJack.dealers.get(0).giveCard(BlackJack.deck.pop());
        BlackJack.dealers.get(0).giveCard(BlackJack.deck.pop());

        BlackJack.showHands(true);

        assertTrue(BlackJack.outputHistory.contains("HA HQ"));
        assertTrue(BlackJack.outputHistory.contains("HJ"));
        assertTrue(BlackJack.outputHistory.contains("HK"));
    }

    @Test
    //Verify that the user and dealer are dealt hands correctly
    public void CheckDealingDeck(){

        BlackJack.players = new ArrayList<>();
        BlackJack.dealers = new ArrayList<>();

        BlackJack.players.add(new Player(false));
        BlackJack.dealers.add(new Player(true));

        BlackJack.deck = new Deck();

        BlackJack.dealHands();

        assertTrue(BlackJack.players.get(0).getHandSize() == 2);
        assertTrue(BlackJack.dealers.get(0).getHandSize() == 2);
    }

    @Test
    //Verify that the user and dealer are dealt hands correctly
    public void CheckDealingFile(){

        BlackJack.players = new ArrayList<>();
        BlackJack.dealers = new ArrayList<>();

        BlackJack.players.add(new Player(false));
        BlackJack.dealers.add(new Player(true));

        String hands[] = {"HK", "HA", "HQ", "HJ"};

        BlackJack.dealHands(hands);

        assertTrue(BlackJack.players.get(0).getHandSize() == 2);
        assertTrue(BlackJack.dealers.get(0).getHandSize() == 2);
    }

    @Test
    //Verify that file input functions correctly
    public void CheckFileValidInput(){

        BlackJack.scanner = new Scanner("Files/file1.txt");
        BlackJack.commandSequence = new ArrayList<>();

        BlackJack.fileGame(false);

        assertTrue(BlackJack.commandSequence.size() == 4);

        assertTrue(BlackJack.commandSequence.get(0).equals("SK"));
        assertTrue(BlackJack.commandSequence.get(1).equals("HA"));
        assertTrue(BlackJack.commandSequence.get(2).equals("HQ"));
        assertTrue(BlackJack.commandSequence.get(3).equals("CA"));
    }

    @Test
    //Verify that invalid file input is rejected
    public void CheckInvalidFileInput(){

        BlackJack.scanner = new Scanner("Files/fil1.txt\nFiles/file1.txt");
        BlackJack.commandSequence = new ArrayList<>();
        BlackJack.outputHistory = "";

        BlackJack.fileGame(false);

        assertTrue(BlackJack.commandSequence.size() == 4);
        assertTrue(BlackJack.commandSequence.get(0).equals("SK"));
        assertTrue(BlackJack.commandSequence.get(1).equals("HA"));
        assertTrue(BlackJack.commandSequence.get(2).equals("HQ"));
        assertTrue(BlackJack.commandSequence.get(3).equals("CA"));
        assertTrue(BlackJack.outputHistory.contains("File not found, please input a valid file:"));
    }

    @Test
    //Verify that invalid file input is rejected
    public void CheckInvalidFileRejected(){

        BlackJack.scanner = new Scanner("Files/file0.txt\nFiles/file1.txt");
        BlackJack.commandSequence = new ArrayList<>();
        BlackJack.outputHistory = "";

        BlackJack.fileGame(false);

        assertTrue(BlackJack.commandSequence.size() == 4);
        assertTrue(BlackJack.commandSequence.get(0).equals("SK"));
        assertTrue(BlackJack.commandSequence.get(1).equals("HA"));
        assertTrue(BlackJack.commandSequence.get(2).equals("HQ"));
        assertTrue(BlackJack.commandSequence.get(3).equals("CA"));
        assertTrue(BlackJack.outputHistory.contains("File uses illegal characters, please use a valid file: "));
    }

    @Test
    //Verify that game setup functions correctly without predefined hands
    public void CheckSetupGame(){

        BlackJack.playingTurn = false;
        BlackJack.players.add(new Player(false));
        BlackJack.players.add(new Player(false));
        BlackJack.dealers.add(new Player(true));
        BlackJack.dealers.add(new Player(true));

        BlackJack.setupGame();

        assertTrue(BlackJack.players.size() == 1);
        assertTrue(BlackJack.dealers.size() == 1);
        assertFalse(BlackJack.players.get(0).isDealer());
        assertTrue(BlackJack.dealers.get(0).isDealer());
        assertTrue(BlackJack.playingTurn);
    }

    @Test
    //Verify that game setup functions correctly with predefined hands
    public void CheckSetupFileGame(){

        BlackJack.playingTurn = false;
        BlackJack.players.add(new Player(false));
        BlackJack.players.add(new Player(false));
        BlackJack.dealers.add(new Player(true));
        BlackJack.dealers.add(new Player(true));

        String hands[] = {"HK", "HA", "HQ", "HJ"};

        BlackJack.setupGame(hands);

        assertTrue(BlackJack.players.size() == 1);
        assertTrue(BlackJack.dealers.size() == 1);
        assertFalse(BlackJack.players.get(0).isDealer());
        assertTrue(BlackJack.dealers.get(0).isDealer());
        assertTrue(BlackJack.playingTurn);

        assertTrue(BlackJack.players.get(0).toString().contains("HK HA"));
        assertTrue(BlackJack.dealers.get(0).toString(true).contains("HQ HJ"));
    }

    @Test
    //Verify that winners are determined correctly
    //If both have blackjack, dealer wins
    public void CheckBBJ(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "A"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "A"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Dealer has blackjack, dealer wins!"));
    }

    @Test
    //Verify that winners are determined correctly
    //If player has blackjack, they win
    public void CheckPBJ(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "A"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "Q"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Player has blackjack, player wins!"));
    }

    @Test
    //Verify that winners are determined correctly
    //If both have bust, dealer wins
    public void CheckBB(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "Q"));
        player.giveCard(new Card("H", "J"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "Q"));
        dealer.giveCard(new Card("C", "J"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Player busts, dealer wins!"));
    }

    @Test
    //Verify that winners are determined correctly
    //If player busts, dealer wins
    public void CheckPB(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "Q"));
        player.giveCard(new Card("H", "J"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "Q"));
        dealer.giveCard(new Card("C", "A"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Player busts, dealer wins!"));
    }

    @Test
    //Verify that winners are determined correctly
    //If dealer busts, player wins
    public void CheckDB(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "Q"));
        player.giveCard(new Card("H", "A"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "Q"));
        dealer.giveCard(new Card("C", "J"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Dealer busts, player wins!"));
    }

    @Test
    //Verify that winners are determined correctly
    //If both have the same score, it's a tie
    public void CheckT(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "Q"));
        player.giveCard(new Card("H", "A"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "Q"));
        dealer.giveCard(new Card("C", "A"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Tie game!"));
    }

    @Test
    //Verify that winners are determined correctly
    //If dealer has a higher score, it reports correctly
    public void CheckDW(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "6"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "Q"));
        dealer.giveCard(new Card("C", "A"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Dealer wins!"));
        assertTrue(BlackJack.outputHistory.contains("Score is 16 to 21"));
    }

    @Test
    //Verify that winners are determined correctly
    //If player has a higher score, it reports correctly
    public void CheckPW(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "Q"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "6"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Player wins!"));
        assertTrue(BlackJack.outputHistory.contains("Score is 20 to 16"));
    }

    @Test
    //Verify that winners are determined correctly
    //If dealer has a higher score and has split, it reports correctly
    public void CheckDSFW(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "6"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "Q"));
        dealer.giveCard(new Card("C", "A"));

        Player dealer2 = new Player(true);
        dealer2.giveCard(new Card("C", "K"));
        dealer2.giveCard(new Card("C", "Q"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);
        BlackJack.dealers.add(dealer2);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Dealer wins!"));
        assertTrue(BlackJack.outputHistory.contains("Score is 16 to 21"));
    }

    @Test
    //Verify that winners are determined correctly
    //If dealer has a higher score and has split, it reports correctly
    public void CheckDSSW(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "6"));

        Player dealer2 = new Player(true);
        dealer2.giveCard(new Card("C", "K"));
        dealer2.giveCard(new Card("C", "Q"));
        dealer2.giveCard(new Card("C", "A"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "Q"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);
        BlackJack.dealers.add(dealer2);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Dealer wins!"));
        assertTrue(BlackJack.outputHistory.contains("Score is 16 to 21"));
    }

    @Test
    //Verify that winners are determined correctly
    //If dealer has a higher score and a bust, it reports correctly
    public void CheckDSFBW(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "6"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "Q"));
        dealer.giveCard(new Card("C", "A"));

        Player dealer2 = new Player(true);
        dealer2.giveCard(new Card("C", "K"));
        dealer2.giveCard(new Card("C", "Q"));
        dealer2.giveCard(new Card("C", "J"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);
        BlackJack.dealers.add(dealer2);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Dealer wins!"));
        assertTrue(BlackJack.outputHistory.contains("Score is 16 to 21"));
    }

    @Test
    //Verify that winners are determined correctly
    //If dealer has a higher score and a bust, it reports correctly
    public void CheckDSSBW(){

        BlackJack.outputHistory = "";

        Player player = new Player(false);
        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "6"));

        Player dealer2 = new Player(true);
        dealer2.giveCard(new Card("C", "K"));
        dealer2.giveCard(new Card("C", "Q"));
        dealer2.giveCard(new Card("C", "A"));

        Player dealer = new Player(true);
        dealer.giveCard(new Card("C", "K"));
        dealer.giveCard(new Card("C", "Q"));
        dealer.giveCard(new Card("C", "J"));

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);
        BlackJack.dealers.add(dealer2);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Dealer wins!"));
        assertTrue(BlackJack.outputHistory.contains("Score is 16 to 21"));
    }

    @Test
    //Verify that winners are determined correctly
    //If player has a higher score and has split, it reports correctly
    public void CheckPSFW(){

        BlackJack.outputHistory = "";

        Player dealer = new Player(true);
        dealer.giveCard(new Card("H", "K"));
        dealer.giveCard(new Card("H", "6"));

        Player player = new Player(false);
        player.giveCard(new Card("C", "K"));
        player.giveCard(new Card("C", "Q"));
        player.giveCard(new Card("C", "A"));

        Player player2 = new Player(false);
        player2.giveCard(new Card("C", "K"));
        player2.giveCard(new Card("C", "Q"));

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);
        BlackJack.players.add(player2);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Player wins!"));
        assertTrue(BlackJack.outputHistory.contains("Score is 21 to 16"));
    }

    @Test
    //Verify that winners are determined correctly
    //If player has a higher score and has split, it reports correctly
    public void CheckPSSW(){

        BlackJack.outputHistory = "";

        Player dealer = new Player(true);
        dealer.giveCard(new Card("H", "K"));
        dealer.giveCard(new Card("H", "6"));

        Player player2 = new Player(false);
        player2.giveCard(new Card("C", "K"));
        player2.giveCard(new Card("C", "Q"));
        player2.giveCard(new Card("C", "A"));

        Player player = new Player(false);
        player.giveCard(new Card("C", "K"));
        player.giveCard(new Card("C", "Q"));

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);
        BlackJack.players.add(player2);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Player wins!"));
        assertTrue(BlackJack.outputHistory.contains("Score is 21 to 16"));
    }

    @Test
    //Verify that winners are determined correctly
    //If player has a higher score and a bust, it reports correctly
    public void CheckPSFBW(){

        BlackJack.outputHistory = "";

        Player dealer = new Player(true);
        dealer.giveCard(new Card("H", "K"));
        dealer.giveCard(new Card("H", "6"));

        Player player = new Player(false);
        player.giveCard(new Card("C", "K"));
        player.giveCard(new Card("C", "Q"));
        player.giveCard(new Card("C", "A"));

        Player player2 = new Player(false);
        player2.giveCard(new Card("C", "K"));
        player2.giveCard(new Card("C", "Q"));
        player2.giveCard(new Card("C", "J"));

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);
        BlackJack.players.add(player2);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Player wins!"));
        assertTrue(BlackJack.outputHistory.contains("Score is 21 to 16"));
    }

    @Test
    //Verify that winners are determined correctly
    //If player has a higher score and a bust, it reports correctly
    public void CheckPSSBW(){

        BlackJack.outputHistory = "";

        Player dealer = new Player(true);
        dealer.giveCard(new Card("H", "K"));
        dealer.giveCard(new Card("H", "6"));

        Player player2 = new Player(false);
        player2.giveCard(new Card("C", "K"));
        player2.giveCard(new Card("C", "Q"));
        player2.giveCard(new Card("C", "A"));

        Player player = new Player(false);
        player.giveCard(new Card("C", "K"));
        player.giveCard(new Card("C", "Q"));
        player.giveCard(new Card("C", "J"));

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);
        BlackJack.players.add(player2);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Player wins!"));
        assertTrue(BlackJack.outputHistory.contains("Score is 21 to 16"));
    }

    @Test
    //Verify that winners are determined correctly
    //Splits can tie
    public void CheckST(){

        BlackJack.outputHistory = "";

        Player dealer = new Player(true);
        dealer.giveCard(new Card("H", "K"));
        dealer.giveCard(new Card("H", "6"));

        Player dealer2 = new Player(true);
        dealer2.giveCard(new Card("S", "K"));
        dealer2.giveCard(new Card("S", "6"));

        Player player = new Player(false);
        player.giveCard(new Card("C", "K"));
        player.giveCard(new Card("C", "6"));

        Player player2 = new Player(false);
        player2.giveCard(new Card("D", "K"));
        player2.giveCard(new Card("D", "6"));

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);
        BlackJack.dealers.add(dealer2);

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);
        BlackJack.players.add(player2);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Tie game!"));
    }

    @Test
    //Verify that winners are determined correctly
    //Splits can blackjack
    public void CheckDSBJ(){

        BlackJack.outputHistory = "";

        Player dealer = new Player(true);
        dealer.giveCard(new Card("H", "K"));
        dealer.giveCard(new Card("H", "6"));

        Player dealer2 = new Player(true);
        dealer2.giveCard(new Card("S", "K"));
        dealer2.giveCard(new Card("S", "6"));

        Player player = new Player(false);
        player.giveCard(new Card("C", "K"));
        player.giveCard(new Card("C", "6"));

        Player player2 = new Player(false);
        player2.giveCard(new Card("D", "K"));
        player2.giveCard(new Card("D", "A"));

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);
        BlackJack.dealers.add(dealer2);

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);
        BlackJack.players.add(player2);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Player has blackjack, player wins!"));
    }

    @Test
    //Verify that winners are determined correctly
    //Splits can blackjack
    public void CheckPSBJ(){

        BlackJack.outputHistory = "";

        Player dealer = new Player(true);
        dealer.giveCard(new Card("H", "K"));
        dealer.giveCard(new Card("H", "6"));

        Player dealer2 = new Player(true);
        dealer2.giveCard(new Card("S", "K"));
        dealer2.giveCard(new Card("S", "A"));

        Player player = new Player(false);
        player.giveCard(new Card("C", "K"));
        player.giveCard(new Card("C", "6"));

        Player player2 = new Player(false);
        player2.giveCard(new Card("D", "K"));
        player2.giveCard(new Card("D", "6"));

        BlackJack.dealers = new ArrayList<>();
        BlackJack.dealers.add(dealer);
        BlackJack.dealers.add(dealer2);

        BlackJack.players = new ArrayList<>();
        BlackJack.players.add(player);
        BlackJack.players.add(player2);

        BlackJack.getWinner();

        assertTrue(BlackJack.outputHistory.contains("Dealer has blackjack, dealer wins!"));
    }
}
