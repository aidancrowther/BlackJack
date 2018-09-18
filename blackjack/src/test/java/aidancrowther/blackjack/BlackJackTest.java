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

}
