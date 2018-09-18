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

}
