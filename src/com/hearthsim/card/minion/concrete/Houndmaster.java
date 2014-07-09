package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class Houndmaster extends Minion {

	private static final String NAME = "Houndmaster";
	private static final byte MANA_COST = 4;
	private static final byte ATTACK = 4;
	private static final byte HEALTH = 3;
	
	public Houndmaster() {
		this(
				MANA_COST,
				ATTACK,
				HEALTH,
				ATTACK,
				(byte)0,
				HEALTH,
				HEALTH,
				false,
				false,
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
	
	public Houndmaster(	
			byte mana,
			byte attack,
			byte health,
			byte baseAttack,
			byte extraAttackUntilTurnEnd,
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
			boolean destroyOnTurnStart,
			boolean destroyOnTurnEnd,
			boolean isInHand,
			boolean hasBeenUsed) {
		
		super(
			NAME,
			mana,
			attack,
			health,
			baseAttack,
			extraAttackUntilTurnEnd,
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
			destroyOnTurnStart,
			destroyOnTurnEnd,
			isInHand,
			hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new Houndmaster(
				this.mana_,
				this.attack_,
				this.health_,
				this.baseAttack_,
				this.extraAttackUntilTurnEnd_,
				this.baseHealth_,
				this.maxHealth_,
				this.taunt_,
				this.divineShield_,
				this.windFury_,
				this.charge_,
				this.hasAttacked_,
				this.hasWindFuryAttacked_,
				this.frozen_,
				this.summoned_,
				this.transformed_,
				this.destroyOnTurnStart_,
				this.destroyOnTurnEnd_,
				this.isInHand_,
				this.hasBeenUsed_);
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
