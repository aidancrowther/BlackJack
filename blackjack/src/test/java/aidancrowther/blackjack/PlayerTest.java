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
    //Assert that dealer hand shows fully at end of game
    public void CheckDealerEndHand(){
        Boolean isDealer = true;
        Boolean gameOver = true;
        Player dealer = new Player(isDealer);

        dealer.giveCard(new Card("H", "K"));
        dealer.giveCard(new Card("H", "A"));

        assertTrue(dealer.toString(gameOver).split(" ").length == 2);
    }

    @Test
    //Test that players hands increment size when adding cards
    public void CheckPlayerDealing(){

        Player player = new Player(false);
        assertTrue(player.getHandSize() == 0);

        player.giveCard(new Card("H", "K"));
        assertTrue(player.getHandSize() == 1);
    }

    @Test
    //Test that player hand value increases correctly when adding cards
    public void CheckPlayerHandValue(){

        Player player = new Player(false);

        assertTrue(player.getHandValue() == 0);

        player.giveCard(new Card("S", "A"));
        assertTrue(player.getHandValue() == 11);

        player.giveCard(new Card("H", "A"));
        assertTrue(player.getHandValue() == 12);

        player.giveCard(new Card("C", "A"));
        assertTrue(player.getHandValue() == 13);

        player.giveCard(new Card("H", "K"));
        assertTrue(player.getHandValue() == 13);

        player.giveCard(new Card("H", "Q"));
        assertTrue(player.getHandValue() == 23);

    }

    @Test
    //Verify that the user can identify a blackjack
    public void CheckBJ(){
        Player player = new Player(false);

        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "Q"));

        assertFalse(player.hasBJ());

        player = new Player(false);

        player.giveCard(new Card("H", "K"));
        player.giveCard(new Card("H", "A"));

        assertTrue(player.hasBJ());

    }

    @Test
    //Verify that the player can identify a soft 17
    public void CheckSoft(){

        Player player = new Player(false);

        player.giveCard(new Card("H", "A"));
        player.giveCard(new Card("H", "5"));

        assertFalse(player.getSoft());

        player.giveCard(new Card("C", "A"));

        assertTrue(player.getSoft());
    }

    @Test
    //Verify that a dealer is reported as one and a player is not
    public void CheckIsDealer(){

        Player player = new Player(false);
        assertFalse(player.isDealer());

        Player dealer = new Player(true);
        assertTrue(dealer.isDealer());
    }
}
