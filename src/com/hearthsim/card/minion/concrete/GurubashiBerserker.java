package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
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
	
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	
	public GurubashiBerserker() {
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
	
	public GurubashiBerserker(	
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
		return new GurubashiBerserker(
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
	 * Called when this minion takes damage
	 * 
	 * Override for special ability: gain +3 attack whenever this minion takes damage
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
		if (!divineShield_) {
			byte totalDamage = isSpellDamage ? (byte)(damage + boardState.data_.getSpellDamage(attackerPlayerIndex)) : damage;
			health_ = (byte)(health_ - totalDamage);
			
			//Notify all that the minion is damaged
			HearthTreeNode toRet = boardState;
			toRet = toRet.data_.getHero_p0().minionDamagedEvent(0, 0, thisPlayerIndex, thisMinionIndex, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
				toRet = toRet.data_.getMinion_p0(j).minionDamagedEvent(0, j + 1, thisPlayerIndex, thisMinionIndex, toRet, deck);
			}
			toRet = toRet.data_.getHero_p1().minionDamagedEvent(1, 0, thisPlayerIndex, thisMinionIndex, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
				toRet = toRet.data_.getMinion_p1(j).minionDamagedEvent(1, j + 1, thisPlayerIndex, thisMinionIndex, toRet, deck);
			}
			
			//If fatal, notify all that it is dead
			if (health_ <= 0) {
				toRet = this.destroyed(thisPlayerIndex, thisMinionIndex, toRet, deck);
			}
			
			this.attack_ = (byte)(this.attack_ + 3);
			return toRet;
		} else {
			divineShield_ = false;
			return boardState;
		}
	}
}
