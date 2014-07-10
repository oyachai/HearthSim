package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class RockbiterWeapon extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public RockbiterWeapon(boolean hasBeenUsed) {
		super("Rockbiter Weapon", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public RockbiterWeapon() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new RockbiterWeapon(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Gives a minion +4/+4
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
		
		HearthTreeNode<BoardState> toRet = boardState;
		if (minionIndex == 0) {
			toRet.data_.getHero(playerIndex).setExtraAttackUntilTurnEnd((byte)(3 + toRet.data_.getHero(playerIndex).getExtraAttackUntilTurnEnd()));
		} else {
			toRet.data_.getMinion(playerIndex, minionIndex - 1).setExtraAttackUntilTurnEnd((byte)(3 + toRet.data_.getMinion(playerIndex, minionIndex - 1).getExtraAttackUntilTurnEnd()));
		}
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
