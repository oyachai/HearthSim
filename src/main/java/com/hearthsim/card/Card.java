package com.hearthsim.card;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.HearthAction.Verb;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Card implements DeepCopyable<Card> {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Name of the card
	 */
	protected String name_;

	/**
	 * Mana cost of the card
	 */
	protected byte baseManaCost;
	
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
	public Card(String name, byte baseManaCost, boolean hasBeenUsed, boolean isInHand) {
		this.baseManaCost = baseManaCost;
		this.hasBeenUsed = hasBeenUsed;
		isInHand_ = isInHand;
		name_ = name;
	}

	public Card(byte baseManaCost, boolean hasBeenUsed, boolean isInHand) {
		ImplementedCardList cardList = ImplementedCardList.getInstance();
		ImplementedCardList.ImplementedCard implementedCard = cardList.getCardForClass(this.getClass());
		name_ = implementedCard.name_;
		this.baseManaCost = baseManaCost;
		this.hasBeenUsed = hasBeenUsed;
		isInHand_ = isInHand;
	}

	public Card() {
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
	 * @param side The PlayerSide of the card for which you want the mana cost
	 * @param board The HearthTreeNode representing the current board state
	 * @return Mana cost of the card
	 */
	public final byte getManaCost(PlayerSide side, HearthTreeNode boardState) {
		return getManaCost(side, boardState.data_);
	}

	/**
	 * Get the mana cost of the card
	 * 
	 * @param side The PlayerSide of the card for which you want the mana cost
	 * @param board The BoardModel representing the current board state
	 * 
	 * @return Mana cost of the card
	 */
	public byte getManaCost(PlayerSide side, BoardModel board) {
		return baseManaCost;
	}

	/**
	 * Set the mana cost of the card
	 * 
	 * @param mana The new mana cost
	 */
	public void setBaseManaCost(byte mana) {
		this.baseManaCost = mana;
	}

	/**
	 * Get the base mana cost of the card
	 * 
	 * @return Mana cost of the card
	 */
	public byte getBaseManaCost() {
		return baseManaCost;
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
	 * 
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
	public Card deepCopy() {
		return new Card(this.name_, this.baseManaCost, this.hasBeenUsed, this.isInHand_);
	}

	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}

		if(this.getClass() != other.getClass()) {
			return false;
		}

		// More logic here to be discuss below...
		if(baseManaCost != ((Card)other).baseManaCost)
			return false;

		if(hasBeenUsed != ((Card)other).hasBeenUsed)
			return false;

		if(isInHand_ != ((Card)other).isInHand_)
			return false;

		if(!name_.equals(((Card)other).name_))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = name_ != null ? name_.hashCode() : 0;
		result = 31 * result + baseManaCost;
		result = 31 * result + (hasBeenUsed ? 1 : 0);
		result = 31 * result + (isInHand_ ? 1 : 0);
		return result;
	}

	/**
	 * End the turn and resets the card state
	 * 
	 * This function is called at the end of the turn. Any derived class must override it and remove any temporary buffs that it has.
	 */
	public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0,
			Deck deckPlayer1) throws HSException {
		return boardModel;
	}

	/**
	 * Called at the start of the turn
	 * 
	 * This function is called at the start of the turn. Any derived class must override it to implement whatever "start of the turn" effect the card has.
	 */
	public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0,
			Deck deckPlayer1) throws HSException {
		return boardModel;
	}

	/**
	 * Returns whether this card can be used on the given target or not
	 * 
	 * This function is an optional optimization feature. Some cards in Hearthstone have limited targets; Shadow Bolt cannot be used on heroes, Mind Blast can only target enemy heroes, etc. Even in this situation though, BoardStateFactory
	 * will still try to play non- sensical moves because it doesn't know that such moves are invalid until it tries to play them. The problem is, the BoardStateFactory has to go and make a deep copy of the current BoardState before it can
	 * go and try to play the invalid move, and it turns out that 98% of execution time in HearthSim is BoardStateFactory calling BoardState.deepCopy(). By overriding this function and returning false on appropriate occasions, we can save
	 * some calls to deepCopy and get better performance out of the code.
	 * 
	 * By default, this function returns true, in which case BoardStateFactory will still go and try to use the card.
	 * 
	 *
	 * @param playerSide
	 * @param boardModel
	 * @return
	 */
	public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
		return true;
	}

	public final HearthTreeNode useOn(PlayerSide side, Minion targetMinion, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		int cardIndex = PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getHand().indexOf(this);
		int targetIndex = targetMinion instanceof Hero ? 0 : side.getPlayer(boardState).getMinions()
				.indexOf(targetMinion) + 1;
		HearthTreeNode toRet = this.useOn(side, targetMinion, boardState, deckPlayer0, deckPlayer1, false);
		if(toRet != null) {
			toRet.setAction(new HearthAction(Verb.USE_CARD, PlayerSide.CURRENT_PLAYER, cardIndex, side, targetIndex));
		}
		return toRet;
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
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @param deckPlayer0 The deck for player0
	 * @param deckPlayer1 The deck for player1
	 * @param singleRealizationOnly For cards with random effects, setting this to true will return only a single realization of the random event.
	 *
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode useOn(PlayerSide side, Minion targetMinion, HearthTreeNode boardState, Deck deckPlayer0,
			Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
		// A generic card does nothing except for consuming mana
		if(!this.canBeUsedOn(side, targetMinion, boardState.data_))
			return null;
		
		if (this.getManaCost(PlayerSide.CURRENT_PLAYER, boardState) > boardState.data_.getCurrentPlayer().getMana())
			return null;
		
		HearthTreeNode toRet = this.notifyCardPlayBegin(boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		toRet = this.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);

		if(toRet != null)
			toRet = this.notifyCardPlayResolve(toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);

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
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
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
		boardState.data_.getCurrentPlayer().subtractMana(this.getManaCost(PlayerSide.CURRENT_PLAYER, boardState));
		boardState.data_.removeCard_hand(this);
		return boardState;
	}

	// ======================================================================================
	// Various notifications
	// ======================================================================================
	protected HearthTreeNode notifyCardPlayBegin(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1,
			boolean singleRealizationOnly) throws HSException {
		HearthTreeNode toRet = boardState;
		ArrayList<Minion> tmpList = new ArrayList<Minion>(7);
		for(Card card : toRet.data_.getCurrentPlayerHand()) {
			toRet = card.onCardPlayBegin(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
					deckPlayer0, deckPlayer1, singleRealizationOnly);
		}
		toRet = toRet.data_.getCurrentPlayerHero().onCardPlayBegin(PlayerSide.CURRENT_PLAYER,
				PlayerSide.CURRENT_PLAYER, this, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
		{
			for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
				tmpList.add(minion);
			}
			for(Minion minion : tmpList) {
				if(!minion.isSilenced())
					toRet = minion.onCardPlayBegin(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
							deckPlayer0, deckPlayer1, singleRealizationOnly);
			}
		}
		for(Card card : toRet.data_.getWaitingPlayerHand()) {
			toRet = card.onCardPlayBegin(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
					deckPlayer0, deckPlayer1, singleRealizationOnly);
		}
		toRet = toRet.data_.getWaitingPlayerHero().onCardPlayBegin(PlayerSide.WAITING_PLAYER,
				PlayerSide.CURRENT_PLAYER, this, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
		{
			tmpList.clear();
			for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
				tmpList.add(minion);
			}
			for(Minion minion : tmpList) {
				if(!minion.isSilenced())
					toRet = minion.onCardPlayBegin(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
							deckPlayer0, deckPlayer1, singleRealizationOnly);
			}
		}

		// check for and remove dead minions
		toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		return toRet;
	}

	protected HearthTreeNode notifyCardPlayResolve(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1,
			boolean singleRealizationOnly) throws HSException {
		HearthTreeNode toRet = boardState;
		ArrayList<Minion> tmpList = new ArrayList<Minion>(7);
		for(Card card : toRet.data_.getCurrentPlayerHand()) {
			toRet = card.onCardPlayResolve(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
					deckPlayer0, deckPlayer1, singleRealizationOnly);
		}
		toRet = toRet.data_.getCurrentPlayerHero().onCardPlayResolve(PlayerSide.CURRENT_PLAYER,
				PlayerSide.CURRENT_PLAYER, this, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
		{
			for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
				tmpList.add(minion);
			}
			for(Minion minion : tmpList) {
				if(!minion.isSilenced())
					toRet = minion.onCardPlayResolve(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
							deckPlayer0, deckPlayer1, singleRealizationOnly);
			}
		}
		for(Card card : toRet.data_.getWaitingPlayerHand()) {
			toRet = card.onCardPlayResolve(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
					deckPlayer0, deckPlayer1, singleRealizationOnly);
		}
		toRet = toRet.data_.getWaitingPlayerHero().onCardPlayResolve(PlayerSide.WAITING_PLAYER,
				PlayerSide.CURRENT_PLAYER, this, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
		{
			tmpList.clear();
			for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
				tmpList.add(minion);
			}
			for(Minion minion : tmpList) {
				if(!minion.isSilenced())
					toRet = minion.onCardPlayResolve(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
							deckPlayer0, deckPlayer1, singleRealizationOnly);
			}
		}

		// check for and remove dead minions
		toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		return toRet;
	}

	// ======================================================================================
	// Hooks for various events
	// ======================================================================================
	/**
	 * 
	 * Called whenever another card is played, before the card is actually played
	 * 
	 * @param thisCardPlayerSide The player index of the card receiving the event
	 * @param cardUserPlayerSide
	 * @param usedCard The card that was used
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer1 The deck of player1
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard,
			HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly)
			throws HSException {
		return boardState;
	}

	/**
	 * 
	 * Called whenever another card is played, after it is actually played
	 * 
	 * @param thisCardPlayerSide The player index of the card receiving the event
	 * @param cardUserPlayerSide
	 * @param usedCard The card that was used
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer1 The deck of player1
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode onCardPlayResolve(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide,
			Card usedCard, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly)
			throws HSException {
		return boardState;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("name", name_);
		json.put("mana", baseManaCost);
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
