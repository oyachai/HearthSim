package com.hearthsim.card.spellcard.concrete;


import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SavageRoar extends SpellCard {


	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public SavageRoar(boolean hasBeenUsed) {
		super((byte)3, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public SavageRoar() {
		this(false);
	}

	
	public Object deepCopy() {
		return new SavageRoar(this.hasBeenUsed);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Gives all friendly characters +2 attack for this turn
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
		toRet.data_.getCurrentPlayerHero().setExtraAttackUntilTurnEnd((byte)2);
		for (BaseEntity minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions())
			minion.setExtraAttackUntilTurnEnd((byte)2);
		
		return toRet;
	}
}
