package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class Claw extends SpellCard {
	
	private static final byte DAMAGE_AMOUNT = 2;
	private static final byte ARMOR_AMOUNT = 2;

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Claw(boolean hasBeenUsed) {
		super("Claw", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Claw() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new Claw(this.hasBeenUsed_);
	}

	/**
	 * Claw
	 * 
	 * Gives the hero +2 attack for this turn and +2 armor
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
			toRet.data_.getHero_p0().setArmor(ARMOR_AMOUNT);
			this.hasBeenUsed(true);
		}
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, toRet, deck);
	}
}
