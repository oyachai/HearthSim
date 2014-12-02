package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Bite extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Bite(boolean hasBeenUsed) {
		super((byte)4, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Bite() {
		this(false);
	}

	
	public Object deepCopy() {
		return new Bite(this.hasBeenUsed);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Gives a minion +4/+4
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
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (isNotHero(targetMinion) || isWaitingPlayer(side)) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			Hero hero = (Hero)targetMinion;
			hero.setExtraAttackUntilTurnEnd((byte)(hero.getExtraAttackUntilTurnEnd() + 4));
			hero.setArmor((byte)(hero.getArmor() + 4));
		}
		return toRet;
	}
}
