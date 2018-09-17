/* COMP3004 - BlackJack
*  Aidan Crowther, 100980915
*/

package aidancrowther.blackjack;

public class Card {
    
    private String suit;
    private String value;

    //Gnerate a new card using the specified suit and value
    public Card(String suit, String value){
        this.suit = suit;
        this.value = value;
    }

    //Return the cards suit
    public String getSuit(){
        return suit;
    }

    //Return the cards value
    public String getValue(){
        return value;
    }

    //Print the cards suit and value in a human readable format
    @Override
    public String toString(){
        return suit+value;
    }
    
}