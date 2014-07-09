package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class AcidicSwampOoze extends Minion {

	public AcidicSwampOoze() {
		this(
				(byte)2,
				(byte)3,
				(byte)2,
				(byte)3,
				(byte)2,
				(byte)2,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				true,
				false
			);
	}
	
	public AcidicSwampOoze(	
							byte mana,
							byte attack,
							byte health,
							byte baseAttack,
							byte baseHealth,
							byte maxHealth,
							boolean taunt,
							boolean divineShield,
							boolean windFury,
							boolean charge,
							boolean hasAttacked,
							boolean hasWindFuryAttacked,
							boolean frozen,
							boolean summoned,
							boolean transformed,
							boolean isInHand,
							boolean hasBeenUsed) {
		
		super(
			"Acidic Swamp Ooze",
			mana,
			attack,
			health,
			baseAttack,
			baseHealth,
			maxHealth,
			taunt,
			divineShield,
			windFury,
			charge,
			hasAttacked,
			hasWindFuryAttacked,
			frozen,
			summoned,
			transformed,
			isInHand,
			hasBeenUsed);
	}
	

	/**
	 * 
	 * Override for battlecry
	 * 
	 * Battlecry: Destroy your opponent's weapon
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode<BoardState> use_core(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		
		if (hasBeenUsed_) {
			//Card is already used, nothing to do
			return null;
		}
		
		if (playerIndex == 1 || minionIndex == 0)
			return null;
		
		if (boardState.data_.getNumMinions_p0() >= 7)
			return null;
		
		if (boardState.data_.getHero_p1().getWeaponCharge() > 0) {
			boardState.data_.getHero_p1().setWeaponCharge((byte)0);
			boardState.data_.getHero_p1().setAttack((byte)0);
		}
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}

}
