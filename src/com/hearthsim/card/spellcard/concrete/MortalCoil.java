package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
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
	 * Deals 2 damage and heals the hero for 2.
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
		if (minionIndex == 0) 
			return null;

		if (this.hasBeenUsed()) {
			//Card is already used, nothing to do
			return null;
		}
				
		this.hasBeenUsed(true);


		Minion target = null;
		if (playerIndex == 0) {
			if (boardState.data_.getNumMinions_p0() + 1 > minionIndex)
				target = boardState.data_.getMinion_p0(minionIndex - 1);
			else
				return null;
		} else {
			if (boardState.data_.getNumMinions_p1() + 1 > minionIndex)
				target = boardState.data_.getMinion_p1(minionIndex - 1);
			else
				return null;
		}
		
		this.attack(target, playerIndex, minionIndex, boardState, deck);
		boardState.data_.setMana_p0(boardState.data_.getMana_p0() - this.mana_);
		boardState.data_.removeCard_hand(thisCardIndex);

		if (target.getHealth() <= 0) {
			Card card = deck.drawCard(boardState.data_.getDeckPos_p0());
			if (card == null) {
				byte fatigueDamage = boardState.data_.getFatigueDamage_p0();
				boardState.data_.setFatigueDamage_p0((byte)(fatigueDamage + 1));
				boardState.data_.getHero_p0().setHealth((byte)(boardState.data_.getHero_p0().getHealth() - fatigueDamage));
			} else {
				boardState.data_.placeCard_hand_p0(card);
				boardState.data_.setDeckPos_p0(boardState.data_.getDeckPos_p0() + 1);
			}
			if (playerIndex == 0)
				boardState.data_.removeMinion_p0(minionIndex-1);
			else
				boardState.data_.removeMinion_p1(minionIndex-1);
		}
		return boardState;
	}
}
