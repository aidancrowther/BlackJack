package aidancrowther.blackjack;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for BlackJack Deck
 */
public class PlayerTest 
{

    @Test
    //Assert that a new players hand shows both their initial cards
    public void CheckPlayerToString(){
        Boolean isDealer = false;
        Player player = new Player(isDealer);

        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "A"));

        assertTrue(player.toString().split(" ").length == 2);
    }

    @Test
    //Assert that a new dealer hand shows only one of their initial cards
    public void CheckDealerToString(){
        Boolean isDealer = true;
        Player dealer = new Player(isDealer);

        dealer.giveCard(new Card("H", "K"));
        dealer.giveCard(new Card("H", "A"));

        assertTrue(dealer.toString().split(" ").length == 1);
    }

    @Test
    //Test that players hands increment size when adding cards
    public void CheckPlayerDealing(){
        
        Player player = new Player(false);
        assertTrue(player.getHandSize() == 0);

        player.giveCard(new Card("H", "K"));
        assertTrue(player.getHandSize() == 1);
    }
}
