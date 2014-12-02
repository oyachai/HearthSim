package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.MirrorImageMinion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class MirrorImage extends SpellCard {


	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public MirrorImage(boolean hasBeenUsed) {
		super((byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public MirrorImage() {
		this(false);
	}

	
	public Object deepCopy() {
		return new MirrorImage(this.hasBeenUsed);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Summons 2 mirror images
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
		
		int numMinions = PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getNumMinions();
		if (numMinions >= 7)
			return null;

		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			Minion mi0 = new MirrorImageMinion();
			BaseEntity placementTarget = toRet.data_.getCharacter(PlayerSide.CURRENT_PLAYER, numMinions);
			toRet = mi0.summonMinion(side, placementTarget, toRet, deckPlayer0, deckPlayer1, false);
			
			if (numMinions < 6) {
				Minion mi1 = new MirrorImageMinion();
				BaseEntity placementTarget2 = toRet.data_.getCharacter(PlayerSide.CURRENT_PLAYER, numMinions + 1);
				toRet = mi1.summonMinion(side, placementTarget2, toRet, deckPlayer0, deckPlayer1, false);
			}
		}		
		return toRet;
	}

}
