package com.hearthsim.card.minion;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class MinionWithEnrage extends Minion {


	public MinionWithEnrage(
					String name,
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
					boolean enraged,
					boolean isInHand,
					boolean hasBeenUsed) {
		super(name, mana, attack, health, baseAttack, extraAttackUntilTurnEnd, baseHealth, maxHealth, taunt, divineShield, windFury, charge, hasAttacked, hasWindFuryAttacked, frozen, summoned, transformed, destroyOnTurnStart, destroyOnTurnEnd, isInHand, hasBeenUsed);
		enraged_ = enraged;
	}
	
	@Override
	public abstract Object deepCopy();
	
	/**
	 * Turn on enrage
	 * 
	 */
	public abstract void enrage();
	
	/**
	 * Turn off enrage
	 */
	public abstract void pacify();
	
	/**
	 * Called when this minion takes damage
	 * 
	 * Override for enrage
	 * 
	 * @param damage The amount of damage to take
	 * @param attackerPlayerIndex The player index of the attacker.  This is needed to do things like +spell damage.
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @param isSpellDamage True if this is a spell damage
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode takeDamage(byte damage, int attackerPlayerIndex, int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deck, boolean isSpellDamage) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = super.takeDamage(damage, attackerPlayerIndex, thisPlayerIndex, thisMinionIndex, boardState, deck, isSpellDamage);
		this.enrageCheck();
		return toRet;
	}
	
	/**
	 * Called when this minion is healed
	 * 
	 * @param healAmount The amount of healing to take
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode takeHeal(byte healAmount, int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deck) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = super.takeHeal(healAmount, thisPlayerIndex, thisMinionIndex, boardState, deck);
		this.enrageCheck();
		return toRet;
	}
	
	/**
	 * Called when this minion is silenced
	 * 
	 * Always use this function to "silence" minions
	 * 
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode silenced(int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deck) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = super.silenced(thisPlayerIndex, thisMinionIndex, boardState, deck);
		if (enraged_)
			this.pacify();
		return toRet;
	}
	
	private void enrageCheck() {
		if (health_ < maxHealth_ && !enraged_) {
			enraged_ = true;
			this.enrage();
		} else if (health_ == maxHealth_ && enraged_) {
			enraged_ = false;
			this.pacify();
		}
	}
	
	protected boolean enraged_;
	

}
