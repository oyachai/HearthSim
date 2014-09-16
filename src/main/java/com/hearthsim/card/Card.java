package com.hearthsim.card;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Card implements DeepCopyable {
    protected final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Name of the card
	 */
	protected String name_;
	
	/**
	 * Mana cost of the card
	 */
	protected byte mana_;
	
	protected boolean hasBeenUsed;
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
		this.hasBeenUsed = hasBeenUsed;
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
		return hasBeenUsed;
	}
	
	/**
	 * Sets whether the card has been used or not
	 * @param value The new hasBeenUsed value
	 */
	public void hasBeenUsed(boolean value) {
		hasBeenUsed = value;
	}
	
	public void isInHand(boolean value) {
		isInHand_ = value;
	}
	
	public boolean isInHand() {
		return isInHand_;
	}
	
	@Override
	public Object deepCopy() {
		return new Card(this.name_, this.mana_, this.hasBeenUsed, this.isInHand_);
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
	   
	   if (hasBeenUsed != ((Card)other).hasBeenUsed)
		   return false;

	   if (isInHand_ != ((Card)other).isInHand_)
		   return false;
	   
	   if (!name_.equals(((Card)other).name_))
		   return false;

	   return true;
	}

    @Override
    public int hashCode() {
        int result = name_ != null ? name_.hashCode() : 0;
        result = 31 * result + (int) mana_;
        result = 31 * result + (hasBeenUsed ? 1 : 0);
        result = 31 * result + (isInHand_ ? 1 : 0);
        return result;
    }

    /**
	 * End the turn and resets the card state
	 * 
	 * This function is called at the end of the turn.  Any derived class must override it and remove any 
	 * temporary buffs that it has.
	 */
	public BoardModel endTurn(PlayerSide thisCardPlayerSide, BoardModel boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		return boardModel;
	}
	
	/**
	 * Called at the start of the turn
	 * 
	 * This function is called at the start of the turn.  Any derived class must override it to implement whatever
	 * "start of the turn" effect the card has.
	 */
	public BoardModel startTurn(PlayerSide thisCardPlayerSide, BoardModel boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		return boardModel;
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
	 *
     * @param playerSide
     * @param boardModel
* @return
     */
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        return true;
    }

        
	public final HearthTreeNode useOn(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		return this.useOn(side, targetMinion, boardState, deckPlayer0, deckPlayer1, false);
	}    
    
	/**
	 * 
	 * Use the card on the given target
	 * 
	 *
     *
     *
     * @param side
     * @param targetMinion The target minion (can be a Hero)
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck for player0
     * @param deckPlayer1 The deck for player1
     * @param singleRealizationOnly For cards with random effects, setting this to true will return only a single realization of the random event.
     *
     * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode useOn(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		//A generic card does nothing except for consuming mana
		HearthTreeNode toRet = this.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);

		//Notify all other cards/characters of the card's use
		if (toRet != null) {
			ArrayList<Minion> tmpList = new ArrayList<Minion>(7);
            for (Card card : toRet.data_.getCurrentPlayerHand()) {
                toRet = card.otherCardUsedEvent(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet, deckPlayer0, deckPlayer1);
            }
			toRet = toRet.data_.getCurrentPlayerHero().otherCardUsedEvent(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet, deckPlayer0, deckPlayer1);
			{
                for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
                    tmpList.add(minion);
                }
				for (Minion minion : tmpList) {
					if (!minion.isSilenced())
						toRet = minion.otherCardUsedEvent(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet, deckPlayer0, deckPlayer1);
				}
			}
            for (Card card : toRet.data_.getWaitingPlayerHand()) {
                toRet = card.otherCardUsedEvent(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet, deckPlayer0, deckPlayer1);
            }
			toRet = toRet.data_.getWaitingPlayerHero().otherCardUsedEvent(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet, deckPlayer0, deckPlayer1);
			{
				tmpList.clear();
                for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
                    tmpList.add(minion);
                }
				for (Minion minion : tmpList) {
					if (!minion.isSilenced())
						toRet = minion.otherCardUsedEvent(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet, deckPlayer0, deckPlayer1);
				}
			}

			//check for and remove dead minions
			toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);			
		}
		
		
		return toRet;
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This is the core implementation of card's ability
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	protected HearthTreeNode use_core(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		//A generic card does nothing except for consuming mana
		boardState.data_.setMana_p0(boardState.data_.getMana_p0() - this.mana_);
		boardState.data_.removeCard_hand(this);
		return boardState;
	}
	

	//======================================================================================
	// Hooks for various events
	//======================================================================================	
	/**
	 * 
	 * Called whenever another card is used
	 *  @param thisCardPlayerSide The player index of the card receiving the event
	 * @param cardUserPlayerSide
     * @param usedCard The card that was used
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @param deckPlayer1 The deck of player1
     * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode otherCardUsedEvent(
			PlayerSide thisCardPlayerSide,
			PlayerSide cardUserPlayerSide,
			Card usedCard,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		return boardState;
	}

	
	
	
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("name", name_);
		json.put("mana", mana_);
		json.put("hasBeenUsed", hasBeenUsed);
		return json;
	}
	
        @Override
	public String toString() {
		return this.toJSON().toString();
	}

    public boolean isWaitingPlayer(PlayerSide side) {
        return PlayerSide.WAITING_PLAYER == side;
    }

    protected boolean isNotHero(Minion targetMinion) {
        return !isHero(targetMinion);
    }

    protected boolean isCurrentPlayer(PlayerSide side) {
        return PlayerSide.CURRENT_PLAYER == side;
    }

    protected boolean isHero(Minion targetMinion) {
        return targetMinion instanceof Hero;
    }
}



