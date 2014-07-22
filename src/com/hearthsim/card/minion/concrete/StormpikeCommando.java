package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class StormpikeCommando extends Minion {


	private static final String NAME = "Stormpike Commando";
	private static final byte MANA_COST = 5;
	private static final byte ATTACK = 4;
	private static final byte HEALTH = 2;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	
	public StormpikeCommando() {
		this(
				MANA_COST,
				ATTACK,
				HEALTH,
				ATTACK,
				(byte)0,
				HEALTH,
				HEALTH,
				TAUNT,
				DIVINE_SHIELD,
				WINDFURY,
				CHARGE,
				false,
				false,
				false,
				SUMMONED,
				TRANSFORMED,
				false,
				false,
				true,
				false
			);
	}
	
	public StormpikeCommando(	
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
		return new StormpikeCommando(
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
	 * Battlecry: Deal 2 damage to a chosen target
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode use_core(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{	
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
			boardState.data_.placeMinion(0, this, minionIndex - 1);
			
			{
				HearthTreeNode newState = boardState.addChild(new HearthTreeNode((BoardState)boardState.data_.deepCopy()));
				newState.data_.getHero_p0().takeDamage((byte)2, 0, 0, 0, newState, deckPlayer0, deckPlayer1);
			}

			{
				for (int index = 0; index < boardState.data_.getNumMinions_p0(); ++index) {
					if (boardState.data_.getMinion_p0(index) == this)
						continue;
					HearthTreeNode newState = boardState.addChild(new HearthTreeNode((BoardState)boardState.data_.deepCopy()));
					newState.data_.getMinion_p0(index).takeDamage((byte)2, 0, 0, index + 1, newState, deckPlayer0, deckPlayer1);
					if (newState.data_.getMinion_p0(index).getHealth() <= 0) {
						newState.data_.removeMinion_p0(index);
					}
				}
			}

			{
				HearthTreeNode newState = boardState.addChild(new HearthTreeNode((BoardState)boardState.data_.deepCopy()));
				newState.data_.getHero_p1().takeDamage((byte)2, 0, 1, 0, newState, deckPlayer0, deckPlayer1);
			}

			{
				for (int index = 0; index < boardState.data_.getNumMinions_p1(); ++index) {		
					HearthTreeNode newState = boardState.addChild(new HearthTreeNode((BoardState)boardState.data_.deepCopy()));
					newState.data_.getMinion_p1(index).takeDamage((byte)2, 0, 1, index + 1, newState, deckPlayer0, deckPlayer1);
					if (newState.data_.getMinion_p1(index).getHealth() <= 0) {
						newState.data_.removeMinion_p1(index);
					}
				}
			}

			return boardState;
							
		} else {
			return null;				
		}

	}
}
