package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.event.deathrattle.DeathrattleDamageAll;

public class Abomination extends Minion {

	private static final String NAME = "Abomination";
	private static final byte MANA_COST = 5;
	private static final byte ATTACK = 4;
	private static final byte HEALTH = 4;
	
	private static final boolean TAUNT = true;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean STEALTHED = false;
	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public Abomination() {
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
				new DeathrattleDamageAll((byte)2),
				null,
				true,
				false
			);
	}
	
	public Abomination(	
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
		return new Abomination(
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
				this.hasBeenUsed_);
	}
//	/**
//	 * Called when this minion dies (destroyed)
//	 * 
//	 * Death rattle: deal 2 damage to all characters
//	 * 
//	 * @param thisPlayerIndex The player index of this minion
//	 * @param thisMinionIndex The minion index of this minion
//	 * @param boardState 
//	 * @param deck
//	 * @throws HSInvalidPlayerIndexException
//	 */
//	public HearthTreeNode destroyed(int thisPlayerIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
//		
//		HearthTreeNode toRet = super.destroyed(thisPlayerIndex, boardState, deckPlayer0, deckPlayer1);
//		if (!silenced_) {
//			toRet = toRet.data_.getHero_p1().takeDamage((byte)2, thisPlayerIndex, 1, toRet, deckPlayer0, deckPlayer1, false, false);
//			for(Minion minion : toRet.data_.getMinions_p1()) {
//				toRet = minion.takeDamage((byte)2, thisPlayerIndex, 1, toRet, deckPlayer0, deckPlayer1, false, false);
//			}
//			toRet = toRet.data_.getHero_p0().takeDamage((byte)2, thisPlayerIndex, 0, toRet, deckPlayer0, deckPlayer1, false, false);
//			for(Minion minion : toRet.data_.getMinions_p0()) {
//				toRet = minion.takeDamage((byte)2, thisPlayerIndex, 0, toRet, deckPlayer0, deckPlayer1, false, false);
//			}
//		}
//		return toRet;
//
//	}
}
