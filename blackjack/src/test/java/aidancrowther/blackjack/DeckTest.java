package aidancrowther.blackjack;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for BlackJack Deck
 */
public class DeckTest 
{

    @Test
    //Assert that a new deck will have exactly 52 cards in it.
    public void Check52Cards()
    {
        int counter = 0;
        Deck testDeck = new Deck();

        for(;!testDeck.isEmpty();testDeck.pop()) counter++;

        assertTrue(counter == 52);

    }
}
