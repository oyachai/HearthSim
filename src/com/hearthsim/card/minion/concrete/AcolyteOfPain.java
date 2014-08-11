package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;

public class AcolyteOfPain extends Minion {

	private static final String NAME = "Acolyte of Pain";
	private static final byte MANA_COST = 3;
	private static final byte ATTACK = 1;
	private static final byte HEALTH = 3;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	
	public AcolyteOfPain() {
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
				null,
				null,
				true,
				false
			);
	}
	
	public AcolyteOfPain(	
			byte mana,
			byte attack,
			byte health,
			byte baseAttack,
			byte extraAttackUntilTurnEnd,
			byte auraAttack,
			byte baseHealth,
			byte maxHealth,
			byte auraHealth,
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
			deathrattleAction,
			attackAction,
			isInHand,
			hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new AcolyteOfPain(
				this.mana_,
				this.attack_,
				this.health_,
				this.baseAttack_,
				this.extraAttackUntilTurnEnd_,
				this.auraAttack_,
				this.baseHealth_,
				this.maxHealth_,
				this.auraHealth_,
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
	 * Called when this minion takes damage
	 * 
	 * Draw a card whenever this minion takes damage
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
	public HearthTreeNode takeDamage(byte damage, int attackerPlayerIndex, int thisPlayerIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1, boolean isSpellDamage) throws HSInvalidPlayerIndexException {
		if (!divineShield_) {
			HearthTreeNode toRet = super.takeDamage(damage, attackerPlayerIndex, thisPlayerIndex, boardState, deckPlayer0, deckPlayer1, isSpellDamage);
			if (damage > 0 && thisPlayerIndex == 0) {
				if (toRet instanceof CardDrawNode) {
					((CardDrawNode) toRet).addNumCardsToDraw(1);
				} else {
					toRet = new CardDrawNode(toRet, 1); //draw one card
				}
			} else if (damage > 0) {
				//This minion is an enemy minion.  Let's draw a card for the enemy.  No need to use a StopNode for enemy card draws.
				toRet.data_.drawCardFromDeck_p1(deckPlayer1, 1);
			}
			return toRet;
		} else {
			divineShield_ = false;
			return boardState;
		}
	}
}
