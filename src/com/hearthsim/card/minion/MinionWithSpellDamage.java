package com.hearthsim.card.minion;

import com.hearthsim.card.Deck;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
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
							byte auraAttack,
							byte baseHealth,
							byte maxHealth,
							byte auraHealth,
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
							DeathrattleAction deathrattleAction,
							AttackAction attackAction,
							boolean isInHand,
							boolean hasBeenUsed) {
		
		super(
			name,
			mana,
			attack,
			health,
			baseAttack,
			extraAttackUntilTurnEnd,
			auraAttack,
			baseHealth,
			maxHealth,
			auraHealth,
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
			deathrattleAction,
			attackAction,
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
				this.auraAttack_,
				this.baseHealth_,
				this.maxHealth_,
				this.auraHealth_,
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
				this.deathrattleAction_,
				this.attackAction_,
				this.isInHand_,
				this.hasBeenUsed_);
	}
	
    
	/**
	 * 
	 * Places a minion on the board.
	 * 
	 * @param targetPlayerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param targetMinion The target minion (can be a Hero).  If it is a Hero, then the minion is placed on the last (right most) spot on the board.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		HearthTreeNode toRet = super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
			toRet.data_.setSpellDamage(0, (byte)(toRet.data_.getSpellDamage(0) + spellDamage_));
		}
		return toRet;
	}
	
	/**
	 * Called when this minion is silenced
	 * 
	 * Always use this function to "silence" minions
	 * 
	 * @param thisPlayerIndex The player index of this minion
	 * @param boardState 
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode silenced(int thisPlayerIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = boardState;
		if (!silenced_)
			toRet.data_.setSpellDamage(0, (byte)(boardState.data_.getSpellDamage(0) - spellDamage_));
		toRet = super.silenced(thisPlayerIndex, toRet, deckPlayer0, deckPlayer1);
		return toRet;
	}
	
	/**
	 * Called when this minion dies (destroyed)
	 * 
	 * Always use this function to "kill" minions
	 * 
	 * @param thisPlayerIndex The player index of this minion
	 * @param boardState 
	 * @param deckPlayer0
	 * @param deckPlayer1
	 * 
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode destroyed(int thisPlayerIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;
		if (!silenced_)
			toRet.data_.setSpellDamage(0, (byte)(boardState.data_.getSpellDamage(0) - spellDamage_));
		toRet = super.destroyed(thisPlayerIndex, toRet, deckPlayer0, deckPlayer1);
		return toRet;
	}
}
