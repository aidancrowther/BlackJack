package aidancrowther.blackjack;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for BlackJack Deck
 */
public class CardTest{

    @Test
    //Assert that a new card can be generated correctly
    public void CheckCardInit(){

        String suit = "H";
        String value = "10";
        Card card = new Card(suit, value);

        assertTrue(card.getSuit().equals(suit));
        assertTrue(card.getValue().equals(value));
    }

    @Test
    //Verify that cards get printed correctly
    public void CheckCardPrint(){

        String suit = "H";
        String value = "A";
        Card card = new Card(suit, value);

        assertTrue(card.toString().split("")[0].equals(suit));
        assertTrue(card.toString().split("")[1].equals(value));
    }
}
