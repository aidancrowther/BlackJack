/* COMP3004 - BlackJack
*  Aidan Crowther, 100980915
*/

package aidancrowther.blackjack;

import java.util.*;

public class Deck{

    private Stack<Card> deck = new Stack<>();

    public Deck(){
        String suits[] = {"S", "C", "D", "H"};
        String values[] = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for(String suit : suits){
            for(String value : values){
                deck.push(new Card(suit, value));
            }
        }
    }

    public Card pop(){
        return deck.pop();
    }
    
    public Boolean isEmpty(){
        return deck.isEmpty();
    }

    public void shuffle(){
        Collections.shuffle(deck);
    }

    public Stack<Card> getDeck(){
        return deck;
    }

}
