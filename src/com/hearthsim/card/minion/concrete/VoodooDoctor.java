package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class VoodooDoctor extends Minion {



	private static final String NAME = "Voodoo Doctor";
	private static final byte MANA_COST = 1;
	private static final byte ATTACK = 2;
	private static final byte HEALTH = 1;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	
	public VoodooDoctor() {
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
				false,
				SUMMONED,
				TRANSFORMED,
				false,
				false,
				true,
				false
			);
	}
	
	public VoodooDoctor(	
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
			boolean silenced,
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
			silenced,
			summoned,
			transformed,
			destroyOnTurnStart,
			destroyOnTurnEnd,
			isInHand,
			hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new VoodooDoctor(
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
				this.silenced_,
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
	 * Battlecry: Restore 2 health
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode use_core(int thisCardIndex, int playerIndex, int minionIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		
		if (hasBeenUsed_) {
			//Card is already used, nothing to do
			return null;
		}
		
		if (boardState.data_.getNumMinions_p0() < 7) {

			HearthTreeNode toRet = boardState;
			hasBeenUsed_ = true;
			toRet.data_.setMana_p0(toRet.data_.getMana_p0() - this.mana_);
			toRet.data_.removeCard_hand(thisCardIndex);
			toRet.data_.placeMinion(0, this, minionIndex - 1);

			{
				HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
				newState = newState.data_.getHero_p0().takeHeal((byte)2, 0, 0, newState, deckPlayer0, deckPlayer1);
				toRet.addChild(newState);
			}
			
			{
				for (int index = 0; index < boardState.data_.getNumMinions_p0(); ++index) {
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
					newState = newState.data_.getMinion_p0(index).takeHeal((byte)2, 0, index + 1, newState, deckPlayer0, deckPlayer1);
					toRet.addChild(newState);
				}
			}

			{
				HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
				newState = newState.data_.getHero_p1().takeHeal((byte)2, 1, 0, newState, deckPlayer0, deckPlayer1);
				toRet.addChild(newState);
			}
			
			{
				for (int index = 0; index < boardState.data_.getNumMinions_p1(); ++index) {
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
					newState = newState.data_.getMinion_p1(index).takeHeal((byte)2, 1, index + 1, newState, deckPlayer0, deckPlayer1);
					toRet.addChild(newState);
				}
			}

			return toRet;
							
		} else {
			return null;
		}

	}
}
