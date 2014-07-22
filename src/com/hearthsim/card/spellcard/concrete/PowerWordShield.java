package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.CardDrawNode;
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
			Deck deckPlayer0, Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		if (minionIndex == 0) {
			return null;
		}
		HearthTreeNode toRet = super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deckPlayer0, deckPlayer1);

		toRet.data_.getMinion(playerIndex, minionIndex - 1).setHealth((byte)(toRet.data_.getMinion(playerIndex, minionIndex - 1).getHealth() + 2));
		toRet.data_.getMinion(playerIndex, minionIndex - 1).setMaxHealth((byte)(toRet.data_.getMinion(playerIndex, minionIndex - 1).getMaxHealth() + 2));
		
		CardDrawNode cNode = new CardDrawNode(toRet, 4, this, 0, thisCardIndex, playerIndex, minionIndex); //draw two cards
		return cNode;
	}
}
