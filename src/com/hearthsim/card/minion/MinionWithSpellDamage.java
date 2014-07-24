package com.hearthsim.card.minion;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class MinionWithSpellDamage extends Minion {

	protected byte spellDamage_;
	
	public MinionWithSpellDamage(
							String name,
							byte mana,
							byte attack,
							byte health,
							byte baseAttack,
							byte extraAttackUntilTurnEnd,
							byte baseHealth,
							byte maxHealth,
							byte spellDamage,
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
							boolean destroyAtTurnStart,
							boolean destroyAtTurnEnd,
							boolean isInHand,
							boolean hasBeenUsed) {
		
		super(
			name,
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
			destroyAtTurnStart,
			destroyAtTurnEnd,
			isInHand,
			hasBeenUsed);
		spellDamage_ = spellDamage;
	}
	

	@Override
	public Object deepCopy() {
		return new MinionWithSpellDamage(
				this.name_,
				this.mana_,
				this.attack_,
				this.health_,
				this.baseAttack_,
				this.extraAttackUntilTurnEnd_,
				this.baseHealth_,
				this.maxHealth_,
				this.spellDamage_,
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
	 * Use the card on the given target
	 * 
	 * Override for the temporary buff to spell damage (spell damage +1)
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
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
			boardState.data_.placeMinion(0, this, minionIndex - 1);
			boardState.data_.setMana_p0(boardState.data_.getMana_p0() - this.mana_);
			boardState.data_.removeCard_hand(thisCardIndex);
			
			boardState.data_.setSpellDamage(0, (byte)(boardState.data_.getSpellDamage(0) + spellDamage_));
			return boardState;
							
		} else {
			return null;
		}
	}
	
	/**
	 * Called when this minion is silenced
	 * 
	 * Override for the spell damage
	 * 
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode silenced(int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = boardState;
		if (!silenced_)
			toRet.data_.setSpellDamage(0, (byte)(boardState.data_.getSpellDamage(0) - spellDamage_));
		toRet = super.silenced(thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
		return toRet;
	}
	
	/**
	 * Called when this minion dies (destroyed)
	 * 
	 * Override for the spell damage
	 * 
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode destroyed(int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {		
		HearthTreeNode toRet = boardState;
		if (!silenced_)
			toRet.data_.setSpellDamage(0, (byte)(boardState.data_.getSpellDamage(0) - spellDamage_));
		toRet = super.destroyed(thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
		return toRet;
	}
}
