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
	@Deprecated
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

	// This deepCopy pattern is required because we use the class of each card to recreate it under certain circumstances
	@Override
	public Card deepCopy() {
		Card copy = null;
		try {
			copy = getClass().newInstance();
		} catch(InstantiationException e) {
			log.error("instantiation error", e);
		} catch(IllegalAccessException e) {
			log.error("illegal access error", e);
		}
		if(copy == null) {
			throw new RuntimeException("unable to instantiate card.");
		}

		copy.name_ = this.name_;
		copy.baseManaCost = this.baseManaCost;
		copy.hasBeenUsed = this.hasBeenUsed;
		copy.isInHand_ = this.isInHand_;

		return copy;
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
	 * Returns whether this card can be used on the given target or not
	 * 
	 * This function is an optional optimization feature. Some cards in Hearthstone have limited targets; Shadow Bolt cannot be used on heroes, Mind Blast can only target enemy heroes, etc. Even in this situation though, BoardStateFactory
	 * will still try to play non- sensical moves because it doesn't know that such moves are invalid until it tries to play them. The problem is, the BoardStateFactory has to go and make a deep copy of the current BoardState before it can
	 * go and try to play the invalid move, and it turns out that 98% of execution time in HearthSim is BoardStateFactory calling BoardState.deepCopy(). By overriding this function and returning false on appropriate occasions, we can save
	 * some calls to deepCopy and get better performance out of the code.
	 * 
	 * By default, this function only checks mana cost.
	 *
	 * @param playerSide
	 * @param boardModel
	 * @return
	 */
	public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
		// A generic card does nothing except for consuming mana
		return this.getManaCost(PlayerSide.CURRENT_PLAYER, boardModel) <= boardModel.getCurrentPlayer().getMana();
	}

	public final HearthTreeNode useOn(PlayerSide side, Minion targetMinion, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		return this.useOn(side, targetMinion, boardState, deckPlayer0, deckPlayer1, false);
	}

	public HearthTreeNode useOn(PlayerSide side, int targetIndex, HearthTreeNode boardState, Deck deckPlayer0,
			Deck deckPlayer1) throws HSException {
		return this.useOn(side, targetIndex, boardState, deckPlayer0, deckPlayer1, false);
	}

	public HearthTreeNode useOn(PlayerSide side, int targetIndex, HearthTreeNode boardState, Deck deckPlayer0,
			Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
		Minion target = boardState.data_.getCharacter(side, targetIndex);
		return this.useOn(side, target, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
	}

	/**
	 * Use the card on the given target
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
		if(!this.canBeUsedOn(side, targetMinion, boardState.data_))
			return null;
		
		// Need to record card and target index *before* the board state changes
		int cardIndex = PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getHand().indexOf(this);
		int targetIndex = targetMinion instanceof Hero ? 0 : side.getPlayer(boardState).getMinions()
				.indexOf(targetMinion) + 1;

		HearthTreeNode toRet = this.notifyCardPlayBegin(boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if(toRet != null) {
			toRet = this.use_core(side, targetMinion, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
		}

		if(toRet != null) {
			toRet = this.notifyCardPlayResolve(toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
		}

		if(toRet != null) {
			toRet.setAction(new HearthAction(Verb.USE_CARD, PlayerSide.CURRENT_PLAYER, cardIndex, side, targetIndex));
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
		boardState.data_.getCurrentPlayer().subtractMana(this.getManaCost(PlayerSide.CURRENT_PLAYER, boardState.data_));
		boardState.data_.removeCard_hand(this);
		return boardState;
	}

	// ======================================================================================
	// Various notifications
	// ======================================================================================
	protected HearthTreeNode notifyCardPlayBegin(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1,
			boolean singleRealizationOnly) throws HSException {
		HearthTreeNode toRet = boardState;
		ArrayList<CardPlayBeginInterface> matches = new ArrayList<CardPlayBeginInterface>();

		for(Card card : toRet.data_.getCurrentPlayerHand()) {
			if(card instanceof CardPlayBeginInterface) {
				matches.add((CardPlayBeginInterface)card);
			}
		}

		Card hero = toRet.data_.getCurrentPlayerHero();
		if(hero instanceof CardPlayBeginInterface) {
			matches.add((CardPlayBeginInterface)hero);
		}

		for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof CardPlayBeginInterface) {
				matches.add((CardPlayBeginInterface)minion);
			}
		}

		for(CardPlayBeginInterface match : matches) {
			toRet = match.onCardPlayBegin(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
					deckPlayer0, deckPlayer1, singleRealizationOnly);
		}
		matches.clear();

		for(Card card : toRet.data_.getWaitingPlayerHand()) {
			if(card instanceof CardPlayBeginInterface) {
				matches.add((CardPlayBeginInterface)card);
			}
		}

		hero = toRet.data_.getWaitingPlayerHero();
		if(hero instanceof CardPlayBeginInterface) {
			matches.add((CardPlayBeginInterface)hero);
		}

		for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof CardPlayBeginInterface) {
				matches.add((CardPlayBeginInterface)minion);
			}
		}

		for(CardPlayBeginInterface match : matches) {
			toRet = match.onCardPlayBegin(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
					deckPlayer0, deckPlayer1, singleRealizationOnly);
		}

		// check for and remove dead minions
		toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		return toRet;
	}

	protected HearthTreeNode notifyCardPlayResolve(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1,
			boolean singleRealizationOnly) throws HSException {
		HearthTreeNode toRet = boardState;
		ArrayList<CardPlayAfterInterface> matches = new ArrayList<CardPlayAfterInterface>();

		for(Card card : toRet.data_.getCurrentPlayerHand()) {
			if(card instanceof CardPlayAfterInterface) {
				matches.add((CardPlayAfterInterface)card);
			}
		}

		Card hero = toRet.data_.getCurrentPlayerHero();
		if(hero instanceof CardPlayAfterInterface) {
			matches.add((CardPlayAfterInterface)hero);
		}

		for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof CardPlayAfterInterface) {
				matches.add((CardPlayAfterInterface)minion);
			}
		}

		for(CardPlayAfterInterface match : matches) {
			toRet = match.onCardPlayResolve(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
					deckPlayer0, deckPlayer1, singleRealizationOnly);
		}
		matches.clear();

		for(Card card : toRet.data_.getWaitingPlayerHand()) {
			if(card instanceof CardPlayAfterInterface) {
				matches.add((CardPlayAfterInterface)card);
			}
		}

		hero = toRet.data_.getWaitingPlayerHero();
		if(hero instanceof CardPlayAfterInterface) {
			matches.add((CardPlayAfterInterface)hero);
		}

		for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof CardPlayAfterInterface) {
				matches.add((CardPlayAfterInterface)minion);
			}
		}

		for(CardPlayAfterInterface match : matches) {
			toRet = match.onCardPlayResolve(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet,
					deckPlayer0, deckPlayer1, singleRealizationOnly);
		}

		// check for and remove dead minions
		toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		return toRet;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("name", name_);
		json.put("mana", baseManaCost);
		if(hasBeenUsed) json.put("hasBeenUsed", hasBeenUsed);
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
