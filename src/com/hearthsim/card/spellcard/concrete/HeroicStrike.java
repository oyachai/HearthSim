package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class HeroicStrike extends SpellCard {
	
	private static final byte DAMAGE_AMOUNT = 3;

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public HeroicStrike(boolean hasBeenUsed) {
		super("Heroic Strike", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public HeroicStrike() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new HeroicStrike(this.hasBeenUsed_);
	}
	
	/**
	 * Heroic Strike
	 * 
	 * Gives the hero +4 attack this turn
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode<BoardState> use_core(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		if (this.hasBeenUsed()) {
			//Card is already used, nothing to do
			return null;
		}
				
		if (playerIndex == 1 || minionIndex > 0) {
			return null;
		}

		
		HearthTreeNode<BoardState> toRet = boardState;
		if (toRet != null) {
			toRet.data_.getHero_p0().setTemporaryAttackDamage((byte)(DAMAGE_AMOUNT + toRet.data_.getHero_p0().getTemporaryAttackDamage()));
			this.hasBeenUsed(true);
		}
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, toRet, deck);
	}
}
