package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class PowerWordShield extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public PowerWordShield(boolean hasBeenUsed) {
		super("Power Word: Shield", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public PowerWordShield() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new PowerWordShield(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Gives a minion +4/+4
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		if (minionIndex == 0) {
			return null;
		}
		
		boardState.data_.getMinion(playerIndex, minionIndex - 1).setHealth((byte)(boardState.data_.getMinion(playerIndex, minionIndex - 1).getHealth() + 2));
		boardState.data_.getMinion(playerIndex, minionIndex - 1).setMaxHealth((byte)(boardState.data_.getMinion(playerIndex, minionIndex - 1).getMaxHealth() + 2));
		
		
		Card card = deck.drawCard(boardState.data_.getDeckPos_p0());
		if (card == null) {
			byte fatigueDamage = boardState.data_.getFatigueDamage_p0();
			boardState.data_.setFatigueDamage_p0((byte)(fatigueDamage + 1));
			boardState.data_.getHero_p0().setHealth((byte)(boardState.data_.getHero_p0().getHealth() - fatigueDamage));
		} else {
			boardState.data_.placeCard_hand_p0(card);
			boardState.data_.setDeckPos_p0(boardState.data_.getDeckPos_p0() + 1);
		}
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
