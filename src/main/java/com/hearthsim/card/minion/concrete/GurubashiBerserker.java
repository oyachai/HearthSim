package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;


public class GurubashiBerserker extends Minion {

	private static final String NAME = "Gurubashi Berserker";
	private static final byte MANA_COST = 5;
	private static final byte ATTACK = 2;
	private static final byte HEALTH = 7;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean STEALTHED = false;
	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public GurubashiBerserker() {
		this(
				MANA_COST,
				ATTACK,
				HEALTH,
				ATTACK,
				(byte)0,
				(byte)0,
				HEALTH,
				HEALTH,
				(byte)0,
				SPELL_DAMAGE,
				TAUNT,
				DIVINE_SHIELD,
				WINDFURY,
				CHARGE,
				false,
				false,
				false,
				false,
				STEALTHED,
				HERO_TARGETABLE,
				SUMMONED,
				TRANSFORMED,
				false,
				false,
				null,
				null,
				true,
				false
			);
	}
	
	public GurubashiBerserker(	
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
			boolean stealthed,
			boolean hero_targetable,
			boolean summoned,
			boolean transformed,
			boolean destroyOnTurnStart,
			boolean destroyOnTurnEnd,
			DeathrattleAction deathrattleAction,
			AttackAction attackAction,
			boolean isInHand,
			boolean hasBeenUsed) {
		
		super(
			NAME,
			mana,
			attack,
			health,
			baseAttack,
			extraAttackUntilTurnEnd,
			auraAttack,
			baseHealth,
			maxHealth,
			auraHealth,
			spellDamage,
			taunt,
			divineShield,
			windFury,
			charge,
			hasAttacked,
			hasWindFuryAttacked,
			frozen,
			silenced,
			stealthed,
			hero_targetable,
			summoned,
			transformed,
			destroyOnTurnStart,
			destroyOnTurnEnd,
			deathrattleAction,
			attackAction,
			isInHand,
			hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new GurubashiBerserker(
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
				this.stealthed_,
				this.heroTargetable_,
				this.summoned_,
				this.transformed_,
				this.destroyOnTurnStart_,
				this.destroyOnTurnEnd_,
				this.deathrattleAction_,
				this.attackAction_,
				this.isInHand_,
				this.hasBeenUsed);
	}
	
	
	/**
	 * Called when this minion takes damage
	 * 
	 * Override for special ability: gain +3 attack whenever this minion takes damage
	 *  @param damage The amount of damage to take
	 * @param attackPlayerSide The player index of the attacker.  This is needed to do things like +spell damage.
     * @param thisPlayerSide
     * @param boardState
     * @param isSpellDamage True if this is a spell damage   @throws HSInvalidPlayerIndexException
     * */
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
	{
		if (!divineShield_) {
			HearthTreeNode toRet = super.takeDamage(damage, attackPlayerSide, thisPlayerSide, boardState, deckPlayer0, deckPlayer1, isSpellDamage, handleMinionDeath);
			if (!silenced_)
				this.attack_ = (byte)(this.attack_ + 3);
			return toRet;
		} else {
			divineShield_ = false;
			return boardState;
		}
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
     * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode silenced(PlayerSide thisPlayerSide, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = super.silenced(thisPlayerSide, boardState, deckPlayer0, deckPlayer1);
		this.attack_ = this.baseAttack_;
		return toRet;
	}
}
