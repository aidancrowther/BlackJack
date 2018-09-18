package aidancrowther.blackjack;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.*;
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

}
