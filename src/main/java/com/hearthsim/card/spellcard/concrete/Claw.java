package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Claw extends SpellCard {
	
	private static final byte DAMAGE_AMOUNT = 2;
	private static final byte ARMOR_AMOUNT = 2;

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Claw(boolean hasBeenUsed) {
		super((byte)1, hasBeenUsed);
		
		this.canTargetEnemyHero = false;
		this.canTargetEnemyMinions = false;
		this.canTargetOwnMinions = false;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Claw() {
		this(false);
	}
	
	/**
	 * Claw
	 * 
	 * Gives the hero +2 attack for this turn and +2 armor
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
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			targetMinion.setExtraAttackUntilTurnEnd((byte)(DAMAGE_AMOUNT + targetMinion.getExtraAttackUntilTurnEnd()));
			((Hero)targetMinion).setArmor(ARMOR_AMOUNT);
			this.hasBeenUsed(true);
		}
		return toRet;
	}
}
