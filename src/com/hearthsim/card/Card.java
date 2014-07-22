package com.hearthsim.card;

import java.util.Iterator;

import com.hearthsim.card.minion.Minion;
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
	public BoardState endTurn(int thisCardPlayerIndex, int thisCardIndex, BoardState boardState, Deck deck) throws HSInvalidPlayerIndexException {
		return boardState;
	}
	
	/**
	 * Called at the start of the turn
	 * 
	 * This function is called at the start of the turn.  Any derived class must override it to implement whatever
	 * "start of the turn" effect the card has.
	 */
	public BoardState startTurn(int thisCardPlayerIndex, int thisCardIndex, BoardState boardState, Deck deck) throws HSInvalidPlayerIndexException {
		return boardState;
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode useOn(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		//A generic card does nothing except for consuming mana
		HearthTreeNode toRet = this.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);

		//Notify all other cards/characters of the card's use
		if (toRet != null) {
			for (int j = 0; j < toRet.data_.getNumCards_hand(); ++j) {
				toRet = toRet.data_.getCard_hand_p0(j).otherCardUsedEvent(j, toRet, deck);
			}
			toRet = toRet.data_.getHero_p0().otherCardUsedEvent(0, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
				toRet = toRet.data_.getMinion_p0(j).otherCardUsedEvent(j, toRet, deck);
			}
			toRet = toRet.data_.getHero_p1().otherCardUsedEvent(0, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
				toRet = toRet.data_.getMinion_p1(j).otherCardUsedEvent(j, toRet, deck);
			}
		}
		
		//check for and remove dead minions
		Iterator<Minion> iter0 = boardState.data_.getMinions_p0().iterator();
		while (iter0.hasNext()) {
			Minion targetMinion = iter0.next();
			if (targetMinion.getHealth() <= 0) {
				iter0.remove();
			}
		}
		Iterator<Minion> iter1 = boardState.data_.getMinions_p1().iterator();
		while (iter1.hasNext()) {
			Minion targetMinion = iter1.next();
			if (targetMinion.getHealth() <= 0) {
				iter1.remove();
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
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		//A generic card does nothing except for consuming mana
		boardState.data_.setMana_p0(boardState.data_.getMana_p0() - this.mana_);
		boardState.data_.removeCard_hand(thisCardIndex);
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
	public HearthTreeNode otherCardUsedEvent(int thisCardIndex, HearthTreeNode boardState, Deck deck) {
		return boardState;
	}

	
	
	
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("type", "Card");
		json.put("name", name_);
		json.put("mana", mana_);
		json.put("hasBeenUsed", hasBeenUsed_);
		return json;
	}
	
	public String toString() {
		return this.toJSON().toString();
	}
}



