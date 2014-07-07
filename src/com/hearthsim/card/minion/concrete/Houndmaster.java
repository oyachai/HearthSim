package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class Houndmaster extends Minion {

	public Houndmaster() {
		this(
				(byte)4,
				(byte)4,
				(byte)3,
				(byte)4,
				(byte)3,
				(byte)3,
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
	
	public Houndmaster(	
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
							boolean isInHand,
							boolean hasBeenUsed) {
		
		super(
			"Houndmaster",
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
			isInHand,
			hasBeenUsed);
	}
	
	/**
	 * 
	 * Override for battlecry
	 * 
	 * Battlecry: Give a friendly beast +2/+2 and Taunt
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode<BoardState> use_core(int thisCardIndex, int playerIndex, int minionIndex, HearthTreeNode<BoardState> boardState, Deck deck) {
		
		if (hasBeenUsed_) {
			//Card is already used, nothing to do
			return null;
		}
		
		if (playerIndex == 1 || minionIndex == 0)
			return null;
		
		if (boardState.data_.getNumMinions_p0() < 7) {

			if (!charge_) {
				hasAttacked_ = true;
			}
			hasBeenUsed_ = true;
			boardState.data_.setMana_p0(boardState.data_.getMana_p0() - this.mana_);
			boardState.data_.removeCard_hand(thisCardIndex);
			boardState.data_.placeMinion_p0(this, minionIndex - 1);
			
			for (int index = 0; index < boardState.data_.getNumMinions_p0(); ++index) {
				if (index != minionIndex - 1) {
					if (boardState.data_.getMinion_p0(index) instanceof Beast) {
						HearthTreeNode<BoardState> newState = boardState.addChild(new HearthTreeNode<BoardState>((BoardState)boardState.data_.deepCopy()));
						newState.data_.getMinion_p0(index).setAttack((byte)(newState.data_.getMinion_p0(index).getAttack() + 2));
						newState.data_.getMinion_p0(index).setHealth((byte)(newState.data_.getMinion_p0(index).getHealth() + 2));
						newState.data_.getMinion_p0(index).setTaunt(true);
					}
					
				}
			}
			return boardState;
							
		} else {
			return null;				
		}

	}

}
