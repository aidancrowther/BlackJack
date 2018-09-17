package aidancrowther.blackjack;

import static org.junit.Assert.assertFalse;
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

    @Test
    public void checkDeckShuffle(){
        Deck deckOne = new Deck();
        Deck deckTwo = new Deck();
        
        deckOne.shuffle();
        deckTwo.shuffle();

        Boolean decksAreEqual = true;

        while(!deckOne.isEmpty() && !deckTwo.isEmpty()){
            Card cardOne = deckOne.pop();
            Card cardTwo = deckTwo.pop();

            decksAreEqual &= cardOne.toString().equals(cardTwo.toString());
        }

        assertFalse(decksAreEqual);
    }
}
