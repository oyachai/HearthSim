package com.hearthsim.card.spellcard.concrete;

import java.util.Iterator;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class FanOfKnives extends SpellCard {


	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public FanOfKnives(boolean hasBeenUsed) {
		super("Fan of Knives", (byte)3, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public FanOfKnives() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new FanOfKnives(this.hasBeenUsed_);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This card damages all enemy minions by 1
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
		if (playerIndex == 0) {
			return null;
		}
		
		if (minionIndex > 0) {
			return null;
		}
		
		for (int indx = 0; indx < boardState.data_.getNumMinions_p1(); ++indx) {
			Minion targetMinion = boardState.data_.getMinion_p1(indx);
			targetMinion.takeDamage((byte)1, 0, 1, indx + 1, boardState, deck, true);
		}
		
		Iterator<Minion> iter = boardState.data_.getMinions_p1().iterator();
		while (iter.hasNext()) {
			Minion targetMinion = iter.next();
			if (targetMinion.getHealth() <= 0) {
				iter.remove();
			}
		}

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
