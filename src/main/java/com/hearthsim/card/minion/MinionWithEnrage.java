package com.hearthsim.card.minion;

import com.hearthsim.card.Deck;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class MinionWithEnrage extends Minion {


	public MinionWithEnrage(
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
					boolean stealthed_,
					boolean heroTargetable_,
					boolean summoned,
					boolean transformed,
					boolean destroyOnTurnStart,
					boolean destroyOnTurnEnd,
					boolean enraged,
					DeathrattleAction deathrattleAction,
					AttackAction attackAction,
					boolean isInHand,
					boolean hasBeenUsed) {
		super(name, mana, attack, health, baseAttack, extraAttackUntilTurnEnd, auraAttack, baseHealth, maxHealth, auraHealth, spellDamage, taunt, divineShield, windFury, charge, hasAttacked, hasWindFuryAttacked, frozen, silenced, stealthed_, heroTargetable_, summoned, transformed, destroyOnTurnStart, destroyOnTurnEnd, deathrattleAction, attackAction, isInHand, hasBeenUsed);
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
	 *  @param damage The amount of damage to take
	 * @param attackPlayerSide The player index of the attacker.  This is needed to do things like +spell damage.
     * @param thisPlayerSide
     * @param boardState
     * @param deckPlayer0 The deck of player0
     * @param isSpellDamage True if this is a spell damage
     * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode takeDamage(
			byte damage,
			PlayerSide attackPlayerSide,
			PlayerSide thisPlayerSide,
			HearthTreeNode boardState,
			Deck deckPlayer0, 
			Deck deckPlayer1,
			boolean isSpellDamage,
			boolean handleMinionDeath)
		throws HSException
	{		HearthTreeNode toRet = super.takeDamage(damage, attackPlayerSide, thisPlayerSide, boardState, deckPlayer0, deckPlayer1, isSpellDamage, handleMinionDeath);
		this.enrageCheck();
		return toRet;
	}
	
	/**
	 * Called when this minion is healed
	 * 
	 * Always use this function to heal minions
	 *  @param healAmount The amount of healing to take
	 * @param thisPlayerSide
     * @param boardState
     * @param deckPlayer0 The deck of player0   @throws HSInvalidPlayerIndexException
     * */
	@Override
	public HearthTreeNode takeHeal(byte healAmount, PlayerSide thisPlayerSide, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = super.takeHeal(healAmount, thisPlayerSide, boardState, deckPlayer0, deckPlayer1);
		this.enrageCheck();
		return toRet;
	}
	
	/**
	 * Called when this minion is silenced
	 * 
	 * Always use this function to "silence" minions
	 * 
	 *
     *
     * @param thisPlayerSide
     * @param boardState
     * @param deckPlayer0 The deck of player0
     * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode silenced(PlayerSide thisPlayerSide, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = super.silenced(thisPlayerSide, boardState, deckPlayer0, deckPlayer1);
		if (enraged_)
			this.pacify();
		return toRet;
	}
	
	private void enrageCheck() {
		if (!silenced_) {
			if (health_ < maxHealth_ && !enraged_) {
				enraged_ = true;
				this.enrage();
			} else if (health_ == maxHealth_ && enraged_) {
				enraged_ = false;
				this.pacify();
			}
		}
	}
	
	protected boolean enraged_;
	

}
