package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class MortalCoil extends SpellDamage {

	public MortalCoil() {
		this(false);
	}

	public MortalCoil(boolean hasBeenUsed) {
		super("Mortal Coil", (byte)1, (byte)1, hasBeenUsed);
	}

	@Override
	public Object deepCopy() {
		return new MortalCoil(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 1 damage to a minion.  If the damage kills the minion, draw a card.
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
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (targetMinion instanceof Hero) 
			return null;

		if (this.hasBeenUsed()) {
			//Card is already used, nothing to do
			return null;
		}
				
		this.hasBeenUsed(true);
		HearthTreeNode toRet = boardState;

		toRet = this.attack(targetPlayerIndex, targetMinion, toRet, deckPlayer0, deckPlayer1);
		toRet.data_.setMana_p0(toRet.data_.getMana_p0() - this.mana_);
		toRet.data_.removeCard_hand(this);

        if (!(targetMinion instanceof Hero && targetMinion.getTotalHealth() <= 0)) {
            if (toRet instanceof CardDrawNode) {
                ((CardDrawNode) toRet).addNumCardsToDraw(1);
            } else {
                toRet = new CardDrawNode(toRet, 1); //draw two cards
            }
        }

		return toRet;
	}
}
