package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.MirrorImageMinion;
import com.hearthsim.card.spellcard.SpellCard;
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
		super("Mirror Image", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public MirrorImage() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new MirrorImage(this.hasBeenUsed_);
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
		if (!(targetMinion instanceof Hero) || PlayerSide.WAITING_PLAYER == side) {
			return null;
		}
		
		int numMinions = PlayerSide.CURRENT_PLAYER.getNumMinions();
		if (numMinions >= 7)
			return null;

		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			Minion mi0 = new MirrorImageMinion();
			Minion placementTarget = toRet.data_.getCharacter(toRet.data_.getCurrentPlayer(), numMinions);
			toRet = mi0.summonMinion(side, placementTarget, toRet, deckPlayer0, deckPlayer1, false);
			
			if (numMinions < 6) {
				Minion mi1 = new MirrorImageMinion();
				Minion placementTarget2 = toRet.data_.getCharacter(toRet.data_.getCurrentPlayer(), numMinions + 1);
				toRet = mi1.summonMinion(side, placementTarget2, toRet, deckPlayer0, deckPlayer1, false);
			}
		}		
		return toRet;
	}

}
