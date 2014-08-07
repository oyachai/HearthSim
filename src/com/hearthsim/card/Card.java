package com.hearthsim.card;

import java.util.ArrayList;
import java.util.Iterator;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.tree.HearthTreeNode;
import com.json.*;

public class Card implements DeepCopyable {

	/**
	 * Name of the card
	 */
	protected String name_;
	
	/**
	 * Mana cost of the card
	 */
	protected byte mana_;
	
	protected boolean hasBeenUsed_;
	protected boolean isInHand_;

	/**
	 * Constructor
	 * 
	 * @param name Name of the card
	 * @param mana Mana cost of the card
	 * @param hasBeenUsed Has the card been used?
	 * @param isInHand Is the card in your hand?
	 */
	public Card(String name, byte mana, boolean hasBeenUsed, boolean isInHand) {
		name_ = name;
		mana_ = mana;
		hasBeenUsed_ = hasBeenUsed;
		isInHand_ = isInHand;
	}

	/**
	 * Constructor
	 * 
	 * @param name Name of the card
	 * @param mana Mana cost of the card
	 */
	public Card(String name, byte mana) {
		this(name, mana, true, true);
	}
	
	/**
	 * Get the name of the card
	 * 
	 * @return Name of the card
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Get the mana cost of the card
	 * 
	 * @return Mana cost of the card
	 */
	public byte getMana() {
		return mana_;
	}
	
	/**
	 * Set the mana cost of the card
	 * @param mana The new mana cost
	 */
	public void setMana(byte mana) {
		mana_ = mana;
	}
	
	/**
	 * Returns whether the card has been used or not
	 * 
	 * @return
	 */
	public boolean hasBeenUsed() {
		return hasBeenUsed_;
	}
	
	/**
	 * Sets whether the card has been used or not
	 * @param value The new hasBeenUsed value
	 */
	public void hasBeenUsed(boolean value) {
		hasBeenUsed_ = value;
	}
	
	public void isInHand(boolean value) {
		isInHand_ = value;
	}
	
	public boolean isInHand() {
		return isInHand_;
	}
	
	@Override
	public Object deepCopy() {
		return new Card(this.name_, this.mana_, this.hasBeenUsed_, this.isInHand_);
	}
	
	@Override
	public boolean equals(Object other)
	{
	   if (other == null)
	   {
	      return false;
	   }

	   if (this.getClass() != other.getClass())
	   {
	      return false;
	   }

	   // More logic here to be discuss below...
	   if (mana_ != ((Card)other).mana_)
		   return false;
	   
	   if (hasBeenUsed_ != ((Card)other).hasBeenUsed_)
		   return false;

	   if (isInHand_ != ((Card)other).isInHand_)
		   return false;
	   
	   if (!name_.equals(((Card)other).name_))
		   return false;

	   return true;
	}

	/**
	 * End the turn and resets the card state
	 * 
	 * This function is called at the end of the turn.  Any derived class must override it and remove any 
	 * temporary buffs that it has.
	 */
	public BoardState endTurn(int thisCardPlayerIndex, BoardState boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		return boardState;
	}
	
	/**
	 * Called at the start of the turn
	 * 
	 * This function is called at the start of the turn.  Any derived class must override it to implement whatever
	 * "start of the turn" effect the card has.
	 */
	public BoardState startTurn(int thisCardPlayerIndex, BoardState boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		return boardState;
	}

    /**
     * Returns whether this card can be used on the given target or not
     * 
     * This function is an optional optimization feature.  Some cards in 
     * Hearthstone have limited targets; Shadow Bolt cannot be used on
     * heroes, Mind Blast can only target enemy heroes, etc.  Even in this 
     * situation though, BoardStateFactory will still try to play non-
     * sensical moves because it doesn't know that such moves are invalid
     * until it tries to play them.  The problem is, the BoardStateFactory
     * has to go and make a deep copy of the current BoardState before it 
     * can go and try to play the invalid move, and it turns out that 
     * 98% of execution time in HearthSim is BoardStateFactory calling 
     * BoardState.deepCopy().  By overriding this function and returning 
     * false on appropriate occasions, we can save some calls to deepCopy
     * and get better performance out of the code.  
     * 
     * By default, this function returns true, in which case 
     * BoardStateFactory will still go and try to use the card.
     * 
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
     * @return 
     */
    public boolean canBeUsedOn(int playerIndex, Minion minion) {
        return true;
    }

        
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * @param targetPlayerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param targetMinion The target minion (can be a Hero)
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode useOn(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		//A generic card does nothing except for consuming mana
		HearthTreeNode toRet = this.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);

		//Notify all other cards/characters of the card's use
		if (toRet != null) {
			for (Iterator<Card> iter = toRet.data_.getCards_hand_p0().iterator(); iter.hasNext();) {
				toRet = (iter.next()).otherCardUsedEvent(toRet, deckPlayer0, deckPlayer1);
			}
			toRet = toRet.data_.getHero_p0().otherCardUsedEvent(toRet, deckPlayer0, deckPlayer1);
			for (Iterator<Minion> iter = toRet.data_.getMinions_p0().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.isSilenced())
					toRet = minion.otherCardUsedEvent(toRet, deckPlayer0, deckPlayer1);
			}
			toRet = toRet.data_.getHero_p1().otherCardUsedEvent(toRet, deckPlayer0, deckPlayer1);
			for (Iterator<Minion> iter = toRet.data_.getMinions_p1().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.isSilenced())
					toRet = minion.otherCardUsedEvent(toRet, deckPlayer0, deckPlayer1);
			}

			//check for and remove dead minions
			Iterator<Minion> iter0 = toRet.data_.getMinions_p0().iterator();
			ArrayList<Minion> listOfMinionsToCheck0 = new ArrayList<Minion>(toRet.data_.getNumMinions_p0());
			while (iter0.hasNext()) {
				listOfMinionsToCheck0.add(iter0.next());
			}
			for (Minion tMinion : listOfMinionsToCheck0) {
				if (tMinion.getHealth() <= 0) {
					toRet = tMinion.destroyed(0, toRet, deckPlayer0, deckPlayer1);
					toRet.data_.getMinions_p0().remove(tMinion);
				}
			}

			Iterator<Minion> iter1 = toRet.data_.getMinions_p1().iterator();
			ArrayList<Minion> listOfMinionsToCheck1 = new ArrayList<Minion>(toRet.data_.getNumMinions_p1());
			while (iter1.hasNext()) {
				listOfMinionsToCheck1.add(iter1.next());
			}
			for (Minion tMinion : listOfMinionsToCheck1) {
				if (tMinion.getHealth() <= 0) {
					toRet = tMinion.destroyed(1, toRet, deckPlayer0, deckPlayer1);
					toRet.data_.getMinions_p1().remove(tMinion);
				}
			}
		}
		
		
		return toRet;
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This is the core implementation of card's ability
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	protected HearthTreeNode use_core(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		//A generic card does nothing except for consuming mana
		boardState.data_.setMana_p0(boardState.data_.getMana_p0() - this.mana_);
		boardState.data_.removeCard_hand(this);
		return boardState;
	}
	
	
	/**
	 * 
	 * Get the expected value of the card if used on the position
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deck The initiating player's deck
	 * @param scoreFunc The scoring function
	 * 
	 * @return The expected score
	 */
	public double getExpectedScoreIfUsed(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deck,
			ArtificialPlayer ai)
		throws HSInvalidPlayerIndexException
	{
		return 0.0;
	}
	
	
	//======================================================================================
	// Hooks for various events
	//======================================================================================	
	/**
	 * 
	 * Called whenever another card is used
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode otherCardUsedEvent(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) {
		return boardState;
	}

	
	
	
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("name", name_);
		json.put("mana", mana_);
		json.put("hasBeenUsed", hasBeenUsed_);
		return json;
	}
	
        @Override
	public String toString() {
		return this.toJSON().toString();
	}
}



