package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionStateFactory;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Charge extends SpellCard {
	
	MinionStateFactory mf = new MinionStateFactory();
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Charge(boolean hasBeenUsed) {
		super((byte)3, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Charge() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new Charge(this.hasBeenUsed);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This card gives the target +2 attack and Charge.
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (isHero(targetMinion) || isWaitingPlayer(side)) {
			//cant't use it on the heroes or enemy minions
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			targetMinion.setAttack((byte)(targetMinion.getAttack() + 2));
			//targetMinion.setCharge(true);
			targetMinion.addState(mf.makeCharge());
		}
		return toRet;
	}
}
