/* COMP3004 - BlackJack
*  Aidan Crowther, 100980915
*/

package aidancrowther.blackjack;

import java.util.*;

public class Player{
    
    private ArrayList<Card> hand = new ArrayList<>();
    Boolean dealer = false;

    public Player(Boolean dealer){
        this.dealer = dealer;
    }

    public void giveCard(Card card){
        hand.add(card);
    }

    public int getHandSize(){
        return hand.size();
    }

    @Override
    public String toString(){
       String handString = "";

        for(int i=0; i<hand.size(); i++){
            if(i == 0 && !dealer) handString += hand.get(i).toString()+" ";
            else if(i > 0) handString += hand.get(i).toString()+" ";
        }

        return handString;
   }

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