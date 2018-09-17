/* COMP3004 - BlackJack
*  Aidan Crowther, 100980915
*/

package aidancrowther.blackjack;

import java.util.*;

public class Player{
    
    private ArrayList<Card> hand = new ArrayList<>();
    int handValue = 0;
    int acesValue = 0;
    int numAces = 0;
    Boolean dealer = false;

    //Create a new player object
    public Player(Boolean dealer){
        this.dealer = dealer;
    }

    //Give the player a new card, updating the score
    public void giveCard(Card card){

        //Add the card to the hand stack
        hand.add(card);

        //If the card value is an int, increment hand value by that value
        if (card.getValue().matches("[-+]?\\d*\\.?\\d+")){
            handValue += Integer.parseInt(card.getValue());
        }
        //Otherwise, if the value is not A, increment by 10
        else if(!card.getValue().equals("A")){
            handValue += 10;
        }
        //Finally, if the card is an Ace, handle it correctly
        else numAces++;

        //Handle any aces in the hand
        handleAces();
    }

    //Ensure that we dont exceed a maximum hand value with aces
    private void handleAces(){

        //Remove all aces value from the hand
        handValue -= acesValue;
        acesValue = 0;

        //Loop for every ace in the hand
        for(int i=0; i<numAces; i++){
            //Attempt to add the maximum ace value without busting
            if(handValue+11+(numAces-i) <= 21){
                acesValue += 11;
                handValue += 11;
            }
            else{
                acesValue++;
                handValue++;
            }
        }
    }

    //Return the current value of the players hand
    public int getHandValue(){
        return handValue;
    }

    //Return the current size of the players hand
    public int getHandSize(){
        return hand.size();
    }

    //Print the players hand in a readable format
    @Override
    public String toString(){
       String handString = "";

        for(int i=0; i<hand.size(); i++){
            if(i == 0 && !dealer) handString += hand.get(i).toString()+" ";
            else if(i > 0) handString += hand.get(i).toString()+" ";
        }

        return handString;
   }

   //Print the players hand in a readable format and allow overriding a dealer not showing their whole hand
   public String toString(Boolean forceDealer){
    String handString = "";

        for(int i=0; i<hand.size(); i++){
            if(i>0) handString += " ";
            if(i == 0 && (!dealer || forceDealer)) handString += hand.get(i).toString();
            else if(i > 0) handString += hand.get(i).toString();
        }

        return handString;
    }
    
}